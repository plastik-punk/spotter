import { Injectable } from '@angular/core';
import {ESLint} from "eslint";
import {Globals} from '../global/globals';
import {HttpClient, HttpParams, HttpResponse, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {EventCreateDto, EventDetailDto, EventEditDto, EventListDto, EventSearchDto} from "../dtos/event";
import {formatIsoDate} from "../util/date-helper";

@Injectable({
  providedIn: 'root'
})
export class EventService {

  private eventBaseUri: string = this.globals.backendUri + "/events";

  constructor(private httpClient: HttpClient, private globals: Globals) {}

  /**
   * Search for events
   *
   * @param searchParams the search parameters
   */
  search(searchParams: EventSearchDto): Observable<EventListDto[]> {
    // TODO: name of searchEventDto is never used? if so, remove it both in back- and frontend

    let params = new HttpParams();
    if (searchParams.earliestStartDate) {
      params = params.append('earliestDate', formatIsoDate(searchParams.earliestStartDate));
    }
    if (searchParams.latestEndDate) {
      params = params.append('latestDate', formatIsoDate(searchParams.latestEndDate));
    }
    if (searchParams.earliestStartTime) {
      params = params.append('earliestStartTime', formatIsoDate(searchParams.earliestStartTime))
    }
    if (searchParams.latestEndTime) {
      params = params.append('latestEndTime', formatIsoDate(searchParams.latestEndTime))
    }
    return this.httpClient.get<EventListDto[]>(this.eventBaseUri + "/search", { params });
  }

  /**
   * Get next upcoming events (5 at most)
   */
  getUpcomingEvents(): Observable<EventListDto[]> {
    return this.httpClient.get<EventListDto[]>(this.eventBaseUri + "/upcoming");
  }

  /**
   * Get an event by its hashId
   *
   * @param hashId the hashId of the event
   * @return an Observable for the event
   */
  getByHashId(hashId: string): Observable<EventDetailDto> {
    let params = new HttpParams().set('hashId', hashId);
    return this.httpClient.get<EventDetailDto>(this.eventBaseUri + '/detail', {params: params});
  }

  /**
   * Delete an event by its hashId
   *
   * @param hashId
   * @return an Observable for the HttpResponse
   */
  delete(hashId: string): Observable<HttpResponse<void>> {
    return this.httpClient.delete<void>(this.eventBaseUri, {observe: 'response', body: hashId});
  }

  /**
   * Create a new event
   *
   * @param eventCreateDto the event to create
   * @return an Observable for the created event
   */
  create(eventCreateDto: EventCreateDto): Observable<Event> {
    return this.httpClient.post<Event>(this.eventBaseUri, eventCreateDto);
  }

  /**
   * Update an event
   *
   * @param eventEditDto the event to update
   * @return an Observable for the updated event
   */
  update(eventEditDto: EventEditDto): Observable<EventEditDto> {
    return this.httpClient.put<EventEditDto>(this.eventBaseUri, eventEditDto);
  }

  uploadIcsFile(file: File): Observable<any> {
    const formData: FormData = new FormData();
    formData.append('file', file, file.name);

    return this.httpClient.post(`${this.eventBaseUri}/import-ics`, formData, {
      headers: new HttpHeaders({
        'Accept': 'application/json'
      })
    });
  }
}
