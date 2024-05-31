import { Component, OnInit } from '@angular/core';
import { UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { AuthRequest } from '../../dtos/auth-request';
import { NotificationService } from '../../services/notification.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  loginForm: UntypedFormGroup;
  commercialLoginForm: UntypedFormGroup;

  constructor(
    private formBuilder: UntypedFormBuilder,
    private authService: AuthService,
    private router: Router,
    private notificationService: NotificationService
  ) {
    this.loginForm = this.formBuilder.group({
      username: ['', Validators.required],
      password: ['', [Validators.required, Validators.minLength(8)]]
    });
    this.commercialLoginForm = this.formBuilder.group({
      username: ['', Validators.required],
      password: ['', [Validators.required, Validators.minLength(8)]]
    });
  }

  ngOnInit(): void {
    if (this.authService.isLoggedIn()) {
      this.authService.logoutUser();
    }
  }

  loginUser(): void {
    if (this.loginForm.valid) {
      const authRequest = new AuthRequest(this.loginForm.value.username, this.loginForm.value.password);
      this.authService.loginUser(authRequest).subscribe({
        next: () => {
          this.notificationService.showSuccess('Login successful!');
          this.router.navigate(['/reservation-simple']);
        },
        error: error => {
          const errorMessage = typeof error.error === 'object' ? error.error.error : error.error;
          this.notificationService.showError(errorMessage || 'Authentication error!');
        }
      });
    } else {
      this.showFormErrors();
    }
  }

  private showFormErrors(): void {
    const controls = this.loginForm.controls;
    if (controls.username.invalid) {
      this.notificationService.showError('Username is required.');
    }
    if (controls.password.invalid) {
      if (controls.password.errors.required) {
        this.notificationService.showError('Password is required.');
      } else if (controls.password.errors.minlength) {
        this.notificationService.showError('Password must be at least 8 characters long.');
      }
    }
  }
}
