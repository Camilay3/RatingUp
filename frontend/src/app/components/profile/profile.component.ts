import { ChangeDetectorRef, Component, inject, OnInit } from '@angular/core';
import { AuthService } from '../../services/user/auth.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { EditProfileComponent } from '../modais/edit-profile/edit-profile.component';

@Component({
	selector: 'app-profile',
	templateUrl: './profile.component.html',
	imports: [ MatDialogModule ],
	styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {
	private readonly authService = inject(AuthService);
	private readonly snackBar = inject(MatSnackBar);
	private readonly cdr = inject(ChangeDetectorRef);
	private readonly dialog = inject(MatDialog);

	user = this.authService.getMyUser()?.data;
	capAtual: number = 1;

	ngOnInit() {
		this.authService.getProgresso().subscribe({
			next: (response) => {
				this.capAtual = response.data.chapter;
				this.cdr.detectChanges();
			},
			error: (e) => this.snackBar.open(e.error.message, 'Fechar', { duration: 3000 }),
		})
	}

	abrirModal(referencia: string, value?: string) {
		let fieldKey = referencia.toLowerCase();
		switch (referencia) {
			case 'Apelido': fieldKey = 'nickname'; break;
			case 'Nome': fieldKey = 'name'; break;
			case 'Email': fieldKey = 'email'; break;
			case 'Telefone': fieldKey = 'telefone'; break;
		}

		const dialogRef = this.dialog.open(EditProfileComponent, {
			width: '600px',
			panelClass: 'custom-edit-dialog',
			data: {
				header: `Editar ${referencia}`,
				fieldKey,
				value,
				currentEmail: this.user?.email
			}
		})

		dialogRef.afterClosed().subscribe(result => {
			if (result) {
				this.authService.me(true).subscribe({
					next: (resp) => {
						this.user = resp.data;
						this.cdr.detectChanges();
					}
				})
			}
		});
	}

	formatPhone(phone?: string | null): string {
		if (!phone) return '';
		let digits = phone.replace(/\D/g, '');

		if (digits.startsWith('55')) digits = digits.slice(2);

		if (digits.length === 10) return `(${digits.slice(0,2)}) ${digits.slice(2,6)}-${digits.slice(6)}`;

		if (digits.length === 11) return `(${digits.slice(0,2)}) ${digits.slice(2,7)}-${digits.slice(7)}`;

		if (digits.length > 2) {
			const ddd = digits.slice(0,2);
			const rest = digits.slice(2);

			return (rest.length > 4)
				? `(${ddd}) ${rest.slice(0, rest.length-4)}-${rest.slice(-4)}`
				: `(${ddd}) ${rest}`
		}

		return phone;
	}
}
