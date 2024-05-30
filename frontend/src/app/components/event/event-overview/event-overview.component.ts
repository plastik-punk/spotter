import {Component, OnInit} from '@angular/core';
import {FormsModule} from "@angular/forms";
import {NgForOf, NgIf} from "@angular/common";
import {debounceTime, Observable, Subject} from "rxjs";
import {HttpResponse} from "@angular/common/http";
import {AuthService} from "../../../services/auth.service";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {NotificationService} from "../../../services/notification.service";
import {Router, RouterLink} from "@angular/router";
import {EventListDto, EventSearchDto} from "../../../dtos/event";
import {EventService} from "../../../services/event.service";

@Component({
  selector: 'app-event-overview',
  templateUrl: './event-overview.component.html',
  styleUrl: './event-overview.component.scss'
})
export class EventOverviewComponent implements OnInit {

  events: EventListDto[] = [];
  displayedEvents: EventListDto[] = [];
  searchParams: EventSearchDto = {};
  searchEarliestDate: Date | null = null;
  searchLatestDate: Date | null = null;
  searchEarliestStartTime: Date | null = null;
  searchLatestEndTime: Date | null = null;
  searchChangedObservable = new Subject<void>();
  deleteWhat: string = null;

  constructor(
    private authService: AuthService,
    private modalService: NgbModal,
    private eventService: EventService,
    private notificationService: NotificationService,
    private router: Router) {
  }

  ngOnInit(): void {
    this.loadEvents();
    this.searchChangedObservable
      .pipe(debounceTime(300))
      .subscribe({ next: () => this.loadEvents() });
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

  loadEvents() {
    if (this.searchEarliestDate == null) {
      delete this.searchParams.earliestStartDate;
    } else {
      this.searchParams.earliestStartDate = this.searchEarliestDate;
    }
    if (this.searchLatestDate == null) {
      delete this.searchParams.latestEndDate;
    } else {
      this.searchParams.latestEndDate = this.searchLatestDate;
    }
    if (this.searchEarliestStartTime == null) {
      delete this.searchParams.earliestStartTime;
    } else {
      this.searchParams.earliestStartTime = this.searchEarliestStartTime;
    }
    if (this.searchLatestEndTime == null) {
      delete this.searchParams.latestEndTime;
    } else {
      this.searchParams.latestEndTime = this.searchLatestEndTime;
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

}
