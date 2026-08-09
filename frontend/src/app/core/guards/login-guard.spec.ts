import { TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { of, throwError, firstValueFrom } from 'rxjs';
import { AuthService } from '../../services/user/auth.service';
import { loginGuard } from './login-guard';

describe('loginGuard', () => {
	it('redirects authenticated users and allows unauthenticated users', async () => {
		const auth = { me: jest.fn().mockReturnValue(of({})) };
		const router = { navigate: jest.fn() };
		TestBed.configureTestingModule({ providers: [{ provide: AuthService, useValue: auth }, { provide: Router, useValue: router }] });

		const redirect = await firstValueFrom(TestBed.runInInjectionContext(() => loginGuard({} as any, {} as any) as any));
		expect(redirect).toBe(false);
		expect(router.navigate).toHaveBeenCalledWith(['/']);

		auth.me.mockReturnValue(throwError(() => new Error('unauthorized')));
		const allowed = await firstValueFrom(TestBed.runInInjectionContext(() => loginGuard({} as any, {} as any) as any));
		expect(allowed).toBe(true);
});
});
