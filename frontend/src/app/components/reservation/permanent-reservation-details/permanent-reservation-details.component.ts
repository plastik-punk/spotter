import { Component, OnInit } from '@angular/core';
import * as bootstrap from 'bootstrap';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import {
  PermanentReservationDetailDto,
  PermanentReservationListDto,
  RepetitionEnum, ReservationEditDto,
  ReservationModalDetailDto
} from "../../../dtos/reservation";
import { ReservationService } from "../../../services/reservation.service";
import { CommonModule, DatePipe } from "@angular/common";
import moment from "moment";
import { NavigationStateService } from "../../../services/navigation-state.service";
import { NotificationService } from "../../../services/notification.service";
import { Observable } from "rxjs";
import { HttpResponse } from "@angular/common/http";
import {AppModule} from "../../../app.module";

@Component({
  selector: 'app-permanent-reservation-details',
  templateUrl: './permanent-reservation-details.component.html',
  styleUrls: ['./permanent-reservation-details.component.scss']
})
export class PermanentReservationDetailsComponent implements OnInit {
  permanentReservationDetails: PermanentReservationDetailDto;
  deleteWhat: ReservationEditDto | null = null;
  hashId: string;
  reservationModalDetailDto: ReservationModalDetailDto = {
    firstName: undefined,
    lastName: undefined,
    startTime: undefined,
    endTime: undefined,
    notes: undefined,
    placeIds: undefined,
    specialOffers: undefined
  };

  constructor(
    private reservationService: ReservationService,
    private route: ActivatedRoute,
    private router: Router,
    private navigationStateService: NavigationStateService,
    private notificationService: NotificationService
  ) { }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.hashId = params['hashedId'];
      this.loadPermanentReservationDetails(this.hashId);
    });
  }

  loadPermanentReservationDetails(hashedId: string): void {
    this.reservationService.getPermanentReservationDetailsByHashedId(hashedId).subscribe({
      next: (data) => {
        this.permanentReservationDetails = data;
      },
      error: (error) => {
        this.notificationService.showError('Failed to load permanent reservation details. Please try again later.');
      }
    });
  }

  getFrequency(permanentReservationDetailDto: PermanentReservationDetailDto): String {
    if (permanentReservationDetailDto.repetition === RepetitionEnum.DAYS) {
      if (permanentReservationDetailDto.period == 1) {
        return 'Daily'
      } else {
        return 'every ' + permanentReservationDetailDto.period + ' days'
      }
    } else if (permanentReservationDetailDto.repetition === RepetitionEnum.WEEKS) {
      if (permanentReservationDetailDto.period == 1) {
        return 'Weekly'
      } else {
        return 'every ' + permanentReservationDetailDto.period + ' weeks'
      }
    }
  }

  calculateDuration(startTime: string, endTime: string): string {
    const start = moment(startTime, 'HH:mm:ss');
    const end = moment(endTime, 'HH:mm:ss');
    return moment.duration(end.diff(start)).humanize();
  }

  navigateToEdit(singleReservationHashedId: string, hashedId: string): void {
    this.navigationStateService.setNavigationState({
      fromPermanentDetail: true,
      returnUrl: `/permanent-reservation-details/${hashedId}`
    });

    this.router.navigate(['/reservation-edit', singleReservationHashedId]);
  }

  showReservationDetails(hashId: string): void {
    this.reservationService.getModalDetail(hashId).subscribe({
      next: (data: ReservationModalDetailDto) => {
        this.reservationModalDetailDto.firstName = data.firstName;
        this.reservationModalDetailDto.lastName = data.lastName;
        this.reservationModalDetailDto.startTime = data.startTime;
        this.reservationModalDetailDto.endTime = data.endTime;
        this.reservationModalDetailDto.notes = data.notes || 'No notes';
        this.reservationModalDetailDto.placeIds = data.placeIds;

        const modalDetail = new bootstrap.Modal(document.getElementById('confirmation-dialog-reservation-detail'));
        modalDetail.show();
      },
      error: error => {
        this.notificationService.showError('Failed to load reservation details. Please try again later.');
      }
    });
  }

  goBack(): void {
    this.navigationStateService.setNavigationState({
      showPermanentReservations: true
    });
    this.router.navigate(['/reservation-overview']);
  }

  openConfirmationDialog(hashId: string): void {
    this.reservationService.getByHashedId(hashId).subscribe({
      next: data => {
        this.deleteWhat = data;
      },
      error: error => {
        this.notificationService.showError('Failed to delete reservation. Please try again later.');
      }
    });
  }

  onDelete(): void {
    if (!this.deleteWhat) {
      this.notificationService.showError('No reservation selected for deletion.');
      return;
    }

    let observable: Observable<HttpResponse<void>>;
    observable = this.reservationService.delete(this.deleteWhat.hashedId);
    observable.subscribe({
      next: (response) => {
        if (response.status === 204) {
          this.notificationService.showSuccess('Reservation cancelled successfully');
          this.loadPermanentReservationDetails(this.hashId);
        }
      },
      error: (error) => {
        this.notificationService.showError('Failed to cancel reservation. Please try again later.');
      }
    });
  }
}
