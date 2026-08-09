jest.mock('chessground', () => ({ Chessground: jest.fn() }));

import { ChangeDetectorRef } from '@angular/core';
import { of, throwError } from 'rxjs';
import { SubtopicoComponent } from './subtopico.component';

describe('SubtopicoComponent', () => {
	it('loads content and practice type', () => {
		const book = {
			getSubtopicContent: jest.fn().mockReturnValue(of({ data: { chapterId: 1, displayOrder: 2 } })),
			getSubtopicType: jest.fn().mockReturnValue(of({ type: 'BOARD' })),
		};
		const component = new SubtopicoComponent({} as any, {} as any, book as any, {} as any, { detectChanges: jest.fn() } as unknown as ChangeDetectorRef);
		component.subtopicId = 3;
		component.ngOnInit();
		expect(component.subtopicoContent?.chapterId).toBe(1);
		expect(component.tipoFase).toBe('BOARD');
	});

	it('redirects when content cannot be loaded', () => {
		const router = { navigate: jest.fn() };
		const snack = { open: jest.fn() };
		const book = {
			getSubtopicContent: jest.fn().mockReturnValue(throwError(() => ({ error: { message: 'failed' } }))),
			getSubtopicType: jest.fn().mockReturnValue(of({ type: 'MULTIPLE_CHOICE' })),
		};
		const component = new SubtopicoComponent(router as any, {} as any, book as any, snack as any, { detectChanges: jest.fn() } as unknown as ChangeDetectorRef);
		component.subtopicId = 3;
		component.ngOnInit();
		expect(snack.open).toHaveBeenCalled();
		expect(router.navigate).toHaveBeenCalledWith(['/'], { state: { executarAnimacao: true } });
	});

	it('redirects without an id and handles type errors', () => {
		const router = { navigate: jest.fn() };
		const snack = { open: jest.fn() };
		const book = {
			getSubtopicContent: jest.fn().mockReturnValue(of({ data: {} })),
			getSubtopicType: jest.fn().mockReturnValue(throwError(() => ({ error: { message: 'type failed' } }))),
		};
		const component = new SubtopicoComponent(router as any, {} as any, book as any, snack as any, { detectChanges: jest.fn() } as unknown as ChangeDetectorRef);
		component.subtopicId = 0;
		component.ngOnInit();
		expect(router.navigate).toHaveBeenCalledWith(['']);
		expect(snack.open).toHaveBeenCalledWith('type failed', 'Fechar', { duration: 3000 });
	});

	it('updates progress and reports completion errors', () => {
		const router = { navigate: jest.fn() };
		const snack = { open: jest.fn() };
		const auth = { atualizarProgresso: jest.fn().mockReturnValue(of(undefined)) };
		const component = new SubtopicoComponent(router as any, auth as any, {} as any, snack as any, {} as any);
		component.subtopicoContent = { chapterId: 2, displayOrder: 3 } as any;
		component.concluirSubtopico();
		expect(router.navigate).toHaveBeenCalledWith(['/'], { state: { executarAnimacao: true } });
		auth.atualizarProgresso.mockReturnValueOnce(throwError(() => ({ error: { message: 'save failed' } })));
		component.concluirSubtopico();
		expect(snack.open).toHaveBeenCalledWith('save failed', 'Fechar', { duration: 3000 });
	});
});
