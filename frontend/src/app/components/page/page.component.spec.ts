import { TestBed } from '@angular/core/testing';
import { of, throwError } from 'rxjs';
import { PageComponent } from './page.component';
import { AuthService } from '../../services/user/auth.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { TransitionService } from '../../services/transition/transition.service';

describe('PageComponent', () => {
	let fixture: ReturnType<typeof TestBed.createComponent<PageComponent>>;
	let component: PageComponent;
	let router: { navigate: jest.Mock; navigateByUrl: jest.Mock };
	let auth: { logout: jest.Mock };
	let snack: { open: jest.Mock };
	let transition: { startTransition: jest.Mock };

	const chapter = { type: 'capitulo', chapterId: null, displayOrder: 2, title: 'Abertura' } as any;
	const subtopic = (isBlocked = false) => ({
		type: 'subtópico', id: 7, chapterId: 1, displayOrder: 3, title: 'Tática',
		isBlocked, subtopicImageUrl: '/tatic.webp',
	}) as any;

	beforeEach(async () => {
		router = { navigate: jest.fn(), navigateByUrl: jest.fn() };
		auth = { logout: jest.fn() };
		snack = { open: jest.fn() };
		transition = { startTransition: jest.fn().mockResolvedValue(undefined) };

		await TestBed.configureTestingModule({
			imports: [PageComponent],
			providers: [
				{ provide: Router, useValue: router },
				{ provide: AuthService, useValue: auth },
				{ provide: MatSnackBar, useValue: snack },
				{ provide: TransitionService, useValue: transition },
			],
		}).compileComponents();
		fixture = TestBed.createComponent(PageComponent);
		component = fixture.componentInstance;
		jest.spyOn(component.navigate, 'emit');
		fixture.detectChanges();
	});

	it('renders chapters, blocked subtópicos and home summaries', () => {
		fixture.componentRef.setInput('page', chapter);
		fixture.componentRef.setInput('side', 'frente');
		fixture.detectChanges();
		expect(fixture.nativeElement.querySelector('h1').textContent).toContain('Capítulo 2');

		fixture.componentRef.setInput('page', subtopic(true));
		fixture.detectChanges();
		expect(fixture.nativeElement.querySelector('.lock')).not.toBeNull();

		fixture.componentRef.setInput('page', {
			type: 'home', nickname: 'Ada', isFirstHome: true, chunkOffset: 0,
			summary: [{ front: chapter, verse: subtopic(false) }],
		});
		fixture.componentRef.setInput('side', 'verso');
		fixture.detectChanges();
		expect(fixture.nativeElement.querySelector('.header h2').textContent).toContain('Ada');
		expect(fixture.nativeElement.querySelectorAll('li')).toHaveLength(1);
		fixture.nativeElement.querySelector('li').click();
		expect(component.navigate.emit).toHaveBeenCalledWith({ qnt: 4 });
		expect(fixture.nativeElement.querySelectorAll('.controls svg')).toHaveLength(2);
	});

	it('covers page helpers and navigation actions', async () => {
		const waiting = jest.spyOn(component.isWaiting, 'emit');
		expect(component.pageIsBlocked()).toBe(false);
		expect(component.asType(chapter, 'home')).toBeUndefined();
		fixture.componentRef.setInput('page', subtopic(true));
		fixture.detectChanges();
		expect(component.pageIsBlocked()).toBe(true);
		expect(waiting).toHaveBeenCalledWith(true);
		expect(component.asType(chapter, 'capitulo')?.title).toBe('Abertura');
		expect(component.asType(undefined, 'home')).toBeUndefined();
		expect(component.isSubtopico(subtopic())).toBe(true);
		expect(component.setIconList({ isBlocked: true })).toBe('fa-solid fa-lock');
		expect(component.setIconList({ lockOpen: true })).toBe('fa-solid fa-lock-open');
		expect(component.setIconList({})).toBe('fa-solid fa-star');
		expect(component.getPages({ front: chapter, verse: subtopic() } as any, 3)[1].uid).toBe('3-2');

		component.multiplasPaginas(4);
		expect(component.navigate.emit).toHaveBeenCalledWith({ qnt: 4 });
		const image = document.createElement('img');
		component.onImageError({ target: image } as any);
		expect(image.src).toContain('/subtopicos/default.png');

		const button = document.createElement('button');
		const event = new MouseEvent('click', { bubbles: true });
		Object.defineProperty(event, 'currentTarget', { value: button });
		await component.acessarSubtopico(7, event);
		expect(router.navigate).toHaveBeenCalledWith(['/subtopico'], { state: { subtopicoId: 7 } });
		const incompleteCard = document.createElement('div');
		incompleteCard.className = 'page';
		const incompleteImage = document.createElement('img');
		incompleteImage.className = 'subtopico-img';
		Object.defineProperties(incompleteImage, { complete: { value: false }, naturalWidth: { value: 0 } });
		const incompleteButton = document.createElement('button');
		incompleteCard.append(incompleteImage, incompleteButton);
		const incompleteEvent = new MouseEvent('click', { bubbles: true });
		Object.defineProperty(incompleteEvent, 'currentTarget', { value: incompleteButton });
		await component.acessarSubtopico(7, incompleteEvent);
		expect(router.navigate).toHaveBeenCalledTimes(2);

		const card = document.createElement('div');
		card.className = 'page';
		const readyImage = document.createElement('img');
		readyImage.className = 'subtopico-img';
		Object.defineProperties(readyImage, { complete: { value: true }, naturalWidth: { value: 100 } });
		card.append(readyImage, button);
		const readyEvent = new MouseEvent('click', { bubbles: true });
		Object.defineProperty(readyEvent, 'currentTarget', { value: button });
		await component.acessarSubtopico(7, readyEvent);
		expect(transition.startTransition).toHaveBeenCalledWith(readyImage, expect.any(Object));

		auth.logout.mockReturnValueOnce(of(undefined));
		component.logout();
		expect(router.navigateByUrl).toHaveBeenCalledWith('/acesso');
		auth.logout.mockReturnValueOnce(throwError(() => ({ error: { message: 'offline' } })));
		component.logout();
		expect(snack.open).toHaveBeenCalledWith('offline', 'Fechar', { duration: 3000 });
		component.verPerfil();
		expect(router.navigateByUrl).toHaveBeenCalledWith('/perfil');
	});

	it('copies only the selected page content and handles keyboard selection', () => {
		const root = component.pageRoot.nativeElement;
		root.innerHTML = '<span>texto</span><img src="image.png">';
		const selection = window.getSelection()!;
		const range = document.createRange();
		range.selectNodeContents(root);
		selection.removeAllRanges();
		selection.addRange(range);
		const clipboardData = { setData: jest.fn() };
		const copy = new Event('copy', { cancelable: true }) as ClipboardEvent;
		Object.defineProperty(copy, 'clipboardData', { value: clipboardData });
		component.onCopy(copy);
		expect(copy.defaultPrevented).toBe(true);
		expect(clipboardData.setData).toHaveBeenCalledWith('text/plain', 'texto');
		expect(clipboardData.setData).toHaveBeenCalledWith('text/html', '<span>texto</span>');

		const outsideRange = document.createRange();
		outsideRange.selectNode(document.body);
		selection.removeAllRanges();
		selection.addRange(outsideRange);
		const fallbackClipboard = { setData: jest.fn() };
		(window as any).clipboardData = fallbackClipboard;
		component.onCopy(new Event('copy', { cancelable: true }) as ClipboardEvent);
		expect(fallbackClipboard.setData).toHaveBeenCalledWith('Text', expect.any(String));
		delete (window as any).clipboardData;

		selection.removeAllRanges();
		component.onCopy(new Event('copy') as ClipboardEvent);
		const collapsed = document.createRange();
		collapsed.setStart(root, 0);
		collapsed.collapse(true);
		selection.addRange(collapsed);
		component.onCopy(new Event('copy') as ClipboardEvent);
		const keydown = new KeyboardEvent('keydown', { key: 'a', ctrlKey: true, cancelable: true });
		component.onHover(true);
		component.handleKeydown(keydown);
		expect(keydown.defaultPrevented).toBe(true);
		const ignoredKeydown = new KeyboardEvent('keydown', { key: 'a', ctrlKey: true, cancelable: true });
		component.onHover(false);
		selection.removeAllRanges();
		component.handleKeydown(ignoredKeydown);
		expect(ignoredKeydown.defaultPrevented).toBe(false);
		const selectedKeydown = new KeyboardEvent('keydown', { key: 'a', ctrlKey: true, cancelable: true });
		const selected = document.createRange();
		selected.selectNodeContents(root);
		selection.removeAllRanges();
		selection.addRange(selected);
		component.handleKeydown(selectedKeydown);
		expect(selectedKeydown.defaultPrevented).toBe(true);

		root.innerHTML = '<img src="only-image.png">';
		selection.removeAllRanges();
		const imageRange = document.createRange();
		imageRange.selectNodeContents(root);
		selection.addRange(imageRange);
		const emptyTextClipboard = { setData: jest.fn() };
		const emptyTextCopy = new Event('copy', { cancelable: true }) as ClipboardEvent;
		Object.defineProperty(emptyTextCopy, 'clipboardData', { value: emptyTextClipboard });
		component.onCopy(emptyTextCopy);
		expect(emptyTextClipboard.setData).toHaveBeenCalledWith('text/plain', '');

		const fallbackCopy = new Event('copy', { cancelable: true }) as ClipboardEvent;
		Object.defineProperty(fallbackCopy, 'clipboardData', { value: undefined });
		const fallback = { setData: jest.fn() };
		(window as any).clipboardData = fallback;
		component.onCopy(fallbackCopy);
		expect(fallback.setData).toHaveBeenCalledWith('Text', '');
		delete (window as any).clipboardData;

		component.handleKeydown(new KeyboardEvent('keydown', { key: 'x', ctrlKey: true }));
		const child = document.createElement('button');
		root.appendChild(child);
		child.focus();
		const activeKeydown = new KeyboardEvent('keydown', { key: 'a', metaKey: true, cancelable: true });
		component.handleKeydown(activeKeydown);
		expect(activeKeydown.defaultPrevented).toBe(true);

		const originalRoot = component.pageRoot;
		(component as any).pageRoot = undefined;
		component.handleKeydown(new KeyboardEvent('keydown', { key: 'a', ctrlKey: true }));
		(component as any).pageRoot = originalRoot;

		const intersects = {
			rangeCount: 1,
			getRangeAt: () => ({ collapsed: false, intersectsNode: () => true }),
			removeAllRanges: jest.fn(),
			addRange: jest.fn(),
		};
		const getSelection = jest.spyOn(window, 'getSelection').mockReturnValue(intersects as any);
		component.handleKeydown(new KeyboardEvent('keydown', { key: 'a', ctrlKey: true, cancelable: true }));
		getSelection.mockReturnValue(null);
		component.handleKeydown(new KeyboardEvent('keydown', { key: 'a', ctrlKey: true, cancelable: true }));
		getSelection.mockRestore();
	});
});
