import { CanActivateFn, Router } from '@angular/router';
import { LoginService } from '../../components/login/services/login.service';
import { inject } from '@angular/core';

export const authGuard: CanActivateFn = (route, state) => {

  const loginService = inject(LoginService);
  const router = inject(Router);

  if (loginService.isLogged()) {
    return true;
  }

  router.navigate(['/login']);

  return false;
};