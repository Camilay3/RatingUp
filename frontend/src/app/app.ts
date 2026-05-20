import { Component, HostListener, OnInit } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { AuthService } from './services/user/auth.service';
import { LoaderComponent } from './components/loader/loader.component';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, LoaderComponent],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App implements OnInit {
	constructor(private readonly authService: AuthService) {}

	isLoading: boolean = true;
	@HostListener('window:load')
	onLoad() { this.isLoading = false; }

	ngOnInit() { this.authService.me().subscribe({ error: () => {} }); }
}
