import { TestBed } from '@angular/core/testing';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { PromotionSelectorComponent } from './promotion-selector.component';

describe('PromotionSelectorComponent', () => {
	it('closes the dialog with the chosen piece letter', () => {
		const dialogRef = { close: jest.fn() };
		TestBed.configureTestingModule({
			providers: [
				{ provide: MatDialogRef, useValue: dialogRef },
				{ provide: MAT_DIALOG_DATA, useValue: { color: 'white' } },
			],
		});

		const component = TestBed.runInInjectionContext(() => new PromotionSelectorComponent());

		expect(component.opcoes.map(o => o.letra)).toEqual(['q', 'r', 'b', 'n']);
		component.escolher('n');
		expect(dialogRef.close).toHaveBeenCalledWith('n');
	});

	it('renders the options for the player color and handles clicks', () => {
		const dialogRef = { close: jest.fn() };
		TestBed.configureTestingModule({
			providers: [
				{ provide: MatDialogRef, useValue: dialogRef },
				{ provide: MAT_DIALOG_DATA, useValue: { color: 'black' } },
			],
		});

		const fixture = TestBed.createComponent(PromotionSelectorComponent);
		fixture.detectChanges();

		const options = fixture.nativeElement.querySelectorAll('.opcao');
		expect(options).toHaveLength(4);
		expect(options[0].querySelector('.glifo').textContent).toBe('♛');

		options[1].click();
		expect(dialogRef.close).toHaveBeenCalledWith('r');
	});
});
