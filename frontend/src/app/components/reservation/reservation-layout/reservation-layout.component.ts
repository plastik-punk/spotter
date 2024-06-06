import { Component, OnInit, ElementRef, ViewChild, HostListener } from '@angular/core';
import { NgForm } from "@angular/forms";
import {
  ReservationLayoutCheckAvailabilityDto,
  ReservationCreateDto,
  AreaLayoutDto,
  PlaceVisualDto,
  CoordinateDto,
  AreaListDto,
  AreaDto
} from "../../../dtos/reservation";
import { UserOverviewDto } from "../../../dtos/app-user";
import { AuthService } from "../../../services/auth.service";
import { ReservationService } from "../../../services/reservation.service";
import { NotificationService } from "../../../services/notification.service";
import { D3DrawService } from "../../../services/d3-draw.service";
import { formatIsoDate } from "../../../util/date-helper";
import { Observable } from "rxjs";

@Component({
  selector: 'app-component-reservation-layout',
  templateUrl: './reservation-layout.component.html',
  styleUrls: ['./reservation-layout.component.scss']
})
export class ReservationLayoutComponent implements OnInit {

  @ViewChild('d3Container', { static: true }) d3Container: ElementRef;

  reservationCreateDto: ReservationCreateDto = this.initializeReservationCreateDto();
  reservationLayoutCheckAvailabilityDto: ReservationLayoutCheckAvailabilityDto = this.initializeReservationLayoutCheckAvailabilityDto();

  currentUser: UserOverviewDto;
  areaLayout: AreaLayoutDto;
  selectedPlaces: { placeId: number, numberOfSeats: number }[] = [];
  areas: AreaDto[] = [];
  selectedAreaId: number = 1;

  isPaxValid: boolean = true;

  constructor(
    public authService: AuthService,
    private service: ReservationService,
    private notificationService: NotificationService,
    private d3DrawService: D3DrawService
  ) { }

  ngOnInit() {
    this.fetchAllAreas();
    this.fetchLayoutAvailability();
    this.d3DrawService.createSeatingPlan(this.d3Container);
    this.onResize();
  }

  private initializeReservationCreateDto(): ReservationCreateDto {
    return {
      user: undefined,
      startTime: undefined,
      endTime: undefined,
      date: undefined,
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
    const now = formatIsoDate(new Date());
    const nowTime = new Date().toTimeString().slice(0, 5);
    return {
      startTime: nowTime,
      date: now,
      areaId: 1,
      idToExclude: -1
    };
  }

  private fetchAllAreas() {
    this.service.getAllAreas().subscribe({
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
    this.service.getLayoutAvailability(this.reservationLayoutCheckAvailabilityDto).subscribe({
      next: (data: AreaLayoutDto) => {
        this.areaLayout = data;
        this.d3DrawService.updateSeatingPlan(this.d3Container, this.areaLayout, this.selectedPlaces, this.onPlaceClick.bind(this));
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
      this.d3DrawService.updateSeatingPlan(this.d3Container, this.areaLayout, this.selectedPlaces, this.onPlaceClick.bind(this));
    }
  }

  private onPlaceClick(placeId: number, numberOfSeats: number) {
    const index = this.selectedPlaces.findIndex(p => p.placeId === placeId);
    if (index !== -1) {
      this.selectedPlaces.splice(index, 1);
    } else {
      this.selectedPlaces.push({ placeId, numberOfSeats });
    }

    this.reservationCreateDto.placeIds = this.selectedPlaces.map(p => p.placeId);
    this.reservationCreateDto.pax = this.selectedPlaces.reduce((sum, place) => sum + place.numberOfSeats, 0);
    this.d3DrawService.updateSeatingPlan(this.d3Container, this.areaLayout, this.selectedPlaces, this.onPlaceClick.bind(this));
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
    this.reservationLayoutCheckAvailabilityDto.startTime = this.reservationCreateDto.startTime?.toString() || this.reservationLayoutCheckAvailabilityDto.startTime;
    this.reservationLayoutCheckAvailabilityDto.date = this.reservationCreateDto.date?.toString() || this.reservationLayoutCheckAvailabilityDto.date;
    this.reservationLayoutCheckAvailabilityDto.areaId = this.selectedAreaId;
  }

  onSubmit(form: NgForm) {
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
    this.service.createReservation(this.reservationCreateDto).subscribe({
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
    form.resetForm();
    this.reservationCreateDto = this.initializeReservationCreateDto();
    this.selectedPlaces = [];
    this.d3DrawService.updateSeatingPlan(this.d3Container, this.areaLayout, this.selectedPlaces, this.onPlaceClick.bind(this));
    this.reservationLayoutCheckAvailabilityDto = this.initializeReservationLayoutCheckAvailabilityDto();
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
