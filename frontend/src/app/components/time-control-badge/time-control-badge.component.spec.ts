import { TestBed } from '@angular/core/testing';
import { TimeControlBadgeComponent } from './time-control-badge.component';

describe('TimeControlBadgeComponent', () => {
	const render = (inputs: Record<string, unknown>) => {
		const fixture = TestBed.createComponent(TimeControlBadgeComponent);
		Object.entries(inputs).forEach(([k, v]) => fixture.componentRef.setInput(k, v));
		fixture.detectChanges();
		return fixture.nativeElement as HTMLElement;
	};

	it('renders "<ritmo> · ● <minutos>|<acréscimo>"', () => {
		const el = render({ label: 'Bullet', initialSeconds: 60, incrementSeconds: 0 });

		expect(el.querySelector('.ritmo')?.textContent).toBe('Bullet');
		expect(el.querySelector('.sep')?.textContent).toBe('·');
		expect(el.querySelector('.dot')).not.toBeNull();
		expect(el.querySelector('.tempo')?.textContent).toBe('1|0');
	});

	it('shows seconds when the initial time is not a whole minute and uses the increment', () => {
		const el = render({ label: 'Bullet', initialSeconds: 45, incrementSeconds: 2 });

		expect(el.querySelector('.tempo')?.textContent).toBe('45s|2');
	});

	it('defaults the increment to zero', () => {
		const el = render({ label: 'Rapid', initialSeconds: 600 });

		expect(el.querySelector('.tempo')?.textContent).toBe('10|0');
	});
});
