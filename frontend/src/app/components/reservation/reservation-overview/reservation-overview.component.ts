import {Component, OnInit} from '@angular/core';
import * as bootstrap from 'bootstrap';
import {AuthService} from '../../../services/auth.service';
import {
  PermanentReservationDetailDto,
  PermanentReservationListDto,
  permanentReservationSearch,
  RepetitionEnum,
  ReservationEditDto,
  ReservationListDto,
  ReservationModalDetailDto,
  ReservationSearch
} from "../../../dtos/reservation";
import {debounceTime, interval, Observable, Subject, Subscription} from "rxjs";
import {ReservationService} from "../../../services/reservation.service";
import {HttpResponse} from "@angular/common/http";
import {NotificationService} from "../../../services/notification.service";
import moment from 'moment';
import {Router} from "@angular/router";
import {formatDay, formatDotDate, formatIsoTime, formatTime} from "../../../util/date-helper";
import {NavigationStateService} from "../../../services/navigation-state.service";

@Component({
  selector: 'app-reservation-overview',
  templateUrl: './reservation-overview.component.html',
  styleUrls: ['./reservation-overview.component.scss']
})
export class ReservationOverviewComponent implements OnInit {
  reservations: ReservationListDto[] = [];
  reservationModalDetailDto: ReservationModalDetailDto = {
    firstName: undefined,
    lastName: undefined,
    startTime: undefined,
    endTime: undefined,
    notes: undefined,
    placeIds: undefined,
    specialOffers: undefined
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
  deleteWhat: ReservationEditDto | PermanentReservationDetailDto | null = null;
  untouched: boolean = true;
  fetchIntervalSubscription!: Subscription;

  currentPage = 1;
  pageSize = 25;
  totalUpcomingReservations = 0;
  isPermanentView: boolean = false;  // To toggle between views
  permanentReservations: PermanentReservationListDto[] = [];  // To store permanent reservations
  permanentSearchParams: permanentReservationSearch = {};
  fromPermanentDetail = false;
  showPermanentReservation = false;

  constructor(
    private authService: AuthService,
    private reservationService: ReservationService,
    private notificationService: NotificationService,
    private router: Router,
    private navigationStateService: NavigationStateService) {
  }

  ngOnInit(): void {
    const state = this.navigationStateService.getNavigationState();
    this.showPermanentReservation = state.showPermanentReservations || false;
    if (this.showPermanentReservation) {
      this.isPermanentView = true;
      this.loadPermanentReservations();
    } else {
      this.loadReservations();
    }
    this.searchChangedObservable.pipe(debounceTime(100)).subscribe(() => this.loadReservations());

    // Set up the interval to reload reservations every 2 minutes
    this.fetchIntervalSubscription = interval(240000).subscribe(() => {
      if (this.isPermanentView) {
        this.loadPermanentReservations();
      } else {
        this.loadReservations();
      }
    });
  }

  ngOnDestroy(): void {
    if (this.fetchIntervalSubscription) {
      this.fetchIntervalSubscription.unsubscribe();
    }
  }

  getPositivePlaceIds(placeIds: number[]): number[] {
    return placeIds?.map(id => Math.abs(id)) || [];
  }

  isAdmin(): boolean {
    return this.authService.getUserRole() === 'ADMIN';
  }

  isEmployee(): boolean {
    return this.authService.getUserRole() === 'EMPLOYEE';
  }

  isAdminOrEmployee(): boolean {
    const role = this.authService.getUserRole();
    return role === 'ADMIN' || role === 'EMPLOYEE';
  }

  hasSpecialOffer(): boolean {
    return this.reservationModalDetailDto.specialOffers != null && this.reservationModalDetailDto.specialOffers.length > 0;
  }

  toggleView(): void {
    this.isPermanentView = !this.isPermanentView;
    if (this.isPermanentView) {
      this.loadPermanentReservations();
    } else {
      this.loadReservations();
    }
  }

  loadPermanentReservations(): void {
    if (!this.isAdminOrEmployee()) {
      this.permanentSearchParams.userId = this.authService.getCurrentUser()?.id;
    }
    const today = moment().startOf('day');
    const nextWeek = moment().add(7, 'days').endOf('day');

    if (this.searchEarliestDate == null || this.searchEarliestDate === "") {
      this.permanentSearchParams.earliestDate = this.isAdminOrEmployee() ? null : today.toDate();
    } else {
      this.permanentSearchParams.earliestDate = new Date(this.searchEarliestDate);
    }

    if (this.searchLatestDate == null || this.searchLatestDate === "") {
      this.permanentSearchParams.latestDate = null;
    } else {
      this.permanentSearchParams.latestDate = new Date(this.searchLatestDate);
    }

    if (this.searchEarliestStartTime == null || this.searchEarliestStartTime === "") {
      this.permanentSearchParams.earliestStartTime = null;
    } else {
      this.permanentSearchParams.earliestStartTime = this.searchEarliestStartTime;
    }

    if (this.searchLatestEndTime == null || this.searchLatestEndTime === "") {
      this.permanentSearchParams.latestEndTime = null;
    } else {
      this.permanentSearchParams.latestEndTime = this.searchLatestEndTime;
    }
    this.reservationService.getPermanentReservations(this.permanentSearchParams)
      .subscribe({
        next: (reservations: PermanentReservationListDto[]) => {
          this.permanentReservations = reservations;
          // Potentially process or filter reservations as needed
        },
        error: error => {
          this.notificationService.showError('Failed to load permanent reservations. Please try again later.');
        }
      });
  }

  getFrequency(permanentReservationListDto: PermanentReservationListDto): String {
    if (permanentReservationListDto.repetition === RepetitionEnum.DAYS) {
      if (permanentReservationListDto.period == 1) {
        return 'Daily'
      } else {
        return 'every ' + permanentReservationListDto.period + ' days'
      }
    } else if (permanentReservationListDto.repetition === RepetitionEnum.WEEKS) {
      if (permanentReservationListDto.period == 1) {
        return 'Weekly'
      } else {
        return 'every ' + permanentReservationListDto.period + ' weeks'
      }
    }
  }

  loadReservations() {
    const today = moment().startOf('day');
    const nextWeek = moment().add(7, 'days').endOf('day');

    if (this.searchEarliestDate == null || this.searchEarliestDate === "") {
      this.searchParams.earliestDate = this.isAdminOrEmployee() ? null : today.toDate();
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
          if (this.isAdminOrEmployee()) {
            for (let i = 0; i < this.reservations.length; i++) {
              if (this.reservations[i].userFirstName == 'WalkIn') {
                this.reservations[i].confirmed = true;
              }
            }
          }
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
    if (this.isPermanentView) {
      this.reservationService.getPermanentReservationDetailsByHashedId(hashId).subscribe({
        next: data => {
          this.deleteWhat = data;
        },
        error: error => {
          this.notificationService.showError('Failed to load permanent reservation details. Please try again later.');
        }
      });
    } else {
      this.reservationService.getByHashedId(hashId).subscribe({
        next: data => {
          this.deleteWhat = data;
        },
        error: error => {
          this.notificationService.showError('Failed to load reservation details. Please try again later.');
        }
      });
    }
  }

  showReservationDetails(hashId: string): void {
    this.reservationService.getModalDetail(hashId).subscribe({
      next: (data: ReservationModalDetailDto) => {
        this.reservationModalDetailDto.firstName = data.firstName;
        this.reservationModalDetailDto.lastName = data.lastName;
        this.reservationModalDetailDto.startTime = data.startTime;
        this.reservationModalDetailDto.endTime = data.endTime;
        if (data.notes === null) {
          this.reservationModalDetailDto.notes = 'No notes';
        } else {
          this.reservationModalDetailDto.notes = data.notes;
        }
        this.reservationModalDetailDto.placeIds = data.placeIds;
        this.reservationModalDetailDto.specialOffers = data.specialOffers;

        const modalDetail = new bootstrap.Modal(document.getElementById('confirmation-dialog-reservation-detail'));
        modalDetail.show();
      },
      error: error => {
        this.notificationService.showError('Failed to load reservation details. Please try again later.');
      }
    });
  }

  onDelete(): void {
    if (!this.deleteWhat) {
      this.notificationService.showError('No reservation selected for deletion.');
      return;
    }

    if (this.isPermanentView) {
      let observable: Observable<HttpResponse<void>>;
      observable = this.reservationService.deletePermanent(this.deleteWhat.hashedId);
      observable.subscribe({
        next: (response) => {
          if (response.status === 204) {
            this.notificationService.showSuccess('Reservation cancelled successfully');
            this.loadPermanentReservations();
          }
        },
        error: (error) => {
          this.notificationService.showError('Failed to cancel reservation. Please try again later.');
        }
      });
    } else {
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
  }

  changePage(newPage: number): void {
    if (newPage < 1 || newPage > Math.ceil(this.totalUpcomingReservations / this.pageSize)) {
      return;
    }
    this.currentPage = newPage;
    this.displayReservations();
  }

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

    if (!this.isPermanentView) {
      this.loadReservations();
    } else if (this.isPermanentView) {
      this.loadPermanentReservations();
    }
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

  createNewReservationGuest() {
    // Navigate to the target page with a query parameter
    this.router.navigate(['reservation-simple'], {queryParams: {guestView: 'true'}});
  }

  createNewReservation() {
    // Navigate to the target page with a query parameter
    this.router.navigate(['reservation-simple']);
  }

  confirmPermanentReservation(id: string): void {
    this.reservationService.confirmPermanentReservation(id).subscribe({
      next: () => {
        this.notificationService.showSuccess('Permanent reservation confirmed successfully');
        this.loadPermanentReservations();
      },
      error: error => {
        this.notificationService.showError('Failed to confirm permanent reservation. Please try again later.');
      }
    });
  }

  protected readonly formatTime = formatTime;
  protected readonly formatDotDate = formatDotDate;
  protected readonly formatDay = formatDay;
  protected readonly Math = Math;
  protected readonly formatIsoTime = formatIsoTime;
}
