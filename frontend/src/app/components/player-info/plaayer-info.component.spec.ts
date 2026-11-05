import { TestBed } from '@angular/core/testing';
import { PlayerInfoComponent } from './player-info.component';

describe('PlayerInfoComponent', () => {
	const render = (inputs: Record<string, unknown>) => {
		const fixture = TestBed.createComponent(PlayerInfoComponent);
		fixture.componentRef.setInput('name', 'Ana');
		Object.entries(inputs).forEach(([k, v]) => fixture.componentRef.setInput(k, v));
		fixture.detectChanges();
		return fixture.nativeElement as HTMLElement;
	};
	const text = (el: HTMLElement, sel: string) => el.querySelector(sel)?.textContent?.trim();

	it('shows the name, the rating and a placeholder when there is no rating', () => {
		expect(text(render({ rating: 1234 }), '.nome')).toBe('Ana');
		expect(text(render({ rating: 1234 }), '.rating')).toBe('1234');
		expect(text(render({}), '.rating')).toBe('—');
	});

	it('labels the piece color', () => {
		expect(render({ color: 'white' }).querySelector('.peca')?.getAttribute('aria-label')).toBe('Brancas');
		expect(render({ color: 'black' }).querySelector('.peca')?.getAttribute('aria-label')).toBe('Pretas');
	});

	it('highlights the active player and the active clock', () => {
		const el = render({ active: true, timeMs: 60000 });

		expect(el.querySelector('.jogador')?.classList).toContain('ativo');
		expect(el.querySelector('.relogio')?.classList).toContain('ativo');
	});

	it('shows the rating change with its sign', () => {
		const up = render({ change: 12 });
		expect(text(up, '.delta')).toBe('+12');
		expect(up.querySelector('.delta')?.classList).toContain('positivo');

		const down = render({ change: -8 });
		expect(text(down, '.delta')).toBe('-8');
		expect(down.querySelector('.delta')?.classList).toContain('negativo');

		expect(render({}).querySelector('.delta')).toBeNull();
	});

	it('formats the clock and flags low time', () => {
		const normal = render({ timeMs: 600000 });
		expect(text(normal, '.relogio')).toBe('10:00');
		expect(normal.querySelector('.relogio')?.classList).not.toContain('baixo');

		const low = render({ timeMs: 9500 });
		expect(text(low, '.relogio')).toBe('0:09.5');
		expect(low.querySelector('.relogio')?.classList).toContain('baixo');
	});

	it('hides the clock when there is no time information', () => {
		expect(render({}).querySelector('.relogio')).toBeNull();
	});
});
