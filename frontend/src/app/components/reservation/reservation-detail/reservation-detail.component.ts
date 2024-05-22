import {Component, OnInit} from '@angular/core';
import {ReservationDetailDto, ReservationEditDto} from "../../../dtos/reservation";
import {ReservationService} from "../../../services/reservation.service";
import {AuthService} from "../../../services/auth.service";
import {Observable} from "rxjs";
import {ActivatedRoute, Router} from "@angular/router";
import {HttpResponse} from "@angular/common/http";
import {ToastrService} from "ngx-toastr";
import {NotificationService} from "../../../services/notification.service";
import {ReservationEditComponent} from "../reservation-edit/reservation-edit.component";

@Component({
  selector: 'app-reservation-detail',
  templateUrl: './reservation-detail.component.html',
  styleUrl: './reservation-detail.component.scss'
})
export class ReservationDetailComponent implements OnInit {
  hashID: string;

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
    private notification: ToastrService,
    private notificationService: NotificationService,
    private route: ActivatedRoute,
    private router: Router
  ) {
  } // constructor

  ngOnInit() {
    // 1. get reservation id from service
    this.hashID = this.route.snapshot.paramMap.get('id');
    if (this.hashID) {
      // 2. load data via ID from BE and set it to reservationDetailDto
      let observable: Observable<ReservationEditDto>;
      observable = this.service.getByHashedId(this.hashID);
      observable.subscribe({
        next: (data) => {
          if (data != null) {
            this.reservationDetailDto.id= data.reservationId;
            this.reservationDetailDto.startTime = data.startTime;
            this.reservationDetailDto.endTime = data.endTime;
            this.reservationDetailDto.date = data.date;
            this.reservationDetailDto.pax = data.pax;
            this.reservationDetailDto.notes = data.notes;
            this.reservationDetailDto.placeId = data.placeId;
          }
        },
        error: (error) => {
          this.notificationService.handleError(error);
          this.router.navigate(['/reservations-overview']);
        }, // error
      }); // subscribe
    } // ngOnInit
  }

  onSubmit() {
    if (this.authService.isLoggedIn()) {
      this.router.navigate(['/reservations-overview']);
    } else {
      this.router.navigate(['/home']);
    }
  } // onSubmit

  onEdit() {
    this.router.navigate(['/reservation-edit', this.hashID])
  }

  onDelete() {
    let observable: Observable<HttpResponse<void>>;
    observable = this.service.delete(this.reservationDetailDto.id);

    observable.subscribe({
      next: (response) => {
        if (response.status == 204) {
          this.notificationService.handleSuccess('reservation canceled successfully');
          if (this.authService.isLoggedIn()) {
            this.router.navigate(['/reservations-overview']);
          } else {
            this.router.navigate(['/home']);
          }
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
