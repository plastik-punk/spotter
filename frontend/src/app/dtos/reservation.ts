import {AppUser} from "./app-user";

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
}

export interface ReservationListDto {
  id: number,
  userFirstName: string,
  userLastName: string,
  startTime: Date;
  date: Date;
  endTime: Date;
  pax: number;
  placeId: number;
  hashId: string;
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
}

export interface ReservationLayoutCheckAvailabilityDto {
  startTime: string;
  date: string;
  areaId: number;
  idToExclude: number;
}

export interface AreaLayoutDto {
  width: number;
  height: number;
  placeVisuals: PlaceVisualDto[];
}

export interface PlaceVisualDto {
  placeId: number;
  status: boolean;
  reservation: boolean;
  numberOfSeats: number;
  coordinates: CoordinateDto[];
}

export interface CoordinateDto {
  x: number;
  y: number;
}

