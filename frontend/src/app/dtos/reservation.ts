import {AppUser} from "./app-user";

export interface Reservation {
  id: number;
  user: AppUser;
  startTime: Date;
  date: Date;
  endTime: Date;
  pax: number;
  notes: string;
  placeId: number;
  userId: number;
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

export interface ReservationCheckAvailabilityDto {
  startTime: Date;
  date: Date;
  pax: number;
}

export interface ReservationDetailDto {
  id: number;
  startTime: Date;
  endTime: Date;
  date: Date;
  pax: number;
  notes: string;
  placeId: number[];
}
