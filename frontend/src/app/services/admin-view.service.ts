import {Injectable} from '@angular/core';
import {AdminViewDto, PredictionDto} from "../dtos/admin-view";
import {Observable} from "rxjs";
import {HttpClient, HttpParams} from "@angular/common/http";
import {Globals} from "../global/globals";

@Injectable({
  providedIn: 'root'
})

export class AdminViewService {

  private reservationBaseUri: string = this.globals.backendUri + "/adminView"; // todo: change to reservation after auth is implemented

  constructor(private httpClient: HttpClient, private globals: Globals) {
  }

  /**
   * Get a prediction for the restaurant's capacity
   *
   * @param adminViewDto the timeslot for the prediction
   * @return an Observable for the prediction
   */
  getPrediction(adminViewDto: AdminViewDto): Observable<PredictionDto> {
    let params = new HttpParams().set('adminViewDto', adminViewDto.toString());

    return this.httpClient.get<PredictionDto>(this.reservationBaseUri, params);
  }
}
