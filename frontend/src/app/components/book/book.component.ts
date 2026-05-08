import { ChangeDetectorRef, Component, HostListener, OnInit, QueryList, ViewChildren } from '@angular/core';
import { SheetComponent } from "../sheet/sheet.component";
import { IPages } from '../../interfaces/IPages';

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

	pages: IPages[] = [];
	tamanhoLivro: number = 0;
	zIndexValues: number[] = [];

	constructor(private readonly cdr: ChangeDetectorRef) {}

	ngOnInit() {
		this.pages = [
			{ capa: 'capa.png', frenteCapa: true },
			{
				frente: { tipo: 'capitulo', id: 1, titulo: 'Introdução ao Xadrez', subtopicos: [] },
				verso: { tipo: 'subtopico', id: 1, idCapitulo: 1, titulo: 'O que é o xadrez', url: '/subtopico/1' }
			},
			{
				frente: { tipo: 'subtopico', id: 2, idCapitulo: 1, titulo: 'Objetivo do jogo', url: '/subtopico/2' },
				verso: { tipo: 'capitulo', id: 2, titulo: 'O Tabuleiro e as Peças', subtopicos: [] }
			},
			{ capa: 'quartaCapa.png' }
		];

		// isCapitulo = true :: Numeração, Nome
		// subtópico :: id, Título,

		this.tamanhoLivro = this.pages.length;
		this.pageFlipStates = new Array(this.tamanhoLivro).fill(false);
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
		if (this.isWaiting) return;
		this.isWaiting = true;
		setTimeout(() => { this.isWaiting = false; this.cdr.detectChanges(); }, 600);

		const maiorZIndex = Math.max(...this.zIndexValues);
		this.zIndexValues[pageIndex] = maiorZIndex + 1;
		this.pageFlipStates[pageIndex] = flipped;
		this.checkPagesFlipped();
		this.paginaAtual = flipped ? pageIndex + 1 : pageIndex;
	}

	multiplasPaginas(qnt: number, next: boolean = true) {
		if (this.isWaiting) return;
		let viradas = 0;

		const virar = () => {
			if (viradas >= qnt) return;

			if (next) {
				if (this.paginaAtual >= this.tamanhoLivro) return;
				this.sheets.get(this.paginaAtual)?.virarPagina();

			} else {
				const anterior = this.paginaAtual - 1;
				if (anterior < 0) return;
				this.sheets.get(anterior)?.virarPagina();
			}

			viradas++;
			if (viradas < qnt) setTimeout(virar, 650);
		};

		virar();

		/* Exemplo de uso
			<button (click)="multiplasPaginas(3)">Avançar 3 páginas</button>
			<button (click)="multiplasPaginas(3, false)">Voltar 3 páginas</button>
		*/
	}

	checkPagesFlipped() {
		this.onFirstPage = false;
		this.onLastPage = false;

		if (!this.pageFlipStates[0] || !this.pageFlipStates.length) this.onFirstPage = true;
		if (this.pageFlipStates[this.tamanhoLivro - 1]) this.onLastPage = true;
	}
}
