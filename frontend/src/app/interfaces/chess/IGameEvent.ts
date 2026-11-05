export type GameEventType = 'STARTED' | 'MOVE' | 'RESIGNED' | 'TIMEOUT';
export type GameResult = 'WHITE_WINS' | 'BLACK_WINS' | 'DRAW';
export type GameEndReason = 'CHECKMATE' | 'RESIGNATION' | 'DRAW_BY_RULE' | 'TIMEOUT';

export interface IGameEvent {
	type: GameEventType;
	gameId: number;
	moveNumber: number;
	from: string | null;
	to: string | null;
	fen: string;
	turn: 'WHITE' | 'BLACK';
	boardStatus: string;
	gameStatus: string;
	result: GameResult | null;
	endReason: GameEndReason | null;
	whiteRatingChange: number | null;
	blackRatingChange: number | null;
	whiteTimeMs: number;
	blackTimeMs: number;
}
