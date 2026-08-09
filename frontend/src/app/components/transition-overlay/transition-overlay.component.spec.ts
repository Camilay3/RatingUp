import { ComponentFixture, TestBed } from '@angular/core/testing';
import { signal, WritableSignal } from '@angular/core';
import { TransitionOverlayComponent } from './transition-overlay.component';
import { TransitionService, TransitionState } from '../../services/transition/transition.service';
import { Router } from '@angular/router';

describe('TransitionOverlayComponent', () => {
	let fixture: ComponentFixture<TransitionOverlayComponent>;
	let component: TransitionOverlayComponent;
	let state: WritableSignal<TransitionState>;
	let transition: { state: typeof state; finishTransition: jest.Mock };
	let router: { navigate: jest.Mock };

	const idle = (): TransitionState => ({
		active: false, imageUrl: '', dominantColor: '#5A3E2B', phase: 'idle', rect: null,
	});

	beforeEach(async () => {
		state = signal(idle());
		transition = { state, finishTransition: jest.fn() };
		router = { navigate: jest.fn().mockResolvedValue(undefined) };
		await TestBed.configureTestingModule({
			imports: [TransitionOverlayComponent],
			providers: [
				{ provide: Router, useValue: router },
				{ provide: TransitionService, useValue: transition },
			],
		}).compileComponents();
		fixture = TestBed.createComponent(TransitionOverlayComponent);
		component = fixture.componentInstance;
		fixture.detectChanges();
	});

	it('tracks the pending subtopic and removes its listener on destroy', () => {
		document.dispatchEvent(new CustomEvent('transition:prepare', { detail: { subtopicoId: 9 } }));
		expect((component as any).pendingSubtopicoId).toBe(9);
		component.ngOnDestroy();
		document.dispatchEvent(new CustomEvent('transition:prepare', { detail: { subtopicoId: 10 } }));
		expect((component as any).pendingSubtopicoId).toBe(9);
	});

	it('starts the zoom animation and navigates after it finishes', async () => {
		jest.useFakeTimers();
		const frame = jest.spyOn(window, 'requestAnimationFrame').mockImplementation((callback) => {
			callback(0);
			return 0;
		});
		const zoom = { ...idle(), active: true, imageUrl: 'subtopic.webp', dominantColor: '#123456', phase: 'zoom' as const, rect: new DOMRect(1, 2, 30, 40) };
		(component as any).pendingSubtopicoId = 9;
		(component as any).beginAnimation(zoom);
		expect(component.isActive()).toBe(true);
		expect(component.imageUrl()).toBe('subtopic.webp');
		jest.advanceTimersByTime(520);
		expect(router.navigate).toHaveBeenCalledWith(['/subtopico'], { state: { subtopicoId: 9 } });
		await Promise.resolve();
		jest.advanceTimersByTime(80);
		expect(transition.finishTransition).toHaveBeenCalled();
		frame.mockRestore();
		jest.useRealTimers();
	});

	it('finishes directly without an id and handles navigation failures', async () => {
		(component as any).navigateNow();
		expect(transition.finishTransition).toHaveBeenCalledTimes(1);
		(component as any).pendingSubtopicoId = 3;
		router.navigate.mockRejectedValueOnce(new Error('navigation'));
		await (component as any).navigateNow();
		expect(transition.finishTransition).toHaveBeenCalledTimes(2);
		(component as any).deactivate();
		expect(component.isActive()).toBe(false);
		expect(component.isAnimating()).toBe(false);
	});

	it('reacts to zoom and done transition phases', () => {
		const frame = jest.spyOn(window, 'requestAnimationFrame').mockImplementation((callback) => {
			callback(0);
			return 0;
		});
		state.set({ ...idle(), active: true, phase: 'zoom', rect: new DOMRect(0, 0, 10, 10), imageUrl: 'image.webp' });
		fixture.detectChanges();
		expect(component.isActive()).toBe(true);
		state.set({ ...idle(), phase: 'done' });
		fixture.detectChanges();
		expect(component.isActive()).toBe(false);
		frame.mockRestore();
	});
});
