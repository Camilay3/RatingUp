import { Component, input } from '@angular/core';
import { Router } from '@angular/router';
import { ICapitulo, ISubtopico } from '../../interfaces/ICapitulo';
import { IConteudoPage } from '../../interfaces/IPages';

@Component({
  selector: 'app-page',
  templateUrl: './page.component.html',
  styleUrls: ['./page.component.scss']
})
export class PageComponent {
	page = input<IConteudoPage>();

	constructor( private readonly router: Router ) {}

	asCapitulo(page: IConteudoPage | undefined): ICapitulo | undefined {
		return page?.tipo === 'capitulo' ? page : undefined;
	}
	asSubtopico(page: IConteudoPage | undefined): ISubtopico | undefined {
		return page?.tipo === 'subtopico' ? page : undefined;
	}

	acessarSubtopico(capitulo: number, subtopico: number) {
		this.router.navigate(['/subtopico'], {
            state: { capitulo, subtopico }
        });
	}
}
