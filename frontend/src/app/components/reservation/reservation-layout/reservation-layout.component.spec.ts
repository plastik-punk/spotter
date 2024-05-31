import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReservationLayoutComponent } from './reservation-layout.component';

describe('ComponentReservationLayoutComponent', () => {
  let component: ReservationLayoutComponent;
  let fixture: ComponentFixture<ReservationLayoutComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReservationLayoutComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ReservationLayoutComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
