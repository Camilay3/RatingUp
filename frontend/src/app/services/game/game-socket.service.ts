import { Injectable } from '@angular/core';
import { Client } from '@stomp/stompjs';
import { Subject } from 'rxjs';
import { environment } from '../../../environments/environment';
import { IGameEvent } from '../../interfaces/chess/IGameEvent';

@Injectable({ providedIn: 'root' })
export class GameSocketService {
	private client?: Client;
	private readonly eventsSubject = new Subject<IGameEvent>();
	readonly events$ = this.eventsSubject.asObservable();

	// 'http://localhost:80/api' -> 'ws://localhost:80/api/ws' (e https -> wss)
	private readonly wsUrl = environment.apiUrl.replace(/^http/, 'ws') + '/ws';

	// onConnected roda depois de assinar o tópico, em toda (re)conexão
	connect(gameId: number, onConnected?: () => void) {
		this.disconnect();

		this.client = new Client({
			brokerURL: this.wsUrl,
			reconnectDelay: 3000,
			onConnect: () => {
				this.client!.subscribe(`/topic/game/${gameId}`, msg =>
					this.eventsSubject.next(JSON.parse(msg.body))
				);
				onConnected?.();
			},
		});
		this.client.activate();
	}

	disconnect() {
		this.client?.deactivate();
		this.client = undefined;
	}
}
