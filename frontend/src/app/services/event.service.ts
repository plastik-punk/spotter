import { Injectable } from '@angular/core';
import {ESLint} from "eslint";
import {Globals} from '../global/globals';
import {HttpClient, HttpParams, HttpResponse} from "@angular/common/http";
import {Observable} from "rxjs";
import {EventDetailDto, EventListDto, EventSearchDto} from "../dtos/event";
import {formatIsoDate} from "../util/date-helper";

@Injectable({
  providedIn: 'root'
})
export class EventService {

  private eventBaseUri: string = this.globals.backendUri + "/events";

  constructor(private httpClient: HttpClient, private globals: Globals) {}

  search(searchParams: EventSearchDto): Observable<EventListDto[]> {
    let params = new HttpParams();
    if (searchParams.earliestDate) {
      params = params.append('earliestDate', formatIsoDate(searchParams.earliestDate));
    }
    if (searchParams.latestDate) {
      params = params.append('latestDate', formatIsoDate(searchParams.latestDate));
    }
    if (searchParams.earliestStartTime) {
      params = params.append('earliestStartTime', formatIsoDate(searchParams.earliestStartTime))
    }
    if (searchParams.latestEndTime) {
      params = params.append('latestEndTime', formatIsoDate(searchParams.latestEndTime))
    }
    return this.httpClient.get<EventListDto[]>(this.eventBaseUri + "/search", { params });
  }

  getByHashId(hashId: string): Observable<EventDetailDto> {
    let params = new HttpParams().set('hashId', hashId);
    return this.httpClient.get<EventDetailDto>(this.eventBaseUri + '/detail', {params: params});
  }

  delete(hashId: string): Observable<HttpResponse<void>> {
    return this.httpClient.delete<void>(this.eventBaseUri, {observe: 'response', body: hashId});
  }
}
