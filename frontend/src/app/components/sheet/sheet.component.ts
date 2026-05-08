import { ChangeDetectorRef, Component, computed, input, output } from '@angular/core';
import { IConteudoPage } from '../../interfaces/IPages';
import { PageComponent } from '../page/page.component';

@Component({
  selector: 'app-sheet',
  templateUrl: './sheet.component.html',
  imports: [ PageComponent ],
  styleUrls: ['./sheet.component.scss'],
})
export class SheetComponent {
	frente = input<IConteudoPage>();
	verso = input<IConteudoPage>();
	capa = input<string>();
	frenteCapa = input<boolean>(false);
	onFirstPage = input<boolean>(true);
	onLastPage = input<boolean>(true);
	flippedChange = output<boolean>();
	protected flipped: boolean = false;
	isPageWaiting: boolean = false;

	constructor( private readonly cdr: ChangeDetectorRef ) {}

	openSound = new Audio('/livro/sounds/openCover.mp3');
	closeSound = new Audio('/livro/sounds/closeCover.mp3');
	pageFlipSound = new Audio('/livro/sounds/flipPage.wav');
	backgroundImage = computed(() => this.capa() ? `url(/livro/${this.capa()})` : null);

	virarPagina(): void {
		if (this.isPageWaiting) return;
		this.flipped = !this.flipped;
		this.cdr.detectChanges();
		this.flippedChange.emit(this.flipped);

		if (this.capa()) {
			(this.frenteCapa()) ? this.openSound.play() : this.closeSound.play();

		} else {
			this.pageFlipSound.play();
		}
	}
}
