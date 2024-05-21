import {Component, OnInit} from '@angular/core';
import {AuthService} from '../../services/auth.service';
import {NgForm} from "@angular/forms";
import {Reservation, ReservationCheckAvailabilityDto, ReservationCreateDto} from "../../dtos/reservation";
import {Observable} from "rxjs";
import {ReservationService} from "../../services/reservation.service";
import {SimpleViewReservationStatusEnum} from "../../dtos/status-enum";
import {UserOverviewDto} from "../../dtos/app-user";
import {ToastrService} from "ngx-toastr";

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

  currentUser: UserOverviewDto;

  enumReservationTableStatus = SimpleViewReservationStatusEnum;
  reservationStatusText: string = 'Provide Time, Date and Pax';
  reservationStatusClass: string = 'reservation-table-incomplete';

  constructor(
    public authService: AuthService,
    private service: ReservationService,
    private notification: ToastrService,
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
          this.notification.error(
            error.error.errors,
            error.error.message,
            {
              enableHtml: true,
              timeOut: 5000,
            },
          );
        }, // error
      });
  } // onFieldChange

  onSubmit(form: NgForm) {
    // 1. if logged in, fetch user details and update DTO
    if (this.authService.isLoggedIn() == true) {
      this.currentUser = this.authService.getCurrentUser();
      this.reservationCreateDto.firstName = this.currentUser.firstName;
      this.reservationCreateDto.lastName = this.currentUser.lastName;
      this.reservationCreateDto.email = this.currentUser.email;
      this.reservationCreateDto.mobileNumber = Number(this.currentUser.mobileNumber);
    }

    // 2. send request to backend
    if (form.valid) {
      let observable: Observable<Reservation>;
      observable = this.service.createReservation(this.reservationCreateDto);
      observable.subscribe({
        next: (data) => {
          if (data == null) {
            // TODO: handle this case (table was booked in the meantime)
          } else {
            this.notification.success(
              `reservation created successfully`,
              null, {
                timeOut: 5000, // specify the timeout in milliseconds (if not set, this is 5000 by default)
                enableHtml: true // allows for html formatting like bullet points for validation errors
              });
            // TODO: route to reservation detail view?
          }
        },
        error: (error) => {
          this.notification.error(
            error.error.errors,
            error.error.message,
            {
              enableHtml: true,
              timeOut: 5000,
            },
          );
        }, // error
      }); // observable.subscribe
    }
  } // onSubmit
}
