jest.mock('chessground', () => ({ Chessground: jest.fn() }));
jest.mock('sweetalert2', () => ({ __esModule: true, default: { fire: jest.fn() } }));

import { Chessground } from 'chessground';
import Swal from 'sweetalert2';
import { of, Subject, throwError } from 'rxjs';
import { GameBoard } from './game-board';
import { TimeControlService } from '../../services/chess/time-control.service';

const state = (over: Record<string, any> = {}) => ({
	gameId: 5,
	fen: 'start',
	moveNumber: 0,
	turn: 'WHITE',
	myColor: 'WHITE',
	gameStatus: 'IN_PROGRESS',
	boardStatus: 'NORMAL',
	opponentNickname: 'ana',
	initialTimeSeconds: 600,
	incrementSeconds: 2,
	timeControlCategory: 'RAPID',
	result: null,
	endReason: null,
	myRatingChange: null,
	myRating: 1200,
	opponentRating: 1250,
	whiteTimeMs: 600000,
	blackTimeMs: 600000,
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
	result: null,
	endReason: null,
	whiteRatingChange: null,
	blackRatingChange: null,
	whiteTimeMs: 601000,
	blackTimeMs: 600000,
	...over,
});

const timeoutEvent = (over: Record<string, any> = {}) =>
	event({
		type: 'TIMEOUT',
		moveNumber: 0,
		from: null,
		to: null,
		turn: 'WHITE',
		gameStatus: 'FINISHED',
		result: 'BLACK_WINS',
		endReason: 'TIMEOUT',
		whiteRatingChange: -8,
		blackRatingChange: 8,
		whiteTimeMs: 0,
		blackTimeMs: 600000,
		...over,
	});

