import { Injectable } from '@angular/core';
import {Observable} from "rxjs";
import {HttpClient} from "@angular/common/http";
import {Globals} from "../global/globals";

@Injectable({
  providedIn: 'root'
})
export class PlaceService {
  private placeBaseUri : string = this.globals.backendUri + "/places";

  constructor(private httpClient: HttpClient, private globals: Globals) { }

  /**
   * Block tables
   *
   * @param placeIds the ids of the tables to block
   * @return an Observable for the HttpResponse
   */
  blockTables(placeIds: number[]) : Observable<void> {
    return this.httpClient.put<void>(this.placeBaseUri + "/block", placeIds);
  }
}
