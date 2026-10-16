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
import { ActivatedRoute, Router } from '@angular/router';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { Chessground } from 'chessground';
import { firstValueFrom } from 'rxjs';
import { IGameEvent } from '../../interfaces/chess/IGameEvent';
import { IGameState } from '../../interfaces/chess/IGameState';
import { GameService } from '../../services/game/game.service';
import { GameSocketService } from '../../services/game/game-socket.service';
import { PromotionSelectorComponent } from '../modais/promotion-selector/promotion-selector.component';

@Component({
	selector: 'app-game-board',
	standalone: true,
	imports: [MatDialogModule],
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

	private cg: any;
	private lastMove = 0;
	private currentFen = '';

	constructor(
		private route: ActivatedRoute,
		private router: Router,
		private dialog: MatDialog,
		private gameService: GameService,
		private socket: GameSocketService,
		private cdr: ChangeDetectorRef,
		private destroyRef: DestroyRef,
	) {}

	get isMyTurn(): boolean {
		return this.gameStatus === 'IN_PROGRESS' && this.turn === this.myColor.toUpperCase();
	}

	get corTexto(): string {
		return this.myColor === 'white' ? 'brancas' : 'pretas';
	}

	get resultText(): string {
		if (this.boardStatus === 'DRAW') return 'Empate';
		if (this.boardStatus === 'CHECKMATE') {
			const perdi = this.turn === this.myColor.toUpperCase();
			return perdi ? 'Xeque-mate: você perdeu' : 'Xeque-mate: você venceu!';
		}
		return 'Partida encerrada';
	}

	async ngOnInit() {
		this.gameId = Number(this.route.snapshot.paramMap.get('id'));

		try {
			const state = await this.gameService.getGame(this.gameId);
			this.applyState(state);

			this.cg = Chessground(this.boardRef.nativeElement, {
				fen: state.fen,
				orientation: this.myColor,
				turnColor: this.turn.toLowerCase(),
				promovable: { enabled: false},
				movable: {
					free: true,
					color: this.isMyTurn ? this.myColor : undefined,
					events: { after: (orig: string, dest: string) => this.onMove(orig, dest) },
				},
			});
		} catch (e: any) {
			this.errorMessage = e?.error?.message ?? 'Não foi possível carregar a partida.';
			this.cdr.detectChanges();
			return;
		}

		this.socket.events$
			.pipe(takeUntilDestroyed(this.destroyRef))
			.subscribe(event => this.apply(event));

		this.socket.connect(this.gameId, () => this.sync());
		this.cdr.detectChanges();
	}

	ngOnDestroy() {
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

	private apply(e: IGameEvent) {
		if (e.type === 'STARTED') {
			this.sync();
			return;
		}

		if (e.moveNumber <= this.lastMove) return;

		this.errorMessage = '';
		this.lastMove = e.moveNumber;
		this.currentFen = e.fen;
		this.turn = e.turn;
		this.gameStatus = e.gameStatus;
		this.boardStatus = e.boardStatus;

		this.moves = [
			...this.moves,
			{ from: e.from!, to: e.to!, mine: e.turn !== this.myColor.toUpperCase() },
		];

		this.refreshBoard(e.fen, [e.from!, e.to!]);
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
	}

	private refreshBoard(fen: string, lastMove?: [string, string]) {
		const config: any = {
			fen,
			orientation: this.myColor,
			turnColor: this.turn.toLowerCase(),
			movable: { color: this.isMyTurn ? this.myColor : undefined },
		};
		if (lastMove) config.lastMove = lastMove;

		this.cg.set(config);
		this.cdr.detectChanges();
	}

	private resetBoard() {
		this.cg.cancelPremove();
		this.refreshBoard(this.currentFen);
	}
}
