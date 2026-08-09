import { of } from 'rxjs';
import { LoginService } from './login.service';

describe('LoginService', () => {
	it('maps authentication and account operations to their endpoints', () => {
		const http = {
			post: jest.fn().mockReturnValue(of({})),
			patch: jest.fn().mockReturnValue(of({})),
			delete: jest.fn().mockReturnValue(of({})),
		};
		const service = new LoginService(http as any);

		service.login('a@b.com', 'secret').subscribe();
		service.register({ email: 'a@b.com' }).subscribe();
		service.updateProfile({ name: 'Ada' }).subscribe();
		service.deleteAccount().subscribe();
		service.recoverPassword('a@b.com').subscribe();
		service.validateToken('12345').subscribe();
		service.resetPassword('new').subscribe();

		expect(http.post).toHaveBeenNthCalledWith(1, expect.stringContaining('/auth/login'), { email: 'a@b.com', password: 'secret' });
		expect(http.post).toHaveBeenNthCalledWith(2, expect.stringContaining('/conta/cadastro'), { email: 'a@b.com' });
		expect(http.patch).toHaveBeenCalledWith(expect.stringContaining('/conta/me/atualizar'), { name: 'Ada' });
		expect(http.delete).toHaveBeenCalledWith(expect.stringContaining('/conta/meu/deletar'));
		expect(http.post).toHaveBeenNthCalledWith(3, expect.stringContaining('/auth/recover-password'), { email: 'a@b.com' });
		expect(http.post).toHaveBeenNthCalledWith(4, expect.stringContaining('/auth/validate-token'), null, { params: { token: '12345' } });
		expect(http.post).toHaveBeenNthCalledWith(5, expect.stringContaining('/auth/reset-password'), { newPassword: 'new' });
	});
});
