import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Message} from '../dtos/message';
import {Observable,tap} from 'rxjs';
import {Globals} from '../global/globals';
import {formatIsoDate} from '../util/date-helper';
import {ReservationSearch,ReservationListDto} from "../dtos/reservation";
@Injectable({
  providedIn: 'root'
})
export class ReservationService {
  private baseUri: string = this.globals.backendUri+ '/reservations';

  constructor(private httpClient: HttpClient, private globals: Globals) {
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
