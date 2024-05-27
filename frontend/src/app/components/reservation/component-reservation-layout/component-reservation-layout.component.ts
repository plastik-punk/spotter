import { Component, OnInit, ElementRef, ViewChild } from '@angular/core';
import { FormsModule, NgForm, ReactiveFormsModule } from "@angular/forms";
import { NgIf } from "@angular/common";
import { RouterLink } from "@angular/router";
import { ReservationCheckAvailabilityDto, ReservationCreateDto } from "../../../dtos/reservation";
import { UserOverviewDto } from "../../../dtos/app-user";
import { SimpleViewReservationStatusEnum } from "../../../dtos/status-enum";
import { AuthService } from "../../../services/auth.service";
import { ReservationService } from "../../../services/reservation.service";
import { NotificationService } from "../../../services/notification.service";
import * as d3 from 'd3';

@Component({
  selector: 'app-component-reservation-layout',
  standalone: true,
  imports: [
    FormsModule,
    NgIf,
    ReactiveFormsModule,
    RouterLink
  ],
  templateUrl: './component-reservation-layout.component.html',
  styleUrls: ['./component-reservation-layout.component.scss']
})
export class ReservationLayoutComponent implements OnInit {

  @ViewChild('d3Container', { static: true }) d3Container: ElementRef;

  reservationCreateDto: ReservationCreateDto = {
    user: undefined,
    startTime: undefined,
    endTime: undefined,
    date: undefined,
    pax: undefined,
    firstName: undefined,
    lastName: undefined,
    notes: undefined,
    email: undefined,
    mobileNumber: undefined
  };

  reservationCheckAvailabilityDto: ReservationCheckAvailabilityDto = {
    startTime: undefined,
    date: undefined,
    pax: undefined,
    idToExclude: -1
  }

  currentUser: UserOverviewDto;

  enumReservationTableStatus = SimpleViewReservationStatusEnum;
  reservationStatusText: string = 'Provide Time, Date and Pax';
  reservationStatusClass: string = 'reservation-table-incomplete';

  constructor(
    public authService: AuthService,
    private service: ReservationService,
    private notificationService: NotificationService,
  ) {
  }

  ngOnInit() {
    this.createSeatingPlan();
  }

  createSeatingPlan() {
    const svg = d3.select(this.d3Container.nativeElement)
      .append('svg')
      .attr('width', 800)
      .attr('height', 600);

    const tables = [
      { id: 1, x: 50, y: 50, width: 100, height: 100, info: 'Table 1: 4 seats' },
      { id: 2, x: 200, y: 50, width: 100, height: 100, info: 'Table 2: 2 seats' },
      { id: 3, x: 350, y: 50, width: 100, height: 100, info: 'Table 3: 6 seats' }
    ];

    const group = svg.selectAll('g')
      .data(tables)
      .enter()
      .append('g')
      .attr('transform', d => `translate(${d.x},${d.y})`)
      .on('click', (event, d) => {
        alert(d.info);
      });

    group.append('rect')
      .attr('width', d => d.width)
      .attr('height', d => d.height)
      .attr('fill', 'lightblue')
      .attr('stroke', 'black')
      .attr('stroke-width', 2);

    group.append('text')
      .attr('x', d => d.width / 2)
      .attr('y', d => d.height + 20)
      .attr('text-anchor', 'middle')
      .text(d => d.info);
  }

  onFieldChange() {
    this.reservationCheckAvailabilityDto.startTime = this.reservationCreateDto.startTime;
    this.reservationCheckAvailabilityDto.date = this.reservationCreateDto.date;
    this.reservationCheckAvailabilityDto.pax = this.reservationCreateDto.pax;

    if (this.reservationCheckAvailabilityDto.startTime == null || this.reservationCheckAvailabilityDto.pax == null || this.reservationCheckAvailabilityDto.date == null) {
      this.reservationStatusText = 'Provide Time, Date and Pax';
      this.reservationStatusClass = 'reservation-table-incomplete';
      return;
    }

    this.service.getAvailability(this.reservationCheckAvailabilityDto).subscribe({
      next: (data) => {
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
        this.notificationService.showError('Failed to check availability. Please try again later.');
      },
    });
  }

  onSubmit(form: NgForm) {
    if (this.authService.isLoggedIn()) {
      this.currentUser = this.authService.getCurrentUser();
      this.reservationCreateDto.firstName = this.currentUser.firstName;
      this.reservationCreateDto.lastName = this.currentUser.lastName;
      this.reservationCreateDto.email = this.currentUser.email;
      this.reservationCreateDto.mobileNumber = Number(this.currentUser.mobileNumber);
    }
  }
}
