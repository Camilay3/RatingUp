import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { TestBed } from '@angular/core/testing';
import { of, throwError } from 'rxjs';
import { EditProfileComponent } from './edit-profile.component';
import { LoginService } from '../../../services/user/login.service';
import { AuthService } from '../../../services/user/auth.service';
import { MatSnackBar } from '@angular/material/snack-bar';

describe('EditProfileComponent', () => {
	let component: EditProfileComponent;
	let login: { updateProfile: jest.Mock };
	let auth: { getMyUser: jest.Mock; me: jest.Mock };
	let dialog: { close: jest.Mock };
	let snack: { open: jest.Mock };

	beforeEach(() => jest.useFakeTimers());
	afterEach(() => jest.useRealTimers());

	function create(data: any) {
		login = { updateProfile: jest.fn() };
		auth = { getMyUser: jest.fn().mockReturnValue({ data: { email: 'current@example.com' } }), me: jest.fn() };
		dialog = { close: jest.fn() };
		snack = { open: jest.fn() };
		TestBed.configureTestingModule({
			imports: [EditProfileComponent],
			providers: [
				{ provide: MAT_DIALOG_DATA, useValue: data },
				{ provide: MatDialogRef, useValue: dialog },
				{ provide: LoginService, useValue: login },
				{ provide: AuthService, useValue: auth },
				{ provide: MatSnackBar, useValue: snack },
			],
		});
		component = TestBed.runInInjectionContext(() => new EditProfileComponent());
		component.ngOnInit();
	}

	it('creates validators for the selected profile field', () => {
		create({ fieldKey: 'nickname', value: '' });
		expect(component.EditForm.invalid).toBe(true);
		component.EditForm.setValue({ value: 'valid_nickname' });
		expect(component.EditForm.valid).toBe(true);

		TestBed.resetTestingModule();
		create({ fieldKey: 'email', value: '' });
		component.EditForm.setValue({ value: 'not-email' });
		expect(component.EditForm.invalid).toBe(true);

		TestBed.resetTestingModule();
		create({});
		expect(component.EditForm.invalid).toBe(true);
	});

	it('masks phone input and updates the raw form value', () => {
		create({ fieldKey: 'telefone', value: '' });
		const input = document.createElement('input');
		input.value = '';
		component.maskPhone({ target: input } as any);
		expect(component.EditForm.get('value')?.value).toBe('');
		input.value = '11987654321';
		component.maskPhone({ target: input } as any);
		expect(component.EditForm.get('value')?.value).toBe('11987654321');
		jest.runAllTimers();
		expect(input.value).toBe('(11) 98765-4321');

		for (const value of ['12', '123456', '1234567890', '1234567890123']) {
			input.value = value;
			component.maskPhone({ target: input } as any);
		}
		jest.runAllTimers();
		expect(component.EditForm.get('value')?.value).toBe('12345678901');
	});

	it('formats existing telephone values and updates nickname touch state', () => {
		for (const value of ['1', '12345', '1234567890', '12345678901']) {
			TestBed.resetTestingModule();
			create({ fieldKey: 'telefone', value });
			const input = document.createElement('input');
			component.valueInput = { nativeElement: input } as any;
			component.ngAfterViewInit();
			expect(input.value).toBeTruthy();
		}

		TestBed.resetTestingModule();
		create({ fieldKey: 'nickname', value: '' });
		const input = document.createElement('input');
		component.handleInput({ target: input } as any);
		expect(component.EditForm.get('value')?.touched).toBe(true);
		component.EditForm.setValue({ value: 'valid_name' });
		component.handleInput({ target: input } as any);
		expect(component.EditForm.get('value')?.touched).toBe(false);

		TestBed.resetTestingModule();
		create({ fieldKey: 'email', value: '' });
		component.ngAfterViewInit();

		TestBed.resetTestingModule();
		create({ fieldKey: 'telefone', value: '' });
		component.handleInput({ target: input } as any);
	});

	it('sends the selected field and refreshes the user on success', () => {
		create({ fieldKey: 'telefone', value: '' });
		component.EditForm.setValue({ value: '11987654321' });
		login.updateProfile.mockReturnValueOnce(of({}));
		auth.me.mockReturnValueOnce(of({}));
		component.onEdit();

		expect(login.updateProfile).toHaveBeenCalledWith({ email: 'current@example.com', telefone: '11987654321' });
		expect(auth.me).toHaveBeenCalledWith(true);
		expect(dialog.close).toHaveBeenCalledWith(true);

		TestBed.resetTestingModule();
		create({ fieldKey: 'name', value: '' });
		component.EditForm.setValue({ value: 'Ada Lovelace' });
		login.updateProfile.mockReturnValueOnce(of({}));
		auth.me.mockReturnValueOnce(of({}));
		component.onEdit();
		expect(login.updateProfile).toHaveBeenCalledWith({ email: 'current@example.com', name: 'Ada Lovelace' });
	});

	it('handles backend update errors', () => {
		create({ fieldKey: 'nickname', value: '' });
		component.onEdit();

		TestBed.resetTestingModule();
		create({ fieldKey: 'other', value: '' });
		component.EditForm.setValue({ value: 'anything' });
		login.updateProfile.mockReturnValueOnce(of({}));
		auth.me.mockReturnValueOnce(of({}));
		component.onEdit();
		expect(login.updateProfile).toHaveBeenCalledWith({ email: 'current@example.com' });

		TestBed.resetTestingModule();
		create({ fieldKey: 'nickname', value: '' });
		component.EditForm.setValue({ value: 'valid_nickname' });
		login.updateProfile.mockReturnValueOnce(throwError(() => ({ error: { errors: ['taken', 'invalid'] } })));
		component.onEdit();
		expect(component.EditForm.get('value')?.getError('backendError')).toBe('taken, invalid');

		TestBed.resetTestingModule();
		create({ fieldKey: 'email', value: '', currentEmail: 'provided@example.com' });
		component.EditForm.setValue({ value: 'new@example.com' });
		login.updateProfile.mockReturnValueOnce(of({}));
		auth.me.mockReturnValueOnce(of({}));
		component.onEdit();
		expect(login.updateProfile).toHaveBeenCalledWith({ email: 'new@example.com' });

		TestBed.resetTestingModule();
		create({ fieldKey: 'nickname', value: '' });
		component.EditForm.setValue({ value: 'valid_nickname' });
		login.updateProfile.mockReturnValueOnce(throwError(() => ({ error: { message: 'taken' } })));
		component.onEdit();
		expect(component.EditForm.get('value')?.getError('backendError')).toBe('taken');

		component.EditForm.get('value')?.setErrors(null);
		login.updateProfile.mockReturnValueOnce(throwError(() => ({ error: {} })));
		component.onEdit();
		expect(component.EditForm.get('value')?.getError('backendError')).toBe('Erro ao atualizar');

		component.EditForm.get('value')?.setErrors(null);
		login.updateProfile.mockReturnValueOnce(throwError(() => ({ error: { errors: 'invalid' } })));
		component.onEdit();
		expect(component.EditForm.get('value')?.getError('backendError')).toBe('invalid');
	});
});
