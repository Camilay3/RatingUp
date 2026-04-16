import { Routes } from '@angular/router';
import { NivelComponent } from './components/nivel/nivel.component';
import { BookComponent } from './components/book/book.component';

export const routes: Routes = [

	{ path: '', component: BookComponent },
    { path: 'nivel', component: NivelComponent },
    // { path: '**', component: Page404Component }

];
