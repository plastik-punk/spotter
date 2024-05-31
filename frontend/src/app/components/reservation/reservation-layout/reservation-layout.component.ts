import { Component, OnInit, ElementRef, ViewChild, HostListener } from '@angular/core';
import { FormsModule, NgForm, ReactiveFormsModule } from "@angular/forms";
import { NgIf } from "@angular/common";
import { RouterLink } from "@angular/router";
import {
  ReservationLayoutCheckAvailabilityDto,
  ReservationCreateDto,
  AreaLayoutDto,
  PlaceVisualDto, CoordinateDto, Reservation
} from "../../../dtos/reservation";
import { UserOverviewDto } from "../../../dtos/app-user";
import { SimpleViewReservationStatusEnum } from "../../../dtos/status-enum";
import { AuthService } from "../../../services/auth.service";
import { ReservationService } from "../../../services/reservation.service";
import { NotificationService } from "../../../services/notification.service";
import * as d3 from 'd3';
import { formatIsoDate } from "../../../util/date-helper";
import { Observable } from "rxjs";


@Component({
  selector: 'app-component-reservation-layout',
  templateUrl: './reservation-layout.component.html',
  styleUrls: ['./reservation-layout.component.scss']
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
    mobileNumber: undefined,
    placeId: undefined
  };

  hours = new Date().getHours().toString().padStart(2, '0');
  minutes = new Date().getMinutes().toString().padStart(2, '0');
  nowTime = this.hours + ':' + this.minutes;
  now = formatIsoDate(new Date());

  reservationLayoutCheckAvailabilityDto: ReservationLayoutCheckAvailabilityDto = {
    startTime: this.nowTime,
    date: this.now,
    areaId: 1,
    idToExclude: -1
  }

  currentUser: UserOverviewDto;
  areaLayout: AreaLayoutDto;
  selectedPlaceId: number | null = null; // Track the selected place
  selectedPlaceSeats: number | null = null; // Track the number of seats in the selected place

  layoutWidth: number = 1600; // Default width
  layoutHeight: number = 900; // Default height

  constructor(
    public authService: AuthService,
    private service: ReservationService,
    private notificationService: NotificationService,
  ) { }

  ngOnInit() {
    this.fetchLayoutAvailability();
    this.createSeatingPlan();
    this.onResize(); // Adjust the size on initialization
  }

  @HostListener('window:resize', ['$event'])
  onResize() {
    this.adjustSvgSize();
  }

  private adjustSvgSize() {
    const element = this.d3Container.nativeElement;
    const width = element.clientWidth;
    const aspectRatio = this.layoutWidth / this.layoutHeight;
    const height = width / aspectRatio;

    d3.select(element).select('svg')
      .attr('width', width)
      .attr('height', height);
  }

  private createSeatingPlan() {
    const element = this.d3Container.nativeElement;
    const width = element.clientWidth;
    const aspectRatio = this.layoutWidth / this.layoutHeight;
    const height = width / aspectRatio;

    const svg = d3.select(this.d3Container.nativeElement)
      .append('svg')
      .attr('viewBox', `0 0 ${this.layoutWidth} ${this.layoutHeight}`) // Use dynamic width and height
      .attr('preserveAspectRatio', 'xMidYMid meet')
      .attr('width', width)
      .attr('height', height);
  }

  private fetchLayoutAvailability() {
    console.log('ngDoCheck called');

    this.service.getLayoutAvailability(this.reservationLayoutCheckAvailabilityDto).subscribe({
      next: (data: AreaLayoutDto) => {
        this.areaLayout = data;
        this.layoutWidth = this.areaLayout.width;
        this.layoutHeight = this.areaLayout.height;
        this.updateSeatingPlan();
        this.adjustSvgSize(); // Adjust size after updating layout dimensions
      },
      error: (error) => {
        this.notificationService.showError('Failed to fetch layout availability. Please try again later.');
      },
    });
  }

  private updateSeatingPlan() {
    const element = this.d3Container.nativeElement;
    const svg = d3.select(element).select('svg');
    svg.selectAll('*').remove(); // Clear existing elements

    const gridSize = 100;
    // Add places to SVG
    const group = svg.selectAll('g')
      .data(this.areaLayout.placeVisuals)
      .enter()
      .append('g')
      .on('click', (event, d) => {
        event.stopPropagation(); // Prevent triggering the document click event
        if (!d.reservation && d.status) { // Only allow selection of available places
          this.onPlaceClick(d.placeId, d.numberOfSeats);
        }
      })
      .on('mouseover', (event, d) => this.showTooltip(event, d))
      .on('mouseout', () => this.hideTooltip());

    // Add shapes for places based on coordinates
    group.each((d, i, nodes) => {
      const g = d3.select(nodes[i]);

      // Generate the polygon points by connecting the outer edges of the squares
      const polygonPoints = [];
      d.coordinates.forEach(coord => {
        const x = coord.x * gridSize;
        const y = coord.y * gridSize;
        polygonPoints.push([x, y], [x + gridSize, y], [x + gridSize, y + gridSize], [x, y + gridSize]);
      });

      // Count the occurrences of each point
      const pointCounts: Record<string, number> = {};
      polygonPoints.forEach(point => {
        const key = point.join(',');
        pointCounts[key] = (pointCounts[key] || 0) + 1;
      });

      // Filter and remove duplicates based on occurrences
      const uniqueFilteredPoints = Object.entries(pointCounts)
        .filter(([key, count]) => count % 2 === 1)  // Keep points with odd occurrences
        .map(([key]) => key.split(',').map(Number)); // Convert back to number arrays

      // Order points correctly to form the polygon
      const orderedPoints = this.orderPoints(uniqueFilteredPoints);

      // Create the path for the polygon with rounded corners
      const pathData = this.polygonWithRoundedCorners(orderedPoints, 10); // Adjust the radius as needed

      g.append('path')
        .attr('d', pathData)
        .attr('fill', () => this.getPlaceColor(d));

      // Add text to places
      const textPosition = this.getTextPosition(d.coordinates, gridSize);
      g.append('text')
        .attr('x', textPosition.x)
        .attr('y', textPosition.y)
        .attr('dy', '.35em')
        .attr('text-anchor', 'middle')
        .attr('fill', 'white')
        .attr('font-size', '35px')
        .attr('font-family', 'Arial')
        .text(d.numberOfSeats.toString());
    });

    // Add tooltip element
    d3.select('body').append('div')
      .attr('id', 'tooltip')
      .style('position', 'absolute')
      .style('background', '#fff')
      .style('border', '1px solid #ccc')
      .style('padding', '10px')
      .style('display', 'none');
  }

  private orderPoints(points: number[][]): number[][] {
    if (points.length <= 1) return points;

    const orderedPoints = [points[0]];
    points.splice(0, 1);

    while (points.length > 0) {
      const lastPoint = orderedPoints[orderedPoints.length - 1];
      let found = false;

      for (let i = 0; i < points.length; i++) {
        if (points[i][0] === lastPoint[0] || points[i][1] === lastPoint[1]) {
          orderedPoints.push(points[i]);
          points.splice(i, 1);
          found = true;
          break;
        }
      }

      if (!found) {
        orderedPoints.push(points.shift());
      }
    }

    return orderedPoints;
  }

  private polygonWithRoundedCorners(points: number[][], r: number): string {
    let d = "";
    for (let i = 0; i < points.length; i++) {
      let previous = i - 1 >= 0 ? i - 1 : points.length - 1;
      let next = i + 1 < points.length ? i + 1 : 0;
      let c = { x: points[i][0], y: points[i][1] };
      let l1 = { x: points[previous][0], y: points[previous][1] };
      let l2 = { x: points[next][0], y: points[next][1] };
      let a1 = this.getAngle(c, l1);
      let a2 = this.getAngle(c, l2);

      let x1 = (c.x + r * Math.cos(a1)).toFixed(3);
      let y1 = (c.y + r * Math.sin(a1)).toFixed(3);
      let x2 = (c.x + r * Math.cos(a2)).toFixed(3);
      let y2 = (c.y + r * Math.sin(a2)).toFixed(3);
      d += "L" + x1 + "," + y1 + " Q" + c.x + "," + c.y + " " + x2 + "," + y2;
    }
    d += "Z"; // close the path
    return d.replace("L", "M"); // the first command is "M"
  }

  private getAngle(c: { x: number, y: number }, l: { x: number, y: number }): number {
    let delta_x = l.x - c.x;
    let delta_y = l.y - c.y;
    return Math.atan2(delta_y, delta_x);
  }

  private getPlaceColor(place: PlaceVisualDto): string {
    if (place.placeId === this.selectedPlaceId) {
      return '#377eb8'; // Selected color
    }
    if (!place.status) {
      return '#767676FF'; // Closed color
    }
    return !place.reservation ? '#4daf4a' : '#e41a1c'; // Green for free, Red for booked
  }

  private onPlaceClick(placeId: number, numberOfSeats: number) {
    if (this.selectedPlaceId === placeId) {
      return; // If the same place is clicked, do nothing
    }
    this.selectedPlaceId = placeId;
    this.selectedPlaceSeats = numberOfSeats;
    this.reservationCreateDto.placeId = placeId; // Update reservationCreateDto with the selected place ID
    this.updatePlaceColors();
  }

  private updatePlaceColors() {
    const element = this.d3Container.nativeElement;
    d3.select(element).selectAll('path')
      .attr('fill', (d: PlaceVisualDto) => this.getPlaceColor(d));
  }

  private showTooltip(event: MouseEvent, place: PlaceVisualDto) {
    const tooltip = d3.select('#tooltip');
    tooltip.style('display', 'block')
      .style('left', `${event.pageX + 10}px`)
      .style('top', `${event.pageY + 10}px`)
      .html(`ID: ${place.placeId}<br>Seats: ${place.numberOfSeats}<br>Status: ${!place.reservation ? 'Free' : 'Booked'}`);
  }

  private hideTooltip() {
    d3.select('#tooltip').style('display', 'none');
  }

  private getTextPosition(coordinates: CoordinateDto[], gridSize: number): { x: number, y: number } {
    // Find the best square or half-square for placing the text
    for (const coord of coordinates) {
      const x = coord.x * gridSize + gridSize / 2;
      const y = coord.y * gridSize + gridSize / 2;

      const covered = coordinates.some(c => c.x === coord.x && c.y === coord.y + 1);
      if (covered) {
        return { x, y: y + gridSize / 2 };
      }
    }

    // Fallback to centroid if no suitable position found
    let x = 0, y = 0;
    coordinates.forEach(coord => {
      x += coord.x * gridSize + gridSize / 2;
      y += coord.y * gridSize + gridSize / 2;
    });
    x /= coordinates.length;
    y /= coordinates.length;

    return { x, y };
  }

  onFieldChange() {
    console.log('onFieldChange called');
    if (this.reservationCreateDto.startTime) {
      this.reservationLayoutCheckAvailabilityDto.startTime = this.reservationCreateDto.startTime.toString();
    }
    if (this.reservationCreateDto.date) {
      this.reservationLayoutCheckAvailabilityDto.date = this.reservationCreateDto.date.toString();
    }
    this.reservationLayoutCheckAvailabilityDto.areaId = 1; // Hardcoded areaId for now
    this.fetchLayoutAvailability();

    // Check pax against available seats
    if (this.selectedPlaceSeats !== null && this.reservationCreateDto.pax > this.selectedPlaceSeats) {
      this.notificationService.showError(`The selected place only has ${this.selectedPlaceSeats} seats.`);
      this.reservationCreateDto.pax = this.selectedPlaceSeats; // Adjust pax to available seats
      this.isPaxValid();
    }
  }

  isPaxValid(): boolean {
    return this.selectedPlaceSeats === null || (this.reservationCreateDto.pax <= this.selectedPlaceSeats);
  }

  private formatDate(date: Date): string {
    const year = date.getFullYear();
    const month = (date.getMonth() + 1).toString().padStart(2, '0');
    const day = date.getDate().toString().padStart(2, '0');
    return `${year}-${month}-${day}`;
  }

  private formatTime(time: Date): string {
    const hours = time.getHours().toString().padStart(2, '0');
    const minutes = time.getMinutes().toString().padStart(2, '0');
    return `${hours}:${minutes}`;
  }

  onSubmit(form: NgForm) {
    if (this.authService.isLoggedIn()) {
      this.currentUser = this.authService.getCurrentUser();
      this.reservationCreateDto.firstName = this.currentUser.firstName;
      this.reservationCreateDto.lastName = this.currentUser.lastName;
      this.reservationCreateDto.email = this.currentUser.email;
      this.reservationCreateDto.mobileNumber = Number(this.currentUser.mobileNumber);
    }

    // Check pax against available seats
    if (this.selectedPlaceSeats !== null && this.reservationCreateDto.pax > this.selectedPlaceSeats) {
      this.notificationService.showError(`The selected place only has ${this.selectedPlaceSeats} seats.`);
      return; // Prevent form submission
    }

    if (form.valid) {
      let observable: Observable<Reservation>;
      observable = this.service.createReservation(this.reservationCreateDto);
      observable.subscribe({
        next: (data) => {
          if (data == null) {
            this.notificationService.showError('Location Closed');
          } else {
            this.notificationService.showSuccess('Reservation created successfully.');
            this.resetForm(form);
          }
        },
        error: (error) => {
          this.notificationService.showError('Failed to create reservation. Please try again later.');
        },
      });
    } else {
      this.showFormErrors();
    }
  }
  private resetForm(form: NgForm) {
    form.resetForm();
    this.reservationCreateDto = {
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
      placeId: undefined
    };
    this.selectedPlaceId = null;
    this.selectedPlaceSeats = null;
    this.updatePlaceColors();
  }
  private showFormErrors(): void {
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
