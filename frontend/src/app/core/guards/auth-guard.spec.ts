import { TestBed } from '@angular/core/testing';
import { CanActivateFn } from '@angular/router';
import { Router } from '@angular/router';
import { AuthService } from '../../services/user/auth.service';
import { firstValueFrom, of, throwError } from 'rxjs';

import { authGuard } from './auth-guard';

describe('authGuard', () => {
  const executeGuard: CanActivateFn = (...guardParameters) => 
      TestBed.runInInjectionContext(() => authGuard(...guardParameters));

  beforeEach(() => TestBed.configureTestingModule({
    providers: [
      { provide: AuthService, useValue: { me: jest.fn() } },
      { provide: Router, useValue: { navigate: jest.fn() } },
    ],
  }));

  it('allows authenticated users', async () => {
    TestBed.inject(AuthService).me = jest.fn().mockReturnValue(of({}));
    await expect(firstValueFrom(executeGuard({} as any, {} as any) as any)).resolves.toBe(true);
  });

  it('redirects and blocks unauthenticated users', async () => {
    const router = TestBed.inject(Router);
    TestBed.inject(AuthService).me = jest.fn().mockReturnValue(throwError(() => new Error('unauthorized')));

    await expect(firstValueFrom(executeGuard({} as any, {} as any) as any)).resolves.toBe(false);
    expect(router.navigate).toHaveBeenCalledWith(['/acesso']);
  });
});
