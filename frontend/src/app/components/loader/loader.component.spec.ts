import { ComponentFixture, TestBed } from '@angular/core/testing';
import { LoaderComponent } from './loader.component';

describe('LoaderComponent', () => {
	let fixture: ComponentFixture<LoaderComponent>;

	beforeEach(async () => {
		await TestBed.configureTestingModule({ imports: [LoaderComponent] }).compileComponents();
		fixture = TestBed.createComponent(LoaderComponent);
	});

	it('renders the loader by default and hides it when requested', () => {
		fixture.detectChanges();
		expect(fixture.nativeElement.querySelector('.loader-wrap')).not.toBeNull();

		fixture.componentRef.setInput('visible', false);
		fixture.detectChanges();
		expect(fixture.nativeElement.querySelector('.loader-wrap')).toBeNull();
	});
});
