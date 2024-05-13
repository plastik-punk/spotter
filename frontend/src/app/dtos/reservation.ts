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
export interface ReservationListDto {
  id: number,
  user: string,
  startTime: Date;
  date: Date;
  endTime: Date;
  pax: number;
  placeId: number;
}

export interface ReservationSearch {
  earliestDate?: Date;
  latestDate?: Date;
  earliestStartTime?: Date;
  latestEndTime?: Date;
}
