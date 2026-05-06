import { Routes } from '@angular/router';
import { SubtopicoComponent } from './components/subtopico/subtopico.component';
import { BookComponent } from './components/book/book.component';
import { Login } from './components/login/login';
import { authGuard } from './core/guards/auth-guard';

export const routes: Routes = [

  { path: '', component: Login },
  { path: '', redirectTo: 'login', pathMatch: 'full' },

  { path: 'book', component: BookComponent, canActivate: [authGuard] },
  { path: 'subtopico', component: SubtopicoComponent, canActivate: [authGuard] },
    // { path: '**', component: Page404Component }

];