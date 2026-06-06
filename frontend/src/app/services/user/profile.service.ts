import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class ProfileService {
	constructor( private readonly http : HttpClient ){}
	private readonly apiUrl = environment.apiUrl;

	updateProfile(data: any) {
		return this.http.patch(`${this.apiUrl}/conta/me/atualizar`, data);
	}

	deleteAccount() {
		return this.http.delete(`${this.apiUrl}/conta/me/deletar`);
	}

	recoverPassword(email: string) {
		return this.http.post(`${this.apiUrl}/auth/recover-password`, { email });
	}

	resetPassword(data: any) {
		return this.http.post(`${this.apiUrl}/auth/reset-password`, data);
	}

	editPassword(oldPassword: string, newPassword: string) {
		return this.http.put(`${this.apiUrl}/auth/change-password`, { oldPassword, newPassword });
	}
}
