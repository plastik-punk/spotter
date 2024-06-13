import {Injectable} from "@angular/core";
import {HttpClient, HttpParams} from "@angular/common/http";
import {Globals} from "../global/globals";
import {Observable} from "rxjs";
import {AreaLayoutDto, AreaListDto, ReservationLayoutCheckAvailabilityDto} from "../dtos/layout";

@Injectable({
  providedIn: 'root'
})

export class LayoutService {

  private layoutBaseUri: string = this.globals.backendUri + "/layout";

  constructor(private httpClient: HttpClient, private globals: Globals) {
  }


  /**
   * Get list of all areas
   *
   * @return an Observable for the list of areas
   */
  getAllAreas(): Observable<AreaListDto> {
    return this.httpClient.get<AreaListDto>(this.layoutBaseUri + "/areas");
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
    return this.httpClient.get<AreaLayoutDto>(this.layoutBaseUri, { params });
  }

}
