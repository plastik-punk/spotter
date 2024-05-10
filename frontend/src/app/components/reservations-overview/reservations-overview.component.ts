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
export class ReservationsOverviewComponent implements OnInit{
  error = false;
  errorMessage = '';
  reservations: ReservationListDto[] = [];
  displayedReservations: ReservationListDto[] = [];
  searchParams: ReservationSearch = {};
  searchLatest: string | null = null;
  searchEarliest: string | null = null;
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

  private loadReservations() {
    if (this.searchEarliest == null || this.searchEarliest === "") {
      delete this.searchParams.earliestStartDate;
    } else {
      this.searchParams.earliestStartDate = new Date(this.searchEarliest);
    }
    if (this.searchLatest == null || this.searchLatest === "") {
      delete this.searchParams.latestStartDate;
    } else {
      this.searchParams.latestStartDate = new Date(this.searchLatest);
    }
    this.reservationService.search(this.searchParams)
      .subscribe({
        next: (reservation: ReservationListDto[]) => {
          this.reservations = reservation;
        },
        error: error => {
          this.defaultServiceErrorHandling(error);
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
