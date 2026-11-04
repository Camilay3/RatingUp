import { Component, computed, inject, input } from '@angular/core';
import { RatingService } from '../../services/game/rating.service';
import { SkillLevel } from '../../interfaces/chess/IPlayerRating';
import { IRatingRow } from '../../interfaces/chess/IRatingRow';

// Bloco "Ratings" (um card por tipo de partida). Reutilizado no perfil e no lobby.
@Component({
	selector: 'app-ratings-panel',
	standalone: true,
	templateUrl: './ratings-panel.component.html',
	styleUrl: './ratings-panel.component.scss',
})
export class RatingsPanelComponent {
	private readonly ratingService = inject(RatingService);

	rows = input<IRatingRow[]>([]);

	readonly totalGames = computed(() => this.rows().reduce((sum, r) => sum + r.gamesPlayed, 0));

	skillLabel(level: SkillLevel): string {
		return this.ratingService.skillLabel(level);
	}
}
