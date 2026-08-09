import { AudioService } from './audio.service';

describe('AudioService', () => {
	const play = jest.fn();
	const pause = jest.fn();

	beforeEach(() => {
		(globalThis as any).Audio = jest.fn().mockImplementation(() => ({
			preload: '', currentTime: 0, loop: false, play, pause,
		}));
		play.mockClear();
		pause.mockClear();
	});

	it('preloads and controls the book sounds', () => {
		const service = new AudioService();

		service.playFlips();
		expect(service.pagesFlipSound.loop).toBe(true);
		service.stopFlips();
		expect(service.pagesFlipSound.loop).toBe(false);
		expect(service.pagesFlipSound.currentTime).toBe(0);

		service.playFlip();
		service.playOpen();
		service.playClose();
		expect(play).toHaveBeenCalledTimes(4);
		expect(service.openSound.preload).toBe('auto');
	});
});
