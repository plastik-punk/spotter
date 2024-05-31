import {Component, OnInit} from '@angular/core';
import {EventDetailDto} from "../../../dtos/event";
import {ActivatedRoute, Router} from "@angular/router";
import {AuthService} from "../../../services/auth.service";
import {EventService} from "../../../services/event.service";
import {NotificationService} from "../../../services/notification.service";

@Component({
  selector: 'app-event-detail',
  templateUrl: './event-detail.component.html',
  styleUrl: './event-detail.component.scss'
})
export class EventDetailComponent implements OnInit {
  startDateSplit: string;
  startTimeSplit: string;
  endDateSplit: string;
  endTimeSplit: string;

  eventDetailDto: EventDetailDto = {
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
    this.eventDetailDto.hashId = this.route.snapshot.paramMap.get('id');
    if (this.eventDetailDto.hashId) {
      let observable = this.service.getByHashId(this.eventDetailDto.hashId);
      observable.subscribe({
        next: (data) => {
          if (data != null) {
            this.eventDetailDto.hashId = data.hashId;
            this.eventDetailDto.startTime = data.startTime;
            this.eventDetailDto.endTime = data.endTime;
            this.eventDetailDto.name = data.name;
            this.eventDetailDto.description = data.description;
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

  onSubmit() {
    this.router.navigate(['event-overview'])
  }

  onEdit() {
    this.router.navigate(['event-edit', this.eventDetailDto.hashId])
  }

  onDelete() {
    let observable = this.service.delete(this.eventDetailDto.hashId);
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
