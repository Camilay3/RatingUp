import { of } from 'rxjs';
import { AvatarService } from './avatar.service';

describe('AvatarService', () => {
	it('lists and updates avatars', () => {
		const http = { get: jest.fn().mockReturnValue(of({ data: [] })), patch: jest.fn().mockReturnValue(of({})) };
		const service = new AvatarService(http as any);

		service.listAvatars().subscribe();
		service.selectAvatar('knight.webp').subscribe();

		expect(http.get).toHaveBeenCalledWith(expect.stringContaining('/avatar/avatar-list'));
		expect(http.patch).toHaveBeenCalledWith(expect.stringContaining('/avatar/update'), { imageName: 'knight.webp' });
	});
});
