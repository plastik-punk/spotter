import {Component, OnInit} from '@angular/core';
import {ReservationDetailDto} from "../../../dtos/reservation";
import {ReservationService} from "../../../services/reservation.service";
import {AuthService} from "../../../services/auth.service";
import {ReservationIdService} from "../../../services/reservation-id.service";
import {Observable} from "rxjs";
import {HttpResponse} from "@angular/common/http";
import {ToastrService} from "ngx-toastr";
import {Router} from '@angular/router';
import {NotificationService} from "../../../services/notification.service";

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
    private notification: ToastrService,
    private router: Router,
    private notificationService: NotificationService,
  ) { } // constructor

  ngOnInit() {
    // TODO: remove as soon as routing to this page is implemented correctly
    this.reservationIdService.setReservationId(1);
    const id = this.reservationIdService.getReservationId();


    let observable: Observable<ReservationDetailDto>;
    observable = this.service.getById(id);
    observable.subscribe( {
      next: (data) => {
        if (data != null) {
          this.reservationDetailDto = data;
        }
      },
      error: (error) => {
        this.notificationService.handleError(error);
        this.router.navigate(['/home']); // TODO: change to reservation list
      }, // error
    }); // subscribe
  } // ngOnInit

  onSubmit() {
    let observable: Observable<ReservationDetailDto>;
    observable = this.service.update(this.reservationDetailDto);
    observable.subscribe( {
      next: (data) => {
        if (data != null) {
          this.reservationDetailDto = data;
          this.notificationService.handleSuccess('reservation edited successfully');
        }
      },
      error: (error) => {
        this.notificationService.handleError(error);
      }, // error
    }); // subscribe
  } // onSubmit

  toggleEditMode() {
    this.editMode = !this.editMode;
  }

  onDelete() {
    let observable: Observable<HttpResponse<void>>;
    observable = this.service.delete(this.reservationDetailDto.id);

    observable.subscribe( {
      next: (response) => {
        if (response.status == 204) {
          this.notificationService.handleSuccess('reservation canceled successfully');
          this.router.navigate(['/home']); // TODO: change to reservation list

        } else {
          // TODO: depending on final implementation, handle already deleted reservation, logged out user or ended session here. All other cases are handled in error handler below.
        }
      },
      error: (error) => {
        this.notificationService.handleError(error);
      }, // error
    }); // subscribe
  } // onDelete
}
