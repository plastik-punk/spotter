import {Component, OnInit, OnDestroy, ElementRef, ViewChild, HostListener} from '@angular/core';
import * as bootstrap from 'bootstrap';
import {NgForm} from "@angular/forms";
import {
  ReservationLayoutCheckAvailabilityDto,
  ReservationCreateDto,
  AreaLayoutDto,
  AreaListDto,
  AreaDto
} from "../../../dtos/reservation";
import {UserOverviewDto} from "../../../dtos/app-user";
import {AuthService} from "../../../services/auth.service";
import {ReservationService} from "../../../services/reservation.service";
import {NotificationService} from "../../../services/notification.service";
import {D3DrawService} from "../../../services/d3-draw.service";
import {formatDay, formatDotDate, formatIsoDate, formatIsoTime} from "../../../util/date-helper";
import {SimpleViewReservationStatusEnum} from "../../../dtos/status-enum";
import {EventDetailDto, EventListDto} from "../../../dtos/event";
import {EventService} from "../../../services/event.service";

@Component({
  selector: 'app-component-reservation-layout',
  templateUrl: './reservation-layout.component.html',
  styleUrls: ['./reservation-layout.component.scss']
})
export class ReservationLayoutComponent implements OnInit, OnDestroy {

  @ViewChild('d3Container', {static: true}) d3Container: ElementRef;

  reservationCreateDto: ReservationCreateDto;
  reservationLayoutCheckAvailabilityDto: ReservationLayoutCheckAvailabilityDto;

  currentUser: UserOverviewDto;
  areaLayout: AreaLayoutDto;
  selectedPlaces: { placeId: number, numberOfSeats: number }[] = [];
  areas: AreaDto[] = [];
  selectedAreaId: number = 1;

  isPaxValid: boolean = true;
  timer: any;
  isTimeManuallyChanged: boolean = false;

  sharedStartTime: string;
  sharedDate: string;

  events: EventListDto[] = undefined;
  event: EventDetailDto = {
    hashId: undefined,
    name: undefined,
    startTime: undefined,
    endTime: undefined,
    description: undefined
  };
  currentEventPage: number = 1;
  itemsPerPage: number = 3;
  upcomingEventsExist: boolean = false;

  constructor(
    public authService: AuthService,
    private reservationService: ReservationService,
    private notificationService: NotificationService,
    private d3DrawService: D3DrawService,
    private eventService: EventService,
  ) {
    this.initializeSharedProperties();
    this.reservationCreateDto = this.initializeReservationCreateDto();
    this.reservationLayoutCheckAvailabilityDto = this.initializeReservationLayoutCheckAvailabilityDto();
  }

  ngOnInit() {
    this.fetchAllAreas();
    this.fetchLayoutAvailability();
    this.d3DrawService.createSeatingPlan(this.d3Container);
    this.onResize();
    this.startTimer();

    this.eventService.getUpcomingEvents().subscribe({
      next: (data) => {
        this.events = data;
        if (this.events?.length > 0) {
          this.upcomingEventsExist = true;
        }
      },
      error: () => {
        this.notificationService.showError('Failed to get events. Please try again later.');
      },
    });
  }


  showEventDetails(hashId: string): void {
    this.eventService.getByHashId(hashId).subscribe( {
      next: (data: EventDetailDto) => {
        this.event.name = data.name;
        this.event.startTime = data.startTime;
        this.event.endTime = data.endTime;
        this.event.description = data.description;

        const modalDetail = new bootstrap.Modal(document.getElementById('event-detail'));
        modalDetail.show();
      },
      error: error => {
        this.notificationService.showError('Failed to load reservation details. Please try again later.');
      }
    });
  }

  nextPage() {
    if (this.currentEventPage < Math.ceil(this.events?.length / this.itemsPerPage)) {
      this.currentEventPage++;
    }
  }

  previousPage() {
    if (this.currentEventPage > 1) {
      this.currentEventPage--;
    }
  }

