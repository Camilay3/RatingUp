import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { environment } from '../../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class LoginService {

  constructor(
    private http : HttpClient,
    private router : Router
  ){}

  private apiUrl = environment.apiUrl;

  login(email: string, password: string) {
    return this.http.post<{ token: string }>(`${this.apiUrl}/conta/login`, { email, password });
  }
  
  register(data: any) {
    return this.http.post(`${this.apiUrl}/conta/cadastro`, data);
  }

  logout() {
    localStorage.removeItem('token');
    this.router.navigate(['/login']);
  }

  isLogged(): boolean {
    return !!localStorage.getItem('token');
  }

  // Atualizar perfil
updateProfile(data: any) {
  return this.http.patch(`${this.apiUrl}/conta/meu-perfil/atualizar`, data);
}

// Deletar conta
deleteAccount() {
  return this.http.delete(`${this.apiUrl}/conta/meu-perfil/deletar`);
}

// Recuperar senha
recoverPassword(email: string) {
  return this.http.post(`${this.apiUrl}auth/recover-password`, { email });
}

// Resetar senha
resetPassword(data: any) {
  return this.http.post(`${this.apiUrl}auth/reset-password`, data);
}

}