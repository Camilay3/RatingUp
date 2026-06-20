import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { Observable } from 'rxjs';
import { IAvatarList } from '../../interfaces/user/IAvatar';
import { IResponse } from '../../interfaces/IResponse';

@Injectable({
	providedIn: 'root',
})
export class AvatarService {
	constructor(private readonly http: HttpClient) {}

	private readonly apiUrl = environment.apiUrl;

	listAvatars(): Observable<IAvatarList> {
		return this.http.get<IAvatarList>(`${this.apiUrl}/avatar/avatar-list`);
	}

	selectAvatar(imageName: string): Observable<IResponse> {
		return this.http.patch<IResponse>(`${this.apiUrl}/avatar/update`, { imageName });
	}
}