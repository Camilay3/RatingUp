jest.mock('chessground', () => ({ Chessground: jest.fn() }));

import { routes } from './app.routes';

describe('routes', () => {
	const byPath = (path: string) => routes.find((route) => route.path === path)!;

	it('defines the public, protected and fallback routes', () => {
		expect(routes.map(({ path }) => path)).toEqual([
			'lading', 'acesso', 'esqueci-senha', '', 'subtopico', 'perfil',
			'jogar', 'partida/:id', '**',
		]);

		expect(byPath('lading').canActivate).toHaveLength(1);
		expect(byPath('acesso').canActivate).toHaveLength(1);
		expect(byPath('').canActivate).toHaveLength(1);
		expect(byPath('**').component).toBeDefined();
	});
});
