import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { IMyUser } from '../interfaces/IMyUser';

@Injectable({
  providedIn: 'root'
})
export class HomeService {
	private readonly apiUrl = environment.apiUrl;
	constructor( private readonly http : HttpClient ){}

	getMyUser() {
		return this.http.get<IMyUser>(`${this.apiUrl}/conta/me`);
	}
}
