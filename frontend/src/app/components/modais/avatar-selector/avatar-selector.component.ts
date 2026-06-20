import { ChangeDetectorRef, Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { AvatarService } from '../../../services/user/avatar.service';
import { IAvatarItem } from '../../../interfaces/user/IAvatar';
import { environment } from '../../../../environments/environment';

@Component({
	selector: 'app-avatar-selector',
	imports: [CommonModule, MatDialogModule, MatButtonModule, MatSnackBarModule],
	templateUrl: './avatar-selector.component.html',
	styleUrls: ['./avatar-selector.component.scss']
})
export class AvatarSelectorComponent implements OnInit {
	private readonly apiUrl = environment.apiUrl;
	private readonly cdr = inject(ChangeDetectorRef);

	avatars: IAvatarItem[] = [];
	isLoading = true;
	isSelecting = false;
	selectedImageName: string | null = null;
	loadError = false;

	constructor(
		private readonly avatarService: AvatarService,
		private readonly snackBar: MatSnackBar,
		private readonly dialogRef: MatDialogRef<AvatarSelectorComponent>,
	) {}

	ngOnInit() {
		this.carregarAvatares();
	}

	carregarAvatares() {
		this.isLoading = true;
		this.loadError = false;
		this.cdr.detectChanges();

		this.avatarService.listAvatars().subscribe({
			next: (response) => {
				this.avatars = response.data ?? [];
				this.isLoading = false;
				this.cdr.detectChanges();
			},
			error: (e) => {
				this.isLoading = false;
				this.loadError = true;
				this.cdr.detectChanges();
				this.snackBar.open(e?.error?.message ?? 'Erro ao carregar avatares', 'Fechar', { duration: 3000 });
			}
		});
	}

	imageUrl(avatar: IAvatarItem): string {
		return `${this.apiUrl}${avatar.avatarurl}`;
	}

	selecionarAvatar(avatar: IAvatarItem) {
		if (this.isSelecting) return;

		this.isSelecting = true;
		this.selectedImageName = avatar.imageName;
		this.cdr.detectChanges();

		this.avatarService.selectAvatar(avatar.imageName).subscribe({
			next: () => {
				this.dialogRef.close(true);
			},
			error: (e) => {
				this.isSelecting = false;
				this.selectedImageName = null;
				this.cdr.detectChanges();
				this.snackBar.open(e?.error?.message ?? 'Erro ao atualizar avatar', 'Fechar', { duration: 3000 });
			}
		});
	}
}