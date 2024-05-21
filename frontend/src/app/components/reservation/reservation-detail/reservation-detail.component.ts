import {Component, OnInit} from '@angular/core';
import {ReservationDetailDto} from "../../../dtos/reservation";
import {ReservationService} from "../../../services/reservation.service";
import {AuthService} from "../../../services/auth.service";
import {Observable} from "rxjs";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-reservation-detail',
  templateUrl: './reservation-detail.component.html',
  styleUrl: './reservation-detail.component.scss'
})
export class ReservationDetailComponent implements OnInit {

  editMode: boolean = false;

  reservationDetailDto: ReservationDetailDto = {
    id: undefined,
    startTime: undefined,
    endTime: undefined,
    date: undefined,
    pax: undefined,
    notes: undefined,
    placeId: undefined
  };

  constructor(
    public authService: AuthService,
    private service: ReservationService,
    private route: ActivatedRoute,
    private router: Router

  ) {
  } // constructor

  ngOnInit() {
    // 1. get reservation id from service
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      // 2. load data via ID from BE and set it to reservationDetailDto
      let observable: Observable<ReservationDetailDto>;
      observable = this.service.getByHashedId(id);
      observable.subscribe({
        next: (data) => {
          if (data == null) {
            // TODO: null is returned if user doesn't match a user assigned to the reservation, route back to home or somewhere else
          } else {
            console.log("ReservationDetailDto: ", data); // TODO: remove after testing

            this.reservationDetailDto = data;
          }
        },
        error: (error) => {
          // TODO: handle error and notification
          console.log("Error: ", error); // TODO: remove after testing
        }, // error
      }); // subscribe
    } else {
      // TODO: handle this situation, e.g. rerouting to reservation list and a notification
    }
  } // ngOnInit

  onSubmit() {
    let observable: Observable<ReservationDetailDto>;
    observable = this.service.update(this.reservationDetailDto);
    observable.subscribe({
      next: (data) => {
        if (data == null) {
          // TODO: handle this
        } else {
          // TODO: show notification for invisible action success
          console.log("returned ReservationDetailDto: ", data); // TODO: remove after testing
          this.reservationDetailDto = data;
        }
      },
      error: (error) => {
        // TODO: handle error and notification
        console.log("Error: ", error); // TODO: remove after testing
      },
    });
  } // onSubmit

  toggleEditMode() {
    this.editMode = !this.editMode;
  }
}
