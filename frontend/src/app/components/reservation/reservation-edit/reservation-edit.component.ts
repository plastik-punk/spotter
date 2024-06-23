import {Component, OnInit} from '@angular/core';
import {FormsModule, NgForm} from "@angular/forms";
import {ReservationCheckAvailabilityDto, ReservationEditDto} from "../../../dtos/reservation";
import {ReservationService} from "../../../services/reservation.service";
import {AuthService} from "../../../services/auth.service";
import {Observable} from "rxjs";
import {SimpleViewReservationStatusEnum} from "../../../dtos/status-enum";
import {ActivatedRoute, Router} from "@angular/router";
import {ToastrService} from "ngx-toastr";
import {NotificationService} from "../../../services/notification.service";
import {NgIf} from "@angular/common";
import {SpecialOfferAmountDto, SpecialOfferListDto} from "../../../dtos/special-offer";
import {SpecialOfferService} from "../../../services/special-offer.service";

@Component({
  selector: 'app-reservation-edit',
  templateUrl: './reservation-edit.component.html',
  styleUrls: ['./reservation-edit.component.scss']
})

export class ReservationEditComponent implements OnInit {
  hashId: string;
  showSpecialOffers: boolean = false;
  allSpecialOffers: SpecialOfferListDto[]

  reservationEditDto: ReservationEditDto = {
    date: undefined,
    endTime: undefined,
    hashedId: undefined,
    notes: undefined,
    pax: undefined,
    placeIds: undefined,
    reservationId: undefined,
    startTime: undefined,
    user: {
      id: undefined,
      email: undefined,
      firstName: undefined,
      lastName: undefined,
      username: undefined,
      mobileNumber: undefined,
      password: undefined,
      role: undefined
    },
    specialOffers: []
  };
  reservationCheckAvailabilityDto: ReservationCheckAvailabilityDto = {
    startTime: undefined,
    endTime: undefined,
    date: undefined,
    pax: undefined,
    idToExclude: undefined
  }

  enumReservationTableStatus = SimpleViewReservationStatusEnum;
  reservationStatusText: string = 'Provide Time, Date and Pax';
  reservationStatusClass: string = 'reservation-table-incomplete';

  constructor(
    public authService: AuthService,
    private service: ReservationService,
    private specialOfferService: SpecialOfferService,
    private route: ActivatedRoute,
    private notification: ToastrService,
    private notificationService: NotificationService,
    private router: Router
  ) {
  } // constructor

  ngOnInit() {
    this.fetchAllOffers();
    // 1. get reservation id from service
    this.hashId = this.route.snapshot.paramMap.get('id');
    if (this.hashId) {
      // 2. load data via ID from BE and set it to reservationEditDto
      let observable: Observable<ReservationEditDto>;
      observable = this.service.getByHashedId(this.hashId);
      observable.subscribe({
        next: (data) => {
          if (data != null) {
            this.reservationEditDto = data;
          }
        },
        error: (error) => {
          this.notificationService.handleError(error);
          this.router.navigate(['/reservation-overview']);
        },
      });
    }
  }

  onSubmit(form: NgForm) {
    if (form.valid) {
      let observable: Observable<ReservationEditDto>;
      observable = this.service.update(this.reservationEditDto);
      observable.subscribe({
        next: (data) => {
          this.notification.success("Reservation updated successfully");
          if (this.authService.isLoggedIn()) {
            this.router.navigate(['/reservation-overview']);
          }
        },
        error: (error) => {
          this.notificationService.handleError(error);
        },
      });
    }
  }

