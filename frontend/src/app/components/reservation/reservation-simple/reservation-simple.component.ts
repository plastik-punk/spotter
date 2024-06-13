import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../../services/auth.service';
import { NgForm } from '@angular/forms';
import { ReservationCheckAvailabilityDto, ReservationCreateDto } from '../../../dtos/reservation';
import { UserOverviewDto } from '../../../dtos/app-user';
import { ReservationService } from '../../../services/reservation.service';
import { NotificationService } from '../../../services/notification.service';
import { SimpleViewReservationStatusEnum } from '../../../dtos/status-enum';
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-reservation-simple',
  templateUrl: './reservation-simple.component.html',
  styleUrls: ['./reservation-simple.component.scss']
})
export class ReservationSimpleComponent implements OnInit {
  unavailable: boolean = true;
  nextAvailableTables: ReservationCheckAvailabilityDto[] = [];

  reservationCreateDto: ReservationCreateDto;
  reservationCheckAvailabilityDto: ReservationCheckAvailabilityDto;

  currentUser: UserOverviewDto;

  sharedStartTime: string;
  sharedDate: string;

  timer: any;
  isTimeManuallyChanged: boolean = false;
  isBookButtonTimeout: boolean = false;


  isGuestView = false;
  reservationStatusText: string = 'Provide Time, Date and Pax';
  reservationStatusClass: string = 'reservation-table-incomplete';

  constructor(
    private route: ActivatedRoute,
    public authService: AuthService,
    private service: ReservationService,
    private notificationService: NotificationService,
  ) {
    this.initializeSharedProperties();
    this.initializeDtos();
  }

  ngOnInit() {
    this.route.queryParams.subscribe(params => {
      // Override logged-in status based on query parameter
      this.isGuestView = params['guestView'] === 'true';});
    this.startTimer()
  }
  ngOnDestroy() {
    if (this.timer) {
      clearInterval(this.timer);
    }
  }
  private startTimer() {
    this.sharedStartTime = new Date().toTimeString().slice(0, 5);

    const now = new Date();
    const nextMinute = new Date(now.getFullYear(), now.getMonth(), now.getDate(), now.getHours(), now.getMinutes() + 1);
    const timeUntilNextMinute = nextMinute.getTime() - now.getTime();

    setTimeout(() => {
      this.timer = setInterval(() => {
        if (!this.isTimeManuallyChanged) {
          this.sharedStartTime = new Date().toTimeString().slice(0, 5);
        }
      }, 60000);

      if (!this.isTimeManuallyChanged) {
        this.sharedStartTime = new Date().toTimeString().slice(0, 5);
      }

    }, timeUntilNextMinute);

    this.onFieldChange();
  }

  private initializeSharedProperties() {
    const now = new Date();
    this.sharedStartTime = now.toTimeString().slice(0, 5);
    this.sharedDate = now.toISOString().split('T')[0];
  }

  private initializeDtos() {
    this.reservationCreateDto = {
      user: undefined,
      //@ts-ignore
      startTime: this.sharedStartTime,
      endTime: undefined,
      //@ts-ignore
      date: this.sharedDate,
      pax: undefined,
      firstName: undefined,
      lastName: undefined,
      notes: undefined,
      email: undefined,
      mobileNumber: undefined
    };

    this.reservationCheckAvailabilityDto = {
      //@ts-ignore
      startTime: this.sharedStartTime,
      //@ts-ignore
      date: this.sharedDate,
      pax: undefined,
      idToExclude: -1
    };
  }

  onFieldChange() {
    this.updateCheckAvailabilityDto();
    this.updateReservationCreateDto();
    this.checkAvailability();
  }

  private updateCheckAvailabilityDto() {
    //@ts-ignore
    this.reservationCheckAvailabilityDto.startTime = this.sharedStartTime;
    //@ts-ignore
    this.reservationCheckAvailabilityDto.date = this.sharedDate;
    this.reservationCheckAvailabilityDto.pax = this.reservationCreateDto.pax;
  }

  private updateReservationCreateDto() {
    //@ts-ignore
    this.reservationCreateDto.startTime = this.sharedStartTime;
    //@ts-ignore
    this.reservationCreateDto.date = this.sharedDate;
  }

