import { Injectable } from '@angular/core';
import { ITimeControlPreset, TimeControlCategory } from '../../interfaces/chess/ITimeControl';

@Injectable({ providedIn: 'root' })
export class TimeControlService {
	// Valores escolhidos para cair em cada faixa do backend (estimativa = inicial + 40 × incremento):
	// Bullet < 180s, Blitz < 600s, Rapid < 3600s, Classical >= 3600s
	readonly presets: ITimeControlPreset[] = [
		{ category: 'BULLET', label: 'Bullet', initialSeconds: 60, incrementSeconds: 0, display: '1+0' },
		{ category: 'BLITZ', label: 'Blitz', initialSeconds: 300, incrementSeconds: 0, display: '5+0' },
		{ category: 'RAPID', label: 'Rapid', initialSeconds: 600, incrementSeconds: 0, display: '10+0' },
		{ category: 'CLASSICAL', label: 'Clássico', initialSeconds: 3600, incrementSeconds: 0, display: '60+0' },
	];

	readonly defaultPreset: ITimeControlPreset = this.presets[2]; // Rapid, igual ao default do backend

	readonly categoryLabels: Record<TimeControlCategory, string> = {
		BULLET: 'Bullet',
		BLITZ: 'Blitz',
		RAPID: 'Rapid',
		CLASSICAL: 'Clássico',
	};

	readonly maxIncrementSeconds = 180; // mesmo limite do backend (CreateGameRequestDTO)

	categoryLabel(category?: string | null): string {
		return category ? (this.categoryLabels[category as TimeControlCategory] ?? category) : '';
	}

	presetOf(category: TimeControlCategory): ITimeControlPreset {
		return this.presets.find(p => p.category === category)!;
	}

	formatTimeControl(initialSeconds: number, incrementSeconds: number): string {
		return `${this.formatInitial(initialSeconds)}+${incrementSeconds}`;
	}

	// Formato dos botões: "1|0" (minutos|acréscimo em segundos)
	formatTimeBadge(initialSeconds: number, incrementSeconds: number): string {
		return `${this.formatInitial(initialSeconds)}|${incrementSeconds}`;
	}

	// Espelha TimeControlCategory.fromEstimatedDuration do backend
	estimateCategory(initialSeconds: number, incrementSeconds: number): TimeControlCategory {
		const estimated = initialSeconds + 40 * incrementSeconds;
		if (estimated < 180) return 'BULLET';
		if (estimated < 600) return 'BLITZ';
		if (estimated < 3600) return 'RAPID';
		return 'CLASSICAL';
	}

	private formatInitial(initialSeconds: number): string {
		return initialSeconds % 60 === 0 ? `${initialSeconds / 60}` : `${initialSeconds}s`;
	}
}
