import {AppUser} from "./app-user";
import {SpecialOffer, SpecialOfferAmountDto, SpecialOfferListDto} from "./special-offer";

export interface Reservation {
  id: number;
  user: AppUser;
  startTime: Date;
  date: Date;
  endTime: Date;
  pax: number;
  notes: string;
}

export interface ReservationCreateDto {
  user: AppUser;
  firstName: string;
  lastName: string;
  startTime: Date;
  endTime: Date;
  date: Date;
  pax: number;
  notes: string;
  email: string;
  mobileNumber: number;
  placeIds?: number[];
  specialOffers?: SpecialOfferAmountDto[];
}

export interface ReservationListDto {
  id: number,
  userFirstName: string,
  userLastName: string,
  startTime: Date;
  date: Date;
  endTime: Date;
  pax: number;
  placeIds: number[];
  hashId: string;
  confirmed: boolean;
}

export interface ReservationSearch {
  earliestDate?: Date;
  latestDate?: Date;
  earliestStartTime?: string;
  latestEndTime?: string;
}

export interface ReservationCheckAvailabilityDto {
  startTime: Date;
  endTime?: Date;
  date: Date;
  pax: number;
  idToExclude?: number;
}

export interface ReservationDetailDto {
  id: number;
  startTime: Date;
  endTime: Date;
  date: Date;
  pax: number;
  notes: string;
  placeIds: number[];
}

export interface ReservationModalDetailDto {
  firstName: string;
  lastName: string;
  startTime: Date;
  endTime: Date;
  notes: string;
  placeIds: number[];
  specialOffers: SpecialOfferAmountDto[];
}

export interface ReservationEditDto {
  reservationId: number;
  startTime: Date;
  endTime: Date;
  date: Date;
  pax: number;
  notes: string;
  hashedId: string;
  user: AppUser;
  placeIds: number[];
  specialOffers: SpecialOfferAmountDto[];
}

export interface ReservationWalkInDto{
  startTime: string;
  date: string;
  pax: number;
  placeIds: number[];
}

export interface PermanentReservationDto {
  id?: number;
  user: AppUser;
  startDate: Date;
  startTime: Date;
  endTime: Date;
  endDate?: Date;
  repetition: RepetitionEnum;
  period: number;
  confirmed: boolean;
  pax:number;
  hashedId:String;
}

export interface PermanentReservationListDto {
  id: string;
  user: AppUser;
  startDate: Date;
  startTime: Date;
  endTime: Date;
  endDate?: Date;
  repetition: RepetitionEnum;
  period: number;
  confirmed: boolean;
  pax:number;
  hashedId:String;
}

export interface permanentReservationSearch {
  earliestDate?: Date;
  latestDate?: Date;
  earliestStartTime?: string;
  latestEndTime?: string;
  userId?: number;
}

export interface PermanentReservationListDto{
  id: string;
  userFirstName:String;
  userLastName:String;
  startDate: Date;
  startTime: Date;
  endTime: Date;
  endDate?: Date;
  repetition: RepetitionEnum;
  period: number;
  confirmed: boolean;
  pax:number;
  hashedId:String;
}

export interface PermanentReservationDetailDto {
  id: number;
  userFirstName: string;
  userLastName: string;
  startTime: string;
  endTime: string;
  startDate: string;
  endDate: string;
  repetition: string;
  period: number;
  confirmed: boolean;
  pax: number;
  hashedId: string;
  singleReservationList: ReservationListDto[];
}
export enum RepetitionEnum{
  DAYS='DAYS',
  WEEKS='WEEKS'
}
