import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { TransitionOverlayComponent } from './components/transition-overlay/transition-overlay.component';

@Component({
	selector: 'app-root',
	templateUrl: './app.html',
	styleUrls: ['./app.scss'],
	imports: [RouterOutlet, TransitionOverlayComponent],
})
export class App {}