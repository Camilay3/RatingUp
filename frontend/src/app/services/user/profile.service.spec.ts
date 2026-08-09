import { of } from 'rxjs';
import { ProfileService } from './profile.service';

describe('ProfileService', () => {
	it('forwards profile and password requests', () => {
		const http = {
			patch: jest.fn().mockReturnValue(of({})),
			delete: jest.fn().mockReturnValue(of({})),
			post: jest.fn().mockReturnValue(of({})),
			put: jest.fn().mockReturnValue(of({})),
		};
		const service = new ProfileService(http as any);

		service.updateProfile({ name: 'Ada' }).subscribe();
		service.deleteAccount().subscribe();
		service.recoverPassword('a@b.com').subscribe();
		service.resetPassword({ newPassword: 'new' }).subscribe();
		service.editPassword('old', 'new').subscribe();

		expect(http.patch).toHaveBeenCalledWith(expect.stringContaining('/conta/me/atualizar'), { name: 'Ada' });
		expect(http.delete).toHaveBeenCalledWith(expect.stringContaining('/conta/me/deletar'));
		expect(http.post).toHaveBeenNthCalledWith(1, expect.stringContaining('/auth/recover-password'), { email: 'a@b.com' });
		expect(http.post).toHaveBeenNthCalledWith(2, expect.stringContaining('/auth/reset-password'), { newPassword: 'new' });
		expect(http.put).toHaveBeenCalledWith(expect.stringContaining('/auth/change-password'), { oldPassword: 'old', newPassword: 'new' });
	});
});
