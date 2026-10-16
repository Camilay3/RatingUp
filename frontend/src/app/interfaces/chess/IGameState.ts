export interface IGameState {
	gameId: number;
	fen: string;
	moveNumber: number;
	turn: 'WHITE' | 'BLACK';
	myColor: 'WHITE' | 'BLACK';
	gameStatus: string;
	boardStatus: string;
	opponentNickname: string | null;
}
