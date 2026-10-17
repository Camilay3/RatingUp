jest.mock('chessground', () => ({ Chessground: jest.fn() }));

import { Chessground } from 'chessground';
import { of, Subject } from 'rxjs';
import { GameBoard } from './game-board';

const state = (over: Record<string, any> = {}) => ({
	gameId: 5,
	fen: 'start',
	moveNumber: 0,
	turn: 'WHITE',
	myColor: 'WHITE',
	gameStatus: 'IN_PROGRESS',
	boardStatus: 'NORMAL',
	opponentNickname: 'ana',
	...over,
});

const event = (over: Record<string, any> = {}) => ({
	type: 'MOVE',
	gameId: 5,
	moveNumber: 1,
	from: 'e2',
	to: 'e4',
	fen: 'fen1',
	turn: 'BLACK',
	boardStatus: 'NORMAL',
	gameStatus: 'IN_PROGRESS',
	...over,
});

describe('GameBoard', () => {
	let cg: { set: jest.Mock; destroy: jest.Mock; cancelPremove: jest.Mock; state: { pieces: Map<string, any> } };
	let events$: Subject<any>;
	let gameService: Record<string, jest.Mock>;
	let socket: { events$: Subject<any>; connect: jest.Mock; disconnect: jest.Mock };
	let router: { navigateByUrl: jest.Mock };
	let dialog: { open: jest.Mock };
	let cdr: { detectChanges: jest.Mock };
	let component: GameBoard;

	const config = () => (Chessground as unknown as jest.Mock).mock.calls.at(-1)?.[1];

	beforeEach(() => {
		cg = { set: jest.fn(), destroy: jest.fn(), cancelPremove: jest.fn(), state: { pieces: new Map() } };
		(Chessground as unknown as jest.Mock).mockReset().mockReturnValue(cg);
		events$ = new Subject();
		gameService = {
			getGame: jest.fn().mockResolvedValue(state()),
			cancelGame: jest.fn().mockResolvedValue(undefined),
			move: jest.fn().mockResolvedValue(event()),
		};
		socket = { events$, connect: jest.fn(), disconnect: jest.fn() };
		router = { navigateByUrl: jest.fn() };
		dialog = { open: jest.fn().mockReturnValue({ afterClosed: () => of('q') }) };
		cdr = { detectChanges: jest.fn() };

		component = new GameBoard(
			{ snapshot: { paramMap: { get: () => '5' } } } as any,
			router as any,
			dialog as any,
			gameService as any,
			socket as any,
			cdr as any,
			{ onDestroy: jest.fn(() => jest.fn()) } as any,
		);
		component.boardRef = { nativeElement: document.createElement('div') } as any;
	});

	describe('ngOnInit', () => {
		it('loads the game, builds the board and connects the socket', async () => {
			await component.ngOnInit();

			expect(component.gameId).toBe(5);
			expect(component.opponentNickname).toBe('ana');
			expect(config()).toMatchObject({ fen: 'start', orientation: 'white', turnColor: 'white' });
			expect(config().movable.color).toBe('white');
			expect(socket.connect).toHaveBeenCalledWith(5, expect.any(Function));
			expect(cdr.detectChanges).toHaveBeenCalled();
		});

		it('leaves the board idle when it is not my turn and plays as black', async () => {
			gameService['getGame'].mockResolvedValue(state({ myColor: 'BLACK', turn: 'WHITE' }));

			await component.ngOnInit();

			expect(config().orientation).toBe('black');
			expect(config().movable.color).toBeUndefined();
		});

		it('forwards a user move from the board to onMove', async () => {
			await component.ngOnInit();
			const onMove = jest.spyOn(component, 'onMove').mockResolvedValue();

			config().movable.events.after('e2', 'e4');

			expect(onMove).toHaveBeenCalledWith('e2', 'e4');
		});

		it('shows the server message when loading fails', async () => {
			gameService['getGame'].mockRejectedValue({ error: { message: 'Partida inexistente' } });

			await component.ngOnInit();

			expect(component.errorMessage).toBe('Partida inexistente');
			expect(socket.connect).not.toHaveBeenCalled();
		});

		it('shows a fallback message when loading fails without a body', async () => {
			gameService['getGame'].mockRejectedValue(new Error('offline'));

			await component.ngOnInit();

			expect(component.errorMessage).toBe('Não foi possível carregar a partida.');
		});

		it('resyncs when the socket (re)connects', async () => {
			await component.ngOnInit();
			gameService['getGame'].mockResolvedValue(state({ moveNumber: 3, fen: 'synced', turn: 'BLACK' }));

			await socket.connect.mock.calls[0][1]();

			expect(cg.set).toHaveBeenCalledWith(expect.objectContaining({ fen: 'synced', turnColor: 'black' }));
		});

		it('ignores a sync older than the last applied move', async () => {
			await component.ngOnInit();
			events$.next(event({ moveNumber: 5 }));
			cg.set.mockClear();
			gameService['getGame'].mockResolvedValue(state({ moveNumber: 2 }));

			await socket.connect.mock.calls[0][1]();

			expect(cg.set).not.toHaveBeenCalled();
		});

		it('swallows sync errors', async () => {
			await component.ngOnInit();
			gameService['getGame'].mockRejectedValue(new Error('offline'));

			await expect(socket.connect.mock.calls[0][1]()).resolves.toBeUndefined();
		});
	});

	describe('socket events', () => {
		beforeEach(() => component.ngOnInit());

		it('applies a new move to the board and move list', () => {
			events$.next(event());

			expect(component.moves).toEqual([{ from: 'e2', to: 'e4', mine: true }]);
			expect(component.turn).toBe('BLACK');
			expect(cg.set).toHaveBeenCalledWith(expect.objectContaining({ fen: 'fen1', lastMove: ['e2', 'e4'] }));
		});

		it('marks the opponent moves as not mine', () => {
			events$.next(event({ turn: 'WHITE' }));

			expect(component.moves[0].mine).toBe(false);
		});

		it('ignores duplicated or old moves', () => {
			events$.next(event());
			cg.set.mockClear();
			events$.next(event());

			expect(component.moves).toHaveLength(1);
			expect(cg.set).not.toHaveBeenCalled();
		});

		it('resyncs the state when the game starts', async () => {
			gameService['getGame'].mockClear();

			events$.next(event({ type: 'STARTED' }));
			await Promise.resolve();

			expect(gameService['getGame']).toHaveBeenCalledWith(5);
		});
	});

	describe('getters', () => {
		it('computes turn and color texts', () => {
			component.gameStatus = 'IN_PROGRESS';

			expect(component.isMyTurn).toBe(true);
			expect(component.corTexto).toBe('brancas');

			component.myColor = 'black';
			expect(component.isMyTurn).toBe(false);
			expect(component.corTexto).toBe('pretas');

			component.gameStatus = 'WAITING';
			expect(component.isMyTurn).toBe(false);
		});

		it('describes the result', () => {
			component.boardStatus = 'DRAW';
			expect(component.resultText).toBe('Empate');

			component.boardStatus = 'CHECKMATE';
			component.turn = 'WHITE';
			expect(component.resultText).toBe('Xeque-mate: você perdeu');

			component.turn = 'BLACK';
			expect(component.resultText).toBe('Xeque-mate: você venceu!');

			component.boardStatus = 'RESIGNED';
			expect(component.resultText).toBe('Partida encerrada');
		});
	});

	describe('navigation', () => {
		it('goes back to the lobby', () => {
			component.voltar();
			expect(router.navigateByUrl).toHaveBeenCalledWith('/jogar');
		});

		it('cancels the game and goes back', async () => {
			component.gameId = 5;

			await component.cancelar();

			expect(gameService['cancelGame']).toHaveBeenCalledWith(5);
			expect(router.navigateByUrl).toHaveBeenCalledWith('/jogar');
		});

		it('shows the server message when cancelling fails', async () => {
			gameService['cancelGame'].mockRejectedValue({ error: { message: 'Já começou' } });

			await component.cancelar();

			expect(component.errorMessage).toBe('Já começou');
			expect(router.navigateByUrl).not.toHaveBeenCalled();
		});

		it('shows a fallback message when cancelling fails without a body', async () => {
			gameService['cancelGame'].mockRejectedValue(new Error('offline'));

			await component.cancelar();

			expect(component.errorMessage).toBe('Não foi possível cancelar a partida.');
		});

		it('releases the socket and the board on destroy', async () => {
			await component.ngOnInit();

			component.ngOnDestroy();

			expect(socket.disconnect).toHaveBeenCalled();
			expect(cg.destroy).toHaveBeenCalled();
		});

		it('destroys safely before the board exists', () => {
			expect(() => component.ngOnDestroy()).not.toThrow();
		});
	});

	describe('onMove', () => {
		beforeEach(() => component.ngOnInit());

		it('sends a normal move and applies the answer', async () => {
			await component.onMove('e2', 'e4');

			expect(gameService['move']).toHaveBeenCalledWith(5, 'e2', 'e4', undefined);
			expect(component.moves).toHaveLength(1);
		});

		it('asks for the promotion piece when a pawn reaches the last rank', async () => {
			cg.state.pieces.set('e8', { role: 'pawn' });

			await component.onMove('e7', 'e8');

			expect(dialog.open).toHaveBeenCalledWith(
				expect.anything(),
				expect.objectContaining({ data: { color: 'white' } }),
			);
			expect(gameService['move']).toHaveBeenCalledWith(5, 'e7', 'e8', 'q');
		});

		it('also promotes on the first rank', async () => {
			cg.state.pieces.set('d1', { role: 'pawn' });

			await component.onMove('d2', 'd1');

			expect(dialog.open).toHaveBeenCalled();
		});

		it('does not ask for promotion on non-pawn pieces', async () => {
			cg.state.pieces.set('e8', { role: 'rook' });

			await component.onMove('e7', 'e8');

			expect(dialog.open).not.toHaveBeenCalled();
		});

		it('resets the board when the promotion is cancelled', async () => {
			dialog.open.mockReturnValue({ afterClosed: () => of(undefined) });
			cg.state.pieces.set('e8', { role: 'pawn' });

			await component.onMove('e7', 'e8');

			expect(cg.cancelPremove).toHaveBeenCalled();
			expect(cg.set).toHaveBeenCalledWith(expect.objectContaining({ fen: 'start' }));
			expect(gameService['move']).not.toHaveBeenCalled();
		});

		it('resets the board and shows the server message when the move is rejected', async () => {
			gameService['move'].mockRejectedValue({ error: { message: 'Lance ilegal' } });

			await component.onMove('e2', 'e5');

			expect(cg.cancelPremove).toHaveBeenCalled();
			expect(component.errorMessage).toBe('Lance ilegal');
		});

		it('shows a fallback message when the move fails without a body', async () => {
			gameService['move'].mockRejectedValue(new Error('offline'));

			await component.onMove('e2', 'e5');

			expect(component.errorMessage).toBe('Não foi possível realizar o lance.');
		});
	});
});
