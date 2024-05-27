import { Injectable } from '@angular/core';
import { AuthRequest } from '../dtos/auth-request';
import { Observable } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { tap } from 'rxjs/operators';
import { jwtDecode } from 'jwt-decode';
import { Globals } from '../global/globals';
import { UserOverviewDto } from '../dtos/app-user';
import { NotificationService } from './notification.service';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private authBaseUri: string = this.globals.backendUri + '/authentication';

  constructor(
    private httpClient: HttpClient,
    private globals: Globals,
    private notificationService: NotificationService // Import the NotificationService
  ) { }

  /**
   * Login in the user. If it was successful, a valid JWT token will be stored
   *
   * @param authRequest User data
   */
  loginUser(authRequest: AuthRequest): Observable<string> {
    return this.httpClient.post(this.authBaseUri, authRequest, { responseType: 'text' })
      .pipe(
        tap((authResponse: string) => {
          this.setToken(authResponse);
          this.fetchUserDetails();
        })
      );
  }

  /**
   * Get the details of the current logged in user and put them in the Localstorage(Password excluded)
   */
  fetchUserDetails(): void {
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${this.getToken()}`
    });
    this.httpClient.get<UserOverviewDto>(`${this.authBaseUri}`, { headers }).subscribe({
      next: (user: UserOverviewDto) => this.setUser(user),
      error: (error) => console.error('Error fetching user details', error)
    });
  }

  /**
   * Check if a valid JWT token is saved in the localStorage
   */
  isLoggedIn() {
    return !!this.getToken() && (this.getTokenExpirationDate(this.getToken()).valueOf() > new Date().valueOf());
  }

  logoutUser() {
    console.log('Logout');
    localStorage.removeItem('authToken');
    localStorage.removeItem('user');
  }

  getToken() {
    return localStorage.getItem('authToken');
  }

  getCurrentUser(): UserOverviewDto | null {
    const userJson = localStorage.getItem('user');
    if (userJson) {
      return JSON.parse(userJson);
    }
    return null;
  }

  /**
   * Returns the user role based on the current token
   */
  getUserRole() {
    if (this.getToken() != null) {
      const decoded: any = jwtDecode(this.getToken());
      const authInfo: string[] = decoded.rol;
      if (authInfo.includes('ROLE_ADMIN')) {
        return 'ADMIN';
      } else if (authInfo.includes('ROLE_USER')) {
        return 'USER';
      }
    }
    return 'UNDEFINED';
  }

  private setToken(authResponse: string) {
    localStorage.setItem('authToken', authResponse);
  }

  private setUser(user: UserOverviewDto): void {
    localStorage.setItem('user', JSON.stringify(user));
  }

  private getTokenExpirationDate(token: string): Date {
    const decoded: any = jwtDecode(token);
    if (decoded.exp === undefined) {
      return null;
    }

    const date = new Date(0);
    date.setUTCSeconds(decoded.exp);
    return date;
  }
}
