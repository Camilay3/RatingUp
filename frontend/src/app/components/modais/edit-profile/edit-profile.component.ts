import { Component, inject, OnInit, AfterViewInit, ViewChild, ElementRef } from '@angular/core';
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
export class EditProfileComponent implements OnInit, AfterViewInit {

	@ViewChild('valueInput') valueInput?: ElementRef<HTMLInputElement>;

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
				validators = [Validators.required, Validators.pattern('^[a-zA-Z0-9_-]+$'), Validators.minLength(8), Validators.maxLength(16)];
				break;
			case 'email':
				validators = [Validators.required, Validators.email];
				break;
			case 'telefone':
				validators = [Validators.required, Validators.pattern('^[0-9]{10,11}$')];
				break;
			case 'name':
				validators = [Validators.required, Validators.pattern('^[a-zA-ZÀ-ÿ ]+$'), Validators.minLength(10), Validators.maxLength(100)];
				break;
			default:
				validators = [Validators.required];
		}

		this.EditForm = this.fb.group({
			value: [this.data?.value ?? '', validators]
		}, { updateOn: 'change' });
	}

	ngAfterViewInit(): void {
		const field = this.data?.fieldKey;
		if (field === 'telefone' && this.data?.value) {
			const digits = String(this.data.value).replace(/\D/g, '').slice(0, 11);
			let masked = digits;

			if (digits.length <= 2) {
				masked = `(${digits}`;
			} else if (digits.length <= 6) {
				masked = digits.replace(/(\d{2})(\d{0,4})/, '($1) $2');
			} else if (digits.length <= 10) {
				masked = digits.replace(/(\d{2})(\d{4})(\d{0,4})/, '($1) $2-$3');
			} else {
				masked = digits.replace(/(\d{2})(\d{5})(\d{0,4})/, '($1) $2-$3');
			}

			if (this.valueInput?.nativeElement) {
				this.valueInput.nativeElement.value = masked;
			}
		}
	}

	handleInput(event: Event) {
		const field = this.data?.fieldKey;
		if (field === 'telefone') {
			this.maskPhone(event);
		}
		if (field === 'nickname') {
			const control = this.EditForm.get('value');
			control?.updateValueAndValidity();
			if (control?.invalid) {
				control.markAsTouched();
			} else {
				control?.markAsUntouched();
			}
		}
	}

	maskPhone(event: Event){
		const input = event.target as HTMLInputElement;
		let digits = input.value.replace(/\D/g, '');

		if (digits.length === 0) {
			input.value = '';
			this.EditForm.get('value')?.setValue('', { emitEvent: false });
			return;
		}

		if(digits.length > 11){
			digits = digits.slice(0, 11);
		}

		let masked = digits;

		if (digits.length <= 2) {
			masked = `(${digits}`;
		} else if (digits.length <= 6) {
			masked = digits.replace(/(\d{2})(\d{0,4})/, '($1) $2');
		} else if (digits.length <= 10) {
			masked = digits.replace(/(\d{2})(\d{4})(\d{0,4})/, '($1) $2-$3');
		} else {
			masked = digits.replace(/(\d{2})(\d{5})(\d{0,4})/, '($1) $2-$3');
		}

		// store raw digits in the form control (no emit to avoid recursion)
		this.EditForm.get('value')?.setValue(digits, { emitEvent: false });
		setTimeout(() => { input.value = masked; }, 0);
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
