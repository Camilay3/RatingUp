import { ChangeDetectorRef } from '@angular/core';
import { TestBed } from '@angular/core/testing';
import { SheetComponent } from './sheet.component';

describe('SheetComponent', () => {
	it('derives cover images and emits a page flip', () => {
		const audio = { playOpen: jest.fn(), playClose: jest.fn(), playFlip: jest.fn() };
		const component = TestBed.runInInjectionContext(() => new SheetComponent({ detectChanges: jest.fn() } as unknown as ChangeDetectorRef, audio as any));
		(component as any).capa = () => 'capa.webp';
		(component as any).frenteCapa = () => true;
		component.isPageWaiting = false;
		jest.spyOn(component.flippedChange, 'emit');

		expect(component.frontImage()).toBe('/livro/capa.webp');
		expect(component.backImage()).toBe('/livro/contracapa.webp');
		component.virarPagina();
		expect(component.flippedChange.emit).toHaveBeenCalledWith(true);
		expect(audio.playOpen).toHaveBeenCalled();
	});

	it('handles back covers, regular pages, waiting state and animation timing', () => {
		jest.useFakeTimers();
		document.documentElement.style.setProperty('--duracao', '10');
		const audio = { playOpen: jest.fn(), playClose: jest.fn(), playFlip: jest.fn() };
		const component = TestBed.runInInjectionContext(() => new SheetComponent({ detectChanges: jest.fn() } as unknown as ChangeDetectorRef, audio as any));
		(component as any).capa = () => 'capa.webp';
		(component as any).frenteCapa = () => false;
		expect(component.frontImage()).toBe('/livro/contracapaFront.webp');
		expect(component.backImage()).toBe('/livro/capa.webp');
		component.virarPagina(false, true);
		expect(audio.playClose).toHaveBeenCalled();
		expect(audio.playFlip).toHaveBeenCalled();
		jest.advanceTimersByTime(10);

		const plain = TestBed.runInInjectionContext(() => new SheetComponent({ detectChanges: jest.fn() } as unknown as ChangeDetectorRef, audio as any));
		(plain as any).capa = () => undefined;
		expect(plain.frontImage()).toBeNull();
		expect(plain.backImage()).toBeNull();
		plain.virarPagina();
		jest.advanceTimersByTime(10);
		expect(audio.playFlip).toHaveBeenCalledTimes(2);

		component.isPageWaiting = true;
		component.virarPagina();
		expect(audio.playFlip).toHaveBeenCalledTimes(2);
		jest.useRealTimers();
	});
});
