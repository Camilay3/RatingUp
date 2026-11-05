import { formatClock, LOW_TIME_MS } from './clock';

describe('formatClock', () => {
	it('shows a placeholder when there is no time', () => {
		expect(formatClock(null)).toBe('--:--');
		expect(formatClock(undefined)).toBe('--:--');
	});

	it('formats minutes and seconds', () => {
		expect(formatClock(600000)).toBe('10:00');
		expect(formatClock(65000)).toBe('1:05');
		expect(formatClock(10000)).toBe('0:10');
	});

	it('rounds up so 0:00 only appears at zero', () => {
		expect(formatClock(10001)).toBe('0:11');
	});

	it('formats hours', () => {
		expect(formatClock(3600000)).toBe('1:00:00');
		expect(formatClock(3725000)).toBe('1:02:05');
	});

	it('shows tenths below ten seconds', () => {
		expect(formatClock(9950)).toBe('0:09.9');
		expect(formatClock(500)).toBe('0:00.5');
	});

	it('never goes negative', () => {
		expect(formatClock(0)).toBe('0:00.0');
		expect(formatClock(-300)).toBe('0:00.0');
	});

	it('exposes the low-time threshold', () => {
		expect(LOW_TIME_MS).toBe(20000);
	});
});
