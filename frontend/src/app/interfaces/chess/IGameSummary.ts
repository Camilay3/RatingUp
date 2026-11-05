import { TimeControlCategory } from './ITimeControl';

export interface IGameSummary {
	gameId: number;
	status: string;
	myColor: 'WHITE' | 'BLACK' | null;
	opponentNickname: string | null;
	initialTimeSeconds: number;
	incrementSeconds: number;
	timeControlCategory: TimeControlCategory;
}
