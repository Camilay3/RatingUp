export type TimeControlCategory = 'BULLET' | 'BLITZ' | 'RAPID' | 'CLASSICAL';

export interface ITimeControlPreset {
	category: TimeControlCategory;
	label: string;
	initialSeconds: number;
	incrementSeconds: number;
	display: string;
}
