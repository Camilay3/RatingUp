import { ChangeDetectorRef, Component, inject, OnInit } from '@angular/core';
import { AuthService } from '../../services/user/auth.service';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {
	private readonly authService = inject(AuthService);
	private readonly snackBar = inject(MatSnackBar);
	private readonly cdr = inject(ChangeDetectorRef);

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
