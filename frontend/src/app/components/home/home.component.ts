import {Component, OnInit} from '@angular/core';
import {AuthService} from '../../services/auth.service';
import {NgForm} from "@angular/forms";
import {Reservation, ReservationCheckAvailabilityDto, ReservationCreateDto} from "../../dtos/reservation";
import {Observable} from "rxjs";
import {ReservationService} from "../../services/reservation.service";
import {SimpleViewReservationStatusEnum} from "../../dtos/status-enum";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  reservationCreateDto: ReservationCreateDto = {
    user: undefined,
    startTime: undefined,
    endTime: undefined,
    date: undefined,
    pax: undefined,
    firstName: undefined,
    lastName: undefined,
    notes: undefined,
    email: undefined,
    mobileNumber: undefined
  };

  reservationCheckAvailabilityDto: ReservationCheckAvailabilityDto = {
    startTime: undefined,
    date: undefined,
    pax: undefined
  }

  enumReservationTableStatus = SimpleViewReservationStatusEnum;
  reservationStatusText: string = 'Provide Time, Date and Pax';
  reservationStatusClass: string = 'reservation-table-incomplete';

  constructor(
    public authService: AuthService,
    private service: ReservationService
    ) { } // constructor

  ngOnInit() { }

  onFieldChange() {
    // 1. update DTO with current content of form
    this.reservationCheckAvailabilityDto.startTime = this.reservationCreateDto.startTime;
    this.reservationCheckAvailabilityDto.date = this.reservationCreateDto.date;
    this.reservationCheckAvailabilityDto.pax = this.reservationCreateDto.pax;

    if (this.reservationCheckAvailabilityDto.startTime == null || this.reservationCheckAvailabilityDto.pax == null || this.reservationCheckAvailabilityDto.date == null) {
      this.reservationStatusText = 'Provide Time, Date and Pax';
      this.reservationStatusClass = 'reservation-table-incomplete';
      return;
    }

      // 2. send request to backend
      this.service.getAvailability(this.reservationCheckAvailabilityDto).subscribe({
        next: (data) => {
          // b. update reservationTableStatus based on the data received from the backend
          if (data.valueOf() === this.enumReservationTableStatus.available.valueOf()) {
            this.reservationStatusText = 'Tables available';
            this.reservationStatusClass = 'reservation-table-available';
          } else if (data.valueOf() === this.enumReservationTableStatus.closed.valueOf()) {
            this.reservationStatusText = 'Location Closed This Day';
            this.reservationStatusClass = 'reservation-table-conflict';
          } else if (data.valueOf() === this.enumReservationTableStatus.outsideOpeningHours.valueOf()) {
            this.reservationStatusText = 'Outside Of Opening Hours';
            this.reservationStatusClass = 'reservation-table-conflict';
          } else if (data.valueOf() === this.enumReservationTableStatus.respectClosingHour.valueOf()) {
            this.reservationStatusText = 'Respect Closing Hour';
            this.reservationStatusClass = 'reservation-table-conflict';
          } else if (data.valueOf() === this.enumReservationTableStatus.tooManyPax.valueOf()) {
            this.reservationStatusText = 'Too Many Pax for available tables (try advanced reservation)';
            this.reservationStatusClass = 'reservation-table-conflict';
          } else if (data.valueOf() === this.enumReservationTableStatus.allOccupied.valueOf()) {
            this.reservationStatusText = 'All Tables Occupied';
            this.reservationStatusClass = 'reservation-table-conflict';
          } else if (data.valueOf() === this.enumReservationTableStatus.dateInPast.valueOf()) {
            this.reservationStatusText = 'Date In The Past';
            this.reservationStatusClass = 'reservation-table-conflict';
          }
        },
        error: (error) => {
          // TODO: error and notification handling
          console.error("Error Processing Reservation", error);
        },
      });
  } // onFieldChange

  onSubmit(form: NgForm) {
    console.log(form); // TODO: remove after testing
    if (form.valid) {
      let observable: Observable<Reservation>;
      observable = this.service.createReservation(this.reservationCreateDto);
      observable.subscribe({
        next: (data) => {
          // TODO: handle success (notification, redirect etc.)
          console.log("Reservation Processed Successfully", data); // todo: remove after testing
        },
        error: (error) => {
          // TODO: handle error and notifications
          console.error("Error Processing Reservation", error);
        }, // error
      }); // observable.subscribe
    }
  } // onSubmit

}
