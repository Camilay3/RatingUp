/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { NivelComponent } from './nivel.component';

describe('NivelComponent', () => {
  let component: NivelComponent;
  let fixture: ComponentFixture<NivelComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NivelComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NivelComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
