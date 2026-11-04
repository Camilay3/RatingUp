import {
	ChangeDetectorRef,
	Component,
	DestroyRef,
	ElementRef,
	OnDestroy,
	OnInit,
	ViewChild,
} from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { Chessground } from 'chessground';
import Swal from 'sweetalert2';
import { firstValueFrom } from 'rxjs';
import { GameEndReason, GameResult, IGameEvent } from '../../interfaces/chess/IGameEvent';
import { IGameState } from '../../interfaces/chess/IGameState';
import { TimeControlService } from '../../services/chess/time-control.service';
import { GameService } from '../../services/game/game.service';
import { GameSocketService } from '../../services/game/game-socket.service';
import { AuthService } from '../../services/user/auth.service';
import { PromotionSelectorComponent } from '../modais/promotion-selector/promotion-selector.component';
import { PlayerInfoComponent } from '../player-info/player-info.component';
import { TimeControlBadgeComponent } from '../time-control-badge/time-control-badge.component';

const CLOCK_TICK_MS = 100;
// se o servidor ainda não considera o tempo esgotado (latência), tenta de novo depois
const TIMEOUT_RETRY_MS = 1000;

@Component({
	selector: 'app-game-board',
	standalone: true,
	imports: [MatDialogModule, RouterLink, PlayerInfoComponent, TimeControlBadgeComponent],
	templateUrl: './game-board.html',
	styleUrl: './game-board.scss',
})
export class GameBoard implements OnInit, OnDestroy {
	@ViewChild('board', { static: true }) boardRef!: ElementRef;

	gameId!: number;
	myColor: 'white' | 'black' = 'white';
	turn: 'WHITE' | 'BLACK' = 'WHITE';
	gameStatus = 'WAITING';
	boardStatus = 'NORMAL';
	opponentNickname: string | null = null;
	errorMessage = '';
	moves: { from: string; to: string; mine: boolean }[] = [];

	isLoading = true;
	isResigning = false;

	// nomes, ratings e controle de tempo
	myNickname = '';
	myRating: number | null = null;
	opponentRating: number | null = null;
	myRatingChange: number | null = null;
	opponentRatingChange: number | null = null;
	categoryText = '';
	initialTimeSeconds = 0;
	incrementSeconds = 0;

	// relógios (ms restantes) de cada cor
	whiteTimeMs: number | null = null;
	blackTimeMs: number | null = null;

	// resultado
	result: GameResult | null = null;
	endReason: GameEndReason | null = null;

	private cg: any;
	private lastMove = 0;
	private currentFen = '';
	// true quando myRating/opponentRating já incluem a variação da partida
	private ratingsSettled = false;

	// o servidor é a fonte de verdade: o relógio local só interpola entre duas mensagens
	private clockAnchor = { white: 0, black: 0, at: 0 };
	private clockTimer?: ReturnType<typeof setInterval>;
	private claimingTimeout = false;
	private claimRetryAt = 0;

	constructor(
		private route: ActivatedRoute,
		private router: Router,
		private dialog: MatDialog,
		private gameService: GameService,
		private socket: GameSocketService,
		private authService: AuthService,
		private cdr: ChangeDetectorRef,
		private destroyRef: DestroyRef,
		private timeControlService: TimeControlService,
	) {}

	get isMyTurn(): boolean {
		return this.gameStatus === 'IN_PROGRESS' && this.turn === this.myColor.toUpperCase();
	}

	get opponentColor(): 'white' | 'black' {
		return this.myColor === 'white' ? 'black' : 'white';
	}

	get canResign(): boolean {
		return this.gameStatus === 'IN_PROGRESS' && !this.isResigning;
	}

	get myTimeMs(): number | null {
		return this.myColor === 'white' ? this.whiteTimeMs : this.blackTimeMs;
	}

	get opponentTimeMs(): number | null {
		return this.myColor === 'white' ? this.blackTimeMs : this.whiteTimeMs;
	}

	get resultText(): string {
		const iAmWhite = this.myColor === 'white';
		const iWon =
			(this.result === 'WHITE_WINS' && iAmWhite) || (this.result === 'BLACK_WINS' && !iAmWhite);

		if (this.result === 'DRAW' || this.boardStatus === 'DRAW') return 'Empate';
		if (this.endReason === 'TIMEOUT') {
			return iWon ? 'Tempo esgotado: você venceu!' : 'Tempo esgotado: você perdeu';
		}
		if (this.endReason === 'RESIGNATION') {
			return iWon ? 'O adversário desistiu: você venceu!' : 'Você desistiu da partida';
		}
		if (this.result) {
			return iWon ? 'Xeque-mate: você venceu!' : 'Xeque-mate: você perdeu';
		}
		// fallback para eventos sem result
		if (this.boardStatus === 'CHECKMATE') {
			return this.turn === this.myColor.toUpperCase()
				? 'Xeque-mate: você perdeu'
				: 'Xeque-mate: você venceu!';
		}
		return 'Partida encerrada';
	}

