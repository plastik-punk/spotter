import { Component } from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import { RegistrationService } from '../../services/registration.service';
import { UserRegistrationDTO, UserRole } from '../../dtos/app-user';

@Component({
  selector: 'app-register',
  templateUrl: './registration.component.html',
  standalone: true,
  imports: [
    ReactiveFormsModule
  ],
  styleUrls: ['./registration.component.scss']
})
export class RegistrationComponent {
  registrationForm: FormGroup;

  constructor(private fb: FormBuilder, private registrationService: RegistrationService) {
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

      // Call the registration service to send the user data
      this.registrationService.registerUser(userData).subscribe({
        next: (response) => {
          console.log('Registration successful!');
          // Possibly redirect the user or clear the form
        },
        error: (error) => {
          console.error('Registration failed with status: ', error.status);
          // Handle errors, perhaps show user feedback
        }
      });
    }
  }
}
