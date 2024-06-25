import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PermanentReservationDetailsComponent } from './permanent-reservation-details.component';

describe('PermanentReservationDetailsComponent', () => {
  let component: PermanentReservationDetailsComponent;
  let fixture: ComponentFixture<PermanentReservationDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PermanentReservationDetailsComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(PermanentReservationDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
