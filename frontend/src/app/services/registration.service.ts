import {Injectable} from "@angular/core";
import {HttpClient, HttpResponse} from "@angular/common/http";
import {Globals} from '../global/globals';
import {Reservation, ReservationCreateDto} from "../dtos/reservation";
import {Observable} from "rxjs";
import {UserRegistrationDTO} from "../dtos/app-user";

@Injectable({
  providedIn: 'root'
})

export class RegistrationService {

  private registrationBaseURI : string = this.globals.backendUri + "/registration";

  constructor(private httpClient: HttpClient, private globals: Globals) {}

  /**
   * Create a new account
   *
   * @param userRegistrationDto the account information to create
   * @returns an Observable of HttpResponse, indicating the outcome of the registration attempt
   */
  registerUser(userRegistrationDto: UserRegistrationDTO): Observable<HttpResponse<any>> {
    return this.httpClient.post<any>(this.registrationBaseURI, userRegistrationDto, {
      observe: 'response'
    });
  }
}
