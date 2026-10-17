import { TestBed } from '@angular/core/testing';
import { provideRouter, Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { GameService } from '../../services/game/game.service';
import { Lobby } from './lobby';

describe('Lobby', () => {
	let gameService: Record<string, jest.Mock>;
	let snackBar: { open: jest.Mock };
	let navigate: jest.SpyInstance;
	let component: Lobby;

	beforeEach(() => {
		gameService = {
			listOpen: jest.fn().mockResolvedValue([{ gameId: 1, status: 'WAITING', myColor: null, opponentNickname: 'ana' }]),
			listMine: jest.fn().mockResolvedValue([{ gameId: 2, status: 'IN_PROGRESS', myColor: 'WHITE', opponentNickname: 'bob' }]),
			createGame: jest.fn().mockResolvedValue({ gameId: 7 }),
			joinGame: jest.fn().mockResolvedValue({ gameId: 1 }),
			cancelGame: jest.fn().mockResolvedValue(undefined),
		};
		snackBar = { open: jest.fn() };

		TestBed.configureTestingModule({
			providers: [
				provideRouter([]),
				{ provide: GameService, useValue: gameService },
				{ provide: MatSnackBar, useValue: snackBar },
			],
		});
		navigate = jest.spyOn(TestBed.inject(Router), 'navigate').mockResolvedValue(true);
		component = TestBed.createComponent(Lobby).componentInstance;
	});

	afterEach(() => jest.useRealTimers());

	it('loads games on init and refreshes periodically until destroyed', () => {
		jest.useFakeTimers();
		const load = jest.spyOn(component, 'load').mockResolvedValue();

		component.ngOnInit();
		expect(load).toHaveBeenCalledTimes(1);

		jest.advanceTimersByTime(5000);
		expect(load).toHaveBeenCalledTimes(2);

		component.ngOnDestroy();
		jest.advanceTimersByTime(10000);
		expect(load).toHaveBeenCalledTimes(2);
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
});
