import {Injectable} from "@angular/core";
import {HttpClient, HttpParams, HttpResponse} from "@angular/common/http";
import {Globals} from '../global/globals';
import {Observable,tap} from "rxjs";
import {formatIsoDate} from '../util/date-helper';
import {
  ReservationSearch,
  ReservationListDto,
  ReservationEditDto,
  AreaLayoutDto,
  AreaListDto, ReservationWalkInDto
} from "../dtos/reservation";
import {
  Reservation,
  ReservationCheckAvailabilityDto,
  ReservationCreateDto,
  ReservationDetailDto,
  ReservationLayoutCheckAvailabilityDto,
} from "../dtos/reservation";
import {SimpleViewReservationStatusEnum} from "../dtos/status-enum";

@Injectable({
  providedIn: 'root'
})

export class ReservationService {

  private reservationBaseUri : string = this.globals.backendUri + "/reservations";

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
   * Get the layout of an area and check the availability
   * @param reservationLayoutCheckAvailabilityDto the time, date and area to check the availability for
   */
  getLayoutAvailability(reservationLayoutCheckAvailabilityDto: ReservationLayoutCheckAvailabilityDto) : Observable<AreaLayoutDto> {
    let params = new HttpParams();
    Object.keys(reservationLayoutCheckAvailabilityDto).forEach((key) => {
      params = params.append(key, reservationLayoutCheckAvailabilityDto[key]);
    });
    return this.httpClient.get<AreaLayoutDto>(this.reservationBaseUri + "/layout", { params });
  }

  /**
   * Get the next available tables for a reservation
   *
   * @param reservationCheckAvailabilityDto the reservation to get the next available tables for
   * @return an Observable for the next available tables
   */
  getNextAvailableTables(reservationCheckAvailabilityDto: ReservationCheckAvailabilityDto) : Observable<ReservationCheckAvailabilityDto[]> {
   let params = new HttpParams();
    Object.keys(reservationCheckAvailabilityDto).forEach((key) => {
      params = params.append(key, reservationCheckAvailabilityDto[key]);
    });
    return this.httpClient.get<ReservationCheckAvailabilityDto[]>(this.reservationBaseUri + "/next", { params });
  }

  /**
   * Get a reservation (detail) by its id
   *
   * @param id the hashed id of the reservation
   * @return an Observable for the reservation
   */
  getByHashedId(id: string): Observable<ReservationEditDto> {
    let params = new HttpParams().set('id', id);
    return this.httpClient.get<ReservationEditDto>(this.reservationBaseUri + "/detail", { params: params });
  }

  /**
   * Updates a reservation
   *
   * @param reservationEditDto the reservation to update
   * @return an Observable for the updated reservation
   */
  update(reservationEditDto: ReservationEditDto): Observable<ReservationEditDto> {
    return this.httpClient.put<ReservationEditDto>(this.reservationBaseUri, reservationEditDto);
  }

  /**
   * search for reservations fitting Serch parameters
   *
   * @param searchParams the parameters of the search
   * @return an Observable for found reservations
   */
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

  /**
   * Deletes a reservation
   *
   * @param hash the hashed id of the reservation to delete
   * @return an Observable for the HttpResponse
   */
  delete(hash: string): Observable<HttpResponse<void>> {
    return this.httpClient.delete<void>(this.reservationBaseUri, { observe: 'response' , body: hash});
  }

  /**
   * Get list of all areas
   *
   * @return an Observable for the list of areas
   */
  getAllAreas(): Observable<AreaListDto> {
    return this.httpClient.get<AreaListDto>(this.globals.backendUri + "/reservations/areas");
  }

  /**
   * Confirm that the guests for a reservation have arrived.
   *
   * @param hashId the hashed id of the reservation to confirm
   * @return an Observable for the HttpResponse
   */
  confirm(hashId: string): Observable<void> {
    return this.httpClient.put<void>(this.globals.backendUri + "/reservations/confirm", hashId);
  }

  /**
   * Unconfirm that the guests for a reservation have arrived.
   *
   * @param hashId the hashed id of the reservation to unconfirm
   * @return an Observable for the HttpResponse
   */
  unconfirm(hashId: string): Observable<void> {
    return this.httpClient.put<void>(this.globals.backendUri + "/reservations/unconfirm", hashId);
  }
  /**
   * Create a new reservation for a guest
   *
   * @param reservationCreateDto the reservation to create
   * @return an Observable for the created reservation
   */
  createWalkIn(reservationWalkInDto: ReservationWalkInDto) : Observable<ReservationCreateDto> {
    return this.httpClient.post<ReservationCreateDto>(this.reservationBaseUri+ "/walk-in", reservationWalkInDto);
  }
}
