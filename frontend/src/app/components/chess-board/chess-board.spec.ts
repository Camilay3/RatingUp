jest.mock('chessground', () => ({ Chessground: jest.fn() }));

import { Chessground } from 'chessground';
import { ChangeDetectorRef } from '@angular/core';
import { ChessBoard } from './chess-board';

describe('ChessBoard', () => {
	let board: { set: jest.Mock; state: { pieces: Map<string, any> } };
	let service: { startChess: jest.Mock; moveChess: jest.Mock };
	let component: ChessBoard;

	beforeEach(() => {
		board = { set: jest.fn(), state: { pieces: new Map([['e2', { color: 'white', role: 'knight' }]]) } };
		(service = { startChess: jest.fn().mockResolvedValue({ sessionId: 2, fen: 'start' }), moveChess: jest.fn() });
		(Chessground as unknown as jest.Mock).mockReturnValue(board);
		component = new ChessBoard(service as any, { detectChanges: jest.fn() } as unknown as ChangeDetectorRef);
		component.boardRef = { nativeElement: document.createElement('div') } as any;
		component.subtopicId = 5;
	});

	it('starts a board session and emits its initial FEN', async () => {
		jest.spyOn(component.fenAtualizado, 'emit');
		component.ngOnInit();
		await Promise.resolve();
		expect(service.startChess).toHaveBeenCalledWith(5);
		expect(component.fenAtualizado.emit).toHaveBeenCalledWith('start');
	});

	it('records a completed move and disables further movement', async () => {
		component['cg'] = board;
		component['sessionId'] = 2;
		component['currentPiece'] = 'WHITE_KNIGHT';
		component['previousFen'] = 'start';
		service.moveChess.mockResolvedValue({ fen: 'next', status: 'COMPLETED', initialFen: 'start' });
		jest.spyOn(component.concluido, 'emit');

		await component.onMove('e2', 'e4');
		expect(service.moveChess).toHaveBeenCalledWith(2, 'WHITE_KNIGHT', 'E2', 'E4');
		expect(component.moves.at(-1)?.status).toBe('COMPLETED');
		expect(component.concluido.emit).toHaveBeenCalled();
		expect(board.set).toHaveBeenCalledWith({ movable: { color: undefined } });
	});

	it('resets after a failed move request', async () => {
		component['cg'] = board;
		component['sessionId'] = 2;
		service.moveChess.mockRejectedValue(new Error('expired'));
		service.startChess.mockResolvedValue({ sessionId: 3, fen: 'restarted' });

		await component.onMove('e2', 'e4');
		expect(component.moves).toEqual([]);
		expect(service.startChess).toHaveBeenCalledWith(5);
	});

	it('configures selection callbacks and handles a wrong move', async () => {
		component.ngOnInit();
		const config = (Chessground as unknown as jest.Mock).mock.calls.at(-1)?.[1];
		config.events.select('e2');
		config.events.select('a1');
		config.movable.events.after('e2', 'e4');
		expect(component['currentPiece']).toBe('WHITE_KNIGHT');
		service.moveChess.mockResolvedValue({ status: 'WRONG_MOVE', fen: 'ignored', initialFen: 'reset' });
		await component.onMove('e2', 'e4');
		expect(component.moves.at(-1)?.error).toBe(true);
		expect(board.set).toHaveBeenCalledWith({ fen: 'reset' });
	});

	it('records a normal move and the computer response', async () => {
		component['cg'] = board;
		component['sessionId'] = 2;
		component['currentPiece'] = 'WHITE_PAWN';
		component['previousFen'] = '8/4p3/8/8/4P3/8/8/8 w - - 0 1';
		service.moveChess.mockResolvedValue({
			status: 'NORMAL',
			initialFen: component['previousFen'],
			fen: '8/8/4p3/8/8/8/8/8 b - - 0 1',
		});
		await component.onMove('e4', 'e5');
		expect(component.moves).toHaveLength(2);
		expect(component.moves[1]).toMatchObject({ isSystem: true, piece: 'BLACK_PAWN' });
		expect(board.set).toHaveBeenCalledWith({ fen: '8/8/4p3/8/8/8/8/8 b - - 0 1' });
	});

	it('covers FEN parsing, piece names, change detection and start errors', async () => {
		const positions = (component as any).fenToPositions('8/8/8/3p4/8/8/4P3/8 w - - 0 1');
		expect(positions.get('d5')).toBe('p');
		expect(positions.get('e2')).toBe('P');
		expect((component as any).getComputerMove(
			'8/8/8/3p4/8/8/4P3/8 w - - 0 1',
			'8/8/3p4/8/8/8/4P3/8 w - - 0 1',
		)).toEqual({ from: 'd5', to: 'd6', piece: 'BLACK_PAWN' });
		expect((component as any).getComputerMove('8/8/8/8/8/8/8/8 w - - 0 1', '8/8/8/8/8/8/8/8 w - - 0 1')).toBeNull();
		expect((component as any).getComputerMove(
			'8/8/8/3p4/8/8/8/8 w - - 0 1',
			'8/8/8/8/8/8/8/8 w - - 0 1',
		)).toBeNull();
		for (const [piece, name] of Object.entries({ k: 'BLACK_KING', q: 'BLACK_QUEEN', r: 'BLACK_ROOK', b: 'BLACK_BISHOP', n: 'BLACK_KNIGHT', p: 'BLACK_PAWN', K: 'WHITE_KING', Q: 'WHITE_QUEEN', R: 'WHITE_ROOK', B: 'WHITE_BISHOP', N: 'WHITE_KNIGHT', P: 'WHITE_PAWN' }))
			expect((component as any).mapPieceToName(piece)).toBe(name);
		expect((component as any).mapPieceToName('x')).toBe('x');

		component['cg'] = board;
		component.subtopicId = 6;
		(component as any).sessionId = undefined;
		component.ngOnChanges();
		await Promise.resolve();
		expect(service.startChess).toHaveBeenCalledWith(6);
		component.ngOnChanges();
		(component as any).sessionId = 2;
		component.ngOnChanges();
		service.moveChess.mockResolvedValue({ status: 'NORMAL', fen: component['previousFen'], initialFen: component['previousFen'] });
		await component.onMove('e2', 'e4');
		expect(component.moves.at(-1)?.isSystem).toBe(false);
		const error = jest.spyOn(console, 'error').mockImplementation();
		service.startChess.mockRejectedValueOnce(new Error('offline'));
		await component.onStartChess(6);
		expect(component['sessionId']).toBe(2);
		error.mockRestore();
	});
});
