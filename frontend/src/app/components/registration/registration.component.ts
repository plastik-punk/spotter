import {Component} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {RegistrationService} from '../../services/registration.service';
import {UserRegistrationDTO, UserRole} from '../../dtos/app-user';
import {NgIf} from "@angular/common";
import {CommonModule} from "@angular/common";
import {Router} from "@angular/router";

@Component({
  selector: 'app-register',
  templateUrl: './registration.component.html',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    NgIf,
    CommonModule
  ],
  styleUrls: ['./registration.component.scss']
})
export class RegistrationComponent {
  registrationForm: FormGroup;
  error = false;
  errorMessage = '';
  errorMessages: string[] = [];

  constructor(private fb: FormBuilder, private registrationService: RegistrationService, private router: Router) {
    this.registrationForm = this.fb.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      mobileNumber: [''],
      password: ['', [Validators.required, Validators.minLength(8)]],
      ownerCheck: [false]  // Checkbox for determining if the user wants to register as an owner
    });
  }

  register(): void {
    if (this.registrationForm.valid) {
      // Build the DTO from the form values
      const userData: UserRegistrationDTO = {
        firstName: this.registrationForm.get('firstName').value,
        lastName: this.registrationForm.get('lastName').value,
        email: this.registrationForm.get('email').value,
        mobileNumber: this.registrationForm.get('mobileNumber').value,
        password: this.registrationForm.get('password').value,
        role: this.registrationForm.get('ownerCheck').value ? UserRole.UNCONFIRMED_ADMIN : UserRole.CUSTOMER
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
