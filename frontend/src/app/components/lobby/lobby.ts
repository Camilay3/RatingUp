import { ChangeDetectorRef, Component, inject, OnDestroy, OnInit } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { GameService } from '../../services/game/game.service';
import { IGameSummary } from '../../interfaces/chess/IGameSummary';

@Component({
	selector: 'app-lobby',
	standalone: true,
	imports: [RouterLink],
	templateUrl: './lobby.html',
	styleUrl: './lobby.scss',
})
export class Lobby implements OnInit, OnDestroy {
	private readonly gameService = inject(GameService);
	private readonly router = inject(Router);
	private readonly snackBar = inject(MatSnackBar);
	private readonly cdr = inject(ChangeDetectorRef);

	openGames: IGameSummary[] = [];
	myGames: IGameSummary[] = [];
	isLoading = true;
	isBusy = false;

	private refreshTimer?: ReturnType<typeof setInterval>;

	ngOnInit() {
		this.load();
		this.refreshTimer = setInterval(() => this.load(), 5000);
	}

	ngOnDestroy() {
		clearInterval(this.refreshTimer);
	}

	async load() {
		try {
			const [open, mine] = await Promise.all([
				this.gameService.listOpen(),
				this.gameService.listMine(),
			]);
			this.openGames = open;
			this.myGames = mine;
		} catch {
		}
		this.isLoading = false;
		this.cdr.detectChanges();
	}

	async criar() {
		await this.run(async () => {
			const game = await this.gameService.createGame();
			await this.router.navigate(['/partida', game.gameId]);
		}, 'Erro ao criar a partida');
	}

	async entrar(gameId: number) {
		await this.run(async () => {
			const game = await this.gameService.joinGame(gameId);
			await this.router.navigate(['/partida', game.gameId]);
		}, 'Erro ao entrar na partida');
	}

	async cancelar(gameId: number) {
		await this.run(async () => {
			await this.gameService.cancelGame(gameId);
			await this.load();
		}, 'Erro ao cancelar a partida');
	}

	private async run(action: () => Promise<void>, fallbackMessage: string) {
		if (this.isBusy) return;
		this.isBusy = true;
		this.cdr.detectChanges();

		try {
			await action();
		} catch (e: any) {
			this.snackBar.open(e?.error?.message ?? fallbackMessage, 'Fechar', { duration: 3000 });
			await this.load();
		} finally {
			this.isBusy = false;
			this.cdr.detectChanges();
		}
	}
}
