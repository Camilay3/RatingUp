import { Routes } from '@angular/router';
import { SubtopicoComponent } from './components/subtopico/subtopico.component';
import { BookComponent } from './components/book/book.component';
import { Login } from './components/login/login';
import { authGuard } from './core/guards/auth-guard';
import { loginGuard } from './core/guards/login-guard';
import { NotFoundComponent } from './components/not-found.component';

export const routes: Routes = [

	{ path: '', component: Login, canActivate: [loginGuard] },

	{ path: 'book', component: BookComponent, canActivate: [authGuard] },
	{ path: 'subtopico', component: SubtopicoComponent, canActivate: [authGuard] },
    { path: '**', component: NotFoundComponent }

];
