import { ChangeDetectorRef, Component, computed, input, output } from '@angular/core';
import { RouterLink } from '@angular/router';
import { ICapitulo, ISubtopico } from '../../interfaces/ICapitulo';
import { IConteudoPage } from '../../interfaces/IPages';

@Component({
  selector: 'app-sheet',
  templateUrl: './sheet.component.html',
  styleUrls: ['./sheet.component.scss', './assets/capitulo.scss'],
  imports: [RouterLink]
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

	openSound = new Audio('/livro/sounds/openCover.mp3');
	closeSound = new Audio('/livro/sounds/closeCover.mp3');
	pageFlipSound = new Audio('/livro/sounds/flipPage.wav');

	constructor( private readonly cdr: ChangeDetectorRef ) {}

	backgroundImage = computed(() => this.capa() ? `url(/livro/${this.capa()})` : null);

	asCapitulo(page: IConteudoPage | undefined): ICapitulo | undefined {
		return page?.tipo === 'capitulo' ? page : undefined;
	}

	asSubtopico(page: IConteudoPage | undefined): ISubtopico | undefined {
		return page?.tipo === 'subtopico' ? page : undefined;
	}

	virarPagina(): void {
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
