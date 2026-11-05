import { of } from 'rxjs';
import { RatingService } from './rating.service';
import { TimeControlService } from '../chess/time-control.service';

describe('RatingService', () => {
	it('loads the ratings of the logged user', async () => {
		const http = { get: jest.fn().mockReturnValue(of([{ category: 'BLITZ' }])) };
		const service = new RatingService(http as any, new TimeControlService());

		await expect(service.getMyRatings()).resolves.toEqual([{ category: 'BLITZ' }]);
		expect(http.get).toHaveBeenCalledWith(expect.stringContaining('/games/ratings/me'));
	});

	describe('formatting', () => {
		const service = new RatingService({} as any, new TimeControlService());

		it('translates skill levels and keeps unknown ones as they are', () => {
			expect(service.skillLabel('MASTER')).toBe('Mestre');
			expect(service.skillLabel('UNRATED')).toBe('Sem classificação');
			expect(service.skillLabel('XYZ' as any)).toBe('XYZ');
		});

		it('always builds the four categories, zeroing the ones never played', () => {
			const rows = service.toRatingRows([{
				category: 'BLITZ', rating: 1300, gamesPlayed: 4, wins: 3, losses: 1, draws: 0,
				performanceRating: 1400, skillLevel: 'NOVICE',
			}]);

			expect(rows.map(r => r.category)).toEqual(['BULLET', 'BLITZ', 'RAPID', 'CLASSICAL']);
			expect(rows[1]).toMatchObject({ rating: 1300, wins: 3, skillLevel: 'NOVICE' });
			expect(rows[0]).toMatchObject({ rating: null, gamesPlayed: 0, skillLevel: 'UNRATED', performanceRating: null });
		});
	});
});
