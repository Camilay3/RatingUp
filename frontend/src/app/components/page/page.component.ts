import { AuthService } from '../../services/user/auth.service';
import { Component, computed, input, output, ViewChild, ElementRef, HostListener } from '@angular/core';
import { Router } from '@angular/router';
import { IPage, ISheet, ISubtopico, PageData } from '../../interfaces/book/IBook';
import { MatTooltipModule } from '@angular/material/tooltip';

@Component({
  selector: 'app-page',
  templateUrl: './page.component.html',
  styleUrls: ['./page.component.scss'],
  imports: [MatTooltipModule],
})
export class PageComponent {
	page = input<PageData | IPage>();
	side = input<'frente' | 'verso'>();
	isWaiting = output<boolean>();
	navigate = output<{ qnt?: number; next?: boolean }>();

	@ViewChild('pageRoot', { static: true }) pageRoot!: ElementRef<HTMLElement>;
	private isHovered = false;
	onHover(v: boolean) {
		this.isHovered = v;
	}

	constructor(
		private readonly router: Router,
		private readonly authService: AuthService,
	) {}

	private isSubtopicoBlocked(item: PageData | undefined): boolean {
		const valid = (item?.type === 'subtópico' && item.isBlocked) ?? false;
		if (valid) this.isWaiting.emit(valid);
		return valid;
	}
	pageIsBlocked = computed(() => this.isSubtopicoBlocked(this.page()));

	asType<T extends PageData['type']>( page: PageData | undefined, type: T): Extract<PageData, { type: T }> | undefined {
		return page?.type === type ? page as Extract<PageData, { type: T }> : undefined;
	}

	isSubtopico(page: PageData): page is ISubtopico {
		return page.type === 'subtópico';
	}

	setIconList(page: any) {
		if (page.isBlocked) return "fa-solid fa-lock";
		return "fa-solid fa-star";
	}

	acessarSubtopico(capitulo: number, subtopico: number) {
		this.router.navigate(['/subtopico'], {
            state: { capitulo, subtopico }
        });
	}

	getPages(item: ISheet) {
		return [
			{ page: item.front, offset: 1 },
			{ page: item.verse, offset: 2 },
		];
	}

	multiplasPaginas(qnt: number) {
		this.navigate.emit({ qnt });
	}

	onImageError(event: Event) {
		(event.target as HTMLImageElement).src = '/subtopicos/default.png';
	}

	onCopy(event: ClipboardEvent) {
		const sel = window.getSelection();
		if (!sel || sel.rangeCount === 0) return;
		const selRange = sel.getRangeAt(0);

		const pageRange = document.createRange();
		pageRange.selectNodeContents(this.pageRoot.nativeElement);

		// compute intersection between selRange and pageRange
		const intersection = document.createRange();

		// start = later of selRange.start and pageRange.start
		if (selRange.compareBoundaryPoints(Range.START_TO_START, pageRange) < 0) {
			intersection.setStart(pageRange.startContainer, pageRange.startOffset);
		} else {
			intersection.setStart(selRange.startContainer, selRange.startOffset);
		}
		// end = earlier of selRange.end and pageRange.end
		if (selRange.compareBoundaryPoints(Range.END_TO_END, pageRange) > 0) {
			intersection.setEnd(pageRange.endContainer, pageRange.endOffset);
		} else {
			intersection.setEnd(selRange.endContainer, selRange.endOffset);
		}

		if (intersection.collapsed) return; // nothing inside page

		const container = document.createElement('div');
		container.appendChild(intersection.cloneContents());
		container.querySelectorAll('img, picture, svg').forEach(n => n.remove());

		const html = container.innerHTML;
		const text = container.textContent || '';

		event.preventDefault();
		if (event.clipboardData) {
			event.clipboardData.setData('text/plain', text);
			event.clipboardData.setData('text/html', html);
		} else if ((window as any).clipboardData) {
			(window as any).clipboardData.setData('Text', text);
		}
	}

	@HostListener('document:keydown', ['$event'])
	handleKeydown(e: KeyboardEvent) {
		if (!((e.ctrlKey || e.metaKey) && e.key.toLowerCase() === 'a')) return;
		if (!this.pageRoot) return;

		const active = document.activeElement;
		let shouldSelect = false;

		// If focus is inside this page, allow
		if (active && this.pageRoot.nativeElement.contains(active)) shouldSelect = true;

		// If mouse is over this page, allow
		if (this.isHovered) shouldSelect = true;

		// If current selection intersects this page, allow
		try {
			const sel = window.getSelection();
			if (sel && sel.rangeCount > 0) {
				const r = sel.getRangeAt(0);
				if (r && !r.collapsed) {
					if (r.intersectsNode && r.intersectsNode(this.pageRoot.nativeElement)) shouldSelect = true;
				}
			}
		} catch {}

		if (!shouldSelect) return;

		e.preventDefault();
		const sel = window.getSelection();
		if (!sel) return;
		sel.removeAllRanges();
		const range = document.createRange();
		range.selectNodeContents(this.pageRoot.nativeElement);
		sel.addRange(range);
	}

	logout() {
		this.authService.logout().subscribe({
			next: () => this.router.navigateByUrl('/acesso'),
			error: (e) => console.error(e)
		});
	}
}
