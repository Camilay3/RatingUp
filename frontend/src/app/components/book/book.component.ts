import { ChangeDetectorRef, Component, HostListener, OnInit, QueryList, ViewChildren } from '@angular/core';
import { SheetComponent } from "../sheet/sheet.component";
import { ISheet } from '../../interfaces/IBook';

import { BookService } from '../../services/book/book.service';
import { AudioService } from '../../services/book/audio.service';
import { AuthService } from '../../services/user/auth.service';

@Component({
	selector: 'app-book',
	templateUrl: './book.component.html',
	styleUrls: ['./book.component.scss'],
	imports: [SheetComponent]
})
export class BookComponent implements OnInit {
	onFirstPage: boolean = true;
	onLastPage: boolean = false;
	isWaiting: boolean = false;
	pageFlipStates: boolean[] = [];

	pages: ISheet[] = [];
	home: any[] = [];
	book: any[] = [];
	tamanhoLivro: number = 0;
	duracaoAnimacao: number = 1000;
	zIndexValues: number[] = [];
	nickname: string = '';

	constructor(
		private readonly cdr: ChangeDetectorRef,
		private readonly bookService: BookService,
		private readonly authService: AuthService,
		private readonly audioService: AudioService,
	) {}

	private pageHeight = 0;
	private pagesLoaded = false;

	ngOnInit() {
		this.bookService.getSheets().subscribe({
			next: (response) => {
				this.pages = response.data.pages || [];
				this.pagesLoaded = true;
				if (this.pageHeight) {
					this.buildBookFromPages();
					this.cdr.detectChanges();
				}
			},
			error: (e) => console.error(e)
		});
	}

	ngAfterViewInit(): void {
		const containerEl = document.querySelector('.book-container');
		if (!containerEl) return;

		const ro = new ResizeObserver(entries => {
			const newHeight = entries[0].contentRect.height;
			if (newHeight !== this.pageHeight) {
				this.pageHeight = newHeight * 0.95;
				if (this.pagesLoaded) {
					this.buildBookFromPages();
					this.cdr.detectChanges();
				}
			}
		});

		ro.observe(containerEl);
	}

	private buildBookFromPages(): void {
		const homePages = this.splitSummaryIntoHomePages(this.pages);

		this.book = [
			{ capa: 'capa.png', frenteCapa: true },
			...homePages,
			...this.pages,
			{ capa: 'quartaCapa.png' }
		];

		this.tamanhoLivro = this.book.length;
		this.pageFlipStates = new Array(this.tamanhoLivro).fill(false);
		this.zIndexValues = [];
		for (let i = 0; i < this.tamanhoLivro; i++)
			this.zIndexValues.push(this.tamanhoLivro - i + 1);
		}

		private splitSummaryIntoHomePages(pages: any[]): any[] {
			const MAX_FIRST = this.calcMaxHeight(true);
			const MAX_REST  = this.calcMaxHeight(false);

			const chunks: any[][] = [];
			let current: any[] = [];
			let currentHeight = 0;
			let isFirstChunk = true;

			const itemHeight = (item: any) => item?.type === 'capitulo' ? 8 : 4;
			const pairHeight = (page: any) => itemHeight(page.front) + itemHeight(page.verse);
			const startsNewSection = (page: any) =>
				page.front.type === 'capitulo' || page.verse?.type === 'capitulo';

			const maxForCurrent = () => isFirstChunk ? MAX_FIRST : MAX_REST;

			for (const page of pages) {
				const h = pairHeight(page);

				if (currentHeight + h > maxForCurrent() && startsNewSection(page)) {
					chunks.push(current);
					current = [];
					currentHeight = 0;
					isFirstChunk = false;
				}

				current.push(page);
				currentHeight += h;
			}
			if (current.length) chunks.push(current);

			const homePages: any[] = [];

			for (let i = 0; i < chunks.length; i += 2) {
				homePages.push({
					front: {
						type: 'home',
						nickname: i === 0 ? this.authService.getMyUser()?.data.nickname : null,
						isFirstHome: i === 0,
						summary: chunks[i],
						chunkOffset: this.countPagesInChunks(chunks, i)
					},
					verse: chunks[i + 1] ? {
						type: 'home',
						nickname: null,
						isFirstHome: false,
						summary: chunks[i + 1],
						chunkOffset: this.countPagesInChunks(chunks, i + 1)
					} : null
				});
			}
		return homePages;
	}

	private calcMaxHeight(isFirst: boolean): number {
		if (!this.pageHeight) return isFirst ? 55 : 75;

		const PX_PER_UNIT = 9;
		const HEADER_PX = isFirst ? 180 : 0;

		return Math.floor((this.pageHeight - HEADER_PX) / PX_PER_UNIT);
	}

	private countPagesInChunks(chunks: any[][], targetIndex: number): number {
		let count = 0;
		for (let i = 0; i < targetIndex; i++) count += chunks[i].length;
		return count;
	}

	@ViewChildren(SheetComponent) sheets!: QueryList<SheetComponent>;
	paginaAtual: number = 0;

	@HostListener('window:keydown.arrowright')
	onArrowRight() {
		if (this.isWaiting) return;
		this.sheets.get(this.paginaAtual)?.virarPagina();
	}

	@HostListener('window:keydown.arrowleft')
	onArrowLeft() {
		if (this.isWaiting) return;
		const anterior = this.paginaAtual - 1;
		if (anterior >= 0) this.sheets.get(anterior)?.virarPagina();
	}

	onFlip(pageIndex: number, flipped: boolean) {
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

	multiplasPaginas(qnt: number, next: boolean = true) {
		this.isWaiting = true;
		this.duracaoAnimacao = 160;
		this.setVelocidade(this.duracaoAnimacao);
		if (qnt < 0) qnt = this.paginaAtual - 1;
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
				}, this.duracaoAnimacao);
				this.isWaiting = false;
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
