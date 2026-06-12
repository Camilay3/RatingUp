import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { firstValueFrom } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class ChessService {
  private readonly apiUrl = environment.apiUrl;

  constructor(private http: HttpClient){}

  async startChess(subtopicId: number){
    const response = await firstValueFrom(
      this.http.post<{ sessionId: number; fen: string; status: string; initialFen: string }>(
        `${this.apiUrl}/move/session/start`,
        { subtopicId }
      )
    );

    return response;

  }

  async moveChess(sessionId: number, piece: string, posInitial: string, posFinal: string){
    const response = await firstValueFrom(
      this.http.post<{sessaoId: number, fen: string, status: string, initialFen: string}>(
        `${this.apiUrl}/move/session/move`,
        { sessionId, piece , posInitial , posFinal }
      )
    );

    return response;
  }
  
}
