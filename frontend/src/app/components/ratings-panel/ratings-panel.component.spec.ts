import { TestBed } from '@angular/core/testing';
import { RatingsPanelComponent } from './ratings-panel.component';
import { RatingService } from '../../services/game/rating.service';
import { TimeControlService } from '../../services/chess/time-control.service';
import { IRatingRow } from '../../interfaces/chess/IRatingRow';

describe('RatingsPanelComponent', () => {
	const row = (over: Partial<IRatingRow>): IRatingRow => ({
		category: 'BULLET', label: 'Bullet', display: '1+0', rating: 400, skillLevel: 'UNRATED',
		gamesPlayed: 0, wins: 0, losses: 0, draws: 0, performanceRating: null, ...over,
	});

	beforeEach(() => {
		TestBed.configureTestingModule({
			providers: [{ provide: RatingService, useValue: new RatingService({} as any, new TimeControlService()) }],
		});
	});

	function render(rows: IRatingRow[]) {
		const fixture = TestBed.createComponent(RatingsPanelComponent);
		fixture.componentRef.setInput('rows', rows);
		fixture.detectChanges();
		return fixture.nativeElement as HTMLElement;
	}

	it('renders one card per category with the rating of that category', () => {
		const el = render([
			row({}),
			row({ category: 'BLITZ', label: 'Blitz', display: '5+0', rating: 500, skillLevel: 'NOVICE', gamesPlayed: 4, wins: 3, losses: 1 }),
		]);

		const cards = el.querySelectorAll('.card');
		expect(cards).toHaveLength(2);
		expect(cards[0].querySelector('.rating')?.textContent).toBe('400');
		expect(cards[1].querySelector('.rating')?.textContent).toBe('500');
		expect(cards[1].textContent).toContain('4 partidas · 3V 1D 0E');
		expect(cards[1].textContent).toContain('Novato');
	});

	it('shows a dash when there is no rating and totals the games', () => {
		const el = render([row({ rating: null }), row({ category: 'RAPID', gamesPlayed: 6 })]);

		expect(el.querySelector('.card .rating')?.textContent).toBe('—');
		expect(el.querySelector('.total')?.textContent).toContain('Total: 6 partidas');
	});
});
