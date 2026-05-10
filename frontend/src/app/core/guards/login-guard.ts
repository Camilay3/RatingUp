import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { catchError, map, of } from 'rxjs';

import { AuthService } from '../../services/auth.service';

export const loginGuard: CanActivateFn = () => {
	const authService = inject(AuthService);
	const router = inject(Router);

	return authService.me().pipe(
		map(() => {
			router.navigate(['/book']);
			return false;
		}),
		catchError(() => of(true))
	);
};
