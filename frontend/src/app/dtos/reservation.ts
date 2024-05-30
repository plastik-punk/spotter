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
export interface Coordinate {
  x: number;
  y: number;
}

export interface PlaceVisual {
  id: number;
  coordinates: Coordinate[];
  seats: number;
  reserved: number; // 0 for free, 1 for booked
  status: number;
}
