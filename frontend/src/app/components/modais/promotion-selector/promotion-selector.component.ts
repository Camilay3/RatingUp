import { Component, inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';

@Component({
	selector: 'app-promotion-selector',
	imports: [MatDialogModule, MatButtonModule],
	templateUrl: './promotion-selector.component.html',
	styleUrls: ['./promotion-selector.component.scss'],
})
export class PromotionSelectorComponent {
	private readonly dialogRef = inject<MatDialogRef<PromotionSelectorComponent>>(MatDialogRef);
	readonly data = inject<{ color: 'white' | 'black' }>(MAT_DIALOG_DATA);

	readonly opcoes = [
		{ letra: 'q', nome: 'Dama',   branca: '♕', preta: '♛' },
		{ letra: 'r', nome: 'Torre',  branca: '♖', preta: '♜' },
		{ letra: 'b', nome: 'Bispo',  branca: '♗', preta: '♝' },
		{ letra: 'n', nome: 'Cavalo', branca: '♘', preta: '♞' },
	];

	escolher(letra: string) {
		this.dialogRef.close(letra);
	}
}
