import {Injectable} from "@angular/core";
import {HttpClient, HttpParams, HttpResponse} from "@angular/common/http";
import {Globals} from '../global/globals';
import {Observable} from "rxjs";
import {RestaurantDto, RestaurantOpeningHoursDto} from "../dtos/restaurant";

@Injectable({
  providedIn: 'root'
})

export class RestaurantService {

  private restaurantBaseUri : string = this.globals.backendUri + "/restaurants";

  constructor(private httpClient: HttpClient, private globals: Globals) {}

  /**
   * Get the opening hours for the location.
   *
   * @return an Observable for the opening hours
   */
  getOpeningHours(): Observable<RestaurantOpeningHoursDto> {
    return this.httpClient.get<RestaurantOpeningHoursDto>(this.restaurantBaseUri + "/opening-hours");
  }

  /**
   * Get the restaurant information
   *
   * @return an Observable for the restaurant information
   */
  getRestaurantInfo(): Observable<RestaurantDto> {
    return this.httpClient.get<RestaurantDto>(this.restaurantBaseUri + "/restaurant");
  }
}
