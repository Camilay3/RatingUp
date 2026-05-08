import { Component, computed, input, output } from '@angular/core';
import { Router } from '@angular/router';
import { ICapitulo, IHome, ISubtopico } from '../../interfaces/ICapitulo';
import { IConteudoPage } from '../../interfaces/IPages';

@Component({
  selector: 'app-page',
  templateUrl: './page.component.html',
  styleUrls: ['./page.component.scss']
})
export class PageComponent {
	page = input<IConteudoPage>();
	isWaiting = output<boolean>();

	constructor( private readonly router: Router ) {}

	private isSubtopicoBlocked(item: IConteudoPage | undefined): boolean {
		const valid = (item?.tipo === 'subtopico' && item.isBlocked) ?? false;
		if (valid) this.isWaiting.emit(valid);
		return valid;
	}
	pageIsBlocked = computed(() => this.isSubtopicoBlocked(this.page()));

	asCapitulo(page: IConteudoPage | undefined): ICapitulo | undefined {
		return page?.tipo === 'capitulo' ? page : undefined;
	}
	asSubtopico(page: IConteudoPage | undefined): ISubtopico | undefined {
		return page?.tipo === 'subtopico' ? page : undefined;
	}
	asHome(page: IConteudoPage | undefined): IHome | undefined {
		return page?.tipo === 'home' ? page : undefined;
	}

	acessarSubtopico(capitulo: number, subtopico: number) {
		this.router.navigate(['/subtopico'], {
            state: { capitulo, subtopico }
        });
	}
}
