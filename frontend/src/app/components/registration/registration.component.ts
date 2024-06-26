import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { RegistrationService } from '../../services/registration.service';
import { UserRegistrationDTO, UserRole } from '../../dtos/app-user';
import { NgIf } from "@angular/common";
import { CommonModule } from "@angular/common";
import { Router, RouterLink } from "@angular/router";
import { AuthService } from "../../services/auth.service";
import { NotificationService } from '../../services/notification.service';

@Component({
  selector: 'app-register',
  templateUrl: './registration.component.html',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    NgIf,
    CommonModule,
    RouterLink
  ],
  styleUrls: ['./registration.component.scss']
})
export class RegistrationComponent implements OnInit {
  registrationForm: FormGroup;
  title = 'Register';  // Default title
  isAdmin: boolean = false;

  constructor(
    private fb: FormBuilder,
    private registrationService: RegistrationService,
    private authService: AuthService,
    private router: Router,
    private notificationService: NotificationService
  ) {
    // Initialize the form with all fields
    this.registrationForm = this.fb.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      mobileNumber: undefined,
      password: ['', [Validators.required, Validators.minLength(8)]],
      role: [this.isAdmin ? 'EMPLOYEE' : 'CUSTOMER', Validators.required]
    });
  }

  ngOnInit() {
    this.isAdmin = this.authService.getUserRole() === 'ADMIN';
    this.adjustFormBasedOnUserRole();
  }

  adjustFormBasedOnUserRole(): void {
    if (this.isAdmin) {
      this.title = 'Register Employee';  // Change title for admin
    }
  }

  register(): void {
    if (this.registrationForm.valid) {
      let role;
      if (this.isAdmin) {
        role = this.registrationForm.get('role').value === 'ADMIN' ? UserRole.ADMIN : UserRole.EMPLOYEE;
      } else {
        role = this.registrationForm.get('role').value === 'OWNER' ? UserRole.UNCONFIRMED_ADMIN : (this.registrationForm.get('role').value === 'EMPLOYEE' ? UserRole.UNCONFIRMED_EMPLOYEE : UserRole.CUSTOMER);
      }

      const userData: UserRegistrationDTO = {
        firstName: this.registrationForm.get('firstName').value,
        lastName: this.registrationForm.get('lastName').value,
        email: this.registrationForm.get('email').value,
        mobileNumber: this.registrationForm.get('mobileNumber').value,
        password: this.registrationForm.get('password').value,
        role: role
      };

      this.registrationService.registerUser(userData).subscribe({
        next: () => {
          this.notificationService.showSuccess('Registration successful!');
          if (this.isAdmin){ this.router.navigate(['/employees']);
          } else {
            this.router.navigate(['/login']);
          }
        },
        error: (error) => {
          const errorMessages = error.error.errors || [error.message || 'Registration failed'];
          errorMessages.forEach(msg => this.notificationService.showError(msg));
        }
      });
    } else {
      this.showFormErrors();
    }
  }

  private showFormErrors(): void {
    const controls = this.registrationForm.controls;
    if (controls.firstName.invalid) {
      this.notificationService.showError('First name is required.');
    }
    if (controls.lastName.invalid) {
      this.notificationService.showError('Last name is required.');
    }
    if (controls.email.invalid) {
      if (controls.email.errors.required) {
        this.notificationService.showError('Email is required.');
      } else if (controls.email.errors.email) {
        this.notificationService.showError('Invalid email format.');
      }
    }
    if (controls.password.invalid) {
      if (controls.password.errors.required) {
        this.notificationService.showError('Password is required.');
      } else if (controls.password.errors.minlength) {
        this.notificationService.showError('Password must be at least 8 characters long.');
      }
    }
    if (controls.role.invalid) {
      this.notificationService.showError('Role is required.');
    }
  }
}
