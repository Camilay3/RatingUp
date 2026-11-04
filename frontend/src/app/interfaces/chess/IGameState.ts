import { GameEndReason, GameResult } from './IGameEvent';
import { TimeControlCategory } from './ITimeControl';

export interface IGameState {
	gameId: number;
	fen: string;
	moveNumber: number;
	turn: 'WHITE' | 'BLACK';
	myColor: 'WHITE' | 'BLACK';
	gameStatus: string;
	boardStatus: string;
	opponentNickname: string | null;
	initialTimeSeconds: number;
	incrementSeconds: number;
	timeControlCategory: TimeControlCategory;
	result: GameResult | null;
	endReason: GameEndReason | null;
	myRatingChange: number | null;
	myRating: number | null;
	opponentRating: number | null;
	whiteTimeMs: number;
	blackTimeMs: number;
}
