import { TestBed } from '@angular/core/testing';
import { provideRouter, Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { GameService } from '../../services/game/game.service';
import { RatingService } from '../../services/game/rating.service';
import { Lobby } from './lobby';
import { TimeControlService } from '../../services/chess/time-control.service';

// Reaproveita a lógica real do RatingService, já que o serviço é mockado nos testes
class RatingServiceLogic extends RatingService {
	constructor() {
		super({} as any, new TimeControlService());
	}
}

describe('Lobby', () => {
	let gameService: Record<string, jest.Mock>;
	let ratingService: {
		getMyRatings: jest.Mock;
		toRatingRows: jest.Mock;
		skillLabel: jest.Mock;
	};
	let snackBar: { open: jest.Mock };
	let navigate: jest.SpyInstance;
	let component: Lobby;

	const timeControl = new TimeControlService();
	const TIME_CONTROL_PRESETS = timeControl.presets;

	beforeEach(() => {
		gameService = {
			listOpen: jest.fn().mockResolvedValue([{
				gameId: 1, status: 'WAITING', myColor: null, opponentNickname: 'ana',
				initialTimeSeconds: 600, incrementSeconds: 0, timeControlCategory: 'RAPID',
			}]),
			listMine: jest.fn().mockResolvedValue([{
				gameId: 2, status: 'IN_PROGRESS', myColor: 'WHITE', opponentNickname: 'bob',
				initialTimeSeconds: 180, incrementSeconds: 2, timeControlCategory: 'BLITZ',
			}]),
			createGame: jest.fn().mockResolvedValue({ gameId: 7 }),
			joinGame: jest.fn().mockResolvedValue({ gameId: 1 }),
			cancelGame: jest.fn().mockResolvedValue(undefined),
		};

		ratingService = {
			getMyRatings: jest.fn().mockResolvedValue([{
				category: 'BLITZ', rating: 1300, gamesPlayed: 4, wins: 3, losses: 1, draws: 0,
				performanceRating: 1400, skillLevel: 'NOVICE',
			}]),
			toRatingRows: jest.fn((rows: any[]) => new RatingServiceLogic().toRatingRows(rows)),
			skillLabel: jest.fn((level: string) => new RatingServiceLogic().skillLabel(level as any)),
		};

		snackBar = { open: jest.fn() };

		TestBed.configureTestingModule({
			providers: [
				provideRouter([]),
				{ provide: GameService, useValue: gameService },
				{ provide: RatingService, useValue: ratingService },
				{ provide: MatSnackBar, useValue: snackBar },
			],
		});

		navigate = jest.spyOn(TestBed.inject(Router), 'navigate').mockResolvedValue(true);
		component = TestBed.createComponent(Lobby).componentInstance;
	});

	afterEach(() => jest.useRealTimers());

	it('loads games and ratings on init and refreshes games periodically until destroyed', () => {
		jest.useFakeTimers();
		const load = jest.spyOn(component, 'load').mockResolvedValue();
		const ratings = jest.spyOn(component, 'carregarRatings').mockResolvedValue();

		component.ngOnInit();
		expect(load).toHaveBeenCalledTimes(1);
		expect(ratings).toHaveBeenCalledTimes(1);

		jest.advanceTimersByTime(5000);
		expect(load).toHaveBeenCalledTimes(2);

		component.ngOnDestroy();
		jest.advanceTimersByTime(10000);
		expect(load).toHaveBeenCalledTimes(2);
		expect(ratings).toHaveBeenCalledTimes(1);
	});

	it('fills the open and own game lists', async () => {
		await component.load();

		expect(component.openGames).toHaveLength(1);
		expect(component.myGames).toHaveLength(1);
		expect(component.isLoading).toBe(false);
	});

	it('keeps going when loading fails', async () => {
		gameService['listOpen'].mockRejectedValue(new Error('offline'));

		await component.load();

		expect(component.openGames).toEqual([]);
		expect(component.isLoading).toBe(false);
	});

	describe('user rating summary', () => {
		it('builds one row per category and totals the games played', async () => {
			await component.carregarRatings();

			expect(component.ratingRows.map(r => r.category)).toEqual(['BULLET', 'BLITZ', 'RAPID', 'CLASSICAL']);
			expect(component.ratingRows[1]).toMatchObject({ rating: 1300, wins: 3 });
			expect(component.ratingRows[0].rating).toBeNull();
			expect(component.totalGames).toBe(4);
			expect(component.isLoadingRatings).toBe(false);
			expect(component.ratingsError).toBe(false);
		});

		it('flags an error and recovers on retry', async () => {
			ratingService.getMyRatings.mockRejectedValueOnce(new Error('offline'));

			await component.carregarRatings();
			expect(component.ratingsError).toBe(true);
			expect(component.isLoadingRatings).toBe(false);

			await component.carregarRatings();
			expect(component.ratingsError).toBe(false);
			expect(component.ratingRows).toHaveLength(4);
		});
	});

	describe('time control and increment', () => {
		it('starts on Rapid without increment', () => {
			expect(component.selectedControl.category).toBe('RAPID');
			expect(component.increment).toBe(0);
			expect(component.effectiveCategory).toBe('RAPID');
		});

		it('selects a preset', () => {
			component.selecionarControle(TIME_CONTROL_PRESETS[0]);

			expect(component.selectedControl.category).toBe('BULLET');
			expect(component.effectiveCategory).toBe('BULLET');
		});

		it('moves a preset to the category it will really be rated in when an increment is set', () => {
			component.selecionarControle(TIME_CONTROL_PRESETS[0]); // 1 min
			component.increment = 3; // 60 + 40*3 = 180s -> Blitz

			expect(component.effectiveCategory).toBe('BLITZ');
			expect(component.presetCategoryLabel(TIME_CONTROL_PRESETS[0])).toBe('Blitz');
			expect(component.presetCategoryLabel(TIME_CONTROL_PRESETS[3])).toBe('Clássico');
		});

		it('creates the game with the chosen time and increment', async () => {
			component.selecionarControle(TIME_CONTROL_PRESETS[1]);
			component.increment = 5;

			await component.criar();

			expect(gameService['createGame']).toHaveBeenCalledWith({
				initialTimeSeconds: 300,
				incrementSeconds: 5,
			});
		});
	});

	it('creates a game and navigates to it', async () => {
		await component.criar();

		expect(navigate).toHaveBeenCalledWith(['/partida', 7]);
		expect(component.isBusy).toBe(false);
	});

	it('joins a game and navigates to it', async () => {
		await component.entrar(1);

		expect(gameService['joinGame']).toHaveBeenCalledWith(1);
		expect(navigate).toHaveBeenCalledWith(['/partida', 1]);
	});

	it('cancels a game and reloads the lists', async () => {
		await component.cancelar(1);

		expect(gameService['cancelGame']).toHaveBeenCalledWith(1);
		expect(gameService['listOpen']).toHaveBeenCalled();
	});

	it('shows the server message when an action fails', async () => {
		gameService['createGame'].mockRejectedValue({ error: { message: 'Já existe partida' } });

		await component.criar();

		expect(snackBar.open).toHaveBeenCalledWith('Já existe partida', 'Fechar', { duration: 3000 });
		expect(gameService['listOpen']).toHaveBeenCalled();
		expect(component.isBusy).toBe(false);
	});

	it('shows a fallback message when the error has no body', async () => {
		gameService['joinGame'].mockRejectedValue(new Error('offline'));

		await component.entrar(1);

		expect(snackBar.open).toHaveBeenCalledWith('Erro ao entrar na partida', 'Fechar', { duration: 3000 });
	});

	it('ignores actions while another one is running', async () => {
		component.isBusy = true;

		await component.criar();

		expect(gameService['createGame']).not.toHaveBeenCalled();
	});

	it('renders the "Ritmo · ● tempo|acréscimo" buttons and the rating summary', async () => {
		const fixture = TestBed.createComponent(Lobby);
		fixture.detectChanges();
		// não usa whenStable(): o setInterval de 5s do lobby manteria a zona instável
		await new Promise(resolve => setTimeout(resolve));
		fixture.detectChanges();

		const el: HTMLElement = fixture.nativeElement;
		const buttons = Array.from(el.querySelectorAll('.opcoes button'));
		expect(buttons).toHaveLength(4);
		expect(buttons[0].textContent).toContain('Bullet');
		expect(buttons[0].textContent).toContain('1|0');
		expect(el.querySelectorAll('app-ratings-panel .card')).toHaveLength(4);
		expect(el.querySelector('app-increment-selector')).not.toBeNull();
		expect(el.querySelectorAll('.atalho')).toHaveLength(0);

		// o resumo de desempenho mostra só o nome do tipo, sem tempo nem acréscimo
		const blitzRow = el.querySelectorAll('app-ratings-panel .card')[1];
		expect(blitzRow.querySelector('strong')?.textContent?.trim()).toBe('Blitz');
		expect(blitzRow.querySelector('app-time-control-badge')).toBeNull();
		expect(blitzRow.textContent).not.toContain('|');
		expect(blitzRow.textContent).toContain('Performance: 1400');

		fixture.destroy();
	});

});
