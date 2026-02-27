import { ChangeDetectorRef, Component, computed, input, output } from '@angular/core';

@Component({
  selector: 'app-sheet',
  templateUrl: './sheet.component.html',
  styleUrls: ['./sheet.component.scss']
})
export class SheetComponent {
	frente = input<string>();
	verso = input<string>();
	capa = input<string>();
	frenteCapa = input<boolean>(false);
	onFirstPage = input<boolean>(true);
	onLastPage = input<boolean>(true);
	protected flipped: boolean = false;
	flippedChange = output<boolean>();

	constructor( private readonly cdr: ChangeDetectorRef ) {
		console.log(this.capa())
	}

	backgroundImage = computed(() => this.capa() ? `url(/${this.capa()})` : null);

	virarPagina(): void {
		this.flipped = !this.flipped;
		this.cdr.detectChanges();
		this.flippedChange.emit(this.flipped);
	}
}
