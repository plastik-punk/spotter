import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Globals} from '../global/globals';
import {Reservation, ReservationCreateDto} from "../dtos/reservation";
import {Observable} from "rxjs";
import {UserOverviewDto} from "../dtos/app-user";

@Injectable({
  providedIn: 'root'
})

export class UserService {

  private employeesBaseUri: string = this.globals.backendUri + "/employees";

  constructor(private httpClient: HttpClient, private globals: Globals) {
  }

  /**
   * Fetch all user overviews.
   *
   * @return an Observable of an array of UserOverviewDto
   */
  getUsers(): Observable<UserOverviewDto[]> {
    return this.httpClient.get<UserOverviewDto[]>(this.employeesBaseUri);
  }

  /**
   * Update the user based upon its id
   *
   * @param user the info to update which user and with which data
   */
  updateUser(user: UserOverviewDto): Observable<UserOverviewDto> {
    let userId = user.id
    return this.httpClient.put<UserOverviewDto>(this.employeesBaseUri + `/${userId}`, user)
  }
}
