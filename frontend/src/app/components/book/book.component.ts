import { AfterViewInit, ChangeDetectorRef, Component, HostListener, OnInit, QueryList, ViewChildren } from '@angular/core';
import { SheetComponent } from "../sheet/sheet.component";
import { BookItem, HomePage, ISheet, PageData } from '../../interfaces/book/IBook';

import { BookService } from '../../services/book/book.service';
import { AudioService } from '../../services/book/audio.service';
import { AuthService } from '../../services/user/auth.service';
import { LoaderComponent } from '../loader/loader.component';
import { switchMap } from 'rxjs';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
	selector: 'app-book',
	templateUrl: './book.component.html',
	styleUrls: ['./book.component.scss'],
	imports: [SheetComponent, LoaderComponent]
})
export class BookComponent implements OnInit, AfterViewInit {
	onFirstPage: boolean = true;
	onLastPage: boolean = false;
	isWaiting: boolean = false;
	isLoading: boolean = true;
	showBookHint: boolean = false;
	pageFlipStates: boolean[] = [];

	capituloAtual: number = 1;
	subtopicoAtual: number = 1;

	pages: ISheet[] = [];
	book: BookItem[] = [];
	tamanhoLivro: number = 0;
	duracaoAnimacao: number = 1000;
	zIndexValues: number[] = [];

	constructor(
		private readonly cdr: ChangeDetectorRef,
		private readonly bookService: BookService,
		private readonly authService: AuthService,
		private readonly audioService: AudioService,
		private readonly snackBar : MatSnackBar,
	) {}

	private pageHeight = 0;
	private pagesLoaded = false;

	ngOnInit() {
		this.showBookHint = history.state?.mostrarDicaCapa === true;
		if (this.showBookHint) {
			const state = { ...history.state };
			delete state.mostrarDicaCapa;
			history.replaceState(state, '');
		}

		this.authService.getProgresso()
			.pipe(
				switchMap((response) => {
					this.capituloAtual = response.data.chapter;
					this.subtopicoAtual = response.data.subtopic;
					return this.bookService.getSheets();
				})

			).subscribe({
				next: (response) => {
					this.pages = response.data.pages.map(page => {
						(['front', 'verse'] as const).forEach(side => {
							const current = page[side];

							if (current.type == 'subtópico') {
								const isAfterCurrentChapter = current.chapterId > this.capituloAtual;
								const isAfterCurrentSubtopic =
									current.chapterId === this.capituloAtual &&
									current.displayOrder > this.subtopicoAtual;

								const lockOpen =
									current.chapterId === this.capituloAtual &&
									current.displayOrder === this.subtopicoAtual;

								page[side] = {
									...current,
									isBlocked: isAfterCurrentChapter || isAfterCurrentSubtopic,
									lockOpen,
								};
							}
						});
						return page;
					}) || [];

					this.buildBookFromPages();
					this.isLoading = false;
					this.cdr.detectChanges();
				},
				error: (e) => this.snackBar.open(e.error.message, 'Fechar', { duration: 3000 }),
				complete: () => {
					if (history.state?.executarAnimacao) this.multiplasPaginas(0);
				}
			})
	}

	ngAfterViewInit(): void {
		const containerEl = document.querySelector('.book-container');
		if (!containerEl) return;
		const pageContainerEl = containerEl.querySelector('.page-container') as HTMLElement;

		const ro = new ResizeObserver(() => {
			const pageHeight = pageContainerEl?.clientHeight || containerEl.clientHeight * 0.9;
			const pageWidth = pageContainerEl?.clientWidth || pageHeight * 13 / 9;
			const newHeight = Math.max(0, (pageHeight - pageWidth * 0.08) * 0.95);
			if (newHeight !== this.pageHeight) {
				this.pageHeight = newHeight;
				if (this.pagesLoaded) {
					this.buildBookFromPages();
					this.cdr.detectChanges();
				}
			}
		});

		ro.observe(containerEl);
		if (pageContainerEl) ro.observe(pageContainerEl);
	}

	private buildBookFromPages(): void {
		const homePages = this.splitSummaryIntoHomePages(this.pages);

		this.book = [
			{ capa: 'capa.webp', frenteCapa: true },
			...homePages,
			...this.pages,
			{ capa: 'quartaCapa.webp' }
		];

		this.tamanhoLivro = this.book.length;
		this.pageFlipStates = new Array(this.tamanhoLivro).fill(false);
		this.zIndexValues = [];
		for (let i = 0; i < this.tamanhoLivro; i++)
			this.zIndexValues.push(this.tamanhoLivro - i + 1);
		}