	async ngOnInit() {
		this.gameId = Number(this.route.snapshot.paramMap.get('id'));

		try {
			const [state] = await Promise.all([
				this.gameService.getGame(this.gameId),
				this.loadMyNickname(),
			]);
			this.applyState(state);

			this.cg = Chessground(this.boardRef.nativeElement, {
				fen: state.fen,
				orientation: this.myColor,
				turnColor: this.turn.toLowerCase(),
				promovable: { enabled: false },
				movable: {
					free: true,
					color: this.isMyTurn ? this.myColor : undefined,
					events: { after: (orig: string, dest: string) => this.onMove(orig, dest) },
				},
			});
		} catch (e: any) {
			this.errorMessage = e?.error?.message ?? 'Não foi possível carregar a partida.';
			this.isLoading = false;
			this.cdr.detectChanges();
			return;
		}

		this.socket.events$
			.pipe(takeUntilDestroyed(this.destroyRef))
			.subscribe(event => this.apply(event));

		this.socket.connect(this.gameId, () => this.sync());
		this.isLoading = false;
		this.cdr.detectChanges();
	}

	ngOnDestroy() {
		this.stopClock();
		this.socket.disconnect();
		this.cg?.destroy();
	}

	voltar() {
		this.router.navigateByUrl('/jogar');
	}

	async cancelar() {
		try {
			await this.gameService.cancelGame(this.gameId);
			this.voltar();
		} catch (e: any) {
			this.errorMessage = e?.error?.message ?? 'Não foi possível cancelar a partida.';
			this.cdr.detectChanges();
		}
	}

	async desistir() {
		if (!this.canResign) return;

		const { isConfirmed } = await Swal.fire({
			title: 'Desistir da partida?',
			text: 'Você perde a partida e seu rating será atualizado.',
			icon: 'warning',
			showCancelButton: true,
			confirmButtonText: 'Desistir',
			cancelButtonText: 'Continuar jogando',
			confirmButtonColor: '#cc3434',
		});
		if (!isConfirmed) return;

		this.errorMessage = '';
		this.isResigning = true;
		this.cdr.detectChanges();

		try {
			// REST responde com o evento RESIGNED; o mesmo evento também chega pelo WebSocket
			// (o tratamento é idempotente, então a duplicidade é inofensiva)
			const event = await this.gameService.resign(this.gameId);
			this.apply(event);
		} catch (e: any) {
			this.errorMessage = e?.error?.message ?? 'Não foi possível desistir da partida.';
			this.sync(); // a partida pode já ter terminado (409/400): reconcilia com o servidor
		} finally {
			this.isResigning = false;
			this.cdr.detectChanges();
		}
	}

	async onMove(orig: string, dest: string) {
		this.errorMessage = '';

		let promotion: string | undefined;
		const piece = this.cg.state.pieces.get(dest);
		if (piece?.role === 'pawn' && (dest[1] === '8' || dest[1] === '1')) {
			promotion = await this.askPromotion();
			if (!promotion) {
				this.resetBoard();
				return;
			}
		}

		try {
			const event = await this.gameService.move(this.gameId, orig, dest, promotion);
			this.apply(event);
		} catch (e: any) {
			this.resetBoard();
			this.errorMessage = e?.error?.message ?? 'Não foi possível realizar o lance.';
			this.cdr.detectChanges();
		}
	}

	private askPromotion(): Promise<string | undefined> {
		const dialogRef = this.dialog.open(PromotionSelectorComponent, {
			width: 'auto',
			panelClass: 'custom-edit-dialog',
			disableClose: true,
			data: { color: this.myColor },
		});
		return firstValueFrom(dialogRef.afterClosed());
	}

	private async loadMyNickname() {
		try {
			const resp = await firstValueFrom(this.authService.me());
			this.myNickname = resp.data.nickname;
		} catch {
			// sem o apelido a UI mostra "Você"
		}
	}

	private apply(e: IGameEvent) {
		if (e.type === 'STARTED') {
			this.sync();
			return;
		}

		// RESIGNED/TIMEOUT repetem o moveNumber do último lance: precisam vir ANTES do guard abaixo
		if (e.type === 'RESIGNED' || e.type === 'TIMEOUT') {
			this.currentFen = e.fen;
			this.setClocks(e.whiteTimeMs, e.blackTimeMs);
			this.onFinished(e);
			this.refreshBoard(e.fen);
			return;
		}

		if (e.moveNumber <= this.lastMove) return;

		this.errorMessage = '';
		this.lastMove = e.moveNumber;
		this.currentFen = e.fen;
		this.turn = e.turn;
		this.gameStatus = e.gameStatus;
		this.boardStatus = e.boardStatus;
		this.setClocks(e.whiteTimeMs, e.blackTimeMs);

		this.moves = [
			...this.moves,
			{ from: e.from!, to: e.to!, mine: e.turn !== this.myColor.toUpperCase() },
		];

		if (e.gameStatus === 'FINISHED') this.onFinished(e);
		else this.startClock();
		this.refreshBoard(e.fen, [e.from!, e.to!]);
	}

