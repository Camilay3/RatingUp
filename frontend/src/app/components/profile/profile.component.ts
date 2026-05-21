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
}
