import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { ChessPiece } from '../../interfaces/chess/chess-piece.enum';

@Injectable({
  providedIn: 'root',
})
export class ChessService {
  private readonly apiUrl = environment.apiUrl;

  constructor(private http: HttpClient){}

  getStartChess(subtopicId: number){
    this.http.post(`${this.apiUrl}/move/session/start`, {
      subtopicId
    })
  }

  postMoveChess(){}
  
}
