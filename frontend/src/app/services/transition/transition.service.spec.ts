import { TransitionService } from './transition.service';

describe('TransitionService', () => {
	let service: TransitionService;

	beforeEach(() => {
		service = new TransitionService();
	});

	it('falls back when the image is not ready or the canvas has no context', async () => {
		const image = document.createElement('img');
		Object.defineProperty(image, 'complete', { value: false });
		expect(await service.extractDominantColor(image)).toBe(service.PRIMARY_COLOR);

		const imageWithoutContext = document.createElement('img');
		Object.defineProperties(imageWithoutContext, {
			complete: { value: true },
			naturalWidth: { value: 10 },
		});
		jest.spyOn(HTMLCanvasElement.prototype, 'getContext').mockReturnValue(null);
		expect(await service.extractDominantColor(imageWithoutContext)).toBe(service.PRIMARY_COLOR);
		jest.restoreAllMocks();
	});

	it('extracts a weighted dominant color and ignores transparent pixels', async () => {
		const context = {
			drawImage: jest.fn(),
			getImageData: jest.fn().mockReturnValue({
				data: new Uint8ClampedArray([255, 0, 0, 255, 0, 0, 0, 0]),
			}),
		};
		jest.spyOn(HTMLCanvasElement.prototype, 'getContext').mockReturnValue(context as any);
		const image = document.createElement('img');
		Object.defineProperties(image, {
			complete: { value: true },
			naturalWidth: { value: 10 },
		});

		expect(await service.extractDominantColor(image)).toBe('#ff0000');
		expect(context.drawImage).toHaveBeenCalled();
		jest.restoreAllMocks();
	});

	it('falls back when canvas processing throws or has no visible pixels', async () => {
		const image = document.createElement('img');
		Object.defineProperties(image, { complete: { value: true }, naturalWidth: { value: 10 } });
		jest.spyOn(HTMLCanvasElement.prototype, 'getContext').mockReturnValue({
			drawImage: jest.fn().mockImplementation(() => { throw new Error('canvas'); }),
		} as any);
		expect(await service.extractDominantColor(image)).toBe(service.PRIMARY_COLOR);

		jest.spyOn(HTMLCanvasElement.prototype, 'getContext').mockReturnValue({
			drawImage: jest.fn(),
			getImageData: jest.fn().mockReturnValue({ data: new Uint8ClampedArray([0, 0, 0, 0]) }),
		} as any);
		expect(await service.extractDominantColor(image)).toBe(service.PRIMARY_COLOR);
		jest.restoreAllMocks();
	});

	it('starts, finishes and resets transitions', async () => {
		const image = document.createElement('img');
		Object.defineProperties(image, {
			complete: { value: false },
			currentSrc: { value: 'current.webp' },
			src: { value: 'fallback.webp' },
		});
		const rect = new DOMRect(1, 2, 3, 4);

		await service.startTransition(image, rect);
		expect(service.state()).toMatchObject({ active: true, imageUrl: 'current.webp', phase: 'zoom', rect });

		service.finishTransition();
		expect(service.state()).toMatchObject({ active: false, phase: 'done', rect: null });
		service.reset();
		expect(service.state()).toMatchObject({ active: false, phase: 'idle', imageUrl: '' });
	});

	it('uses the image src when currentSrc is empty and handles grayscale pixels', async () => {
		const context = {
			drawImage: jest.fn(),
			getImageData: jest.fn().mockReturnValue({ data: new Uint8ClampedArray([0, 0, 0, 255]) }),
		};
		jest.spyOn(HTMLCanvasElement.prototype, 'getContext').mockReturnValue(context as any);
		const image = document.createElement('img');
		Object.defineProperties(image, {
			complete: { value: true },
			naturalWidth: { value: 10 },
			currentSrc: { value: '' },
			src: { value: 'fallback.webp' },
		});
		await service.startTransition(image, new DOMRect());
		expect(service.state().imageUrl).toBe('fallback.webp');
		jest.restoreAllMocks();
	});
});
