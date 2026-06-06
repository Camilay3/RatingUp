import { Injectable, signal } from "@angular/core";
import { environment } from "../../../environments/environment";
import { HttpClient } from "@angular/common/http";
import { IMyUser, IProgresso } from "../../interfaces/user/IMyUser";
import { of, tap } from "rxjs";
import { Router } from "@angular/router";
import { IResponse } from "../../interfaces/IResponse";

@Injectable({
	providedIn: 'root'
})
export class AuthService {
	private readonly apiUrl = environment.apiUrl;
	private readonly currentUser = signal<IMyUser | null>(null);

	constructor(private readonly http: HttpClient, private readonly router: Router) {}

	me(forceRefresh = false) {
		const user = this.currentUser();
		if (user && !forceRefresh) return of(user);

		return this.http.get<IMyUser>(`${this.apiUrl}/conta/me`).pipe(
			tap(user => this.currentUser.set(user))
		);
	}

	atualizarProgresso(chapter: number, subtopic: number) {
		return this.http.post<IProgresso>(`${this.apiUrl}/progresso/atualiza-fase`, { chapter, subtopic });
	}

	getProgresso() {
		return this.http.get<IProgresso>(`${this.apiUrl}/progresso/disponiveis`);
	}

	logout() {
		return this.http.delete<IResponse>(`${this.apiUrl}/auth/logout`).pipe(
			tap(() => this.currentUser.set(null))
		);
	}

	get getMyUser() { return this.currentUser.asReadonly(); }
}
