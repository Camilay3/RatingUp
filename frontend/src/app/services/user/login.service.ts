import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class LoginService {
  constructor( private readonly http : HttpClient ){}

  private readonly apiUrl = environment.apiUrl;

  login(email: string, password: string) {
    return this.http.post<{ data: string }>(`${this.apiUrl}/auth/login`, { email, password });
  }

  register(data: any) {
    return this.http.post(`${this.apiUrl}/conta/cadastro`, data);
  }

  // Atualizar perfil
updateProfile(data: any) {
	return this.http.patch(`${this.apiUrl}/conta/me/atualizar`, data);
}

// Deletar conta
deleteAccount() {
  return this.http.delete(`${this.apiUrl}/conta/meu/deletar`);
}

// Recuperar senha
recoverPassword(email: string) {
  return this.http.post(`${this.apiUrl}/auth/recover-password`, { email });
}

// Resetar senha
resetPassword(data: any) {
  return this.http.post(`${this.apiUrl}/auth/reset-password`, data);
}

}
