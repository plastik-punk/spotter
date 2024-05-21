import {Injectable} from "@angular/core";
import {HttpClient, HttpParams} from "@angular/common/http";
import {Globals} from '../global/globals';
import {Observable,tap} from "rxjs";
import {formatIsoDate} from '../util/date-helper';
import {ReservationSearch,ReservationListDto} from "../dtos/reservation";
import {
  Reservation,
  ReservationCheckAvailabilityDto,
  ReservationCreateDto,
  ReservationDetailDto
} from "../dtos/reservation";
import {SimpleViewReservationStatusEnum} from "../dtos/status-enum";

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
   * @return an Observable for the availability of the reservation
   */
  getAvailability(reservationCheckAvailabilityDto: ReservationCheckAvailabilityDto) : Observable<SimpleViewReservationStatusEnum> {
    let params = new HttpParams();
    Object.keys(reservationCheckAvailabilityDto).forEach((key) => {
      params = params.append(key, reservationCheckAvailabilityDto[key]);
    });
    return this.httpClient.get<SimpleViewReservationStatusEnum>(this.reservationBaseUri, { params });
  }

  /**
   * Get a reservation (detail) by its id
   *
   * @param id the id of the reservation
   * @return an Observable for the reservation
   */
  getById(id: number): Observable<ReservationDetailDto> {
    let params = new HttpParams().set('id', id.toString());
    return this.httpClient.get<ReservationDetailDto>(this.reservationBaseUri + "/detail", { params: params });
  }

  /**
   * Updates a reservation
   *
   * @param reservationDetailDto the reservation to update
   * @return an Observable for the updated reservation
   */
  update(reservationDetailDto: ReservationDetailDto): Observable<ReservationDetailDto> {
    return this.httpClient.put<ReservationDetailDto>(this.reservationBaseUri, reservationDetailDto);
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
      params = params.append('earliestStartTime', searchParams.earliestStartTime)
    }
    if (searchParams.latestEndTime) {
      params = params.append('latestEndTime', searchParams.latestEndTime)
    }
    return this.httpClient.get<ReservationListDto[]>(this.reservationBaseUri + "/search", { params });
  }
}
