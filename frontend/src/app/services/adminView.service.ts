import {Injectable} from '@angular/core';
import {AdminViewDto, PredictionDto, ReservationForeCastDto, UnusualReservationsDto} from "../dtos/admin-view";
import {Observable} from "rxjs";
import {HttpClient, HttpParams} from "@angular/common/http";
import {Globals} from "../global/globals";


@Injectable({
  providedIn: 'root'
})

export class AdminViewService {

  private reservationBaseUri: string = this.globals.backendUri + "/adminView";

  constructor(private httpClient: HttpClient, private globals: Globals) {
  }

  /**
   * Get a prediction for the restaurant's capacity
   *
   * @param adminViewDto the timeslot for the prediction
   * @return an Observable for the prediction
   */
  getPrediction(adminViewDto: AdminViewDto): Observable<any> {
    let params = new HttpParams().append('adminViewDto', adminViewDto.toString());
    Object.keys(adminViewDto).forEach((key) => {
      params = params.append(key, adminViewDto[key]);
    });
    return this.httpClient.get<PredictionDto>(this.reservationBaseUri + '/prediction', {params});
  }

  getForeCast(adminViewDto: AdminViewDto) {
    let params = new HttpParams().append('adminViewDto', adminViewDto.toString());
    Object.keys(adminViewDto).forEach((key) => {
      params = params.append(key, adminViewDto[key]);
    });
    return this.httpClient.get<ReservationForeCastDto>(this.reservationBaseUri + '/forecast', {params});
  }

  getUnusualReservations(adminViewDto: AdminViewDto) {
    let params = new HttpParams().append('adminViewDto', adminViewDto.toString());
    Object.keys(adminViewDto).forEach((key) => {
      params = params.append(key, adminViewDto[key]);
    });
    return this.httpClient.get<UnusualReservationsDto>(this.reservationBaseUri + '/unusualReservations', {params});
  }

}
