import { Component, OnInit, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { AuthService } from './services/auth.service';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App implements OnInit {
	protected readonly title = signal('frontend');
	constructor(private readonly authService: AuthService) {}
	ngOnInit() { this.authService.me().subscribe({ error: () => {} }); }
}
