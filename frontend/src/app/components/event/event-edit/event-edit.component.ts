import { Component } from '@angular/core';
import {EventDetailDto, EventEditDto} from "../../../dtos/event";
import {AuthService} from "../../../services/auth.service";
import {EventService} from "../../../services/event.service";
import {ActivatedRoute, Router} from "@angular/router";
import {NotificationService} from "../../../services/notification.service";

@Component({
  selector: 'app-event-edit',
  templateUrl: './event-edit.component.html',
  styleUrl: './event-edit.component.scss'
})
export class EventEditComponent {
  startDateSplit: string;
  startTimeSplit: string;
  endDateSplit: string;
  endTimeSplit: string;

  eventEditDto: EventEditDto = {
    hashId: undefined,
    startTime: undefined,
    endTime: undefined,
    name: undefined,
    description: undefined
  }

  constructor(
    public authService: AuthService,
    private service: EventService,
    private router: Router,
    private notificationService: NotificationService,
    private route: ActivatedRoute
  ) {
  }

  ngOnInit(): void {
    this.eventEditDto.hashId = this.route.snapshot.paramMap.get('id');
    if (this.eventEditDto.hashId) {
      let observable = this.service.getByHashId(this.eventEditDto.hashId);
      observable.subscribe({
        next: (data) => {
          if (data != null) {
            this.eventEditDto.hashId = data.hashId;
            this.eventEditDto.startTime = data.startTime;
            this.eventEditDto.endTime = data.endTime;
            this.eventEditDto.name = data.name;
            this.eventEditDto.description = data.description;
            this.startDateSplit = this.formatDate(data.startTime);
            this.startTimeSplit = this.formatTime(data.startTime);
            this.endDateSplit = this.formatDate(data.endTime);
            this.endTimeSplit = this.formatTime(data.endTime);
          }
        },
        error: (error) => {
          this.notificationService.showError(error.error.message);
        }
      });
    }
  }

  onBack() {
    this.router.navigate(['event-overview'])
  }

  onSubmit() {
    let observable = this.service.update(this.eventEditDto);
    observable.subscribe({
      next: () => {
        this.notificationService.showSuccess('Event updated successfully.');
        this.router.navigate(['event-overview']);
      },
      error: (error) => {
        this.notificationService.showError(error.error.message);
      }
    });
  }

  onDelete() {
    let observable = this.service.delete(this.eventEditDto.hashId);
    observable.subscribe({
      next: () => {
        this.notificationService.showSuccess('Event deleted successfully.');
        this.router.navigate(['event-overview']);
      },
      error: (error) => {
        this.notificationService.showError(error.error.message);
      }
    });
  }

  private formatDate(d: Date): string {
    const date = new Date(d);
    return date.toLocaleDateString();
  }

  private formatTime(d: Date): string {
    const date = new Date(d);
    return date.toLocaleTimeString();
  }
}
