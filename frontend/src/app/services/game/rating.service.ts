import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { firstValueFrom } from 'rxjs';
import { environment } from '../../../environments/environment';
import { IPlayerRating, SkillLevel } from '../../interfaces/chess/IPlayerRating';
import { IRatingRow } from '../../interfaces/chess/IRatingRow';
import { TimeControlService } from '../chess/time-control.service';

@Injectable({ providedIn: 'root' })
export class RatingService {
	private readonly apiUrl = `${environment.apiUrl}/games`;

	readonly skillLabels: Record<SkillLevel, string> = {
		UNRATED: 'Sem classificação',
		BEGINNER: 'Iniciante',
		NOVICE: 'Novato',
		INTERMEDIATE: 'Intermediário',
		ADVANCED: 'Avançado',
		EXPERT: 'Expert',
		MASTER: 'Mestre',
	};

	constructor(
		private http: HttpClient,
		private timeControlService: TimeControlService,
	) {}

	getMyRatings() {
		return firstValueFrom(this.http.get<IPlayerRating[]>(`${this.apiUrl}/ratings/me`));
	}

	skillLabel(level: SkillLevel): string {
		return this.skillLabels[level] ?? level;
	}

	// mostra sempre as 4 categorias; as que o jogador nunca jogou vêm zeradas
	toRatingRows(server: IPlayerRating[]): IRatingRow[] {
		return this.timeControlService.presets.map(p => {
			const r = server.find(x => x.category === p.category);
			return {
				category: p.category,
				label: p.label,
				display: p.display,
				rating: r?.rating ?? null,
				skillLevel: r?.skillLevel ?? 'UNRATED',
				gamesPlayed: r?.gamesPlayed ?? 0,
				wins: r?.wins ?? 0,
				losses: r?.losses ?? 0,
				draws: r?.draws ?? 0,
				performanceRating: r?.performanceRating ?? null,
			};
		});
	}
}
