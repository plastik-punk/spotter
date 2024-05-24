import {Component, OnInit} from '@angular/core';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {AuthService} from '../../services/auth.service';
import {ReservationEditDto, ReservationListDto, ReservationSearch} from "../../dtos/reservation";
import {debounceTime, Observable, Subject} from "rxjs";
import {ReservationService} from "../../services/reservation.service";
import {Router} from "@angular/router";
import {HttpResponse} from "@angular/common/http";
import {NotificationService} from "../../services/notification.service";

@Component({
  selector: 'app-reservations-overview',
  templateUrl: './reservations-overview.component.html',
  styleUrl: './reservations-overview.component.scss'
})
export class ReservationsOverviewComponent implements OnInit {
  error = false;
  errorMessage = '';
  reservations: ReservationListDto[] = [];
  displayedReservations: ReservationListDto[] = [];
  searchParams: ReservationSearch = {};
  searchLatestEndTime: string | null = null;
  searchEarliestStartTime: string | null = null;
  searchEarliestDate: string | null = null;
  searchLatestDate: string | null = null;
  searchChangedObservable = new Subject<void>();
  deleteWhat: ReservationEditDto = null;

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
      .subscribe({next: () => this.loadReservations()});
  }

  /**
   * Error flag will be deactivated, which clears the error message
   */
  vanishError() {
    this.error = false;
  }

  showMore() {
    let newLength = this.displayedReservations.length + 5;
    if (newLength > this.reservations.length) {
      newLength = this.reservations.length
    }
    this.displayedReservations = this.reservations.slice(0, newLength);
  }

  /**
   * Returns true if the authenticated user is an admin
   */
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
          this.defaultServiceErrorHandling(error);
        }
      });
  }

  searchChanged(): void {
    this.searchChangedObservable.next();
  }

  openConfirmationDialog(hashId: string): void {
    this.reservationService.getByHashedId(hashId).subscribe({
      next: data => {
        this.deleteWhat = data;
      },
      error: error => {
        //TODO: correct error?
        this.notificationService.handleError(error);
      }
    })
  }

  onDelete(): void {
    let observable: Observable<HttpResponse<void>>;
    observable = this.reservationService.delete(this.deleteWhat.reservationId);
    observable.subscribe({
      next: (response) => {
        if (response.status == 204) {
          this.notificationService.handleSuccess('reservation cancelled successfully');
          this.loadReservations();
        } else {
          // TODO: depending on final implementation, handle already deleted reservation, logged out user or ended session here. All other cases are handled in error handler below.
        }
      },
      error: (error) => {
        this.notificationService.handleError(error);
      }
    });
  }

  private defaultServiceErrorHandling(error: any) {
    console.log(error);
    this.error = true;
    if (typeof error.error === 'object') {
      this.errorMessage = error.error.error;
    } else {
      this.errorMessage = error.error;
    }
  }
}
