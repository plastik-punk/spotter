export interface Reservation {
  id: number;
  startTime: Date;
  endTime: Date;
  pax: number;
  notes: string;
  placeId: number;
  userId: number;
}

export interface GuestReservation {
  startTime: Date;
  date: Date;
  pax: number;
  name: string;
  notes: string;
  email: string;
  mobileNumber: number;
}
