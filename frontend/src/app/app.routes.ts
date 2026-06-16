import { Routes } from '@angular/router';
import { SubtopicoComponent } from './components/subtopico/subtopico.component';
import { BookComponent } from './components/book/book.component';
import { Login } from './components/login/login';
import { authGuard } from './core/guards/auth-guard';
import { loginGuard } from './core/guards/login-guard';
import { NotFoundComponent } from './components/not-found.component';
import { ProfileComponent } from './components/profile/profile.component';
import { ForgotPassword } from './components/forgot-password/forgot-password';

export const routes: Routes = [

	{ path: 'acesso', component: Login, canActivate: [loginGuard] },
	{ path: 'esqueci-senha', component: ForgotPassword },

	{ path: '', component: BookComponent, canActivate: [authGuard] },
	{ path: 'subtopico', component: SubtopicoComponent, canActivate: [authGuard] },
	{ path: 'perfil', component: ProfileComponent, canActivate: [authGuard] },

    { path: '**', component: NotFoundComponent },

];
