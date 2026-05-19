import { AuthService } from '../../services/user/auth.service';
import { Component, computed, input, output} from '@angular/core';
import { Router } from '@angular/router';
import { IPage, ISheet, ISubtopico, PageData} from '../../interfaces/book/IBook';

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

	isSubtopico(page: PageData): page is ISubtopico {
		return page.type === 'subtópico';
	}

	acessarSubtopico(capitulo: number, subtopico: number) {
		this.router.navigate(['/subtopico'], {
            state: { capitulo, subtopico }
        });
	}

	getPages(item: any) {
    return [{ page: item, offset: 1 }];
    }

	multiplasPaginas(qnt: number = 1) {
		this.navigate.emit({ qnt });
	}

	onImageError(event: Event) {
		(event.target as HTMLImageElement).src = '/subtopicos/default.png';
	}

	logout() {
		this.authService.logout().subscribe({
			next: () => this.router.navigateByUrl('/'),
			error: (e) => console.error(e)
		});
	}
}
