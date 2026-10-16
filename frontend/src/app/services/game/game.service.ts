import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { firstValueFrom } from 'rxjs';
import { environment } from '../../../environments/environment';
import { IGameEvent } from '../../interfaces/chess/IGameEvent';
import { IGameState } from '../../interfaces/chess/IGameState';
import { IGameSummary } from '../../interfaces/chess/IGameSummary';

@Injectable({ providedIn: 'root' })
export class GameService {
	private readonly apiUrl = `${environment.apiUrl}/games`;

	constructor(private http: HttpClient) {}

	createGame() {
		return firstValueFrom(this.http.post<IGameState>(this.apiUrl, {}));
	}

	joinGame(gameId: number) {
		return firstValueFrom(this.http.post<IGameState>(`${this.apiUrl}/${gameId}/join`, {}));
	}

	cancelGame(gameId: number) {
		return firstValueFrom(this.http.delete<void>(`${this.apiUrl}/${gameId}`));
	}

	listOpen() {
		return firstValueFrom(this.http.get<IGameSummary[]>(`${this.apiUrl}/open`));
	}

	listMine() {
		return firstValueFrom(this.http.get<IGameSummary[]>(`${this.apiUrl}/mine`));
	}

	getGame(gameId: number) {
		return firstValueFrom(this.http.get<IGameState>(`${this.apiUrl}/${gameId}`));
	}

	move(gameId: number, from: string, to: string, promotion?: string) {
		return firstValueFrom(
			this.http.post<IGameEvent>(`${this.apiUrl}/${gameId}/moves`, { from, to, promotion })
		);
	}
}
