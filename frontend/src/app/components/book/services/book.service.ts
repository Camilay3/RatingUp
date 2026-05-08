import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../../../environments/environment';
import { IBook } from '../../../interfaces/IBook';

@Injectable({
	providedIn: 'root'
})
export class BookService {
    private readonly apiUrl = environment.apiUrl;
	token = localStorage.getItem('token');
	constructor( private readonly http : HttpClient ){}

	getSheets() {
		return this.http.get<IBook>(`${this.apiUrl}/livro/paginas`);
	}
}
