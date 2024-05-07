import { Component, OnInit } from '@angular/core';
import { UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { AuthRequest } from '../../dtos/auth-request';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  loginForm: UntypedFormGroup;
  commercialLoginForm: UntypedFormGroup;
  error = false;
  errorMessage = '';

  constructor(private formBuilder: UntypedFormBuilder, private authService: AuthService, private router: Router) {
    this.loginForm = this.formBuilder.group({
      username: ['', Validators.required],
      password: ['', [Validators.required, Validators.minLength(8)]]
    });
    this.commercialLoginForm = this.formBuilder.group({
      username: ['', Validators.required],
      password: ['', [Validators.required, Validators.minLength(8)]]
    });
  }

  ngOnInit(): void {}

  loginUser(): void {
    if (this.loginForm.valid) {
      const authRequest = new AuthRequest(this.loginForm.value.username, this.loginForm.value.password);
      this.authService.loginUser(authRequest).subscribe({
        next: () => {
          this.router.navigate(['/home']);
        },
        error: error => {
          this.errorMessage = typeof error.error === 'object' ? error.error.error : error.error;
          this.error = true;
        }
      });
    }
  }



  vanishError(): void {
    this.error = false;
  }
}
