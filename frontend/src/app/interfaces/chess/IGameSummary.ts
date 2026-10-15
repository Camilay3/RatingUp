export interface IGameSummary {
	gameId: number;
	status: string;
	myColor: 'WHITE' | 'BLACK' | null;
	opponentNickname: string | null;
}
