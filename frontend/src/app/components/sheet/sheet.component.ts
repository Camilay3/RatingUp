import { ChangeDetectorRef, Component, computed, input, output } from '@angular/core';
import { PageComponent } from '../page/page.component';
import { IPage, PageData } from '../../interfaces/book/IBook';
import { AudioService } from '../../services/book/audio.service';

@Component({
  selector: 'app-sheet',
  templateUrl: './sheet.component.html',
  imports: [ PageComponent ],
  styleUrls: ['./sheet.component.scss'],
})
export class SheetComponent {
	frente = input<PageData>();
	verso = input<IPage>();
	capa = input<string>();
	frenteCapa = input<boolean>(false);
	onFirstPage = input<boolean>(true);
	onLastPage = input<boolean>(true);
	flippedChange = output<boolean>();
	protected flipped: boolean = false;
	isPageWaiting: boolean = false;
	isWaiting = output<boolean>();
	paginaAtual = input<number>(0);

	constructor( private readonly cdr: ChangeDetectorRef, private readonly audioService: AudioService ) {}
	navigate = output<{ qnt?: number; next?: boolean }>();

	virarPagina(multiplas: boolean = false, tocarSomDePagina: boolean = false): void {
		if (this.isPageWaiting) return;
		this.isPageWaiting = true;
		this.isWaiting.emit(this.isPageWaiting);

		this.flipped = !this.flipped;
		this.cdr.detectChanges();
		this.flippedChange.emit(this.flipped);

		if (this.capa()) {
			(this.frenteCapa()) ? this.audioService.playOpen() : this.audioService.playClose();
			if (tocarSomDePagina) this.audioService.playFlip();

		} else if (!multiplas) {
			this.audioService.playFlip();
		}

		const duracao = Number.parseInt(getComputedStyle(document.documentElement).getPropertyValue('--duracao'));
		setTimeout(() => {
			this.isPageWaiting = false;
			if (!multiplas) this.isWaiting.emit(this.isPageWaiting);
		 }, duracao);
	}

	frontImage = computed(() => {
		if (!this.capa()) return null;
		if (this.frenteCapa()) return `/livro/${this.capa()}`;
		return '/livro/contracapaFront.webp';
	});

	backImage = computed(() => {
		if (!this.capa()) return null;
		if (this.frenteCapa()) return '/livro/contracapa.webp';
		return `/livro/${this.capa()}`;
	});
}
