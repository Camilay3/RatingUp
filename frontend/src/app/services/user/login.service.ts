import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { IResponse } from '../../interfaces/IResponse';
import { Observable } from 'rxjs';

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

updateProfile(data: any) {
	return this.http.patch(`${this.apiUrl}/conta/me/atualizar`, data);
}


deleteAccount() {
  return this.http.delete(`${this.apiUrl}/conta/meu/deletar`);
}


  recoverPassword(email: string): Observable<IResponse> {
  return this.http.post<IResponse>(`${this.apiUrl}/auth/recover-password`, { email });
}

validateToken(token: string): Observable<IResponse> {
  return this.http.post<IResponse>(`${this.apiUrl}/auth/validate-token`, null, {
    params: { token }
  });
}

resetPassword(newPassword: string): Observable<IResponse> {
  return this.http.post<IResponse>(`${this.apiUrl}/auth/reset-password`, { newPassword });
}

}
