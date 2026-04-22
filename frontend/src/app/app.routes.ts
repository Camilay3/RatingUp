import { Routes } from '@angular/router';
import { SubtopicoComponent } from './components/subtopico/subtopico.component';
import { BookComponent } from './components/book/book.component';

export const routes: Routes = [

	{ path: '', component: BookComponent },
    { path: 'subtopico', component: SubtopicoComponent },
    // { path: '**', component: Page404Component }

];
