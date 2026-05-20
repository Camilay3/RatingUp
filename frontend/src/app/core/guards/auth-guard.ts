import { inject } from "@angular/core";
import { CanActivateFn, Router } from "@angular/router";
import { map, catchError, of } from "rxjs";
import { AuthService } from "../../services/user/auth.service";

export const authGuard: CanActivateFn = () => {
	const authService = inject(AuthService);
	const router = inject(Router);

	return authService.me().pipe(
		map(() => true),
		catchError(() => {
			router.navigate(['/acesso']);
			return of(false);
		})
	);
};
