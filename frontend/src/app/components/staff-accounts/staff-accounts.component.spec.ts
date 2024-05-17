import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StaffAccountsComponent } from './staff-accounts.component';

describe('UserManagmentComponent', () => {
  let component: StaffAccountsComponent;
  let fixture: ComponentFixture<StaffAccountsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [StaffAccountsComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(StaffAccountsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
