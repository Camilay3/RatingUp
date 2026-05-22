import { Component, inject, OnInit } from '@angular/core';
import { MatDialogModule, MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { MatSnackBarModule, MatSnackBar } from '@angular/material/snack-bar';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { CommonModule } from '@angular/common';
import { LoginService } from '../../../services/user/login.service';
import { AuthService } from '../../../services/user/auth.service';

@Component({
	selector: 'app-edit-profile',
	imports: [CommonModule, ReactiveFormsModule, MatDialogModule, MatButtonModule, MatSnackBarModule, MatFormFieldModule, MatInputModule],
	templateUrl: './edit-profile.component.html',
	styleUrls: ['./edit-profile.component.scss']
})
export class EditProfileComponent implements OnInit {

	public readonly data = inject(MAT_DIALOG_DATA);
	public readonly fb = inject(FormBuilder);
	private readonly snackBar = inject(MatSnackBar);
	private readonly dialogRef = inject(MatDialogRef<EditProfileComponent>);
	private readonly loginService = inject(LoginService);
	private readonly authService = inject(AuthService);
	EditForm!: FormGroup;

	ngOnInit() {
		const field = this.data?.fieldKey || 'nickname';
		let validators = [];

		switch (field) {
			case 'nickname':
				validators = [Validators.required, Validators.pattern('^[a-zA-Z0-9_-]+$'), Validators.minLength(3)];
				break;
			case 'email':
				validators = [Validators.required, Validators.email];
				break;
			case 'telefone':
				validators = [Validators.required, Validators.pattern('^[0-9]{8,11}$')];
				break;
			case 'name':
				validators = [Validators.required];
				break;
			default:
				validators = [Validators.required];
		}

		this.EditForm = this.fb.group({
			value: [this.data?.value ?? '', validators]
		});
	}

	onEdit() {
		if (this.EditForm.invalid) return;

		const field = this.data.fieldKey;
		const val = this.EditForm.value.value;

		const currentEmail = this.data.currentEmail || this.authService.getMyUser()?.data.email;
		const payload: any = { email: currentEmail };

		switch (field) {
			case 'nickname': payload.nickname = val; break;
			case 'name': payload.name = val; break;
			case 'email': payload.email = val; break;
			case 'telefone': payload.telefone = val.replace(/\D/g, ''); break;
		}

		this.loginService.updateProfile(payload).subscribe({
			next: () => {
				this.snackBar.open('Perfil atualizado', 'Fechar', { duration: 2500, panelClass: ['snack-success'] });
				this.authService.me(true).subscribe({ next: () => this.dialogRef.close(true) });
			},
			error: (e) => {
				const control = this.EditForm.get('value');
				let msg = 'Erro ao atualizar';
				if (e?.error?.message) msg = e.error.message;
				if (e?.error?.errors) msg = Array.isArray(e.error.errors) ? e.error.errors.join(', ') : String(e.error.errors);
				control?.setErrors({ backendError: msg });
			}
		});
	}

}
