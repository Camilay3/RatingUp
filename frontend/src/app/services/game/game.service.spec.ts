import { of } from 'rxjs';
import { GameService } from './game.service';

describe('GameService', () => {
	let http: { get: jest.Mock; post: jest.Mock; delete: jest.Mock };
	let service: GameService;

	beforeEach(() => {
		http = {
			get: jest.fn().mockReturnValue(of('get')),
			post: jest.fn().mockReturnValue(of('post')),
			delete: jest.fn().mockReturnValue(of('delete')),
		};
		service = new GameService(http as any);
	});

	it('creates, joins and cancels games', async () => {
		await expect(service.createGame()).resolves.toBe('post');
		expect(http.post).toHaveBeenCalledWith(expect.stringMatching(/\/games$/), {});

		await expect(service.joinGame(4)).resolves.toBe('post');
		expect(http.post).toHaveBeenCalledWith(expect.stringContaining('/games/4/join'), {});

		await expect(service.cancelGame(4)).resolves.toBe('delete');
		expect(http.delete).toHaveBeenCalledWith(expect.stringMatching(/\/games\/4$/));
	});

	it('lists and loads games', async () => {
		await service.listOpen();
		expect(http.get).toHaveBeenCalledWith(expect.stringContaining('/games/open'));

		await service.listMine();
		expect(http.get).toHaveBeenCalledWith(expect.stringContaining('/games/mine'));

		await service.getGame(9);
		expect(http.get).toHaveBeenCalledWith(expect.stringMatching(/\/games\/9$/));
	});

	it('sends a move with optional promotion', async () => {
		await service.move(3, 'e7', 'e8', 'q');
		expect(http.post).toHaveBeenCalledWith(expect.stringContaining('/games/3/moves'), {
			from: 'e7',
			to: 'e8',
			promotion: 'q',
		});
	});
});
