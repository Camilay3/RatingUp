import { ChangeDetectorRef } from '@angular/core';
import { of, throwError } from 'rxjs';
import { BookComponent } from './book.component';

describe('BookComponent', () => {
	function create() {
		const cdr = { detectChanges: jest.fn() } as unknown as ChangeDetectorRef;
		const book = { getSheets: jest.fn().mockReturnValue(of({ data: { pages: [] } })) };
		const auth = {
			getProgresso: jest.fn().mockReturnValue(of({ data: { chapter: 1, subtopic: 1 } })),
			getMyUser: jest.fn().mockReturnValue({ data: { nickname: 'player' } }),
		};
		const audio = { playFlips: jest.fn(), stopFlips: jest.fn() };
		const snack = { open: jest.fn() };
		return { component: new BookComponent(cdr, book as any, auth as any, audio as any, snack as any), book, auth, snack };
	}

	it('builds the initial book after loading progress and sheets', () => {
		const { component, book } = create();
		component.ngOnInit();
		expect(book.getSheets).toHaveBeenCalled();
		expect(component.book[0]).toEqual({ capa: 'capa.webp', frenteCapa: true });
		expect(component.book.at(-1)).toEqual({ capa: 'quartaCapa.webp' });
		expect(component.isLoading).toBe(false);
	});

	it('starts the requested opening animation and handles empty mapped pages', () => {
		const { component, book } = create();
		const animate = jest.spyOn(component, 'multiplasPaginas').mockImplementation();
		history.replaceState({ executarAnimacao: true }, '');
		book.getSheets.mockReturnValueOnce(of({ data: { pages: { map: () => null } } }));

		component.ngOnInit();

		expect(component.pages).toEqual([]);
		expect(animate).toHaveBeenCalledWith(0);
		history.replaceState({}, '');
	});

	it('shows the cover hint once and hides it after opening the book', () => {
		const { component } = create();
		history.replaceState({ mostrarDicaCapa: true }, '');

		component.ngOnInit();

		expect(component.showBookHint).toBe(true);
		expect(history.state.mostrarDicaCapa).toBeUndefined();
		component.showBookHint = true;
		component.zIndexValues = [1];
		component.pageFlipStates = [false];
		component.tamanhoLivro = 1;
		component.onFlip(0, true);
		expect(component.showBookHint).toBe(false);
	});

	it('reports loading errors without building pages', () => {
		const { component, auth, snack } = create();
		auth.getProgresso.mockReturnValueOnce(throwError(() => ({ error: { message: 'failed' } })));
		component.ngOnInit();
		expect(snack.open).toHaveBeenCalledWith('failed', 'Fechar', expect.any(Object));
		expect(component.isLoading).toBe(true);
	});

	it('keeps first and last page flags consistent', () => {
		const { component } = create();
		component.tamanhoLivro = 2;
		component.pageFlipStates = [false, false];
		component.checkPagesFlipped();
		expect(component.onFirstPage).toBe(true);
		component.pageFlipStates = [true, true];
		component.checkPagesFlipped();
		expect(component.onLastPage).toBe(true);
	});

	it('marks subtópicos according to progress and splits the summary into homes', () => {
		const { component, book, auth } = create();
		const pages = [
			{ front: { type: 'capitulo', chapterId: null, displayOrder: 1, title: 'Capítulo' }, verse: { type: 'subtópico', chapterId: 1, displayOrder: 1, title: 'Atual' } },
			{ front: { type: 'subtópico', chapterId: 1, displayOrder: 2, title: 'Depois' }, verse: { type: 'subtópico', chapterId: 2, displayOrder: 1, title: 'Outro capítulo' } },
		] as any;
		book.getSheets.mockReturnValue(of({ data: { pages } }));
		component.ngOnInit();
		expect(component.pages[0].verse.lockOpen).toBe(true);
		expect(component.pages[1].front.isBlocked).toBe(true);
		expect(component.pages[1].verse.isBlocked).toBe(true);
		expect(component.book.some((page) => page.front?.type === 'home')).toBe(true);
		expect(component.book.find((page) => page.front?.type === 'home').front.nickname).toBe('player');
		expect(auth.getMyUser).toHaveBeenCalled();

		const split = (component as any).splitSummaryIntoHomePages([
			{ front: null, verse: { type: 'subtópico' } },
			...Array.from({ length: 30 }, () => ({ front: { type: 'capitulo' }, verse: { type: 'subtópico' } })),
		]);
		expect(split.length).toBeGreaterThan(1);
		(component as any).pageHeight = 500;
		expect((component as any).calcMaxHeight(true)).toBe(42);
		expect((component as any).calcMaxHeight(false)).toBe(55);
		expect((component as any).countPagesInChunks([[1, 2], [3]], 1)).toBe(2);
	});

	it('resizes, navigates with arrows and reorders flipped sheets', () => {
		const { component } = create();
		const resizeCallbacks: ResizeObserverCallback[] = [];
		const OriginalResizeObserver = globalThis.ResizeObserver;
		(globalThis as any).ResizeObserver = class {
			constructor(callback: ResizeObserverCallback) { resizeCallbacks.push(callback); }
			observe = jest.fn();
		};
		document.body.innerHTML = '<div class="book-container"><div class="page-container"></div></div>';
		(component as any).pagesLoaded = true;
		(component as any).pageHeight = 1;
		component.ngAfterViewInit();
		resizeCallbacks[0]([], {} as ResizeObserver);
		expect(component.zIndexValues).toEqual([3, 2]);
		(globalThis as any).ResizeObserver = OriginalResizeObserver;

		const sheet = { virarPagina: jest.fn() };
		component.sheets = { get: jest.fn().mockReturnValue(sheet) } as any;
		component.pageFlipStates = [false, true];
		component.paginaAtual = 0;
		component.onArrowRight();
		expect(sheet.virarPagina).toHaveBeenCalledWith(false, true);
		component.isWaiting = true;
		component.onArrowRight();
		component.onArrowLeft();
		component.isWaiting = false;
		component.paginaAtual = 1;
		component.onArrowLeft();
		expect(sheet.virarPagina).toHaveBeenCalledWith();

		jest.useFakeTimers();
		component.tamanhoLivro = 2;
		component.zIndexValues = [1, 2];
		component.onFlip(0, true);
		component.onFlip(0, false);
		jest.advanceTimersByTime(component.duracaoAnimacao);
		component.reordenarZIndex();
		expect(component.zIndexValues).toEqual([2, 2]);
		jest.useRealTimers();
	});

	it('handles missing book containers and boundary navigation', () => {
		const { component } = create();
		document.body.innerHTML = '';
		component.ngAfterViewInit();

		const callbacks: ResizeObserverCallback[] = [];
		const OriginalResizeObserver = globalThis.ResizeObserver;
		(globalThis as any).ResizeObserver = class {
			constructor(callback: ResizeObserverCallback) { callbacks.push(callback); }
			observe = jest.fn();
		};
		document.body.innerHTML = '<div class="book-container"></div>';
		component.ngAfterViewInit();
		(component as any).pagesLoaded = false;
		callbacks[0]([], {} as ResizeObserver);
		(globalThis as any).ResizeObserver = OriginalResizeObserver;

		component.sheets = { get: jest.fn() } as any;
		component.paginaAtual = 0;
		component.onArrowLeft();
		jest.useFakeTimers();
		component.tamanhoLivro = 1;
		component.multiplasPaginas(1);
		component.paginaAtual = 1;
		jest.runAllTimers();
		component.multiplasPaginas(-1);
		jest.runAllTimers();
		jest.useRealTimers();
	});

	it('flips multiple pages and stops when the target is blocked', () => {
		jest.useFakeTimers();
		const { component } = create();
		const audio = (component as any).audioService;
		const sheet = { virarPagina: jest.fn() };
		component.sheets = { get: jest.fn().mockReturnValue(sheet) } as any;
		component.book = [
			{ capa: 'capa.webp' },
			{ front: { type: 'subtópico', isBlocked: false }, verse: { type: 'subtópico', isBlocked: false } },
			{ front: { type: 'subtópico', isBlocked: true }, verse: { type: 'subtópico', isBlocked: true } },
		];
		component.tamanhoLivro = component.book.length;
		component.multiplasPaginas(0);
		expect(component.isWaiting).toBe(true);
		jest.runAllTimers();
		expect(sheet.virarPagina).toHaveBeenCalled();
		expect(audio.playFlips).toHaveBeenCalled();
		expect(audio.stopFlips).toHaveBeenCalled();

		component.isWaiting = false;
		component.paginaAtual = 1;
		component.multiplasPaginas(-1);
		jest.runAllTimers();
		expect(component.isWaiting).toBe(false);

		component.paginaAtual = 2;
		component.multiplasPaginas(0);
		expect(component.isWaiting).toBe(false);
		jest.useRealTimers();
	});
});
