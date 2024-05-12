import {Component, OnInit} from '@angular/core';
import {AuthService} from '../../services/auth.service';
import {NgForm} from "@angular/forms";
import {Reservation, ReservationCreateDto} from "../../dtos/reservation";
import {Observable} from "rxjs";
import {ReservationService} from "../../services/reservation.service";

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

  constructor(
    public authService: AuthService,
    private service: ReservationService
    ) { }

  ngOnInit() { }

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
