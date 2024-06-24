import {Injectable} from "@angular/core";
import {HttpClient, HttpParams, HttpResponse} from "@angular/common/http";
import {Globals} from '../global/globals';
import {Observable,tap} from "rxjs";
import {formatIsoDate} from '../util/date-helper';
import {
  Reservation,
  ReservationCheckAvailabilityDto,
  ReservationCreateDto,
  ReservationSearch,
  ReservationListDto,
  ReservationEditDto,
  ReservationModalDetailDto,
  ReservationWalkInDto,
  PermanentReservationDto,
  permanentReservationSearch,
  PermanentReservationListDto,
  PermanentReservationDetailDto
} from "../dtos/reservation";
import {SimpleViewReservationStatusEnum} from "../dtos/status-enum";

@Injectable({
  providedIn: 'root'
})

export class ReservationService {

  private reservationBaseUri : string = this.globals.backendUri + "/reservations";
  private permanentReservationUri: string = this.globals.backendUri + "/reservations/permanent";

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
   * Get the details of a reservation for the modal by its id
   *
   * @param id the hashed id of the reservation
   * @return an Observable for the modal details
   */
  getModalDetail(id: string): Observable<ReservationModalDetailDto> {
    let params = new HttpParams().set('id', id);
    return this.httpClient.get<ReservationModalDetailDto>(this.reservationBaseUri + "/modal", { params: params });
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

  /**
   * Create a new  reccuring permanent reservation for a guest
   *
   * @param permanentReservationDto the permanent reservation to create
   * @return an Observable for the created permanent reservation
   */
  createPermanentReservation(permanentReservationDto: PermanentReservationDto) : Observable<PermanentReservationDto> {
    return this.httpClient.post<PermanentReservationDto>(this.reservationBaseUri+"/permanent", permanentReservationDto);
  }

  /**
   * Fetch permanent reservations with optional user filtering
   *
   * @param userId (optional) user ID for filtering reservations for non-admin users
   * @return an Observable with an array of permanent reservations
   */
  getPermanentReservations(searchParams: permanentReservationSearch): Observable<PermanentReservationListDto[]> {
    let params = new HttpParams();

    // Add date and time parameters
    if (searchParams.earliestDate) {
      params = params.append('earliestDate', formatIsoDate(searchParams.earliestDate));
    }
    if (searchParams.latestDate) {
      params = params.append('latestDate', formatIsoDate(searchParams.latestDate));
    }
    if (searchParams.earliestStartTime) {
      params = params.append('earliestStartTime', searchParams.earliestStartTime);
    }
    if (searchParams.latestEndTime) {
      params = params.append('latestEndTime', searchParams.latestEndTime);
    }

    // Check if a user ID is included and append it to the query parameters
    if (searchParams.userId) {
      params = params.append('userId', searchParams.userId.toString());
    }

    // Assuming `permanentReservationUri` points to the backend URI handling permanent reservations
    return this.httpClient.get<PermanentReservationListDto[]>(this.permanentReservationUri, { params });
  }
  /**
   * Confirm a permanent Reservation
   *
   * @param id the  id of the permanent reservation to confirm
   * @return an Observable for the HttpResponse
   */
  confirmPermanentReservation(id: string): Observable<void> {
    return this.httpClient.put<void>(`${this.permanentReservationUri}/confirmation/${id}`, null);
  }

  /**
   * Fetch details of a permanent reservation by its hashed ID
   *
   * @param hashedId the hashed ID of the permanent reservation
   * @return an Observable for the detailed data of the permanent reservation
   */
  getPermanentReservationDetailsByHashedId(hashedId: string): Observable<PermanentReservationDetailDto> {
    return this.httpClient.get<PermanentReservationDetailDto>(`${this.permanentReservationUri}/detail/${hashedId}`);
  }

  /**
   * Deletes a permanent reservation
   *
   * @param hash the hashed id of the permanent  reservation to delete
   * @return an Observable for the HttpResponse
   */
  deletePermanent(hash: string):Observable<HttpResponse<void>>  {
    return this.httpClient.delete<void>(`${this.permanentReservationUri}/delete/${hash}`,{ observe: 'response'});
  }

}
