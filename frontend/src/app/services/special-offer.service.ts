import { Injectable } from '@angular/core';
import {Globals} from "../global/globals";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {SpecialOfferCreateDto, SpecialOfferListDto} from "../dtos/special-offer";

@Injectable({
  providedIn: 'root'
})
export class SpecialOfferService {
  private placeBaseUri : string = this.globals.backendUri + "/special-offers";


  constructor(private httpClient: HttpClient, private globals: Globals) { }

  /**
   * Get all special offers
   *
   * @return an Observable for the HttpResponse
   */
  getSpecialOffers():Observable<SpecialOfferListDto[]> {
    return this.httpClient.get<SpecialOfferListDto[]>(this.placeBaseUri);
  }

  /**
   * Create a new special offer
   *
   * @param specialOfferCreateDto the special offer to create
   * @return an Observable for the HttpResponse
   */
  createSpecialOffer(specialOfferCreateDto: SpecialOfferCreateDto): Observable<SpecialOfferCreateDto> {
    return this.httpClient.post<any>(this.placeBaseUri, specialOfferCreateDto);
  }
}
