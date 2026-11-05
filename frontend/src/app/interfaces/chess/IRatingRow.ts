import { SkillLevel } from './IPlayerRating';
import { TimeControlCategory } from './ITimeControl';

export interface IRatingRow {
	category: TimeControlCategory;
	label: string;
	display: string;
	rating: number | null;
	skillLevel: SkillLevel;
	gamesPlayed: number;
	wins: number;
	losses: number;
	draws: number;
	performanceRating: number | null;
}
