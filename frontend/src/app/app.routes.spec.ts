jest.mock('chessground', () => ({ Chessground: jest.fn() }));

import { routes } from './app.routes';

describe('routes', () => {
	it('defines the public, protected and fallback routes', () => {
		expect(routes.map(({ path }) => path)).toEqual([
			'acesso', 'esqueci-senha', '', 'subtopico', 'perfil', '**',
		]);
		expect(routes[0].canActivate).toHaveLength(1);
		expect(routes[2].canActivate).toHaveLength(1);
		expect(routes[5].component).toBeDefined();
	});
});
