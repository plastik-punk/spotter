import {Injectable} from '@angular/core';
import {Globals} from "../global/globals";
import {HttpClient, HttpParams, HttpResponse} from "@angular/common/http";
import {Observable} from "rxjs";
import {SpecialOfferCreateDto, SpecialOfferDetailDto, SpecialOfferListDto} from "../dtos/special-offer";
import {formatIsoDate} from "../util/date-helper";

@Injectable({
  providedIn: 'root'
})
export class SpecialOfferService {
  private placeBaseUri: string = this.globals.backendUri + "/special-offers";


  constructor(private httpClient: HttpClient, private globals: Globals) {
  }

  /**
   * Get all special offers without image
   *
   * @return all special offers as a ListDto
   */
  getSpecialOffers(): Observable<SpecialOfferListDto[]> {
    return this.httpClient.get<SpecialOfferListDto[]>(this.placeBaseUri);
  }

  /**
   * Create a new special offer
   *
   * @param specialOfferCreateDto the special offer to create
   * @return an Observable for the HttpResponse
   */
  createSpecialOffer(specialOfferCreateDto: SpecialOfferCreateDto): Observable<SpecialOfferCreateDto> {
    const formData: FormData = new FormData();
    formData.append('name', specialOfferCreateDto.name);
    formData.append('pricePerPax', specialOfferCreateDto.pricePerPax.toString());
    formData.append('image', specialOfferCreateDto.image);
    return this.httpClient.post<SpecialOfferCreateDto>(this.placeBaseUri, formData);
  }

  /**
   * Delete a special offer by its ID
   *
   * @param id
   * @return an Observable for the HttpResponse
   */
  delete(id: number): Observable<HttpResponse<void>> {
    return this.httpClient.delete<void>(this.placeBaseUri, {observe: 'response', body: id});
  }

  /**
   * Get a special offer by its ID
   *
   * @param id
   * @return the special offer as a DetailDto
   */
  getSpecialOffer(id: number): Observable<SpecialOfferDetailDto> {
    return this.httpClient.get<SpecialOfferDetailDto>(this.placeBaseUri + "/detail", {params: new HttpParams().set('id', id)});
  }


  /**
   * Get all special offers with image
   *
   * @return all special offers as a DetailDto
   */
  getAllSpecialOffersWithDetail(): Observable<SpecialOfferDetailDto[]> {
    return this.httpClient.get<SpecialOfferDetailDto[]>(this.placeBaseUri + "/details");
  }
}
