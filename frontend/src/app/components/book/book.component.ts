import { ChangeDetectorRef, Component, HostListener, OnInit, QueryList, ViewChildren } from '@angular/core';
import { SheetComponent } from "../sheet/sheet.component";
import { ISheet } from '../../interfaces/IBook';

import { BookService } from '../../services/book/book.service';
import { AudioService } from '../../services/book/audio.service';
import { AuthService } from '../../services/user/auth.service';
import { LoaderComponent } from '../loader/loader.component';

@Component({
	selector: 'app-book',
	templateUrl: './book.component.html',
	styleUrls: ['./book.component.scss'],
	imports: [SheetComponent, LoaderComponent]
})
export class BookComponent implements OnInit {
	onFirstPage: boolean = true;
	onLastPage: boolean = false;
	isWaiting: boolean = false;
	isLoading: boolean = true;
	pageFlipStates: boolean[] = [];

	capituloAtual: number = 1;
	subtopicoAtual: number = 1;

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

	ngOnInit() {
		this.authService.getProgresso().subscribe({
			next: (response) => {
				this.capituloAtual = response.data.chapter ?? 1;
				this.subtopicoAtual = response.data.subtopic ?? 1;
			},
			error: (e) => console.error(e)
		});

		this.bookService.getSheets().subscribe({
			next: (response) => {
				this.pages = response.data.pages.map(page => {
					let currentItem = false;

					(['front', 'verse'] as const).forEach(side => {
						const current = page[side];

						if (current.type == 'subtópico') {
							const isAfterCurrentChapter = current.chapterId > this.capituloAtual;
							const isAfterCurrentSubtopic =
								current.chapterId === this.capituloAtual &&
								current.displayOrder > this.subtopicoAtual;

							currentItem = isAfterCurrentChapter || isAfterCurrentSubtopic;
							page[side] = { ...current, isBlocked: currentItem };
						}
					});
					return page;
				}) || [];

				this.buildBookFromPages();
				this.cdr.detectChanges();
			},
			error: (e) => console.error(e),
			complete: () => this.isLoading = false
		});
	}

	private buildBookFromPages(): void {
		const home = [
			{
				front: {
					type: 'home',
					nickname: this.authService.getMyUser()?.data.nickname,
					summary: this.pages
				},
				verse: null
			}
		];

		this.book = [
			{ capa: 'capa.png', frenteCapa: true },
			...home,
			...this.pages,
			{ capa: 'quartaCapa.png' }
		];

		this.tamanhoLivro = this.book.length;
		this.pageFlipStates = new Array(this.tamanhoLivro).fill(false);
		this.zIndexValues = [];
		for (let i = 0; i < this.tamanhoLivro; i++) this.zIndexValues.push(this.tamanhoLivro - i + 1);
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

	multiplasPaginas(qnt: number) {
		let next = true;
		this.isWaiting = true;
		this.duracaoAnimacao = 160;
		this.setVelocidade(this.duracaoAnimacao);
		if (qnt < 0) {
			qnt = this.paginaAtual - 1
			next = false;
		};
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
