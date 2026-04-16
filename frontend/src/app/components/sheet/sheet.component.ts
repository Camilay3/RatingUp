import { ChangeDetectorRef, Component, computed, input, output } from '@angular/core';
import { Router } from '@angular/router';
import { ICapitulo, ISubtopico } from '../../interfaces/ICapitulo';
import { IConteudoPage } from '../../interfaces/IPages';

@Component({
  selector: 'app-sheet',
  templateUrl: './sheet.component.html',
  styleUrls: ['./sheet.component.scss', './assets/capitulo.scss'],
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
	backgroundImage = computed(() => this.capa() ? `url(/livro/${this.capa()})` : null);

	constructor( private readonly cdr: ChangeDetectorRef, private readonly router: Router ) {}

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

	acessarNivel(capitulo: number, nivel: number) {
		this.router.navigate(['/nivel'], {
            state: { capitulo, nivel }
        });
	}
}
