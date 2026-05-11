import { Injectable, signal } from "@angular/core";
import { environment } from "../../../environments/environment";
import { HttpClient } from "@angular/common/http";
import { IMyUser } from "../../interfaces/IMyUser";
import { tap } from "rxjs";
import { Router } from "@angular/router";

@Injectable({
	providedIn: 'root'
})
export class AuthService {
	private readonly apiUrl = environment.apiUrl;
	private readonly currentUser = signal<IMyUser | null>(null);

	constructor(private readonly http: HttpClient, private readonly router: Router) {}

	me() {
		return this.http.get<IMyUser>(`${this.apiUrl}/conta/me`).pipe(
    		tap(user => this.currentUser.set(user))
		);
	}

	logout() {
		this.router.navigateByUrl('/');
	}

	get getMyUser() { return this.currentUser.asReadonly(); }
}
