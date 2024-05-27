import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ComponentReservationLayoutComponent } from './component-reservation-layout.component';

describe('ComponentReservationLayoutComponent', () => {
  let component: ComponentReservationLayoutComponent;
  let fixture: ComponentFixture<ComponentReservationLayoutComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ComponentReservationLayoutComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ComponentReservationLayoutComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
