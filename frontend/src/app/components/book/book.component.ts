import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
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
				frente: { tipo: 'capitulo', id: 1, titulo: 'Capítulo 1', subtopicos: [] },
				verso: { tipo: 'subtopico', id: 1, titulo: 'Subtópico 1.1', imagem: 'subtopico1.png', url: '/subtopico/1' }
			},
			{
				frente: { tipo: 'subtopico', id: 2, titulo: 'Subtópico 1.2', url: '/subtopico/2' },
				verso: { tipo: 'capitulo', id: 2, titulo: 'Capítulo 2', subtopicos: [] }
			},
			{ capa: 'quartaCapa.png' }
		];

		// isCapitulo = true :: Numeração, Nome
		// subtópico :: id, Título,

		this.tamanhoLivro = this.pages.length;
		this.pageFlipStates = new Array(this.tamanhoLivro).fill(false);
		for (let i = 0; i < this.tamanhoLivro; i++) this.zIndexValues.push(this.tamanhoLivro - i + 1);
	}

	onFlip(pageIndex: number, flipped: boolean) {
		if (this.isWaiting) return;

		this.isWaiting = true;
		setTimeout(() => { this.isWaiting = false; this.cdr.detectChanges(); }, 600);

		const maiorZIndex = Math.max(...this.zIndexValues);
		this.zIndexValues[pageIndex] = maiorZIndex + 1;
		this.pageFlipStates[pageIndex] = flipped;
		this.checkPagesFlipped();
	}

	checkPagesFlipped() {
		this.onFirstPage = false;
		this.onLastPage = false;

		if (!this.pageFlipStates[0] || !this.pageFlipStates.length) this.onFirstPage = true;
		if (this.pageFlipStates[this.tamanhoLivro - 1]) this.onLastPage = true;
	}
}
