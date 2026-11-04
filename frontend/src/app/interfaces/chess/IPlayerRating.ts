import { TimeControlCategory } from './ITimeControl';

export type SkillLevel =
	| 'UNRATED' | 'BEGINNER' | 'NOVICE' | 'INTERMEDIATE' | 'ADVANCED' | 'EXPERT' | 'MASTER';

export interface IPlayerRating {
	category: TimeControlCategory;
	rating: number;
	gamesPlayed: number;
	wins: number;
	losses: number;
	draws: number;
	performanceRating: number | null;
	skillLevel: SkillLevel;
}
