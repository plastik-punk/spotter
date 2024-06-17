import {Component, OnInit} from '@angular/core';
import * as bootstrap from 'bootstrap';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {AuthService} from '../../../services/auth.service';
import {
  ReservationDetailDto,
  ReservationEditDto,
  ReservationListDto,
  ReservationSearch
} from "../../../dtos/reservation";
import {debounceTime, Observable, Subject} from "rxjs";
import {ReservationService} from "../../../services/reservation.service";
import {Router} from "@angular/router";
import {HttpResponse} from "@angular/common/http";
import {NotificationService} from "../../../services/notification.service";
import moment from 'moment';

@Component({
  selector: 'app-reservation-overview',
  templateUrl: './reservation-overview.component.html',
  styleUrls: ['./reservation-overview.component.scss']
})
export class ReservationOverviewComponent implements OnInit {
  reservations: ReservationListDto[] = [];
  reservationDetail: ReservationDetailDto = {
    id: undefined,
    startTime: undefined,
    endTime: undefined,
    date: undefined,
    pax: undefined,
    notes: undefined,
    placeIds: undefined
  };
  todaysReservations: ReservationListDto[] = [];
  upcomingReservations: ReservationListDto[] = [];
  displayedUpcomingReservations: ReservationListDto[] = [];
  searchParams: ReservationSearch = {};
  searchEarliestDate: string | null = null;
  searchLatestDate: string | null = null;
  searchEarliestStartTime: string | null = null;
  searchLatestEndTime: string | null = null;
  searchChangedObservable = new Subject<void>();
  deleteWhat: ReservationEditDto | null = null;
  untouched: boolean = true;

  currentPage = 1;
  pageSize = 25;
  totalUpcomingReservations = 0;

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
      .pipe(debounceTime(100))
      .subscribe({next: () => this.loadReservations()});
  }

  isAdmin(): boolean {
    return this.authService.getUserRole() === 'ADMIN';
  }

  isEmployee(): boolean {
    return this.authService.getUserRole() === 'EMPLOYEE';
  }


  loadReservations() {
    const today = moment().startOf('day');
    const nextWeek = moment().add(7, 'days').endOf('day');

    if (this.searchEarliestDate == null || this.searchEarliestDate === "") {
      this.searchParams.earliestDate = null;
    } else {
      this.searchParams.earliestDate = new Date(this.searchEarliestDate);
    }

    if (this.searchLatestDate == null || this.searchLatestDate === "") {
      this.searchParams.latestDate = null;
    } else {
      this.searchParams.latestDate = new Date(this.searchLatestDate);
    }

    if (this.searchEarliestStartTime == null || this.searchEarliestStartTime === "") {
      this.searchParams.earliestStartTime = null;
    } else {
      this.searchParams.earliestStartTime = this.searchEarliestStartTime;
    }

    if (this.searchLatestEndTime == null || this.searchLatestEndTime === "") {
      this.searchParams.latestEndTime = null;
    } else {
      this.searchParams.latestEndTime = this.searchLatestEndTime;
    }

    // If all fields are null, set default values
    if (this.isAdmin() || this.isEmployee()) {
      if (!this.searchParams.earliestDate && !this.searchParams.latestDate &&
        !this.searchParams.earliestStartTime && !this.searchParams.latestEndTime) {
        this.searchParams.earliestDate = today.toDate();
        this.searchParams.latestDate = nextWeek.toDate();
        this.searchParams.earliestStartTime = today.format('HH:mm');
        this.searchParams.latestEndTime = nextWeek.format('HH:mm');
      }
    }
    this.reservationService.search(this.searchParams)
      .subscribe({
        next: (reservations: ReservationListDto[]) => {
          this.reservations = reservations;
          this.filterReservations();
        },
        error: error => {
          this.notificationService.showError('Failed to load reservations. Please try again later.');
        }
      });
  }

  filterReservations() {

    const today = moment().startOf('day');
    this.todaysReservations = this.reservations.filter(reservation =>
      moment(reservation.date).isSame(today, 'day')
    );

    const todayHashSet = new Set(this.todaysReservations.map(reservation => reservation.id));

    this.upcomingReservations = this.reservations.filter(reservation => !todayHashSet.has(reservation.id));

    this.totalUpcomingReservations = this.upcomingReservations.length;
    this.displayReservations();
  }

  displayReservations() {
    const startIndex = (this.currentPage - 1) * this.pageSize;
    const endIndex = startIndex + this.pageSize;
    this.displayedUpcomingReservations = this.upcomingReservations.slice(startIndex, endIndex);
  }

  searchChanged(): void {
    this.currentPage = 1;
    this.searchChangedObservable.next();
  }

  openConfirmationDialog(hashId: string): void {
    this.reservationService.getByHashedId(hashId).subscribe({
      next: data => {
        this.deleteWhat = data;
      },
      error: error => {
        this.notificationService.showError('Failed to load reservation details. Please try again later.');
      }
    });
  }

  async showReservationDetails(hashId: string): Promise<void> {
    if (hashId) {
      try {
        let data: ReservationEditDto = await this.reservationService.getByHashedId(hashId).toPromise();

        if (data != null) {
          this.reservationDetail.id= data.reservationId;
          this.reservationDetail.startTime = data.startTime;
          this.reservationDetail.endTime = data.endTime;
          this.reservationDetail.date = data.date;
          this.reservationDetail.pax = data.pax;
          this.reservationDetail.notes = data.notes;
          this.reservationDetail.placeIds = data.placeIds;
        }
      } catch (error) {
        this.notificationService.handleError(error);
      }
      const modalDetail = new bootstrap.Modal(document.getElementById('confirmation-dialog-reservation-detail'));
      modalDetail.show();
    }
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
          this.loadReservations();
        }
      },
      error: (error) => {
        this.notificationService.showError('Failed to cancel reservation. Please try again later.');
      }
    });
  }

  changePage(newPage: number): void {
    if (newPage < 1 || newPage > Math.ceil(this.totalUpcomingReservations / this.pageSize)) {
      return;
    }
    this.currentPage = newPage;
    this.displayReservations();
  }

  protected readonly Math = Math;

  resetSearchParams(): void {
    this.currentPage = 1;
    this.searchEarliestDate = null;
    this.searchLatestDate = null;
    this.searchEarliestStartTime = null;
    this.searchLatestEndTime = null;

    this.searchParams.earliestDate = null;
    this.searchParams.latestDate = null;
    this.searchParams.earliestStartTime = null;
    this.searchParams.latestEndTime = null;

    this.loadReservations();
  }

  reservationIsInTheFuture(reservation: ReservationListDto): boolean {

    const reservationDateTime = moment(`${reservation.date} ${reservation.startTime}`, 'YYYY-MM-DD HH:mm:ss')
    return moment(reservationDateTime).isAfter(moment());
  }

  confirmReservation(hashId: string): void {
    this.reservationService.confirm(hashId).subscribe({
      next: () => {
        this.notificationService.showSuccess('Reservation confirmed successfully');
        this.loadReservations();
      },
      error: error => {
        this.notificationService.showError('Failed to confirm reservation. Please try again later.');
      }
    });
  }

  unconfirmReservation(hashId: string): void {
    this.reservationService.unconfirm(hashId).subscribe({
      next: () => {
        this.notificationService.showSuccess('Reservation unconfirmed successfully');
        this.loadReservations();
      },
      error: error => {
        this.notificationService.showError('Failed to unconfirm reservation. Please try again later.');
      }
    });
  }

}