	private onFinished(e: IGameEvent) {
		this.gameStatus = 'FINISHED';
		this.turn = e.turn;
		this.boardStatus = e.boardStatus;
		this.result = e.result ?? this.result;
		this.endReason = e.endReason ?? this.endReason;
		this.stopClock();
		this.settleRatings(e);
	}

	// aplica a variação de rating uma única vez (o estado do servidor, se já pós-jogo, prevalece)
	private settleRatings(e: IGameEvent) {
		if (this.ratingsSettled) return;
		if (e.whiteRatingChange == null || e.blackRatingChange == null) {
			this.sync(); // evento sem variação: busca o estado final no servidor
			return;
		}

		const mine = this.myColor === 'white' ? e.whiteRatingChange : e.blackRatingChange;
		const theirs = this.myColor === 'white' ? e.blackRatingChange : e.whiteRatingChange;

		if (this.myRating != null) this.myRating += mine;
		if (this.opponentRating != null) this.opponentRating += theirs;
		this.myRatingChange = mine;
		this.opponentRatingChange = theirs;
		this.ratingsSettled = true;
	}

	// ---------- relógios ----------

	private setClocks(white?: number | null, black?: number | null) {
		if (white == null || black == null) return;
		this.whiteTimeMs = white;
		this.blackTimeMs = black;
		this.clockAnchor = { white, black, at: Date.now() };
	}

	private startClock() {
		if (this.clockTimer) return;
		this.clockTimer = setInterval(() => this.tick(), CLOCK_TICK_MS);
	}

	private stopClock() {
		clearInterval(this.clockTimer);
		this.clockTimer = undefined;
	}

	// só o relógio de quem tem a vez diminui; o do outro fica parado
	private tick() {
		if (this.gameStatus !== 'IN_PROGRESS') {
			this.stopClock();
			return;
		}

		const elapsed = Date.now() - this.clockAnchor.at;
		let remaining: number;
		if (this.turn === 'WHITE') {
			remaining = this.whiteTimeMs = Math.max(0, this.clockAnchor.white - elapsed);
		} else {
			remaining = this.blackTimeMs = Math.max(0, this.clockAnchor.black - elapsed);
		}
		this.cdr.detectChanges();

		if (remaining === 0) this.claimTimeout();
	}

	// o servidor confere o relógio e encerra a partida (quem chegar primeiro, qualquer dos dois jogadores)
	private async claimTimeout() {
		if (this.claimingTimeout || Date.now() < this.claimRetryAt) return;
		this.claimingTimeout = true;

		try {
			const event = await this.gameService.claimTimeout(this.gameId);
			this.apply(event);
		} catch {
			this.claimRetryAt = Date.now() + TIMEOUT_RETRY_MS;
			await this.sync(); // reancora os relógios com o tempo real do servidor
		} finally {
			this.claimingTimeout = false;
			this.cdr.detectChanges();
		}
	}

	private async sync() {
		try {
			const s = await this.gameService.getGame(this.gameId);
			if (s.moveNumber < this.lastMove) return;

			this.applyState(s);
			this.refreshBoard(s.fen);
		} catch {
		}
	}

	private applyState(s: IGameState) {
		this.myColor = s.myColor === 'WHITE' ? 'white' : 'black';
		this.lastMove = s.moveNumber;
		this.currentFen = s.fen;
		this.turn = s.turn;
		this.gameStatus = s.gameStatus;
		this.boardStatus = s.boardStatus;
		this.opponentNickname = s.opponentNickname;

		this.myRating = s.myRating;
		this.opponentRating = s.opponentRating;
		this.result = s.result;
		this.endReason = s.endReason;
		this.categoryText = this.timeControlService.categoryLabel(s.timeControlCategory);
		this.initialTimeSeconds = s.initialTimeSeconds;
		this.incrementSeconds = s.incrementSeconds;

		this.setClocks(s.whiteTimeMs, s.blackTimeMs);
		if (s.gameStatus === 'IN_PROGRESS') this.startClock();
		else this.stopClock();

		// partida encerrada: os ratings do servidor já incluem a variação
		this.ratingsSettled = s.gameStatus === 'FINISHED';
		if (this.ratingsSettled) this.myRatingChange = s.myRatingChange;
	}

	private refreshBoard(fen: string, lastMove?: [string, string]) {
		const config: any = {
			fen,
			orientation: this.myColor,
			turnColor: this.turn.toLowerCase(),
			movable: { color: this.isMyTurn ? this.myColor : undefined },
		};
		if (lastMove) config.lastMove = lastMove;

		this.cg?.set(config);
		this.cdr.detectChanges();
	}

	private resetBoard() {
		this.cg.cancelPremove();
		this.refreshBoard(this.currentFen);
	}
}
