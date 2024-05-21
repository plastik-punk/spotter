import { Component } from '@angular/core';
import {DatePipe, NgIf} from "@angular/common";
import {FormsModule, NgForm} from "@angular/forms";
import {ReservationCheckAvailabilityDto, ReservationEditDto} from "../../../dtos/reservation";
import {ReservationService} from "../../../services/reservation.service";
import {AuthService} from "../../../services/auth.service";
import {Observable} from "rxjs";
import { ActivatedRoute } from '@angular/router';
import {SimpleViewReservationStatusEnum} from "../../../dtos/status-enum";

@Component({
  selector: 'app-reservation-edit',
  standalone: true,
  imports: [
    DatePipe,
    FormsModule,
    NgIf
  ],
  templateUrl: './reservation-edit.component.html',
  styleUrl: './reservation-edit.component.scss'
})
export class ReservationEditComponent {
  reservationEditDto: ReservationEditDto = {
    id: undefined,
    startTime: undefined,
    endTime: undefined,
    date: undefined,
    pax: undefined,
    notes: undefined,
    placeId: undefined
  };
  reservationCheckAvailabilityDto: ReservationCheckAvailabilityDto = {
    startTime: undefined,
    date: undefined,
    pax: undefined,
    idToExclude: undefined
  }
  enumReservationTableStatus = SimpleViewReservationStatusEnum;
  reservationStatusText: string = 'Provide Time, Date and Pax';
  reservationStatusClass: string = 'reservation-table-incomplete';
  constructor(
    public authService: AuthService,
    private service: ReservationService,
    private route: ActivatedRoute
  ) { } // constructor
  ngOnInit() {
    // 1. get reservation id from service
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      // 2. load data via ID from BE and set it to reservationEditDto
      let observable: Observable<ReservationEditDto>;
      observable = this.service.getByHashedId(id);
      observable.subscribe({
        next: (data) => {
            console.log("returned ReservationEditDto: ", data); // TODO: remove after testing
            this.reservationEditDto = data;
        },
        error: (error) => {
          // TODO: handle error and notification
          console.log("Error: ", error); // TODO: remove after testing
        },
      });
    }
  }

  onSubmit(form: NgForm) {

  }

  onFieldChange() {
    // 1. update DTO with current content of form
    this.reservationCheckAvailabilityDto.startTime = this.reservationEditDto.startTime;
    this.reservationCheckAvailabilityDto.date = this.reservationEditDto.date;
    this.reservationCheckAvailabilityDto.pax = this.reservationEditDto.pax;
    this.reservationCheckAvailabilityDto.idToExclude = this.reservationEditDto.id;

    if (this.reservationCheckAvailabilityDto.startTime == null || this.reservationCheckAvailabilityDto.pax == null || this.reservationCheckAvailabilityDto.date == null) {
      this.reservationStatusText = 'Provide Time, Date and Pax';
      this.reservationStatusClass = 'reservation-table-incomplete';
      return;
    }

    // 2. send request to backend
    this.service.getAvailability(this.reservationCheckAvailabilityDto).subscribe({
      next: (data) => {
        // update reservationTableStatus based on the data received from the backend
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
}
