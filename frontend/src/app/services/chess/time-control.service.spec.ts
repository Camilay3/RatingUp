import { TimeControlService } from './time-control.service';

describe('TimeControlService', () => {
	const service = new TimeControlService();

	it('exposes the four presets, defaulting to Rapid', () => {
		expect(service.presets.map(p => p.category)).toEqual(['BULLET', 'BLITZ', 'RAPID', 'CLASSICAL']);
		expect(service.defaultPreset.category).toBe('RAPID');
		expect(service.presetOf('BLITZ')).toBe(service.presets[1]);
	});

	it('translates categories and keeps unknown ones', () => {
		expect(service.categoryLabel('CLASSICAL')).toBe('Clássico');
		expect(service.categoryLabel('OTHER')).toBe('OTHER');
		expect(service.categoryLabel(null)).toBe('');
	});

	it('formats time controls', () => {
		expect(service.formatTimeControl(600, 5)).toBe('10+5');
		expect(service.formatTimeControl(30, 0)).toBe('30s+0');
		expect(service.formatTimeBadge(60, 0)).toBe('1|0');
		expect(service.formatTimeBadge(45, 2)).toBe('45s|2');
	});

	it('estimates the category like the backend', () => {
		expect(service.estimateCategory(60, 0)).toBe('BULLET');
		expect(service.estimateCategory(60, 3)).toBe('BLITZ');
		expect(service.estimateCategory(600, 0)).toBe('RAPID');
		expect(service.estimateCategory(3600, 0)).toBe('CLASSICAL');
	});
});
