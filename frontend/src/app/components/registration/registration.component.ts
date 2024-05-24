import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { RegistrationService } from '../../services/registration.service';
import { UserRegistrationDTO, UserRole } from '../../dtos/app-user';
import { NgIf } from "@angular/common";
import { CommonModule } from "@angular/common";
import {Router, RouterLink} from "@angular/router";
import { AuthService } from "../../services/auth.service";

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
  error = false;
  errorMessage = '';
  errorMessages: string[] = [];
  title = 'Register';  // Default title
  isAdmin: boolean = false;

  constructor(private fb: FormBuilder, private registrationService: RegistrationService, private authService: AuthService, private router: Router) {
    // Initialize the form with all fields
    this.registrationForm = this.fb.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      mobileNumber: [''],
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
          console.log('Registration successful!');
          this.router.navigate(['/login']);
        },
        error: (error) => {
          console.error('Registration failed with status:', error.status);
          this.errorMessages = error.error.errors;
          this.error = true;
        }
      });
    }
  }

  vanishError(): void {
    this.error = false;
    this.errorMessages = [];
  }
}
