import { FormBuilder } from '@angular/forms';
import { of, throwError } from 'rxjs';
import { ForgotPassword } from './forgot-password';

describe('ForgotPassword', () => {
	let component: ForgotPassword;
	let service: { recoverPassword: jest.Mock; validateToken: jest.Mock; resetPassword: jest.Mock };
	let router: { navigate: jest.Mock };
	let snackBar: { open: jest.Mock };
	let cdr: { detectChanges: jest.Mock };

	beforeEach(() => {
		service = { recoverPassword: jest.fn(), validateToken: jest.fn(), resetPassword: jest.fn() };
		router = { navigate: jest.fn() };
		snackBar = { open: jest.fn() };
		cdr = { detectChanges: jest.fn() };
		component = new ForgotPassword(new FormBuilder(), snackBar as any, service as any, router as any, cdr as any);
		component.ngOnInit();
	});

	it('builds all forms and validates code boundaries', () => {
		expect(component.EmailForm.get('email')?.hasError('required')).toBe(true);
		component.CodeForm.setValue({ code: '1234' });
		expect(component.CodeForm.invalid).toBe(true);
		component.CodeForm.setValue({ code: '12345' });
		expect(component.CodeForm.valid).toBe(true);
		component.NewPasswordForm.setValue({ NewPassword: '<script>' });
		expect(component.NewPasswordForm.invalid).toBe(true);
	});

	it('moves from email to code on successful recovery and handles response errors', () => {
		component.EmailForm.setValue({ email: 'user@example.com' });
		service.recoverPassword.mockReturnValueOnce(of({ status: true }));
		component.emailSubmit();
		expect(component.step).toBe('code');
		expect(component.email).toBe('user@example.com');

		service.recoverPassword.mockReturnValueOnce(of({ status: false, message: 'not found' }));
		component.emailSubmit();
		expect(snackBar.open).toHaveBeenCalledWith('not found', 'Fechar', expect.any(Object));

		service.recoverPassword.mockReturnValueOnce(throwError(() => ({ error: { data: { email: 'invalid' } } })));
		component.emailSubmit();
		expect(component.EmailForm.get('email')?.hasError('backendError')).toBe(true);

		service.recoverPassword.mockReturnValueOnce(throwError(() => ({ error: { message: 'blocked' } })));
		component.emailSubmit();
		expect(snackBar.open).toHaveBeenCalledWith('blocked', 'Fechar', expect.any(Object));

		service.recoverPassword.mockReturnValueOnce(throwError(() => ({ error: {} })));
		component.emailSubmit();
		expect(snackBar.open).toHaveBeenCalledWith('Ops, ocorreu um erro inesperado', 'Fechar', expect.any(Object));
	});

	it('validates the token and resets the password', () => {
		component.CodeForm.setValue({ code: '12345' });
		service.validateToken.mockReturnValueOnce(of({}));
		component.codeSubmit();
		expect(component.step).toBe('password');

		service.validateToken.mockReturnValueOnce(throwError(() => ({ error: { message: 'expired' } })));
		component.codeSubmit();
		expect(snackBar.open).toHaveBeenCalledWith('expired', 'Fechar', expect.any(Object));
		service.validateToken.mockReturnValueOnce(throwError(() => ({ error: {} })));
		component.codeSubmit();
		expect(snackBar.open).toHaveBeenCalledWith('Código inválido', 'Fechar', expect.any(Object));

		component.NewPasswordForm.setValue({ NewPassword: 'NewPassword1!' });
		service.resetPassword.mockReturnValueOnce(of({ status: true }));
		component.NewPasswordSubmit();
		expect(router.navigate).toHaveBeenCalledWith(['/acesso']);

		service.resetPassword.mockReturnValueOnce(of({ status: false, message: 'rejected' }));
		component.NewPasswordSubmit();
		expect(snackBar.open).toHaveBeenCalledWith('rejected', 'Fechar', expect.any(Object));

		service.resetPassword.mockReturnValueOnce(throwError(() => ({ error: { data: { newPassword: 'weak' } } })));
		component.NewPasswordSubmit();
		expect(component.NewPasswordForm.get('NewPassword')?.hasError('backendError')).toBe(true);
	});

	it('uses generic messages for unexpected password errors and toggles visibility', () => {
		const initial = component.hidePassword;
		component.togglePassword();
		expect(component.hidePassword).toBe(!initial);
		component.NewPasswordForm.setValue({ NewPassword: 'NewPassword1!' });
		service.resetPassword.mockReturnValueOnce(throwError(() => ({ error: { message: 'bad' } })));
		component.NewPasswordSubmit();
		expect(snackBar.open).toHaveBeenCalledWith('bad', 'Fechar', expect.any(Object));
		service.resetPassword.mockReturnValueOnce(throwError(() => ({ error: {} })));
		component.NewPasswordSubmit();
		expect(snackBar.open).toHaveBeenCalledWith('Ops, ocorreu um erro inesperado', 'Fechar', expect.any(Object));
	});
});
