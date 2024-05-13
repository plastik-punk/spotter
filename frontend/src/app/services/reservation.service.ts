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

  private reservationBaseUri : string = this.globals.backendUri + "/authentication"; // todo: change to reservation after auth is implemented

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
    if (searchParams.earliestStartDate) {
      params = params.append('earliestStartDate', formatIsoDate(searchParams.earliestStartDate));
    }
    if (searchParams.latestStartDate) {
      params = params.append('latestStartDate', formatIsoDate(searchParams.latestStartDate));
    }
    return this.httpClient.get<ReservationListDto[]>(this.baseUri, { params })
      .pipe(tap(reservations => reservations.map(r => {
        r.startDate = new Date(r.startDate);
        r.endDate = new Date(r.endDate);// Parse date string
      })));
  }
}
