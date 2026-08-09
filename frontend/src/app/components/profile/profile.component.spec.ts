import { TestBed } from '@angular/core/testing';
import { of, throwError } from 'rxjs';
import { ProfileComponent } from './profile.component';
import { AuthService } from '../../services/user/auth.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { provideRouter } from '@angular/router';

describe('ProfileComponent', () => {
	let component: ProfileComponent;
	let auth: any;
	let snack: any;
	let dialog: any;
	let router: any;

	beforeEach(async () => {
		auth = {
			getMyUser: jest.fn().mockReturnValue({ data: { email: 'user@example.com', avatarUrl: null } }),
			getProgresso: jest.fn().mockReturnValue(of({ data: { chapter: 4 } })),
		};
		snack = { open: jest.fn() };
		dialog = { open: jest.fn() };
		TestBed.overrideComponent(ProfileComponent, {
			set: { providers: [{ provide: MatDialog, useValue: dialog }] },
		});
		await TestBed.configureTestingModule({
			imports: [ProfileComponent],
			providers: [
				{ provide: AuthService, useValue: auth },
				{ provide: MatSnackBar, useValue: snack },
				{ provide: MatDialog, useValue: dialog },
				provideRouter([]),
			],
		}).compileComponents();
		const fixture = TestBed.createComponent(ProfileComponent);
		component = fixture.componentInstance;
		router = TestBed.inject(Router);
		jest.spyOn(router, 'navigateByUrl');
		fixture.detectChanges();
	});

	it('loads the current chapter and formats profile values', () => {
		expect(component.capAtual).toBe(4);
		expect(component.avatarSrc).toBe('/userDefault.webp');
		expect(component.formatPhone('5511987654321')).toBe('(11) 98765-4321');
		expect(component.formatPhone('12')).toBe('12');
		expect(component.formatDate('2025-04-03')).toBe('03/04/2025');
		expect(component.formatDate(new Date(2025, 0, 2))).toBe('02/01/2025');
		expect(component.formatDate('not-a-date')).toBe('not-a-date');
	});

	it('reports progress loading errors', async () => {
		TestBed.resetTestingModule();
		auth.getProgresso.mockReturnValue(throwError(() => ({ error: { message: 'failed' } })));
		await TestBed.configureTestingModule({
			imports: [ProfileComponent],
			providers: [
				{ provide: AuthService, useValue: auth }, { provide: MatSnackBar, useValue: snack },
				{ provide: MatDialog, useValue: dialog }, provideRouter([]),
			],
		}).compileComponents();
		TestBed.createComponent(ProfileComponent).detectChanges();
		expect(snack.open).toHaveBeenCalledWith('failed', 'Fechar', expect.any(Object));
	});

	it('opens profile dialogs and refreshes the user after edits', () => {
		const afterClosed = jest.fn().mockReturnValue(of(true));
		dialog.open.mockReturnValue({ afterClosed });
		auth.me = jest.fn().mockReturnValue(of({ data: { email: 'new@example.com', avatarUrl: '/avatar.webp' } }));

		component.abrirModal('Nome', 'Ada');
		expect(dialog.open).toHaveBeenCalledWith(expect.anything(), expect.objectContaining({
			data: expect.objectContaining({ fieldKey: 'name', value: 'Ada' }),
		}));
		expect(component.user?.email).toBe('new@example.com');
		expect(component.avatarSrc).toContain('/avatar.webp');

		component.abrirAvatarModal();
		component.abrirModal('Campo desconhecido');
		component.abrirModal('Apelido');
		component.abrirModal('Email');
		component.abrirModal('Telefone');
		expect(dialog.open).toHaveBeenCalledTimes(6);
	});

	it('logs out after changing the password and formats edge cases', () => {
		dialog.open.mockReturnValue({ afterClosed: jest.fn().mockReturnValue(of(true)) });
		auth.logout = jest.fn().mockReturnValueOnce(of(undefined));
		component.solicitarSenha();
		expect(router.navigateByUrl).toHaveBeenCalledWith('/acesso');

		dialog.open.mockReturnValue({ afterClosed: jest.fn().mockReturnValue(of(true)) });
		auth.logout.mockReturnValueOnce(throwError(() => ({ error: { message: 'expired' } })));
		component.solicitarSenha();
		expect(snack.open).toHaveBeenCalledWith('expired', 'Fechar', { duration: 3000 });

		expect(component.formatPhone()).toBe('');
		expect(component.formatPhone('1198765432')).toBe('(11) 9876-5432');
		expect(component.formatPhone('1198')).toBe('(11) 98');
		expect(component.formatPhone('119876')).toBe('(11) 9876');
		expect(component.formatDate()).toBe('');
		expect(component.formatPhone('1234567')).toBe('(12) 3-4567');
		expect(component.formatDate('April 3, 2025')).toBe('03/04/2025');
	});
});
