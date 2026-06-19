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
          const publicRoutes = [
            '/auth/recover-password',
            '/auth/validate-token',
            '/auth/reset-password',
            '/auth/login',
          ];

          const isPublic = publicRoutes.some(route => req.url.includes(route));

          if (isPublic) {
            return next(req); // não adiciona withCredentials nas rotas públicas
          }

          const authReq = req.clone({ withCredentials: true });
          return next(authReq);
        }
      ])
    )
  ]
};