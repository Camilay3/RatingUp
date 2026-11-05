import { Component, computed, inject, input } from '@angular/core';
import { TimeControlService } from '../../services/chess/time-control.service';

@Component({
	selector: 'app-time-control-badge',
	standalone: true,
	templateUrl: './time-control-badge.component.html',
	styleUrl: './time-control-badge.component.scss',
})
export class TimeControlBadgeComponent {
	private readonly timeControlService = inject(TimeControlService);

	label = input.required<string>();
	initialSeconds = input.required<number>();
	incrementSeconds = input<number>(0);

	readonly time = computed(() => this.timeControlService.formatTimeBadge(this.initialSeconds(), this.incrementSeconds()));
}
