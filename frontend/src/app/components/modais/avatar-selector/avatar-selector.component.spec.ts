import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of, throwError } from 'rxjs';
import { MatDialogRef } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { AvatarService } from '../../../services/user/avatar.service';
import { AvatarSelectorComponent } from './avatar-selector.component';

describe('AvatarSelectorComponent', () => {
	let fixture: ComponentFixture<AvatarSelectorComponent>;
	let component: AvatarSelectorComponent;
	let avatarService: { listAvatars: jest.Mock; selectAvatar: jest.Mock };
	let snackBar: { open: jest.Mock };
	let dialog: { close: jest.Mock };

	beforeEach(async () => {
		avatarService = { listAvatars: jest.fn(), selectAvatar: jest.fn() };
		snackBar = { open: jest.fn() };
		dialog = { close: jest.fn() };
		await TestBed.configureTestingModule({
			imports: [AvatarSelectorComponent],
			providers: [
				{ provide: AvatarService, useValue: avatarService },
				{ provide: MatSnackBar, useValue: snackBar },
				{ provide: MatDialogRef, useValue: dialog },
			],
		}).compileComponents();
		fixture = TestBed.createComponent(AvatarSelectorComponent);
		component = fixture.componentInstance;
		(component as any).snackBar = snackBar;
		(component as any).dialogRef = dialog;
	});

	it('loads avatars and builds their URLs', () => {
		const avatar = { imageName: 'knight.webp', avatarurl: '/avatars/knight.webp' } as any;
		avatarService.listAvatars.mockReturnValueOnce(of({ data: [avatar] }));
		fixture.detectChanges();
		expect(component.avatars).toEqual([avatar]);
		expect(component.isLoading).toBe(false);
		expect(component.imageUrl(avatar)).toContain('/avatars/knight.webp');

		avatarService.listAvatars.mockReturnValueOnce(of({ data: undefined }));
		component.carregarAvatares();
		expect(component.avatars).toEqual([]);
	});

	it('reports avatar loading errors with specific and fallback messages', () => {
		avatarService.listAvatars.mockReturnValueOnce(throwError(() => ({ error: { message: 'failed' } })));
		fixture.detectChanges();
		expect(component.loadError).toBe(true);
		expect(snackBar.open).toHaveBeenCalledWith('failed', 'Fechar', expect.any(Object));

		avatarService.listAvatars.mockReturnValueOnce(throwError(() => ({ error: {} })));
		component.carregarAvatares();
		expect(snackBar.open).toHaveBeenCalledWith('Erro ao carregar avatares', 'Fechar', expect.any(Object));
	});

	it('selects an avatar once and closes on success', () => {
		const avatar = { imageName: 'knight.webp', avatarurl: '/knight.webp' } as any;
		avatarService.selectAvatar.mockReturnValueOnce(of({}));
		component.selecionarAvatar(avatar);
		expect(component.isSelecting).toBe(true);
		expect(dialog.close).toHaveBeenCalledWith(true);

		component.selecionarAvatar({ imageName: 'other.webp' } as any);
		expect(avatarService.selectAvatar).toHaveBeenCalledTimes(1);
	});

	it('resets selection and reports update errors', () => {
		avatarService.selectAvatar.mockReturnValueOnce(throwError(() => ({ error: { message: 'update failed' } })));
		component.selecionarAvatar({ imageName: 'knight.webp' } as any);
		expect(component.isSelecting).toBe(false);
		expect(component.selectedImageName).toBeNull();
		expect(snackBar.open).toHaveBeenCalledWith('update failed', 'Fechar', expect.any(Object));

		component.isSelecting = false;
		avatarService.selectAvatar.mockReturnValueOnce(throwError(() => ({ error: {} })));
		component.selecionarAvatar({ imageName: 'other.webp' } as any);
		expect(snackBar.open).toHaveBeenCalledWith('Erro ao atualizar avatar', 'Fechar', expect.any(Object));
	});
});
