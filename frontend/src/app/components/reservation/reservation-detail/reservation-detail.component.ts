import {Component, OnInit} from '@angular/core';
import {ReservationDetailDto} from "../../../dtos/reservation";
import {ReservationService} from "../../../services/reservation.service";
import {AuthService} from "../../../services/auth.service";
import {ReservationIdService} from "../../../services/reservation-id.service";
import {Observable} from "rxjs";
import {HttpResponse} from "@angular/common/http";

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
    private reservationIdService: ReservationIdService,
    ) { } // constructor

  ngOnInit() {
    // TODO: remove as soon as routing to this page is implemented correctly
    this.reservationIdService.setReservationId(1);

    // 1. get reservation id from service
    const id = this.reservationIdService.getReservationId();
    if (id) {
      // 2. load data via ID from BE and set it to reservationDetailDto
      let observable: Observable<ReservationDetailDto>;
      observable = this.service.getById(id);
      observable.subscribe( {
        next: (data) => {
          if (data != null) {
            console.log("ReservationDetailDto: ", data); // TODO: remove after testing
            this.reservationDetailDto = data;
          }
        },
          error: (error) => {
            // TODO: route back to reservation list (e.g. reservation was deleted in the meantime and then the current page was refreshed)
            console.log("Error: ", error); // TODO: remove after testing
        }, // error
      }); // subscribe
    } else {
      // TODO: handle this situation, e.g. rerouting to reservation list and a notification
    }
  } // ngOnInit

  onSubmit() {
    if (this.reservationDetailDto.id != this.reservationIdService.getReservationId()) {
      // NOTE: if we reach this, the user probably opened multiple detail pages. Only the last opened page is valid,
      // since the reservationIdService will only track the latest opened reservation detail page.
      // Therefore, we do not allow any edits, inform the user accordingly and route back to the list.

      // TODO: route back to reservation list
      // TODO: notification to inform that only one reservation can be viewed at once
    }

    let observable: Observable<ReservationDetailDto>;
    observable = this.service.update(this.reservationDetailDto);
    observable.subscribe( {
      next: (data) => {
        if (data == null) {
          // TODO: handle this (or remove this case if error handles this definitely)
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

  onDelete() {
    if (this.reservationDetailDto.id != this.reservationIdService.getReservationId()) {
      // NOTE: if we reach this, the user probably opened multiple detail pages. Only the last opened page is valid
      // since the reservationIdService will only track the latest opened reservation detail page.
      // Therefore, we do not allow the deletion, inform the user accordingly and route back to the list.

      // TODO: route back to reservation list
      // TODO: notification to inform that only one reservation can be viewed at once
    }

    let observable: Observable<HttpResponse<void>>;
    observable = this.service.delete(this.reservationDetailDto.id);

    observable.subscribe( {
      next: (response) => {
        if (response.status == 204) {
          console.log("Reservation deleted successfully."); // TODO: remove after testing
          // TODO: handle success (route back to reservation list)
        } else {
          // TODO: handle already deleted reservation, logged out user or ended session explicitly here! All other cases are handled in error handler below.
        }
      },
      error: (error) => {
        // TODO: handle error and notification
        console.log("Error: ", error); // TODO: remove after testing
      }, // error
    }); // subscribe
  } // onDelete
}
