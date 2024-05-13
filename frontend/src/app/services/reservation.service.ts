import {Injectable} from "@angular/core";
import {HttpClient, HttpParams} from "@angular/common/http";
import {Globals} from '../global/globals';
import {Reservation, ReservationCreateDto} from "../dtos/reservation";
import {Observable,tap} from "rxjs";
import {formatIsoDate} from '../util/date-helper';
import {ReservationSearch,ReservationListDto} from "../dtos/reservation";
@Injectable({
  providedIn: 'root'
})
export class ReservationService {

  private reservationBaseUri : string = this.globals.backendUri + "/authentication"; // todo: change to reservations after auth is implemented

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
  search(searchParams: ReservationSearch): Observable<ReservationListDto[]> {
    let params = new HttpParams();
    if (searchParams.earliestDate) {
      params = params.append('earliestDate', formatIsoDate(searchParams.earliestDate));
    }
    if (searchParams.latestDate) {
      params = params.append('latestDate', formatIsoDate(searchParams.latestDate));
    }
    if (searchParams.earliestStartTime) {
      params = params.append('earliestStartTime', formatIsoDate(searchParams.earliestStartTime));
    }
    if (searchParams.latestEndTime) {
      params = params.append('latestEndTime', formatIsoDate(searchParams.latestEndTime));
    }
    return this.httpClient.get<ReservationListDto[]>(this.reservationBaseUri, { params });
  }
}
