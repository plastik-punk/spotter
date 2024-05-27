import { Component, OnInit, ElementRef, ViewChild, HostListener } from '@angular/core';
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

interface Place {
  id: number;
  x: number;
  y: number;
  width: number;
  height: number;
  seats: number;
  status: number; // 0 for free, 1 for booked
}

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
  selectedPlaceId: number | null = null; // Track the selected place

  constructor(
    public authService: AuthService,
    private service: ReservationService,
    private notificationService: NotificationService,
  ) { }

  ngOnInit() {
    this.createSeatingPlan();
    this.onResize(); // Adjust the size on initialization

    // Add an event listener to handle clicks outside the SVG
    document.addEventListener('click', this.onDocumentClick.bind(this));
  }

  @HostListener('window:resize', ['$event'])
  onResize() {
    this.adjustSvgSize();
  }

  private adjustSvgSize() {
    const element = this.d3Container.nativeElement;
    const width = element.clientWidth;
    const aspectRatio = 16 / 9; // 16:9 aspect ratio
    const height = width / aspectRatio;

    d3.select(element).select('svg')
      .attr('width', width)
      .attr('height', height);
  }

  private createSeatingPlan() {
    const element = this.d3Container.nativeElement;
    const width = element.clientWidth;
    const aspectRatio = 16 / 9; // 16:9 aspect ratio
    const height = width / aspectRatio;

    const svg = d3.select(this.d3Container.nativeElement)
      .append('svg')
      .attr('viewBox', `0 0 1600 900`) // 1600x900 for 16:9 aspect ratio
      .attr('preserveAspectRatio', 'xMidYMid meet')
      .attr('width', width)
      .attr('height', height);

    const gridSize = 100;

    // Define places with their properties
    const places: Place[] = [
      { id: 1, x: 1, y: 3, width: 2, height: 1, seats: 6, status: 0 },
      { id: 2, x: 1, y: 5, width: 2, height: 1, seats: 6, status: 0 },
      { id: 3, x: 1, y: 7, width: 2, height: 1, seats: 6, status: 0 },
      { id: 4, x: 5, y: 3, width: 2, height: 1, seats: 6, status: 0 },
      { id: 5, x: 5, y: 5, width: 2, height: 1, seats: 6, status: 0 },
      { id: 6, x: 5, y: 7, width: 2, height: 1, seats: 6, status: 0 },
      { id: 7, x: 11, y: 3, width: 2, height: 1, seats: 6, status: 0 },
      { id: 8, x: 11, y: 5, width: 2, height: 1, seats: 6, status: 0 },
      { id: 9, x: 11, y: 7, width: 2, height: 1, seats: 6, status: 0 },
      { id: 10, x: 12, y: 0, width: 1, height: 1, seats: 3, status: 1 },
      { id: 11, x: 14, y: 0, width: 2, height: 1, seats: 5, status: 1 },
      { id: 12, x: 15, y: 1, width: 1, height: 2, seats: 4, status: 1 },
      { id: 13, x: 15, y: 4, width: 1, height: 1, seats: 3, status: 1 },
      { id: 14, x: 15, y: 6, width: 1, height: 1, seats: 3, status: 1 },
      { id: 16, x: 2, y: 0, width: 9, height: 1, seats: 20, status: 0 }, // Bar
    ];

    // Add places to SVG
    const group = svg.selectAll('g')
      .data(places)
      .enter()
      .append('g')
      .attr('transform', d => `translate(${d.x * gridSize},${d.y * gridSize})`)
      .on('click', (event, d) => {
        event.stopPropagation(); // Prevent triggering the document click event
        if (d.status === 0) { // Only allow selection of available places
          this.onPlaceClick(d.id);
        }
      })
      .on('mouseover', (event, d) => this.showTooltip(event, d))
      .on('mouseout', () => this.hideTooltip());

    // Add rectangles for places
    group.append('rect')
      .attr('width', d => d.width * gridSize)
      .attr('height', d => d.height * gridSize)
      .attr('rx', 10) // Rounded corners
      .attr('ry', 10)
      .attr('fill', d => this.getPlaceColor(d))

    // Add text to places
    group.append('text')
      .attr('x', d => (d.width / 2) * gridSize)
      .attr('y', d => (d.height / 2) * gridSize)
      .attr('dy', '.35em')
      .attr('text-anchor', 'middle')
      .attr('fill', 'white')
      .attr('font-size', '35px')
      .attr('font-family', 'Arial')
      .text(d => d.seats.toString());

    // Add tooltip element
    d3.select('body').append('div')
      .attr('id', 'tooltip')
      .style('position', 'absolute')
      .style('background', '#fff')
      .style('border', '1px solid #ccc')
      .style('padding', '10px')
      .style('display', 'none');
  }

  private getPlaceColor(place: Place): string {
    if (place.id === this.selectedPlaceId) {
      return '#377eb8'; // Selected color
    }
    return place.status === 0 ? '#4daf4a' : '#e41a1c'; // Green for free, Red for booked
  }

  private onPlaceClick(placeId: number) {
    this.selectedPlaceId = placeId;
    this.updatePlaceColors();
  }

  private onDocumentClick() {
    if (this.selectedPlaceId !== null) {
      this.selectedPlaceId = null;
      this.updatePlaceColors();
    }
  }

  private updatePlaceColors() {
    d3.select(this.d3Container.nativeElement).selectAll('rect')
      .attr('fill', (d: Place) => this.getPlaceColor(d));
  }

  private showTooltip(event: MouseEvent, place: Place) {
    const tooltip = d3.select('#tooltip');
    tooltip.style('display', 'block')
      .style('left', `${event.pageX + 10}px`)
      .style('top', `${event.pageY + 10}px`)
      .html(`ID: ${place.id}<br>Seats: ${place.seats}<br>Status: ${place.status === 0 ? 'Free' : 'Booked'}`);
  }

  private hideTooltip() {
    d3.select('#tooltip').style('display', 'none');
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

    this.service.createReservation(this.reservationCreateDto).subscribe({
      next: (response) => {
        this.notificationService.showSuccess('Reservation created successfully.');
      },
      error: (error) => {
        this.notificationService.showError('Failed to create reservation. Please try again later.');
      }
    });
  }
}