  private checkAvailability() {
    this.unavailable = false;
    this.nextAvailableTables = [];

    if (!this.reservationCheckAvailabilityDto.startTime || !this.reservationCheckAvailabilityDto.pax || !this.reservationCheckAvailabilityDto.date) {
      this.reservationStatusText = 'Provide Time, Date and Pax';
      this.reservationStatusClass = 'reservation-table-incomplete';
      return;
    }

    this.service.getAvailability(this.reservationCheckAvailabilityDto).subscribe({
      next: (data) => {
        if (data.valueOf() === SimpleViewReservationStatusEnum.available.valueOf()) {
          this.reservationStatusText = 'Tables available';
          this.reservationStatusClass = 'reservation-table-available';
        } else {
          this.handleAvailabilityStatus(data);
          if (this.unavailable) {
            this.fetchNextAvailableTables();
          }
        }
      },
      error: () => {
        this.notificationService.showError('Failed to check availability. Please try again later.');
      },
    });
  }

  private handleAvailabilityStatus(status: SimpleViewReservationStatusEnum) {
    switch (status) {
      case SimpleViewReservationStatusEnum.closed:
        this.reservationStatusText = 'Location Closed This Day';
        this.reservationStatusClass = 'reservation-table-conflict';
        this.unavailable = true;
        break;
      case SimpleViewReservationStatusEnum.outsideOpeningHours:
        this.reservationStatusText = 'Outside Of Opening Hours';
        this.reservationStatusClass = 'reservation-table-conflict';
        this.unavailable = true;
        break;
      case SimpleViewReservationStatusEnum.respectClosingHour:
        this.reservationStatusText = 'Respect Closing Hour';
        this.reservationStatusClass = 'reservation-table-conflict';
        this.unavailable = true;
        break;
      case SimpleViewReservationStatusEnum.tooManyPax:
        this.reservationStatusText = 'Too Many Pax for available tables (try advanced reservation)';
        this.reservationStatusClass = 'reservation-table-conflict';
        this.unavailable = true;
        break;
      case SimpleViewReservationStatusEnum.allOccupied:
        this.reservationStatusText = 'All Tables Occupied';
        this.reservationStatusClass = 'reservation-table-conflict';
        this.unavailable = true;
        break;
      case SimpleViewReservationStatusEnum.dateInPast:
        this.reservationStatusText = 'Date In The Past';
        this.reservationStatusClass = 'reservation-table-conflict';
        this.unavailable = true;
        break;
      default:
        this.unavailable = false;
        break;
    }
  }

  private fetchNextAvailableTables() {
    this.service.getNextAvailableTables(this.reservationCheckAvailabilityDto).subscribe({
      next: (data) => {
        this.nextAvailableTables = data;
        if (data.length < 1 && !SimpleViewReservationStatusEnum.tooManyPax) {
          this.notificationService.showError('No tables available anymore for this day. Please try another.');
        }
      },
      error: () => {
        this.notificationService.showError('Failed to get next available tables. Please try again later.');
      },
    });
  }


  onSubmit(form: NgForm) {
    if (this.authService.isLoggedIn()) {
      this.setCurrentUserDetails();
    }

    if (form.valid) {
      this.isBookButtonTimeout = true;
      setTimeout(() => {
        this.isBookButtonTimeout = false;
        this.isTimeManuallyChanged = false;
      }, 2000);
      this.service.createReservation(this.reservationCreateDto).subscribe({
        next: (data) => {
          if (data == null) {
            this.notificationService.showError('The table was booked in the meantime. Please try again.');
          } else {
            this.notificationService.showSuccess('Reservation created successfully.');
            this.initializeSharedProperties();
            this.initializeDtos();
          }
        },
        error: () => {
          this.notificationService.showError('Location Closed');
        },
      });
    } else {
      this.showFormErrors();
    }
  }

  private setCurrentUserDetails() {
    this.currentUser = this.authService.getCurrentUser();
    this.reservationCreateDto.firstName = this.currentUser.firstName;
    this.reservationCreateDto.lastName = this.currentUser.lastName;
    this.reservationCreateDto.email = this.currentUser.email;
    this.reservationCreateDto.mobileNumber = Number(this.currentUser.mobileNumber);
  }

  selectAvailable(availabilityDto: ReservationCheckAvailabilityDto) {
    this.reservationCreateDto.date = availabilityDto.date;
    this.reservationCreateDto.startTime = availabilityDto.startTime;
    this.onFieldChange();
  }

  private resetForm(form: NgForm) {
    form.resetForm();
    this.initializeSharedProperties();
    this.initializeDtos();
  }

  private showFormErrors() {
    if (!this.reservationCreateDto.startTime) {
      this.notificationService.showError('Start time is required.');
    }
    if (!this.reservationCreateDto.date) {
      this.notificationService.showError('Date is required.');
    }
    if (!this.reservationCreateDto.pax) {
      this.notificationService.showError('Number of people (pax) is required.');
    }
  }
}
