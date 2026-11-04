export const LOW_TIME_MS = 20_000;

const pad = (n: number) => String(n).padStart(2, '0');

// >= 10s: m:ss (ou h:mm:ss), arredondando para cima; < 10s: 0:ss.d com décimos
export function formatClock(ms: number | null | undefined): string {
	if (ms === null || ms === undefined) return '--:--';
	const safe = Math.max(0, ms);

	if (safe < 10_000) {
		const tenths = Math.floor(safe / 100);
		return `0:${pad(Math.floor(tenths / 10))}.${tenths % 10}`;
	}

	const total = Math.ceil(safe / 1000);
	const hours = Math.floor(total / 3600);
	const minutes = Math.floor((total % 3600) / 60);
	const seconds = total % 60;
	return hours > 0 ? `${hours}:${pad(minutes)}:${pad(seconds)}` : `${minutes}:${pad(seconds)}`;
}
