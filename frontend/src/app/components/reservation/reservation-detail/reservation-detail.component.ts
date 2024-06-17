import {Component, OnInit} from '@angular/core';
import {ReservationDetailDto, ReservationEditDto} from "../../../dtos/reservation";
import {ReservationService} from "../../../services/reservation.service";
import {AuthService} from "../../../services/auth.service";
import {Observable} from "rxjs";
import {ActivatedRoute, Router} from "@angular/router";
import {HttpResponse} from "@angular/common/http";
import {NotificationService} from "../../../services/notification.service";

@Component({
  selector: 'app-reservation-detail',
  templateUrl: './reservation-detail.component.html',
  styleUrl: './reservation-detail.component.scss'
})
export class ReservationDetailComponent implements OnInit {
  hashID: string;
  deleteWhat: ReservationEditDto | null = null;

  reservationDetailDto: ReservationDetailDto = {
    id: undefined,
    startTime: undefined,
    endTime: undefined,
    date: undefined,
    pax: undefined,
    notes: undefined,
    placeIds: undefined
  };

  constructor(
    public authService: AuthService,
    private service: ReservationService,
    private router: Router,
    private notificationService: NotificationService,
    private route: ActivatedRoute,
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
            this.reservationDetailDto.placeIds = data.placeIds;
          }
        },
        error: (error) => {
          this.notificationService.handleError(error);
          this.router.navigate(['/reservation-overview']);
        }, // error
      }); // subscribe
    }
  } // ngOnInit

  openConfirmationDialog(hashId: string): void {
    this.service.getByHashedId(hashId).subscribe({
      next: data => {
        this.deleteWhat = data;
      },
      error: error => {
        this.notificationService.showError('Failed to load reservation details. Please try again later.');
      }
    });
  } // openConfirmationDialog

  onDelete(): void {
    if (!this.deleteWhat) {
      this.notificationService.showError('No reservation selected for deletion.');
      return;
    }

    let observable: Observable<HttpResponse<void>>;
    observable = this.service.delete(this.deleteWhat.hashedId);
    observable.subscribe({
      next: (response) => {
        if (response.status === 204) {
          this.notificationService.showSuccess('Reservation cancelled successfully');
          this.router.navigate(['/reservation-overview']);
        }
      },
      error: (error) => {
        this.notificationService.showError('Failed to cancel reservation. Please try again later.');
      }
    });
  } // onDelete
}
