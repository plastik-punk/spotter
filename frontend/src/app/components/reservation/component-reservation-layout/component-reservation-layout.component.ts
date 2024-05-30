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

interface Coordinate {
  x: number;
  y: number;
}

interface Place {
  id: number;
  coordinates: Coordinate[];
  seats: number;
  reserved: number; // 0 for free, 1 for booked
  status: number;
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
      { id: 1, coordinates: [{ x: 1, y: 3 }, { x: 2, y: 3 }], seats: 6, reserved: 0, status: 0},
      { id: 2, coordinates: [{ x: 1, y: 5 }, { x: 2, y: 5 }], seats: 6, reserved: 0, status: 0},
      { id: 3, coordinates: [{ x: 1, y: 7 }, { x: 2, y: 7 }], seats: 6, reserved: 0, status: 0},
      { id: 4, coordinates: [{ x: 5, y: 3 }, { x: 6, y: 3 }], seats: 6, reserved: 0, status: 1},
      { id: 5, coordinates: [{ x: 5, y: 5 }, { x: 6, y: 5 }], seats: 6, reserved: 0, status: 1},

      { id: 6, coordinates: [{ x: 5, y: 7 }, { x: 6, y: 7 }], seats: 6, reserved: 0, status: 1},
      { id: 7, coordinates: [{ x: 11, y: 3 }, { x: 12, y: 3 }], seats: 6, reserved: 0, status: 1},
      { id: 8, coordinates: [{ x: 11, y: 5 }, { x: 12, y: 5 }], seats: 6, reserved: 0, status: 1},
      { id: 9, coordinates: [{ x: 11, y: 7 }, { x: 12, y: 7 }], seats: 6, reserved: 0, status: 1},
      { id: 10, coordinates: [{ x: 12, y: 0 }], seats: 3, reserved: 1, status: 1},
      { id: 11, coordinates: [{ x: 14, y: 0 }, { x: 15, y: 0 }, { x: 15, y: 1 }, { x: 15, y: 2 }], seats: 5, reserved: 1, status: 1}, // L-shaped table
      { id: 13, coordinates: [{ x: 15, y: 4 }], seats: 3, reserved: 1, status: 1},
      { id: 14, coordinates: [{ x: 15, y: 6 }], seats: 3, reserved: 1, status: 1},
      { id: 16, coordinates: [{ x: 2, y: 0 }, { x: 3, y: 0 }, { x: 4, y: 0 }, { x: 5, y: 0 }, { x: 6, y: 0 }, { x: 7, y: 0 }, { x: 8, y: 0 }, { x: 9, y: 0 }, { x: 10, y: 0 }], seats: 20, reserved: 0, status: 1}, // Bar
    ];

    // Add places to SVG
    const group = svg.selectAll('g')
      .data(places)
      .enter()
      .append('g')
      .on('click', (event, d) => {
        event.stopPropagation(); // Prevent triggering the document click event
        if (d.reserved === 0 && d.status === 1) { // Only allow selection of available places
          this.onPlaceClick(d.id);
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
        .text(d.seats.toString());
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

  private getPlaceColor(place: Place): string {
    if (place.id === this.selectedPlaceId) {
      return '#377eb8'; // Selected color
    }
    if(place.status === 0) {
      return '#767676FF'; // Closed color
    }
    return place.reserved === 0 ? '#4daf4a' : '#e41a1c'; // Green for free, Red for booked
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
    const element = this.d3Container.nativeElement;
    d3.select(element).selectAll('path')
      .attr('fill', (d: Place) => this.getPlaceColor(d));
  }

  private showTooltip(event: MouseEvent, place: Place) {
    const tooltip = d3.select('#tooltip');
    tooltip.style('display', 'block')
      .style('left', `${event.pageX + 10}px`)
      .style('top', `${event.pageY + 10}px`)
      .html(`ID: ${place.id}<br>Seats: ${place.seats}<br>Status: ${place.reserved === 0 ? 'Free' : 'Booked'}`);
  }

  private hideTooltip() {
    d3.select('#tooltip').style('display', 'none');
  }

  private getTextPosition(coordinates: Coordinate[], gridSize: number): { x: number, y: number } {
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
