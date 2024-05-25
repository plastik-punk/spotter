import { Component, OnInit } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { AuthService } from '../../../services/auth.service';
import { ReservationEditDto, ReservationListDto, ReservationSearch } from "../../../dtos/reservation";
import { debounceTime, Observable, Subject } from "rxjs";
import { ReservationService } from "../../../services/reservation.service";
import { Router } from "@angular/router";
import { HttpResponse } from "@angular/common/http";
import { NotificationService } from "../../../services/notification.service";

@Component({
  selector: 'app-reservation-overview',
  templateUrl: './reservation-overview.component.html',
  styleUrl: './reservation-overview.component.scss'
})
export class ReservationOverviewComponent implements OnInit {
  reservations: ReservationListDto[] = [];
  displayedReservations: ReservationListDto[] = [];
  searchParams: ReservationSearch = {};
  searchLatestEndTime: string | null = null;
  searchEarliestStartTime: string | null = null;
  searchEarliestDate: string | null = null;
  searchLatestDate: string | null = null;
  searchChangedObservable = new Subject<void>();
  deleteWhat: string = null;

  constructor(
    private authService: AuthService,
    private modalService: NgbModal,
    private reservationService: ReservationService,
    private notificationService: NotificationService,
    private router: Router) {
  }

  ngOnInit(): void {
    this.loadReservations();
    this.searchChangedObservable
      .pipe(debounceTime(300))
      .subscribe({ next: () => this.loadReservations() });
  }

  showMore() {
    let newLength = this.displayedReservations.length + 5;
    if (newLength > this.reservations.length) {
      newLength = this.reservations.length;
    }
    this.displayedReservations = this.reservations.slice(0, newLength);
  }

  isAdmin(): boolean {
    return this.authService.getUserRole() === 'ADMIN';
  }

  loadReservations() {
    if (this.searchEarliestDate == null || this.searchEarliestDate === "") {
      delete this.searchParams.earliestDate;
    } else {
      this.searchParams.earliestDate = new Date(this.searchEarliestDate);
    }
    if (this.searchLatestDate == null || this.searchLatestDate === "") {
      delete this.searchParams.latestDate;
    } else {
      this.searchParams.latestDate = new Date(this.searchLatestDate);
    }
    if (this.searchEarliestStartTime == null || this.searchEarliestStartTime === "") {
      delete this.searchParams.earliestStartTime;
    } else {
      this.searchParams.earliestStartTime = this.searchEarliestStartTime;
    }
    if (this.searchLatestEndTime == null || this.searchLatestEndTime === "") {
      delete this.searchParams.latestEndTime;
    } else {
      this.searchParams.latestEndTime = this.searchLatestEndTime;
    }

    this.reservationService.search(this.searchParams)
      .subscribe({
        next: (reservation: ReservationListDto[]) => {
          this.reservations = reservation;
          this.displayedReservations = this.reservations.slice(0, 10);
        },
        error: error => {
          this.notificationService.showError('Failed to load reservations. Please try again later.');
        }
      });
  }

  searchChanged(): void {
    this.searchChangedObservable.next();
  }

  openConfirmationDialog(hashId: string): void {
    this.reservationService.getByHashedId(hashId).subscribe({
      next: data => {
        this.deleteWhat = hashId;
      },
      error: error => {
        this.notificationService.showError('Failed to load reservation details. Please try again later.');
      }
    });
  }

  onDelete(): void {
    if (!this.deleteWhat) {
      this.notificationService.showError('No reservation selected for cancellation.');
      return;
    }

    let observable: Observable<HttpResponse<void>>;
    observable = this.reservationService.delete(this.deleteWhat);
    observable.subscribe({
      next: (response) => {
        if (response.status === 204) {
          this.notificationService.showSuccess('Reservation cancelled successfully');
          this.loadReservations();
        }
      },
      error: (error) => {
        this.notificationService.showError('Failed to cancel reservation. Please try again later.');
      }
    });
  }
}
