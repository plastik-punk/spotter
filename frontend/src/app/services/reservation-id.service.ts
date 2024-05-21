import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ReservationIdService {
  private reservationId: number;

  setReservationId(setReservationId: number) {
    this.reservationId = setReservationId;
  }

  getReservationId(): number {
    return this.reservationId;
  }
}
