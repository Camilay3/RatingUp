import { ApplicationConfig, provideBrowserGlobalErrorListeners } from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import { provideHttpClient, withInterceptors } from '@angular/common/http';

export const appConfig: ApplicationConfig = {
  providers: [
    provideBrowserGlobalErrorListeners(),
    provideRouter(routes),
	provideHttpClient(
		withInterceptors([
			(req, next) => {
				const authReq = req.clone({ withCredentials: true });
				return next(authReq);
			}
		])
	)
  ]
};
