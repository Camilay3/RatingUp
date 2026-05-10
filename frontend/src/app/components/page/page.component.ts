import { AuthService } from '../../services/user/auth.service';
import { Component, computed, input, output } from '@angular/core';
import { Router } from '@angular/router';
import { IPage, PageData } from '../../interfaces/IBook';

@Component({
  selector: 'app-page',
  templateUrl: './page.component.html',
  styleUrls: ['./page.component.scss'],
  imports: [],
})
export class PageComponent {
	page = input<PageData | IPage>();
	side = input<'frente' | 'verso'>();
	isWaiting = output<boolean>();
	navigate = output<{ qnt?: number; next?: boolean }>();

	constructor(
		private readonly router: Router,
		private readonly authService: AuthService,
	) {}

	private isSubtopicoBlocked(item: PageData | undefined): boolean {
		const valid = (item?.type === 'subtópico' && item.isBlocked) ?? false;
		if (valid) this.isWaiting.emit(valid);
		return valid;
	}
	pageIsBlocked = computed(() => this.isSubtopicoBlocked(this.page()));

	asType<T extends PageData['type']>( page: PageData | undefined, type: T): Extract<PageData, { type: T }> | undefined {
		return page?.type === type ? page as Extract<PageData, { type: T }> : undefined;
	}

	acessarSubtopico(capitulo: number, subtopico: number) {
		this.router.navigate(['/subtopico'], {
            state: { capitulo, subtopico }
        });
	}

	multiplasPaginas(qnt: number = 1, next: boolean = true) {
		this.navigate.emit({ qnt, next });
	}

	onImageError(event: Event) {
		(event.target as HTMLImageElement).src = '/subtopicos/default.png';
	}

	logout() {
		this.authService.logout();
	}
}
