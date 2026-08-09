import { of, throwError } from 'rxjs';
import { AuthService } from './auth.service';

describe('AuthService', () => {
	it('caches the current user unless a refresh is requested', () => {
		const user = { data: { email: 'user@example.com' } };
		const http = { get: jest.fn().mockReturnValue(of(user)) };
		const service = new AuthService(http as any, {} as any);

		service.me().subscribe();
		service.me().subscribe();
		service.me(true).subscribe();

		expect(http.get).toHaveBeenCalledTimes(2);
		expect(service.getMyUser()?.data.email).toBe('user@example.com');
	});

	it('delegates progress and logout operations and clears the cache', () => {
		const http = {
			get: jest.fn().mockReturnValue(of({ data: { email: 'a@b.com' } })),
			post: jest.fn().mockReturnValue(of({})),
			delete: jest.fn().mockReturnValue(of({})),
		};
		const service = new AuthService(http as any, {} as any);

		service.me().subscribe();
		service.atualizarProgresso(2, 3).subscribe();
		service.getProgresso().subscribe();
		service.logout().subscribe();

		expect(http.post).toHaveBeenCalledWith(expect.stringContaining('/progresso/atualiza-fase'), { chapter: 2, subtopic: 3 });
		expect(http.get).toHaveBeenCalledWith(expect.stringContaining('/progresso/disponiveis'));
		expect(http.delete).toHaveBeenCalledWith(expect.stringContaining('/auth/logout'));
		expect(service.getMyUser()).toBeNull();
	});

	it('does not cache failed user requests', () => {
		const http = { get: jest.fn().mockReturnValue(throwError(() => new Error('unauthorized'))) };
		const service = new AuthService(http as any, {} as any);

		service.me().subscribe({ error: () => undefined });
		expect(service.getMyUser()).toBeNull();
	});
});
