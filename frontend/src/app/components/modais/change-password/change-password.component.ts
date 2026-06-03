import { Component, OnInit } from '@angular/core';
import { ProfileService } from '../../../services/user/profile.service';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatDialogModule } from '@angular/material/dialog';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { AuthService } from '../../../services/user/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-change-password',
	imports: [CommonModule, ReactiveFormsModule, MatDialogModule, MatButtonModule, MatSnackBarModule, MatFormFieldModule, MatInputModule, MatIconModule],
  templateUrl: './change-password.component.html',
  styleUrls: ['./change-password.component.scss']
})
export class ChangePasswordComponent implements OnInit {
	isLoading: boolean = false;
	hideSenhaAtual: boolean = true;
	hideSenhaNova: boolean = true;
	hideSenhaNovaConfirmada: boolean = true;
	senhaAtual: string = '';
	senhaNova: string = '';
	senhaNovaConfirmada: string = '';
	ChangePassForm!: FormGroup;

	constructor(
		private readonly profileService: ProfileService,
		private readonly authService: AuthService,
		private readonly snackBar: MatSnackBar,
		private readonly fb: FormBuilder,
		private readonly router: Router,
	) {}

	ngOnInit(): void {
		this.ChangePassForm = this.fb.group({
			senhaAtual: ['', [Validators.required]],
			senhaNova: ['', [Validators.required, Validators.minLength(8), Validators.maxLength(12)]],
			senhaNovaConfirmada: ['', [Validators.required, Validators.minLength(8), Validators.maxLength(12)]],
		});
	}

	toggleSenhaAtual() {
		this.hideSenhaAtual = !this.hideSenhaAtual;
	}

	toggleSenhaNova() {
		this.hideSenhaNova = !this.hideSenhaNova;
	}

	toggleSenhaNovaConfirmada() {
		this.hideSenhaNovaConfirmada = !this.hideSenhaNovaConfirmada;
	}

	onChangePassword() {
		if (this.ChangePassForm.invalid) return;
		const { senhaAtual, senhaNova, senhaNovaConfirmada } = this.ChangePassForm.value;

		if (senhaNova !== senhaNovaConfirmada) {
			const controlConfirm = this.ChangePassForm.get('senhaNovaConfirmada');
			controlConfirm?.setErrors({ mismatch: 'As senhas não coincidem' });
			return;
		}
		this.profileService.editPassword(senhaAtual, senhaNova).subscribe({
			next: (response) => {
				this.snackBar.open('Senha alterada com sucesso', 'Fechar', { duration: 2500, panelClass: ['snack-success'] });
				this.authService.logout().subscribe({
					next: () => this.router.navigateByUrl('/acesso'),
					error: (e) => this.snackBar.open(e.error.message, 'Fechar', { duration: 3000 })
				});
			},
			error: (e) => {
				const controlAtual = this.ChangePassForm.get('senhaAtual');
				const controlNova = this.ChangePassForm.get('senhaNova');
				const controlConfirm = this.ChangePassForm.get('senhaNovaConfirmada');

				const data = e?.error?.data;
				if (data && typeof data === 'object') {
					if (data.newPassword) controlNova?.setErrors({ backendError: data.newPassword });
					if (data.currentPassword) controlAtual?.setErrors({ backendError: data.currentPassword });
					if (data.confirmPassword) controlConfirm?.setErrors({ backendError: data.confirmPassword });

				} else {
					let msg = 'Erro ao atualizar';
					if (e?.error?.message) msg = e.error.message;
					if (e?.error?.errors) msg = Array.isArray(e.error.errors) ? e.error.errors.join(', ') : String(e.error.errors);
					controlAtual?.setErrors({ backendError: msg });
					controlNova?.setErrors({ backendError: msg });
					controlConfirm?.setErrors({ backendError: msg });
				}
			}
		})
	}
}
