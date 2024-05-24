import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReservationsOverviewComponent } from './reservation-overview.component';

describe('ReservationOverviewComponent', () => {
  let component: ReservationsOverviewComponent;
  let fixture: ComponentFixture<ReservationsOverviewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReservationsOverviewComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ReservationsOverviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
