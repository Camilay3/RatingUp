jest.mock('@angular/platform-browser', () => ({
	bootstrapApplication: jest.fn().mockResolvedValue(undefined),
}));

jest.mock('./app/app.config', () => ({ appConfig: { providers: [] } }));
jest.mock('./app/app', () => ({ App: class App {} }));

describe('main', () => {
	it('bootstraps the application', async () => {
		const { bootstrapApplication } = await import('@angular/platform-browser');
		await import('./main');
		expect(bootstrapApplication).toHaveBeenCalled();
	});

	it('logs bootstrap errors', async () => {
		jest.resetModules();
		const { bootstrapApplication } = await import('@angular/platform-browser');
		const error = new Error('failed');
		(bootstrapApplication as jest.Mock).mockRejectedValueOnce(error);
		const consoleError = jest.spyOn(console, 'error').mockImplementation();

		await import('./main');
		await Promise.resolve();

		expect(consoleError).toHaveBeenCalledWith(error);
		consoleError.mockRestore();
	});

	it('loads the development environment', async () => {
		const { environment } = await import('./environments/environment.development');
		expect(environment).toEqual({ production: false, apiUrl: 'http://localhost:80/api' });
	});
});
