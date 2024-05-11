import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Globals} from '../global/globals';
import {GuestReservation, Reservation} from "../dtos/reservation";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})

export class ReservationService {

  private reservationBaseUri : string = this.globals.backendUri + "/reservations";

  constructor(private httpClient: HttpClient, private globals: Globals) {}

  /**
   * Create a new reservation for a guest
   *
   * @param guestReservation the reservation to create
   * @return an Observable for the created reservation
   */
  createGuestReservation(guestReservation: GuestReservation) : Observable<Reservation> {
    console.log('Create reservation for guest with email ' + guestReservation.email);
    return this.httpClient.post<Reservation>(this.reservationBaseUri, guestReservation);
  }
}


