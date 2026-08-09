import { TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { NotFoundComponent } from './not-found.component';

describe('NotFoundComponent', () => {
	it('renders the not-found image and home link', async () => {
		await TestBed.configureTestingModule({
			imports: [NotFoundComponent],
			providers: [provideRouter([])],
		}).compileComponents();

		const fixture = TestBed.createComponent(NotFoundComponent);
		fixture.detectChanges();

		expect(fixture.nativeElement.querySelector('img').alt).toBe('Página não encontrada');
		expect(fixture.nativeElement.querySelector('a').getAttribute('routerLink')).toBe('/');
	});
});
