import { ChangeDetectorRef, Component, inject, OnInit } from '@angular/core';
import { AuthService } from '../../services/user/auth.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { EditProfileComponent } from '../modais/edit-profile/edit-profile.component';
import { Router, RouterLink } from '@angular/router';
import { ChangePasswordComponent } from '../modais/change-password/change-password.component';

@Component({
	selector: 'app-profile',
	templateUrl: './profile.component.html',
	imports: [ MatDialogModule, RouterLink ],
	styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {
	private readonly authService = inject(AuthService);
	private readonly router = inject(Router);
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
			disableClose: true,
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

	solicitarSenha() {
		const dialogRef = this.dialog.open(ChangePasswordComponent, {
			width: '600px',
			panelClass: 'custom-edit-dialog',
			disableClose: true,
			data: {}
		})

		dialogRef.afterClosed().subscribe(result => {
			if (result) {
				this.authService.logout().subscribe({
					next: () => this.router.navigateByUrl('/acesso'),
					error: (e) => this.snackBar.open(e.error.message, 'Fechar', { duration: 3000 }),
				});
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

	formatDate(date?: string | Date | null): string {
		if (!date) return '';

		if (date instanceof Date) {
			const dd = String(date.getDate()).padStart(2, '0');
			const mm = String(date.getMonth() + 1).padStart(2, '0');
			const yyyy = date.getFullYear();
			return `${dd}/${mm}/${yyyy}`;
		}

		const s = String(date);
		const isoMatch = s.match(/^(\d{4})-(\d{2})-(\d{2})/);
		if (isoMatch) return `${isoMatch[3]}/${isoMatch[2]}/${isoMatch[1]}`;

		const parsed = new Date(s);
		if (!Number.isNaN(parsed.getTime())) {
			const dd = String(parsed.getDate()).padStart(2, '0');
			const mm = String(parsed.getMonth() + 1).padStart(2, '0');
			const yyyy = parsed.getFullYear();
			return `${dd}/${mm}/${yyyy}`;
		}

		return s;
	}
}
