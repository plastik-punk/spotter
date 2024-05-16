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

  enumReservationTableStatus = SimpleViewReservationStatusEnum; // needed to make enum available in html
  reservationTableStatus: SimpleViewReservationStatusEnum = SimpleViewReservationStatusEnum.checking;

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

    // 2. check if all required fields are filled
    if (this.reservationCheckAvailabilityDto.startTime
          && this.reservationCheckAvailabilityDto.date
          && this.reservationCheckAvailabilityDto.pax) {

      // TODO: remove after testing
      console.log("checking availability for: ", this.reservationCheckAvailabilityDto)

      // 3. send request to backend
      this.service.getAvailability(this.reservationCheckAvailabilityDto).subscribe({
        next: (data) => {
          // b. update reservationTableStatus based on the data received from the backend
          if (data.valueOf() === this.enumReservationTableStatus.available.valueOf()) {
            this.reservationTableStatus = SimpleViewReservationStatusEnum.available;
          } else {
            this.reservationTableStatus = SimpleViewReservationStatusEnum.allOccupied;
          }
        },
        error: (error) => {
          // TODO: error and notification handling
          console.error("Error Processing Reservation", error);
        },
      });
    } else {
      this.reservationTableStatus = SimpleViewReservationStatusEnum.checking;
    }
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
