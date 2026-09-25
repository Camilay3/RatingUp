import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { IBook, ISubtopicoContent } from '../../interfaces/book/IBook';

@Injectable({
	providedIn: 'root'
})
export class BookService {
    private readonly apiUrl = environment.apiUrl;
	constructor( private readonly http : HttpClient ){}

	getSheets() {
		return this.http.get<IBook>(`${this.apiUrl}/livro/paginas`);
	}

	getSubtopicContent(subtopicId: number) {
		return this.http.get<ISubtopicoContent>(`${this.apiUrl}/subtopics/${subtopicId}/content`);
	}

	getSubtopicType(subtopicId: number){
     return this.http.get<{ subtopicId: number, type: 'BOARD' | 'MULTIPLE_CHOICE' }>(
     `${this.apiUrl}/subtopics/${subtopicId}/type`
    );
   }
}
