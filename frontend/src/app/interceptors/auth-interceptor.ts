import { Injectable } from '@angular/core';
import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { AuthService } from '../services/auth.service';
import { Observable } from 'rxjs';
import { Globals } from '../global/globals';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  constructor(private authService: AuthService, private globals: Globals) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const authUri = this.globals.backendUri + '/authentication';

    // Do not intercept authentication requests
    if (req.url === authUri) {
      return next.handle(req);
    }

    // Retrieve the token from the AuthService
    const token = this.authService.getToken();

    // Check if the token exists and is not empty
    if (token) {
      // Clone the request and set the Authorization header
      const authReq = req.clone({
        headers: req.headers.set('Authorization', `Bearer ${token}`)
      });
      return next.handle(authReq);
    }

    // If no token, forward the original request without modification
    return next.handle(req);
  }
}
