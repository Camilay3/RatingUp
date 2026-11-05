import { Component, inject, input, model } from '@angular/core';
import { TimeControlService } from '../../services/chess/time-control.service';

@Component({
	selector: 'app-increment-selector',
	standalone: true,
	templateUrl: './increment-selector.component.html',
	styleUrl: './increment-selector.component.scss',
})
export class IncrementSelectorComponent {
	private readonly timeControlService = inject(TimeControlService);

	readonly min = 0;
	readonly max = this.timeControlService.maxIncrementSeconds;

	value = model<number>(0);
	disabled = input<boolean>(false);

	step(delta: number) {
		this.set(this.value() + delta);
	}

	set(next: number) {
		this.value.set(Math.min(this.max, Math.max(this.min, Math.round(next))));
	}
}
