import { FormBuilder } from '@angular/forms';
import { of, throwError } from 'rxjs';
import { ChangePasswordComponent } from './change-password.component';

describe('ChangePasswordComponent', () => {
	let component: ChangePasswordComponent;
	let profile: { editPassword: jest.Mock };
	let auth: { logout: jest.Mock };
	let snackBar: { open: jest.Mock };
	let router: { navigateByUrl: jest.Mock };

	beforeEach(() => {
		profile = { editPassword: jest.fn() };
		auth = { logout: jest.fn() };
		snackBar = { open: jest.fn() };
		router = { navigateByUrl: jest.fn() };
		component = new ChangePasswordComponent(profile as any, auth as any, snackBar as any, new FormBuilder(), router as any);
		component.ngOnInit();
	});

	it('rejects invalid and mismatched passwords before calling the API', () => {
		component.onChangePassword();
		expect(profile.editPassword).not.toHaveBeenCalled();

		component.ChangePassForm.setValue({ senhaAtual: 'old', senhaNova: 'newpassword', senhaNovaConfirmada: 'different' });
		component.onChangePassword();
		expect(component.ChangePassForm.get('senhaNovaConfirmada')?.hasError('mismatch')).toBe(true);
		expect(profile.editPassword).not.toHaveBeenCalled();
	});

	it('changes the password, logs out and redirects', () => {
		component.ChangePassForm.setValue({ senhaAtual: 'old', senhaNova: 'newpassword', senhaNovaConfirmada: 'newpassword' });
		profile.editPassword.mockReturnValueOnce(of({}));
		auth.logout.mockReturnValueOnce(of({}));
		component.onChangePassword();

		expect(profile.editPassword).toHaveBeenCalledWith('old', 'newpassword');
		expect(snackBar.open).toHaveBeenCalledWith('Senha alterada com sucesso', 'Fechar', expect.any(Object));
		expect(router.navigateByUrl).toHaveBeenCalledWith('/acesso');
	});

	it('reports logout errors after a successful password change', () => {
		component.ChangePassForm.setValue({ senhaAtual: 'old', senhaNova: 'newpassword', senhaNovaConfirmada: 'newpassword' });
		profile.editPassword.mockReturnValueOnce(of({}));
		auth.logout.mockReturnValueOnce(throwError(() => ({ error: { message: 'logout failed' } })));
		component.onChangePassword();
		expect(snackBar.open).toHaveBeenCalledWith('logout failed', 'Fechar', expect.any(Object));
	});

	it('maps structured and generic backend errors to the form', () => {
		component.ChangePassForm.setValue({ senhaAtual: 'old', senhaNova: 'newpassword', senhaNovaConfirmada: 'newpassword' });
		profile.editPassword.mockReturnValueOnce(throwError(() => ({ error: { data: {
			newPassword: 'weak', oldPassword: 'wrong old', confirmPassword: 'wrong confirm',
		} } })));
		component.onChangePassword();
		expect(component.ChangePassForm.get('senhaNova')?.hasError('backendError')).toBe(true);
		expect(component.ChangePassForm.get('senhaAtual')?.hasError('backendError')).toBe(true);

		component.ChangePassForm.get('senhaAtual')?.setErrors(null);
		component.ChangePassForm.get('senhaNova')?.setErrors(null);
		component.ChangePassForm.get('senhaNovaConfirmada')?.setErrors(null);
		profile.editPassword.mockReturnValueOnce(throwError(() => ({ error: { data: { newPassword: 'still weak' } } })));
		component.onChangePassword();
		expect(component.ChangePassForm.get('senhaNova')?.getError('backendError')).toBe('still weak');

		component.ChangePassForm.get('senhaAtual')?.setErrors(null);
		component.ChangePassForm.get('senhaNova')?.setErrors(null);
		component.ChangePassForm.get('senhaNovaConfirmada')?.setErrors(null);
		profile.editPassword.mockReturnValueOnce(throwError(() => ({ error: { message: 'bad', errors: ['one', 'two'] } })));
		component.onChangePassword();
		expect(component.ChangePassForm.get('senhaAtual')?.getError('backendError')).toBe('one, two');

		component.ChangePassForm.get('senhaAtual')?.setErrors(null);
		component.ChangePassForm.get('senhaNova')?.setErrors(null);
		component.ChangePassForm.get('senhaNovaConfirmada')?.setErrors(null);
		profile.editPassword.mockReturnValueOnce(throwError(() => ({ error: {} })));
		component.onChangePassword();
		expect(component.ChangePassForm.get('senhaAtual')?.getError('backendError')).toBe('Erro ao atualizar');
	});

	it('toggles each password field', () => {
		component.toggleSenhaAtual();
		component.toggleSenhaNova();
		component.toggleSenhaNovaConfirmada();
		expect(component.hideSenhaAtual).toBe(false);
		expect(component.hideSenhaNova).toBe(false);
		expect(component.hideSenhaNovaConfirmada).toBe(false);
	});
});
