import { ChangeDetectorRef, Component, inject, OnDestroy, OnInit } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { GameService } from '../../services/game/game.service';
import { RatingService } from '../../services/game/rating.service';
import { IGameSummary } from '../../interfaces/chess/IGameSummary';
import { IRatingRow } from '../../interfaces/chess/IRatingRow';
import { ITimeControlPreset, TimeControlCategory } from '../../interfaces/chess/ITimeControl';
import { TimeControlService } from '../../services/chess/time-control.service';
import {TimeControlBadgeComponent} from '../time-control-badge/time-control-badge.component';
import {IncrementSelectorComponent} from '../increment-selector/increment-selector.component';
import { RatingsPanelComponent } from '../ratings-panel/ratings-panel.component';

@Component({
	selector: 'app-lobby',
	standalone: true,
	imports: [RouterLink, TimeControlBadgeComponent, IncrementSelectorComponent, RatingsPanelComponent],
	templateUrl: './lobby.html',
	styleUrl: './lobby.scss',
})
export class Lobby implements OnInit, OnDestroy {
	private readonly gameService = inject(GameService);
	private readonly ratingService = inject(RatingService);
	private readonly timeControlService = inject(TimeControlService);
	private readonly router = inject(Router);
	private readonly snackBar = inject(MatSnackBar);
	private readonly cdr = inject(ChangeDetectorRef);

	readonly timeControls = this.timeControlService.presets;

	selectedControl: ITimeControlPreset = this.timeControlService.defaultPreset;
	// segundos de acréscimo por jogada (0 = sem acréscimo)
	increment = this.timeControlService.defaultPreset.incrementSeconds;

	openGames: IGameSummary[] = [];
	myGames: IGameSummary[] = [];
	isLoading = true;
	isBusy = false;

	ratingRows: IRatingRow[] = [];
	isLoadingRatings = true;
	ratingsError = false;

	private refreshTimer?: ReturnType<typeof setInterval>;

	get effectiveCategory(): TimeControlCategory {
		return this.timeControlService.estimateCategory(this.selectedControl.initialSeconds, this.increment);
	}

	get totalGames(): number {
		return this.ratingRows.reduce((sum, r) => sum + r.gamesPlayed, 0);
	}

	ngOnInit() {
		this.load();
		this.carregarRatings();
		this.refreshTimer = setInterval(() => this.load(), 5000);
	}

	ngOnDestroy() {
		clearInterval(this.refreshTimer);
	}

	selecionarControle(preset: ITimeControlPreset) {
		this.selectedControl = preset;
	}

	categoryLabel(category?: string | null): string {
		return this.timeControlService.categoryLabel(category);
	}

	presetCategoryLabel(preset: ITimeControlPreset): string {
		return this.timeControlService.categoryLabel(
			this.timeControlService.estimateCategory(preset.initialSeconds, this.increment),
		);
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

	async carregarRatings() {
		this.isLoadingRatings = true;
		this.ratingsError = false;
		try {
			this.ratingRows = this.ratingService.toRatingRows(await this.ratingService.getMyRatings());
		} catch {
			this.ratingsError = true;
		}
		this.isLoadingRatings = false;
		this.cdr.detectChanges();
	}

	async criar() {
		await this.run(async () => {
			const game = await this.gameService.createGame({
				initialTimeSeconds: this.selectedControl.initialSeconds,
				incrementSeconds: this.increment,
			});
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
