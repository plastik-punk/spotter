export interface Reservation {
  id: number;
  startTime: Date;
  endTime: Date;
  pax: number;
  notes: string;
  placeId: number;
  userId: number;
}
