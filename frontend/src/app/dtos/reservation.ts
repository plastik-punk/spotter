
export interface Reservation {
  //TODO: Add properties
}

export interface ReservationListDto {
  id: number,
  user: string,
  startDate: Date,
  endDate: Date;
  pax: number;
  placeId: number;
}


export interface ReservationSearch {
  earliestStartDate?: Date;
  latestStartDate?: Date;
}

