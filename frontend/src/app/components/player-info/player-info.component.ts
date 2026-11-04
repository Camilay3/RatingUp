import { Component, computed, input } from '@angular/core';
import { formatClock, LOW_TIME_MS } from '../../utils/clock';

@Component({
	selector: 'app-player-info',
	standalone: true,
	templateUrl: './player-info.component.html',
	styleUrl: './player-info.component.scss',
})
export class PlayerInfoComponent {
	name = input.required<string>();
	rating = input<number | null>(null);
	color = input<'white' | 'black'>('white');
	active = input<boolean>(false);
	change = input<number | null>(null);
	timeMs = input<number | null>(null);

	readonly clockText = computed(() => formatClock(this.timeMs()));
	readonly lowTime = computed(() => {
		const t = this.timeMs();
		return t !== null && t < LOW_TIME_MS;
	});
}
