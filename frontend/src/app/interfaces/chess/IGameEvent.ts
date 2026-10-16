export interface IGameEvent {
	type: string;
	gameId: number;
	moveNumber: number;
	from: string | null;
	to: string | null;
	fen: string;
	turn: 'WHITE' | 'BLACK';
	boardStatus: string;
	gameStatus: string;
}
