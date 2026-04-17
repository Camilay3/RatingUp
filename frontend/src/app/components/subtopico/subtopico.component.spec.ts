/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { subtopicoComponent } from './subtopico.component';

describe('subtopicoComponent', () => {
  let component: subtopicoComponent;
  let fixture: ComponentFixture<subtopicoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ subtopicoComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(subtopicoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
