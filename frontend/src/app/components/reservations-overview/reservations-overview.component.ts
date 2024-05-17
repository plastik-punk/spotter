import {ChangeDetectorRef, Component, OnInit, TemplateRef, ViewChild, ViewChildren} from '@angular/core';
import {MessageService} from '../../services/message.service';
import {Message} from '../../dtos/message';
import {NgbModal, NgbPaginationConfig} from '@ng-bootstrap/ng-bootstrap';
import {UntypedFormBuilder, NgForm} from '@angular/forms';
import {AuthService} from '../../services/auth.service';
import {ReservationListDto, ReservationSearch} from "../../dtos/reservation";
import {debounceTime, Subject} from "rxjs";
import {ReservationService} from "../../services/reservation.service";

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

  constructor(
    private authService: AuthService,
    private modalService: NgbModal,
    private reservationService: ReservationService) {
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
      this.searchParams.earliestStartTime = new Date(this.searchEarliestStartTime);
    }
    if (this.searchLatestEndTime == null || this.searchLatestEndTime === "") {
      delete this.searchParams.latestEndTime;
    } else {
      this.searchParams.latestEndTime = new Date(this.searchLatestEndTime);
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
