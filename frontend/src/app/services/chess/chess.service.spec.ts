import { of, throwError } from 'rxjs';
import { ChessService } from './chess.service';

describe('ChessService', () => {
	it('starts a session and sends moves with the expected payloads', async () => {
		const http = {
			post: jest.fn()
				.mockReturnValueOnce(of({ sessionId: 4, fen: 'start', status: 'NORMAL', initialFen: 'start' }))
				.mockReturnValueOnce(of({ fen: 'next', status: 'COMPLETED', initialFen: 'start' })),
		};
		const service = new ChessService(http as any);

		await expect(service.startChess(8)).resolves.toEqual({ sessionId: 4, fen: 'start', status: 'NORMAL', initialFen: 'start' });
		await expect(service.moveChess(4, 'WHITE_KNIGHT', 'E2', 'E4')).resolves.toEqual({ fen: 'next', status: 'COMPLETED', initialFen: 'start' });

		expect(http.post).toHaveBeenNthCalledWith(1, expect.stringContaining('/move/session/start'), { subtopicId: 8 });
		expect(http.post).toHaveBeenNthCalledWith(2, expect.stringContaining('/move/session/move'), {
			sessionId: 4,
			piece: 'WHITE_KNIGHT',
			posInitial: 'E2',
			posFinal: 'E4',
		});
	});

	it('propagates backend errors', async () => {
		const error = new Error('offline');
		const http = { post: jest.fn().mockReturnValue(throwError(() => error)) };

		await expect(new ChessService(http as any).startChess(1)).rejects.toBe(error);
	});
});
