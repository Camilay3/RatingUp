jest.mock('chessground', () => ({ Chessground: jest.fn() }));

import { HttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { appConfig } from './app.config';

describe('appConfig', () => {
	it('keeps public requests unchanged and adds credentials to private requests', () => {
		TestBed.configureTestingModule({
			providers: [...appConfig.providers, provideHttpClientTesting()],
		});

		const http = TestBed.inject(HttpClient);
		const httpTesting = TestBed.inject(HttpTestingController);

		http.get('/auth/login').subscribe();
		httpTesting.expectOne((request) => request.url === '/auth/login' && !request.withCredentials).flush({});

		http.get('/api/profile').subscribe();
		httpTesting.expectOne((request) => request.url === '/api/profile' && request.withCredentials).flush({});

		httpTesting.verify();
	});
});