	private splitSummaryIntoHomePages(pages: ISheet[]): BookItem[] {
		const MAX_FIRST = this.calcMaxHeight(true);
		const MAX_REST = this.calcMaxHeight(false);

		const chunks: ISheet[][] = [];
		let current: ISheet[] = [];
		let currentHeight = 0;
		let isFirstChunk = true;

		const itemHeight = (item: PageData | null | undefined) => !item ? 0 : item.type === 'capitulo' ? 8 : 2;
		const pairHeight = (page: ISheet) => itemHeight(page.front) + itemHeight(page.verse);

		for (const page of pages) {
			const height = pairHeight(page);
			const maxHeight = isFirstChunk ? MAX_FIRST : MAX_REST;

			if (current.length && currentHeight + height > maxHeight) {
				chunks.push(current);
				current = [];
				currentHeight = 0;
				isFirstChunk = false;
			}

			current.push(page);
			currentHeight += height;
		}
		if (current.length) chunks.push(current);

		const homePages: BookItem[] = [];
		let chunkOffset = 0;

		for (let i = 0; i < chunks.length; i += 2) {
			const isFirstHome = i === 0;
			const front: HomePage = {
				type: 'home',
				nickname: isFirstHome ? this.authService.getMyUser()?.data.nickname ?? null : null,
				isFirstHome,
				summary: chunks[i],
				chunkOffset,
			};
			chunkOffset += chunks[i].length;

			let verse: HomePage | null = null;
			if (chunks[i + 1]) {
				verse = {
					type: 'home',
					nickname: null,
					isFirstHome: false,
					summary: chunks[i + 1],
					chunkOffset,
				};
				chunkOffset += chunks[i + 1].length;
			}

			homePages.push({ front, verse });
		}

		return homePages;
	}

	private calcMaxHeight(isFirst: boolean): number {
		if (!this.pageHeight) return isFirst ? 55 : 75;

		const PX_PER_UNIT = 9;
		const HEADER_PX = isFirst ? 120 : 0;

		return Math.floor((this.pageHeight - HEADER_PX) / PX_PER_UNIT);
	}

	@ViewChildren(SheetComponent) sheets!: QueryList<SheetComponent>;
	paginaAtual: number = 0;

	@HostListener('window:keydown.arrowright')
	onArrowRight() {
		if (this.isWaiting) return;
		const primeiraNavegacao = this.paginaAtual === 0 && !this.pageFlipStates[0];
		this.sheets.get(this.paginaAtual)?.virarPagina(false, primeiraNavegacao);
	}

	@HostListener('window:keydown.arrowleft')
	onArrowLeft() {
		if (this.isWaiting) return;
		const anterior = this.paginaAtual - 1;
		if (anterior >= 0) this.sheets.get(anterior)?.virarPagina();
	}

	onFlip(pageIndex: number, flipped: boolean) {
		if (pageIndex === 0 && flipped) this.showBookHint = false;
		this.zIndexValues[pageIndex] = Math.max(...this.zIndexValues) + 1;
		this.pageFlipStates[pageIndex] = flipped;
		this.checkPagesFlipped();
		this.paginaAtual = flipped ? pageIndex + 1 : pageIndex;
		if (!flipped) setTimeout(() => { this.reordenarZIndex(); }, this.duracaoAnimacao);
	}

	reordenarZIndex() {
		this.zIndexValues = this.pageFlipStates.map((flipped, index) => {
			return flipped ? index + 1 : this.tamanhoLivro - index;
		});
	}

	multiplasPaginas(qnt: number) {
		let next = true;
		this.isWaiting = true;
		this.duracaoAnimacao = 160;
		this.setVelocidade(this.duracaoAnimacao);
		if (qnt < 0) {
			qnt = this.paginaAtual - 1
			next = false;

		} else if (qnt == 0) {
			const canFlipForward = (index: number): boolean => {
				const page = this.book[index];

				return !!(page && (index + 1 !== this.book.length) &&
					(
						'capa' in page ||
						(page.front?.type !== 'subtópico' || !page.front.isBlocked) &&
						(page.verse?.type !== 'subtópico' || !page.verse.isBlocked)
					)
				);
			};

			let count = 0;
			for (let i = this.paginaAtual; i < this.tamanhoLivro; i++) {
				if (!canFlipForward(i)) break;
				count++;
			}
			qnt = count;

			if (qnt <= 0) {
				this.isWaiting = false;
				return;
			}
		}
		let viradas = 0;

		this.audioService.playFlips();
		const tempoTotal = (qnt - 1) * 100 + this.duracaoAnimacao;
		setTimeout(() => { this.audioService.stopFlips(); }, tempoTotal);

		const virar = () => {
			if (next) {
				if (this.paginaAtual >= this.tamanhoLivro) return;
				this.sheets.get(this.paginaAtual)?.virarPagina(true);

			} else {
				const anterior = this.paginaAtual - 1;
				if (anterior < 0) return;
				this.sheets.get(anterior)?.virarPagina(true);
			}
			viradas++;

			if (viradas >= qnt) {
				setTimeout(() => {
					this.duracaoAnimacao = 1000;
					this.setVelocidade(this.duracaoAnimacao);
					this.isWaiting = false;
					this.cdr.detectChanges();
				}, this.duracaoAnimacao);
				return;
			}
			setTimeout(virar, 100);
		};

		virar();
	}

	setVelocidade(valor: number) {
		document.documentElement.style.setProperty('--duracao', `${valor}ms`);
	}

	checkPagesFlipped() {
		this.onFirstPage = false;
		this.onLastPage = false;

		if (!this.pageFlipStates[0] || !this.pageFlipStates.length) this.onFirstPage = true;
		if (this.pageFlipStates[this.tamanhoLivro - 1]) this.onLastPage = true;
	}
}
