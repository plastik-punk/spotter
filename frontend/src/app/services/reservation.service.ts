import {Injectable} from "@angular/core";
import {HttpClient, HttpParams} from "@angular/common/http";
import {Globals} from '../global/globals';
import {Reservation, ReservationCheckAvailabilityDto, ReservationCreateDto} from "../dtos/reservation";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})

export class ReservationService {

  private reservationBaseUri : string = this.globals.backendUri + "/reservations"; // todo: change to reservation after auth is implemented

  constructor(private httpClient: HttpClient, private globals: Globals) {}

  /**
   * Create a new reservation for a guest
   *
   * @param reservationCreateDto the reservation to create
   * @return an Observable for the created reservation
   */
  createReservation(reservationCreateDto: ReservationCreateDto) : Observable<Reservation> {
    return this.httpClient.post<Reservation>(this.reservationBaseUri, reservationCreateDto);
  }

  /**
   * Check the availability for a reservation
   *
   * @param reservationCheckAvailabilityDto the reservation to check the availability for
   * @return an Observable for the availability of the reservation which is a boolean
   */
  getAvailability(reservationCheckAvailabilityDto: ReservationCheckAvailabilityDto) : Observable<boolean> {
    let params = new HttpParams();
    Object.keys(reservationCheckAvailabilityDto).forEach((key) => {
      params = params.append(key, reservationCheckAvailabilityDto[key]);
    });
    return this.httpClient.get<boolean>(this.reservationBaseUri, { params });
  }
}