  onFieldChange() {
    // 1. update DTO with current content of form
    this.reservationCheckAvailabilityDto.startTime = this.reservationEditDto.startTime;
    this.reservationCheckAvailabilityDto.endTime = this.reservationEditDto.endTime;
    this.reservationCheckAvailabilityDto.date = this.reservationEditDto.date;
    this.reservationCheckAvailabilityDto.pax = this.reservationEditDto.pax;
    this.reservationCheckAvailabilityDto.idToExclude = this.reservationEditDto.reservationId;

    if (this.reservationCheckAvailabilityDto.startTime == null || this.reservationCheckAvailabilityDto.pax == null || this.reservationCheckAvailabilityDto.date == null) {
      this.reservationStatusText = 'Provide Time, Date and Pax';
      this.reservationStatusClass = 'reservation-table-incomplete';
      return;
    }

    // 2. send request to backend
    this.service.getAvailability(this.reservationCheckAvailabilityDto).subscribe({
      next: (data) => {
        // update reservationTableStatus based on the data received from the backend
        if (data.valueOf() === this.enumReservationTableStatus.available.valueOf()) {
          this.reservationStatusText = 'Tables available';
          this.reservationStatusClass = 'reservation-table-available';
        } else if (data.valueOf() === this.enumReservationTableStatus.closed.valueOf()) {
          this.reservationStatusText = 'Location Closed This Day';
          this.reservationStatusClass = 'reservation-table-conflict';
        } else if (data.valueOf() === this.enumReservationTableStatus.outsideOpeningHours.valueOf()) {
          this.reservationStatusText = 'Outside Of Opening Hours';
          this.reservationStatusClass = 'reservation-table-conflict';
        } else if (data.valueOf() === this.enumReservationTableStatus.respectClosingHour.valueOf()) {
          this.reservationStatusText = 'Respect Closing Hour';
          this.reservationStatusClass = 'reservation-table-conflict';
        } else if (data.valueOf() === this.enumReservationTableStatus.tooManyPax.valueOf()) {
          this.reservationStatusText = 'Too Many Pax for available tables (try advanced reservation)';
          this.reservationStatusClass = 'reservation-table-conflict';
        } else if (data.valueOf() === this.enumReservationTableStatus.allOccupied.valueOf()) {
          this.reservationStatusText = 'All Tables Occupied';
          this.reservationStatusClass = 'reservation-table-conflict';
        } else if (data.valueOf() === this.enumReservationTableStatus.dateInPast.valueOf()) {
          this.reservationStatusText = 'Date In The Past';
          this.reservationStatusClass = 'reservation-table-conflict';
        }
      },
      error: (error) => {
        // TODO: error and notification handling
        console.error("Error Processing Reservation", error);
      },
    });
  } // onFieldChange

  removeSpecialOffer(index: number) {
    let SpecialOfferAmountDto = this.reservationEditDto.specialOffers[index];
    if (SpecialOfferAmountDto.amount > 1) {
      SpecialOfferAmountDto.amount--;
    } else {
      this.reservationEditDto.specialOffers.splice(index, 1);
    }
  }

  addSpecialOffer(index: number) {
    let SpecialOfferAmountDto = this.reservationEditDto.specialOffers[index];
    SpecialOfferAmountDto.amount++;
  }

  selectOffer(offerId: number) {
    //if the offer is already in the list, increase the amount
    let found = false;
    for (let i = 0; i < this.reservationEditDto.specialOffers.length; i++) {
      if (this.reservationEditDto.specialOffers[i].specialOffer.id === offerId) {
        this.reservationEditDto.specialOffers[i].amount++;
        found = true;
        break;
      }
    }
    //if the offer is not in the list, add it
    if (!found) {
      let specialOfferAmountDto: SpecialOfferAmountDto = {
        specialOffer: this.allSpecialOffers.find(offer => offer.id === offerId),
        amount: 1
      }
      this.reservationEditDto.specialOffers.push(specialOfferAmountDto);
    }
  }

  private fetchAllOffers() {
    this.specialOfferService.getSpecialOffers().subscribe({
      next: (data) => {
        this.allSpecialOffers = data;
      },
      error: (error) => {
        this.notificationService.handleError(error);
      },
    });
  }
}
