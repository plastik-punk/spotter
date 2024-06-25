import {Component, OnInit} from '@angular/core';
import {debounceTime, Observable, Subject} from "rxjs";
import {HttpResponse} from "@angular/common/http";
import {AuthService} from "../../../services/auth.service";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {NotificationService} from "../../../services/notification.service";
import {Router} from "@angular/router";
import {EventDetailDto, EventListDto, EventSearchDto} from "../../../dtos/event";
import {EventService} from "../../../services/event.service";
import {formatDay, formatDotDate, formatIsoTime} from "../../../util/date-helper";
import * as bootstrap from 'bootstrap';

@Component({
  selector: 'app-event-overview',
  templateUrl: './event-overview.component.html',
  styleUrl: './event-overview.component.scss'
})
export class EventOverviewComponent implements OnInit {

  events: EventListDto[] = [];
  displayedEvents: EventListDto[] = [];
  searchParams: EventSearchDto = {};
  searchEarliestDate: string | null = null;
  searchLatestDate: string | null = null;
  searchEarliestStartTime: string | null = null;
  searchLatestEndTime: string | null = null;
  searchName: string | null = null;
  searchChangedObservable = new Subject<void>();
  deleteWhat: string = null;
  selectedFile: File | null = null;

  event: EventDetailDto = {
    hashId: undefined,
    name: undefined,
    startTime: undefined,
    endTime: undefined,
    description: undefined
  };

  constructor(
    private authService: AuthService,
    private modalService: NgbModal,
    private eventService: EventService,
    private notificationService: NotificationService,
    private router: Router) {
  }

  ngOnInit(): void {
    this.searchEarliestDate = new Date().toISOString().split('T')[0]
    this.loadEvents();
    this.searchChangedObservable
      .pipe(debounceTime(300))
      .subscribe({next: () => this.loadEvents()});
  }

  showMore() {
    let newLength = this.displayedEvents.length + 5;
    if (newLength > this.events.length) {
      newLength = this.events.length;
    }
    this.displayedEvents = this.events.slice(0, newLength);
  }

  isAdmin(): boolean {
    return this.authService.getUserRole() === 'ADMIN';
  }

  showEventDetails(hashId: string): void {
    this.eventService.getByHashId(hashId).subscribe({
      next: (data: EventDetailDto) => {
        this.event.name = data.name;
        this.event.startTime = data.startTime;
        this.event.endTime = data.endTime;
        this.event.description = data.description;

        const modalDetail = new bootstrap.Modal(document.getElementById('event-detail'));
        modalDetail.show();
      },
      error: error => {
        this.notificationService.showError('Failed to load event details. Please try again later.');
      }
    });
  }

  loadEvents() {
    if (this.searchEarliestDate == null || this.searchEarliestDate === '') {
      delete this.searchParams.earliestStartDate;
    } else {
      this.searchParams.earliestStartDate = new Date(this.searchEarliestDate);
    }
    if (this.searchLatestDate == null || this.searchLatestDate === '') {
      delete this.searchParams.latestEndDate;
    } else {
      this.searchParams.latestEndDate = new Date(this.searchLatestDate);
    }
    if (this.searchEarliestStartTime == null || this.searchEarliestStartTime === '') {
      delete this.searchParams.earliestStartTime;
    } else {
      this.searchParams.earliestStartTime = this.searchEarliestStartTime;
    }
    if (this.searchLatestEndTime == null || this.searchLatestEndTime === '') {
      delete this.searchParams.latestEndTime;
    } else {
      this.searchParams.latestEndTime = this.searchLatestEndTime;
    }
    if (this.searchName == null || this.searchName === '') {
      delete this.searchParams.name;
    } else {
      this.searchParams.name = this.searchName;
    }

    this.eventService.search(this.searchParams)
      .subscribe({
        next: (event: EventListDto[]) => {
          this.events = event;
          this.displayedEvents = this.events.slice(0, 10);
        },
        error: error => {
          this.notificationService.showError('Failed to load events. Please try again later.');
        }
      });
  }

  searchChanged(): void {
    this.searchChangedObservable.next();
  }

  openConfirmationDialog(hashId: string): void {
    this.eventService.getByHashId(hashId).subscribe({
      next: data => {
        this.deleteWhat = hashId;
      },
      error: error => {
        this.notificationService.showError('Failed to load event details. Please try again later.');
      }
    });
  }

  onDelete(): void {
    if (!this.deleteWhat) {
      this.notificationService.showError('No event selected for deletion.');
      return;
    }

    let observable: Observable<HttpResponse<void>>;
    observable = this.eventService.delete(this.deleteWhat);
    observable.subscribe({
      next: (response) => {
        if (response.status === 204) {
          this.notificationService.showSuccess('Event deleted successfully');
          this.loadEvents();
        }
      },
      error: (error) => {
        this.notificationService.showError('Failed to delete event. Please try again later.');
      }
    });
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.selectedFile = input.files[0];
    }
  }

  onUpload(): void {
    if (this.selectedFile) {
      this.eventService.uploadIcsFile(this.selectedFile).subscribe(
        response => {
          this.loadEvents();
          this.notificationService.showSuccess('File uploaded successfully');
        },
        error => {
          this.loadEvents();
          this.notificationService.showError('Error uploading file');
        }
      );
    }
  }

  formatDateTime(dateTime: string): string {
    if (!dateTime) {
      return "";
    }
    const date = new Date(dateTime);
    const formattedDate = date.toISOString().split('T')[0];
    const hours = date.getHours().toString().padStart(2, '0');
    const minutes = date.getMinutes().toString().padStart(2, '0');

    return `${formattedDate} ${hours}:${minutes}`;
  }

  protected readonly formatIsoTime = formatIsoTime;
  protected readonly formatDotDate = formatDotDate;
  protected readonly formatDay = formatDay;
}