  ngOnDestroy() {
    if (this.timer) {
      clearInterval(this.timer);
    }
  }

  private initializeSharedProperties() {
    const now = new Date();
    this.sharedStartTime = now.toTimeString().slice(0, 5);
    this.sharedDate = formatIsoDate(now);
  }

  private initializeReservationCreateDto(): ReservationCreateDto {
    return {
      user: undefined,
      // @ts-ignore
      startTime: this.sharedStartTime,
      endTime: undefined,
      // @ts-ignore
      date: this.sharedDate,
      pax: undefined,
      firstName: undefined,
      lastName: undefined,
      notes: undefined,
      email: undefined,
      mobileNumber: undefined,
      placeIds: []
    };
  }

  private initializeReservationLayoutCheckAvailabilityDto(): ReservationLayoutCheckAvailabilityDto {
    return {
      startTime: this.sharedStartTime,
      date: this.sharedDate,
      areaId: 1,
      idToExclude: -1
    };
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


  private fetchAllAreas() {
    this.reservationService.getAllAreas().subscribe({
      next: (data: AreaListDto) => {
        this.areas = data.areas;
        if (this.areas.length > 0) {
          this.selectedAreaId = this.selectedAreaId || this.areas[0].id;
          this.reservationLayoutCheckAvailabilityDto.areaId = this.selectedAreaId;
          this.fetchLayoutAvailability();
        }
      },
      error: () => {
        this.notificationService.showError('Failed to fetch areas. Please try again later.');
      },
    });
  }

  onAreaChange(event: Event) {
    const selectElement = event.target as HTMLSelectElement;
    this.selectedAreaId = Number(selectElement.value);
    this.reservationLayoutCheckAvailabilityDto.areaId = this.selectedAreaId;
    this.fetchLayoutAvailability();
  }

  @HostListener('window:resize', ['$event'])
  onResize() {
    this.d3DrawService.adjustSvgSize(this.d3Container);
  }

  private fetchLayoutAvailability() {
    this.reservationService.getLayoutAvailability(this.reservationLayoutCheckAvailabilityDto).subscribe({
      next: (data: AreaLayoutDto) => {
        this.areaLayout = data;
        this.d3DrawService.updateSeatingPlan(this.d3Container, this.areaLayout, this.selectedPlaces, this.onPlaceClick.bind(this), false);
        this.checkSelectedPlacesAvailability();
      },
      error: () => {
        this.notificationService.showError('Failed to fetch layout availability. Please try again later.');
      },
    });
  }

  private checkSelectedPlacesAvailability() {
    const unavailablePlaces = this.selectedPlaces.filter(selectedPlace => {
      const place = this.areaLayout.placeVisuals.find(p => p.placeId === selectedPlace.placeId);
      return !place || place.reservation || !place.status;
    });

    if (unavailablePlaces.length > 0) {
      unavailablePlaces.forEach(unavailablePlace => {
        this.selectedPlaces = this.selectedPlaces.filter(p => p.placeId !== unavailablePlace.placeId);
        this.notificationService.showError(`Table ${unavailablePlace.placeId} is no longer available.`);
      });
      this.d3DrawService.updateSeatingPlan(this.d3Container, this.areaLayout, this.selectedPlaces, this.onPlaceClick.bind(this), false);
    }
  }

  private onPlaceClick(placeId: number, numberOfSeats: number) {
    const index = this.selectedPlaces.findIndex(p => p.placeId === placeId);
    if (index !== -1) {
      this.selectedPlaces.splice(index, 1);
    } else {
      this.selectedPlaces.push({placeId, numberOfSeats});
    }

    this.reservationCreateDto.placeIds = this.selectedPlaces.map(p => p.placeId);
    this.reservationCreateDto.pax = this.selectedPlaces.reduce((sum, place) => sum + place.numberOfSeats, 0);
    this.d3DrawService.updateSeatingPlan(this.d3Container, this.areaLayout, this.selectedPlaces, this.onPlaceClick.bind(this), false);
    this.updateTotalSeats();

    const paxInput = document.getElementById('reservationPax') as HTMLInputElement;
    if (paxInput) {
      paxInput.value = this.reservationCreateDto.pax.toString();
    }
  }

  private updateTotalSeats() {
    const totalSeats = this.selectedPlaces.reduce((sum, place) => sum + place.numberOfSeats, 0);
    console.log(`Total available seats: ${totalSeats}`);
    this.reservationCreateDto.pax = totalSeats;
  }

  onFieldChange() {
    this.updateCheckAvailabilityDto();
    this.updateReservationCreateDto();
    this.fetchLayoutAvailability();

    const totalSeats = this.selectedPlaces.reduce((sum, place) => sum + place.numberOfSeats, 0);
    if (this.reservationCreateDto.pax > totalSeats) {
      this.notificationService.showError(`The selected places only have ${totalSeats} seats.`);
      this.reservationCreateDto.pax = totalSeats;
      this.isPaxValid = false;
    } else if (this.reservationCreateDto.pax < this.selectedPlaces.length) {
      this.notificationService.showError(`Not enough people for ${this.selectedPlaces.length} tables.`);
      this.isPaxValid = false;
    } else {
      this.isPaxValid = true;
    }
  }

  private updateCheckAvailabilityDto() {
    this.reservationLayoutCheckAvailabilityDto.startTime = this.sharedStartTime;
    this.reservationLayoutCheckAvailabilityDto.date = this.sharedDate;
  }

  onSubmit(form: NgForm) {
    this.updateReservationCreateDto();
    this.updateCheckAvailabilityDto()

    if (this.authService.isLoggedIn()) {
      this.setCurrentUserDetails();
    }

    const totalSeats = this.selectedPlaces.reduce((sum, place) => sum + place.numberOfSeats, 0);
    if (this.reservationCreateDto.pax > totalSeats) {
      this.notificationService.showError(`The selected places only have ${totalSeats} seats.`);
      return;
    }

    if (form.valid) {
      this.createReservation(form);
      this.isPaxValid = false;
      setTimeout(() => {
        this.isPaxValid = true;
        this.isTimeManuallyChanged = false;
      }, 2000);
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

  private createReservation(form: NgForm) {
    this.reservationService.createReservation(this.reservationCreateDto).subscribe({
      next: (data) => {
        if (data == null) {
          this.notificationService.showError('Location Closed');
        } else {
          this.notificationService.showSuccess('Reservation created successfully.');
          this.resetForm(form);
        }
      },
      error: () => {
        this.notificationService.showError('Failed to create reservation. Please try again later.');
      },
    });
  }

  private resetForm(form: NgForm) {
    this.initializeSharedProperties();
    this.fetchAllAreas();
    this.fetchLayoutAvailability();
    this.reservationLayoutCheckAvailabilityDto = this.initializeReservationLayoutCheckAvailabilityDto();
    this.reservationCreateDto = this.initializeReservationCreateDto();
    this.selectedPlaces = [];
    this.d3DrawService.updateSeatingPlan(this.d3Container, this.areaLayout, this.selectedPlaces, this.onPlaceClick.bind(this), false);
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

  private updateReservationCreateDto() {
    // @ts-ignore
    this.reservationCreateDto.startTime = this.sharedStartTime;
    // @ts-ignore
    this.reservationCreateDto.date = this.sharedDate;
  }

  onTimeChange(event: Event) {
    this.isTimeManuallyChanged = true;
    clearInterval(this.timer);
    this.sharedStartTime = (event.target as HTMLInputElement).value;
    this.onFieldChange();
  }

  protected readonly SimpleViewReservationStatusEnum = SimpleViewReservationStatusEnum;
  protected readonly formatDay = formatDay;
  protected readonly formatDotDate = formatDotDate;
  protected readonly Math = Math;
  protected readonly formatIsoTime = formatIsoTime;
}
