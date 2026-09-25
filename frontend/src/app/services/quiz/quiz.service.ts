import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { IQuiz } from '../../interfaces/chess/iquiz';

@Injectable({
  providedIn: 'root',
})
export class QuizService {
    private readonly apiUrl = environment.apiUrl;

  constructor(private http: HttpClient){}

  searchQuiz(subtopicId: number){
      return this.http.get<IQuiz>(`${this.apiUrl}/multiple-choice/session/quiz/${subtopicId}`)
    }
  
    answerQuiz(subtopicId: number, selectedOptionId: number){
    return this.http.post<{ correct: boolean }>(
      `${this.apiUrl}/multiple-choice/session/quiz/answer`, 
      { subtopicId, selectedOptionId }
    )
    }
  
}
