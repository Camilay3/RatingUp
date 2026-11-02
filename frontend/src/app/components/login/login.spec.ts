import { FormBuilder } from '@angular/forms';
import { convertToParamMap, ParamMap } from '@angular/router';
import { BehaviorSubject, of, throwError } from 'rxjs';
import { Login } from './login';

describe('Login', () => {
	let component: Login;
	let loginService: { login: jest.Mock; register: jest.Mock };
	let router: { navigate: jest.Mock };
	let route: { queryParamMap: BehaviorSubject<ParamMap> };
	let snackBar: { open: jest.Mock };

	beforeEach(() => {
		jest.useFakeTimers();
		loginService = { login: jest.fn(), register: jest.fn() };
		router = { navigate: jest.fn() };
		// BehaviorSubject emite o valor atual ao se inscrever, igual ao ActivatedRoute real
		route = { queryParamMap: new BehaviorSubject<ParamMap>(convertToParamMap({})) };
		snackBar = { open: jest.fn() };
		component = new Login(new FormBuilder(), loginService as any, router as any, route as any, snackBar as any);
		component.ngOnInit();
	});

	afterEach(() => jest.useRealTimers());

	describe('modo inicial vindo da landing page', () => {
		const abrirCom = (params: Record<string, string>) => {
			route.queryParamMap.next(convertToParamMap(params));
		};

		it('abre em login quando não há query param', () => {
			expect(component.isFirstAcess).toBe(false);
		});

		it('abre em login quando modo=login', () => {
			abrirCom({ modo: 'login' });
			expect(component.isFirstAcess).toBe(false);
		});

		it('abre em cadastro quando modo=cadastro', () => {
			abrirCom({ modo: 'cadastro' });
			expect(component.isFirstAcess).toBe(true);
		});

		it('abre em login quando o modo é desconhecido', () => {
			abrirCom({ modo: 'qualquer-coisa' });
			expect(component.isFirstAcess).toBe(false);
		});

		it('abre direto em cadastro se a rota já nasce com modo=cadastro', () => {
			const rota = { queryParamMap: new BehaviorSubject<ParamMap>(convertToParamMap({ modo: 'cadastro' })) };
			const novo = new Login(new FormBuilder(), loginService as any, router as any, rota as any, snackBar as any);
			novo.ngOnInit();
			expect(novo.isFirstAcess).toBe(true);
		});

		it('acompanha mudanças do query param com o componente já aberto', () => {
			abrirCom({ modo: 'cadastro' });
			expect(component.isFirstAcess).toBe(true);
			abrirCom({ modo: 'login' });
			expect(component.isFirstAcess).toBe(false);
		});
	});

	it('initializes the forms and validates required fields', () => {
		expect(component.LoginForm.invalid).toBe(true);
		expect(component.RegisterForm.invalid).toBe(true);
		component.LoginForm.setValue({ email: 'user@example.com', password: 'safe password' });
		expect(component.LoginForm.valid).toBe(true);
		component.RegisterForm.patchValue({
			email: 'user@example.com', password: 'safe', name: 'Ada Lovelace', nickname: 'adalovelace', telefone: '11999999999',
		});
		expect(component.RegisterForm.valid).toBe(true);
	});

	it('switches forms, toggles password visibility and navigates to recovery', () => {
		component.choseForm(true);
		expect(component.isFirstAcess).toBe(true);
		component.choseForm(false);
		expect(component.isFirstAcess).toBe(false);
		const initial = component.hidePassword;
		component.togglePassword();
		expect(component.hidePassword).toBe(!initial);
		component.forgotPassword();
		expect(router.navigate).toHaveBeenCalledWith(['/esqueci-senha']);
	});

	it('mantém a troca manual de formulário mesmo com modo=login na URL', () => {
		route.queryParamMap.next(convertToParamMap({ modo: 'login' }));
		component.choseForm(true);
		expect(component.isFirstAcess).toBe(true);
	});

	it('masks phone input and keeps only digits in the form', () => {
		const input = document.createElement('input');
		const mask = (value: string) => {
			input.value = value;
			component.maskPhone({ target: input } as any);
			jest.runAllTimers();
			return input.value;
		};

		expect(mask('')).toBe('');
		expect(mask('12')).toBe('(12');
		expect(mask('123456')).toBe('(12) 3456');
		expect(mask('1234567890')).toBe('(12) 3456-7890');
		expect(mask('1234567890123')).toBe('(12) 34567-8901');
		expect(component.RegisterForm.get('telefone')?.value).toBe('12345678901');
	});

	it('logs in successfully and handles field and generic errors', () => {
		component.LoginForm.setValue({ email: 'user@example.com', password: 'safe' });
		loginService.login.mockReturnValueOnce(of({}));
		component.onLogin();
		expect(router.navigate).toHaveBeenCalledWith(['/']);

		loginService.login.mockReturnValueOnce(throwError(() => ({ error: { errors: { email: 'invalid email' } } })));
		component.onLogin();
		expect(component.LoginForm.get('email')?.hasError('backendError')).toBe(true);

		loginService.login.mockReturnValueOnce(throwError(() => ({ error: { message: 'wrong password' } })));
		component.onLogin();
		expect(component.LoginForm.get('password')?.hasError('backendError')).toBe(true);

		loginService.login.mockReturnValueOnce(throwError(() => ({ error: {} })));
		component.onLogin();
		expect(snackBar.open).toHaveBeenCalledWith('Ops, ocorreu um erro inesperado', 'Fechar', expect.any(Object));
	});

	it('registers, logs in after registration and maps backend validation errors', () => {
		component.RegisterForm.setValue({ email: 'user@example.com', password: 'safe', name: 'Ada Lovelace', nickname: 'adalovelace', telefone: '11999999999' });
		loginService.register.mockReturnValueOnce(of({}));
		loginService.login.mockReturnValue(of({}));
		component.onRegister();
		expect(loginService.register).toHaveBeenCalled();
		expect(loginService.login).toHaveBeenCalledWith('user@example.com', 'safe');
		expect(router.navigate).toHaveBeenCalledWith(['/'], { state: { mostrarDicaCapa: true } });

		loginService.register.mockReturnValueOnce(throwError(() => ({ error: {
			code: 'CONFLICT', errors: ['email already exists', 'nickname already exists', 'telefone already exists'],
		} })));
		component.onRegister();
		expect(component.RegisterForm.get('email')?.hasError('backendError')).toBe(true);
		expect(component.RegisterForm.get('nickname')?.hasError('backendError')).toBe(true);

		loginService.register.mockReturnValueOnce(throwError(() => ({ error: {
			errors: { name: 'bad name', nickname: 'bad nickname', telefone: 'bad phone', email: 'bad email', password: 'bad password' },
		} })));
		component.onRegister();
		expect(component.RegisterForm.get('password')?.hasError('backendError')).toBe(true);

		loginService.register.mockReturnValueOnce(throwError(() => ({ error: { errors: { email: 'only email' } } })));
		component.onRegister();
		expect(component.RegisterForm.get('email')?.hasError('backendError')).toBe(true);

		loginService.register.mockReturnValueOnce(throwError(() => ({ error: { code: 'CONFLICT' } })));
		component.onRegister();
		expect(loginService.register).toHaveBeenCalledTimes(5);

		loginService.register.mockReturnValueOnce(throwError(() => ({ error: {} })));
		component.onRegister();
		expect(snackBar.open).toHaveBeenCalledWith('Ops, ocorreu um erro inesperado', 'Fechar', expect.any(Object));
	});

	it('returns to login when the post-registration login fails', () => {
		component.RegisterForm.setValue({ email: 'user@example.com', password: 'safe', name: 'Ada Lovelace', nickname: 'adalovelace', telefone: '11999999999' });
		loginService.register.mockReturnValueOnce(of({}));
		loginService.login.mockReturnValueOnce(throwError(() => new Error('offline')));
		component.onRegister();
		expect(snackBar.open).toHaveBeenCalledWith(expect.stringContaining('Um erro inesperado aconteceu'), 'Fechar', expect.any(Object));
		expect(component.isFirstAcess).toBe(false);
	});
});