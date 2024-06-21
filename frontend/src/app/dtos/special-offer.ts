import {AppUser} from "./app-user";

export interface SpecialOffer {
  id: number;
  name: string;
  pricePerPax: number;
  image: File;
}

export interface SpecialOfferListDto {
  id: number;
  name: string;
  pricePerPax: number;
}

export interface SpecialOfferCreateDto {
  name: string;
  pricePerPax: number;
  image: File;
}

export interface SpecialOfferDetailDto {
  id: number;
  name: string;
  pricePerPax: number;
  image: Uint8Array;
}