describe('GameBoard', () => {
	let cg: { set: jest.Mock; destroy: jest.Mock; cancelPremove: jest.Mock; state: { pieces: Map<string, any> } };
	let events$: Subject<any>;
	let gameService: Record<string, jest.Mock>;
	let socket: { events$: Subject<any>; connect: jest.Mock; disconnect: jest.Mock };
	let authService: { me: jest.Mock };
	let router: { navigateByUrl: jest.Mock };
	let dialog: { open: jest.Mock };
	let cdr: { detectChanges: jest.Mock };
	let component: GameBoard;

	const config = () => (Chessground as unknown as jest.Mock).mock.calls.at(-1)?.[1];

	beforeEach(() => {
		cg = { set: jest.fn(), destroy: jest.fn(), cancelPremove: jest.fn(), state: { pieces: new Map() } };
		(Chessground as unknown as jest.Mock).mockReset().mockReturnValue(cg);
		(Swal.fire as unknown as jest.Mock).mockReset();
		events$ = new Subject();
		gameService = {
			getGame: jest.fn().mockResolvedValue(state()),
			cancelGame: jest.fn().mockResolvedValue(undefined),
			move: jest.fn().mockResolvedValue(event()),
			resign: jest.fn().mockResolvedValue(timeoutEvent({
				type: 'RESIGNED', endReason: 'RESIGNATION', whiteRatingChange: -10, blackRatingChange: 10,
			})),
			claimTimeout: jest.fn().mockResolvedValue(timeoutEvent()),
		};
		socket = { events$, connect: jest.fn(), disconnect: jest.fn() };
		authService = { me: jest.fn().mockReturnValue(of({ data: { nickname: 'eu' } })) };
		router = { navigateByUrl: jest.fn() };
		dialog = { open: jest.fn().mockReturnValue({ afterClosed: () => of('q') }) };
		cdr = { detectChanges: jest.fn() };

		component = new GameBoard(
			{ snapshot: { paramMap: { get: () => '5' } } } as any,
			router as any,
			dialog as any,
			gameService as any,
			socket as any,
			authService as any,
			cdr as any,
			{ onDestroy: jest.fn(() => jest.fn()) } as any,
			new TimeControlService(),
		);
		component.boardRef = { nativeElement: document.createElement('div') } as any;
	});

	afterEach(() => {
		component.ngOnDestroy();
		jest.useRealTimers();
	});

	describe('ngOnInit', () => {
		it('loads the game, builds the board and connects the socket', async () => {
			await component.ngOnInit();

			expect(component.gameId).toBe(5);
			expect(component.opponentNickname).toBe('ana');
			expect(component.myNickname).toBe('eu');
			expect(component.categoryText).toBe('Rapid');
			expect(component.initialTimeSeconds).toBe(600);
			expect(component.incrementSeconds).toBe(2);
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
			expect(component.opponentColor).toBe('white');
		});

		it('keeps going without the nickname when it cannot be loaded', async () => {
			authService.me.mockReturnValue(throwError(() => new Error('offline')));

			await component.ngOnInit();

			expect(component.myNickname).toBe('');
			expect(socket.connect).toHaveBeenCalled();
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

		it('takes the clocks from the move event', () => {
			events$.next(event({ whiteTimeMs: 598000, blackTimeMs: 590000 }));

			expect(component.whiteTimeMs).toBe(598000);
			expect(component.blackTimeMs).toBe(590000);
		});

		it('finishes on checkmate and settles the rating change once', () => {
			events$.next(event({
				gameStatus: 'FINISHED', boardStatus: 'CHECKMATE', result: 'WHITE_WINS', endReason: 'CHECKMATE',
				whiteRatingChange: 9, blackRatingChange: -9,
			}));

			expect(component.gameStatus).toBe('FINISHED');
			expect(component.resultText).toBe('Xeque-mate: você venceu!');
			expect(component.myRating).toBe(1209);
			expect(component.opponentRating).toBe(1241);
			expect(component.myRatingChange).toBe(9);
			expect(component.opponentRatingChange).toBe(-9);
		});

		it('fetches the final state when the finishing event has no rating change', async () => {
			gameService['getGame'].mockClear();

			events$.next(event({ gameStatus: 'FINISHED', result: 'WHITE_WINS', endReason: 'CHECKMATE' }));
			await Promise.resolve();

			expect(gameService['getGame']).toHaveBeenCalled();
		});

		it('does not settle ratings twice', () => {
			const finished = { gameStatus: 'FINISHED', whiteRatingChange: 5, blackRatingChange: -5, result: 'WHITE_WINS' };
			events$.next(event(finished));
			events$.next(timeoutEvent({ ...finished, type: 'RESIGNED', endReason: 'RESIGNATION' }));

			expect(component.myRating).toBe(1205);
		});

		it('applies a TIMEOUT event coming from the opponent client', () => {
			events$.next(timeoutEvent());

			expect(component.gameStatus).toBe('FINISHED');
			expect(component.endReason).toBe('TIMEOUT');
			expect(component.whiteTimeMs).toBe(0);
			expect(cg.set).toHaveBeenCalledWith(expect.objectContaining({ fen: 'fen1' }));
		});
	});

	describe('applyState', () => {
		it('keeps the server rating change when the game is already finished', async () => {
			gameService['getGame'].mockResolvedValue(state({
				gameStatus: 'FINISHED', myRatingChange: -5, myRating: 1195, result: 'BLACK_WINS', endReason: 'CHECKMATE',
			}));

			await component.ngOnInit();
			events$.next(timeoutEvent({ whiteRatingChange: -7, blackRatingChange: 7 }));

			expect(component.myRatingChange).toBe(-5);
			expect(component.myRating).toBe(1195);
		});

		it('leaves the clocks empty when the server sends no time', async () => {
			gameService['getGame'].mockResolvedValue(state({ whiteTimeMs: undefined, blackTimeMs: undefined }));

			await component.ngOnInit();

			expect(component.whiteTimeMs).toBeNull();
			expect(component.blackTimeMs).toBeNull();
		});
	});

	describe('getters', () => {
		it('computes turn and color helpers', () => {
			component.gameStatus = 'IN_PROGRESS';

			expect(component.isMyTurn).toBe(true);
			expect(component.canResign).toBe(true);

			component.myColor = 'black';
			expect(component.isMyTurn).toBe(false);
			expect(component.opponentColor).toBe('white');

			component.gameStatus = 'WAITING';
			expect(component.isMyTurn).toBe(false);
			expect(component.canResign).toBe(false);
		});

		it('maps the clocks to me and to the opponent', () => {
			component.whiteTimeMs = 1000;
			component.blackTimeMs = 2000;

			expect(component.myTimeMs).toBe(1000);
			expect(component.opponentTimeMs).toBe(2000);

			component.myColor = 'black';
			expect(component.myTimeMs).toBe(2000);
			expect(component.opponentTimeMs).toBe(1000);
		});

		it('describes the result', () => {
			component.boardStatus = 'DRAW';
			expect(component.resultText).toBe('Empate');

			component.boardStatus = 'CHECKMATE';
			component.turn = 'WHITE';
			expect(component.resultText).toBe('Xeque-mate: você perdeu');

			component.turn = 'BLACK';
			expect(component.resultText).toBe('Xeque-mate: você venceu!');

			component.boardStatus = 'NORMAL';
			expect(component.resultText).toBe('Partida encerrada');
		});

		it('describes results that come with a reason', () => {
			component.result = 'WHITE_WINS';
			component.endReason = 'CHECKMATE';
			expect(component.resultText).toBe('Xeque-mate: você venceu!');
			component.result = 'BLACK_WINS';
			expect(component.resultText).toBe('Xeque-mate: você perdeu');

			component.endReason = 'RESIGNATION';
			expect(component.resultText).toBe('Você desistiu da partida');
			component.result = 'WHITE_WINS';
			expect(component.resultText).toBe('O adversário desistiu: você venceu!');

			component.endReason = 'TIMEOUT';
			expect(component.resultText).toBe('Tempo esgotado: você venceu!');
			component.result = 'BLACK_WINS';
			expect(component.resultText).toBe('Tempo esgotado: você perdeu');

			component.result = 'DRAW';
			expect(component.resultText).toBe('Empate');
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

	describe('desistir', () => {
		beforeEach(() => component.ngOnInit());

		it('resigns after confirmation and shows the result', async () => {
			(Swal.fire as unknown as jest.Mock).mockResolvedValue({ isConfirmed: true });

			await component.desistir();

			expect(gameService['resign']).toHaveBeenCalledWith(5);
			expect(component.gameStatus).toBe('FINISHED');
			expect(component.resultText).toBe('Você desistiu da partida');
			expect(component.myRatingChange).toBe(-10);
			expect(component.isResigning).toBe(false);
		});

		it('does nothing when the confirmation is dismissed', async () => {
			(Swal.fire as unknown as jest.Mock).mockResolvedValue({ isConfirmed: false });

			await component.desistir();

			expect(gameService['resign']).not.toHaveBeenCalled();
		});

		it('does nothing when the game is not in progress', async () => {
			component.gameStatus = 'WAITING';

			await component.desistir();

			expect(Swal.fire).not.toHaveBeenCalled();
		});

		it('shows the error and resyncs when resigning fails', async () => {
			(Swal.fire as unknown as jest.Mock).mockResolvedValue({ isConfirmed: true });
			gameService['resign'].mockRejectedValue({ error: { message: 'Já terminou' } });
			gameService['getGame'].mockClear();

			await component.desistir();

			expect(component.errorMessage).toBe('Já terminou');
			expect(gameService['getGame']).toHaveBeenCalled();
			expect(component.isResigning).toBe(false);
		});

		it('uses a fallback message when resigning fails without a body', async () => {
			(Swal.fire as unknown as jest.Mock).mockResolvedValue({ isConfirmed: true });
			gameService['resign'].mockRejectedValue(new Error('offline'));

			await component.desistir();

			expect(component.errorMessage).toBe('Não foi possível desistir da partida.');
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

		it('ends the game as a loss when the server answers a late move with TIMEOUT', async () => {
			gameService['move'].mockResolvedValue(timeoutEvent());

			await component.onMove('e2', 'e4');

			expect(component.gameStatus).toBe('FINISHED');
			expect(component.resultText).toBe('Tempo esgotado: você perdeu');
			expect(component.moves).toHaveLength(0);
			expect(cg.set).toHaveBeenCalledWith(expect.objectContaining({ fen: 'fen1' }));
		});
	});

	describe('clocks', () => {
		beforeEach(() => jest.useFakeTimers());

		it('counts down only the clock of the side to move', async () => {
			await component.ngOnInit();

			await jest.advanceTimersByTimeAsync(3000);

			expect(component.whiteTimeMs).toBe(597000);
			expect(component.blackTimeMs).toBe(600000);
			expect(component.myTimeMs).toBe(597000);
			expect(component.opponentTimeMs).toBe(600000);
		});

		it('passes the countdown to the other side after a move', async () => {
			await component.ngOnInit();
			await jest.advanceTimersByTimeAsync(1000);

			events$.next(event({ whiteTimeMs: 601000, blackTimeMs: 600000, turn: 'BLACK' }));
			await jest.advanceTimersByTimeAsync(2000);

			expect(component.whiteTimeMs).toBe(601000);
			expect(component.blackTimeMs).toBe(598000);
		});

		it('reanchors the clocks with the server time on resync', async () => {
			await component.ngOnInit();
			await jest.advanceTimersByTimeAsync(1000);
			gameService['getGame'].mockResolvedValue(state({ whiteTimeMs: 590000 }));

			await socket.connect.mock.calls[0][1]();

			expect(component.whiteTimeMs).toBe(590000);
		});

		it('does not run a clock before the game starts or after it ends', async () => {
			gameService['getGame'].mockResolvedValue(state({ gameStatus: 'WAITING' }));
			await component.ngOnInit();
			expect(jest.getTimerCount()).toBe(0);

			gameService['getGame'].mockResolvedValue(state({ gameStatus: 'FINISHED' }));
			await socket.connect.mock.calls[0][1]();
			expect(jest.getTimerCount()).toBe(0);
		});

		it('stops the ticker when the status leaves IN_PROGRESS', async () => {
			await component.ngOnInit();
			expect(jest.getTimerCount()).toBe(1);

			component.gameStatus = 'WAITING';
			await jest.advanceTimersByTimeAsync(100);

			expect(jest.getTimerCount()).toBe(0);
		});

		it('stops the ticker on destroy', async () => {
			await component.ngOnInit();

			component.ngOnDestroy();

			expect(jest.getTimerCount()).toBe(0);
		});

		it('claims the timeout when my clock reaches zero and ends the game as a loss', async () => {
			gameService['getGame'].mockResolvedValue(state({ whiteTimeMs: 300 }));
			await component.ngOnInit();

			await jest.advanceTimersByTimeAsync(400);

			expect(gameService['claimTimeout']).toHaveBeenCalledTimes(1);
			expect(gameService['claimTimeout']).toHaveBeenCalledWith(5);
			expect(component.gameStatus).toBe('FINISHED');
			expect(component.endReason).toBe('TIMEOUT');
			expect(component.whiteTimeMs).toBe(0);
			expect(component.resultText).toBe('Tempo esgotado: você perdeu');
			expect(component.myRatingChange).toBe(-8);
			expect(jest.getTimerCount()).toBe(0);
		});

		it('also claims the timeout when the opponent clock reaches zero and I win', async () => {
			gameService['getGame'].mockResolvedValue(state({ myColor: 'BLACK', turn: 'WHITE', whiteTimeMs: 200 }));
			await component.ngOnInit();

			await jest.advanceTimersByTimeAsync(300);

			expect(gameService['claimTimeout']).toHaveBeenCalledTimes(1);
			expect(component.resultText).toBe('Tempo esgotado: você venceu!');
			expect(component.myRatingChange).toBe(8);
		});

		it('counts down the black clock when it is black to move and claims at zero', async () => {
			gameService['getGame'].mockResolvedValue(state({ turn: 'BLACK', blackTimeMs: 200 }));
			gameService['claimTimeout'].mockResolvedValue(timeoutEvent({
				turn: 'BLACK', result: 'WHITE_WINS', whiteTimeMs: 600000, blackTimeMs: 0,
			}));
			await component.ngOnInit();

			await jest.advanceTimersByTimeAsync(300);

			expect(component.blackTimeMs).toBe(0);
			expect(gameService['claimTimeout']).toHaveBeenCalledTimes(1);
			expect(component.resultText).toBe('Tempo esgotado: você venceu!');
		});

		it('does not send a second claim while the first one is pending', async () => {
			gameService['getGame'].mockResolvedValue(state({ whiteTimeMs: 200 }));
			gameService['claimTimeout'].mockReturnValue(new Promise(() => {}));
			await component.ngOnInit();

			await jest.advanceTimersByTimeAsync(1500);

			expect(gameService['claimTimeout']).toHaveBeenCalledTimes(1);
		});

		it('resyncs and retries later when the server says the time has not expired yet', async () => {
			gameService['getGame']
				.mockResolvedValueOnce(state({ whiteTimeMs: 300 }))
				.mockResolvedValue(state({ whiteTimeMs: 200 }));
			gameService['claimTimeout']
				.mockRejectedValueOnce({ error: { message: 'O tempo do jogador ainda não esgotou' } })
				.mockResolvedValue(timeoutEvent());
			await component.ngOnInit();

			await jest.advanceTimersByTimeAsync(600);
			expect(gameService['claimTimeout']).toHaveBeenCalledTimes(1);
			expect(gameService['getGame']).toHaveBeenCalledTimes(2);
			expect(component.gameStatus).toBe('IN_PROGRESS');

			await jest.advanceTimersByTimeAsync(1000);
			expect(gameService['claimTimeout']).toHaveBeenCalledTimes(2);
			expect(component.gameStatus).toBe('FINISHED');
		});
	});
});
