import { Component, OnInit } from '@angular/core';
import { IPages } from '../../interfaces/IPages';

@Component({
  selector: 'app-book',
  templateUrl: './book.component.html',
  styleUrls: ['./book.component.scss']
})
export class BookComponent implements OnInit {
	// onFirstPage: boolean = true;
	// onLastPage: boolean = false;
	isWaiting: boolean = false;
	// pageFlipStates: boolean[] = [];

	pages: IPages[] = [];
	tamanhoLivro: number = 0;
	zIndexValues: number[] = [];

	ngOnInit() {
		this.tamanhoLivro = this.pages.length;

		for (let i = 0; i < this.tamanhoLivro; i++) this.zIndexValues.push(this.tamanhoLivro - i + 1);
	}
}
