import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EventImportComponent } from './event-import.component';

describe('EventImportComponent', () => {
  let component: EventImportComponent;
  let fixture: ComponentFixture<EventImportComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EventImportComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(EventImportComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
