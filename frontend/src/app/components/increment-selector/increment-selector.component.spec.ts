import { TestBed } from '@angular/core/testing';
import { IncrementSelectorComponent } from './increment-selector.component';

describe('IncrementSelectorComponent', () => {
	const setup = (value = 0) => {
		const fixture = TestBed.createComponent(IncrementSelectorComponent);
		fixture.componentRef.setInput('value', value);
		fixture.detectChanges();
		const emitted: number[] = [];
		fixture.componentInstance.value.subscribe(v => emitted.push(v));
		const el = fixture.nativeElement as HTMLElement;
		return { fixture, component: fixture.componentInstance, el, emitted };
	};

	const steps = (el: HTMLElement) => el.querySelectorAll<HTMLButtonElement>('.passo');

	it('displays the current increment', () => {
		const { el } = setup(3);

		expect(el.querySelector('.numero')?.textContent).toBe('+3s');
	});

	it('increases and decreases one second at a time', () => {
		const { fixture, el, emitted } = setup(2);

		steps(el)[1].click();
		fixture.detectChanges();
		expect(el.querySelector('.numero')?.textContent).toBe('+3s');

		steps(el)[0].click();
		steps(el)[0].click();
		fixture.detectChanges();
		expect(el.querySelector('.numero')?.textContent).toBe('+1s');
		expect(emitted).toEqual([3, 2, 1]);
	});

	it('clamps the value between 0 and the backend limit', () => {
		const { component } = setup(0);

		component.set(-4);
		expect(component.value()).toBe(0);

		component.set(500);
		expect(component.value()).toBe(180);

		component.step(1);
		expect(component.value()).toBe(180);
	});

	it('disables the step buttons at the limits', () => {
		const { fixture, el, component } = setup(0);
		expect(steps(el)[0].disabled).toBe(true);
		expect(steps(el)[1].disabled).toBe(false);

		component.set(180);
		fixture.detectChanges();
		expect(steps(el)[1].disabled).toBe(true);
	});

	it('does not render quick-option shortcuts', () => {
		const { el } = setup(0);

		expect(el.querySelectorAll('.atalho')).toHaveLength(0);
		expect(el.querySelectorAll('button')).toHaveLength(2);
	});

	it('disables every control when disabled', () => {
		const { fixture, el } = setup(1);

		fixture.componentRef.setInput('disabled', true);
		fixture.detectChanges();

		const all = el.querySelectorAll<HTMLButtonElement>('button');
		expect(Array.from(all).every(b => b.disabled)).toBe(true);
	});
});
