jest.mock('@stomp/stompjs', () => ({ Client: jest.fn() }));

import { Client } from '@stomp/stompjs';
import { GameSocketService } from './game-socket.service';

describe('GameSocketService', () => {
	let clients: { activate: jest.Mock; deactivate: jest.Mock; subscribe: jest.Mock }[];
	let configs: any[];
	let service: GameSocketService;

	beforeEach(() => {
		clients = [];
		configs = [];
		(Client as unknown as jest.Mock).mockImplementation((config: any) => {
			const client = { activate: jest.fn(), deactivate: jest.fn(), subscribe: jest.fn() };
			clients.push(client);
			configs.push(config);
			return client;
		});
		service = new GameSocketService();
	});

	it('connects to the websocket endpoint and activates the client', () => {
		service.connect(1);

		expect(configs[0].brokerURL).toMatch(/^ws.*\/ws$/);
		expect(configs[0].reconnectDelay).toBe(3000);
		expect(clients[0].activate).toHaveBeenCalled();
	});

	it('subscribes to the game topic, forwards events and notifies the caller', () => {
		const onConnected = jest.fn();
		const received: any[] = [];
		service.events$.subscribe(e => received.push(e));

		service.connect(7, onConnected);
		configs[0].onConnect();

		expect(clients[0].subscribe).toHaveBeenCalledWith('/topic/game/7', expect.any(Function));
		const handler = clients[0].subscribe.mock.calls[0][1];
		handler({ body: JSON.stringify({ type: 'MOVE', moveNumber: 1 }) });

		expect(received).toEqual([{ type: 'MOVE', moveNumber: 1 }]);
		expect(onConnected).toHaveBeenCalled();
	});

	it('works without an onConnected callback', () => {
		service.connect(2);
		expect(() => configs[0].onConnect()).not.toThrow();
	});

	it('deactivates the previous client when reconnecting and on disconnect', () => {
		service.connect(1);
		service.connect(2);
		expect(clients[0].deactivate).toHaveBeenCalled();

		service.disconnect();
		expect(clients[1].deactivate).toHaveBeenCalled();
	});

	it('does nothing when disconnecting without a client', () => {
		expect(() => service.disconnect()).not.toThrow();
	});
});
