import {Injectable} from "@angular/core";
import {HttpClient, HttpParams} from "@angular/common/http";
import {Globals} from "../global/globals";
import {Observable} from "rxjs";
import {
  AreaCreateDto, AreaDetailDto, AreaDetailListDto,
  AreaLayoutDto,
  AreaListDto,
  LayoutCreateDto,
  ReservationLayoutCheckAvailabilityDto
} from "../dtos/layout";

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
   * Get list of all areas with detailed information
   *
   * @return an Observable for the list of areas
   */
  getAllAreasDetailed(): Observable<AreaDetailListDto> {
    return this.httpClient.get<AreaDetailListDto>(this.layoutBaseUri + "/areas/detailed");
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

  /**
   * Create a new layout
   * @param layoutCreateDto the layout to be created
   * @return an Observable for the created layout
   */
  createLayout(layoutCreateDto: LayoutCreateDto): Observable<any> {
    return this.httpClient.post<any>(`${this.layoutBaseUri}/create`, layoutCreateDto);
  }

  /**
   * Delete an area
   * @param areaId the id of the area to be deleted
   * @return an Observable for the deleted area
   */
  deleteArea(areaId: number): Observable<any> {
    return this.httpClient.delete<any>(`${this.layoutBaseUri}/delete/${areaId}`);
  }

  /**
   * Get an area by id
   * @param id the id of the area to be retrieved
   * @return an Observable for the area
   */
  getAreaById(id: number): Observable<AreaDetailDto> {
    return this.httpClient.get<AreaDetailDto>(`${this.layoutBaseUri}/area/${id}`);
  }

  /**
   * Update an areas status
   * @param areaId the id of the area to be updated
   * @param isOpen the new status of the area
   */
  toggleOpen(areaId: number, isOpen: boolean): Observable<any> {
    return this.httpClient.put<any>(`${this.layoutBaseUri}/toggleOpen/${areaId}`, isOpen, {headers: {'Content-Type': 'application/json'}});
  }

  /**
   * Make an area the main area
   * @param areaId the id of the area to be updated
   * @param isMain the new status of the area
   */
  toggleMain(areaId: number, isMain: boolean): Observable<any> {
    return this.httpClient.put<any>(`${this.layoutBaseUri}/toggleMain/${areaId}`, isMain, {headers: {'Content-Type': 'application/json'}});
  }

  /**
   * Update an area
   * @param areaEditDto the area to be updated
   */
  updateArea(areaEditDto: AreaDetailDto): Observable<any> {
    return this.httpClient.put<any>(`${this.layoutBaseUri}/area/update`, areaEditDto);
  }
}

