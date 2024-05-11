import {Component, OnInit} from '@angular/core';
import {AuthService} from '../../services/auth.service';
import {NgForm} from "@angular/forms";
import {GuestReservation, Reservation} from "../../dtos/reservation";
import {Observable} from "rxjs";
import {ReservationService} from "../../services/reservation.service";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  guestReservation: GuestReservation = {
    startTime: undefined,
    date: undefined,
    pax: undefined,
    name: undefined,
    notes: undefined,
    email: undefined,
    mobileNumber: undefined
  };

  constructor(
    public authService: AuthService,
    private service: ReservationService

    ) { }

  ngOnInit() {
  }

  onSubmit(form: NgForm) {
    console.log(form);
    if (form.valid) {
      let observable: Observable<Reservation>;
      observable = this.service.createGuestReservation(this.guestReservation);
      observable.subscribe({
        next: (data) => {
          // todo: handle success (notification, redirect etc.)
          console.log("Reservation Processed Successfully", data); // todo: remove after testing
        },
        error: (error) => {
          // todo: handle error and notifications
          console.error("Error Processing Reservation", error);
        }, // error
      }); // observable.subscribe
    }
  } // onSubmit
}
