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
			{ frente: 'Página 1', verso: 'Página 2' },
			{ frente: 'Página 3', verso: 'Página 4' },
			{ frente: 'Página 5', verso: 'Página 6' },
			{ frente: 'Página 7', verso: 'Página 8' },
			{ frente: 'Página 9', verso: 'Página 10' },
			{ frente: 'Página 11', verso: 'Página 12' },
			{ capa: 'quartaCapa.png' }
		];

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
