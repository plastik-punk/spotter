import { Component } from '@angular/core';
import {EventDetailDto, EventEditDto} from "../../../dtos/event";
import {AuthService} from "../../../services/auth.service";
import {EventService} from "../../../services/event.service";
import {ActivatedRoute, Router} from "@angular/router";
import {NotificationService} from "../../../services/notification.service";
import {NgForm} from "@angular/forms";

@Component({
  selector: 'app-event-edit',
  templateUrl: './event-edit.component.html',
  styleUrl: './event-edit.component.scss'
})
export class EventEditComponent {

  startDateString: string;
  endDateString: string;

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
            this.formatDateTime();
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

  onSubmit(form: NgForm) {
    if (form.valid) {
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
    } else {
      this.showFormErrors();
    }

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


  private showFormErrors(): void {
    if (!this.eventEditDto.startTime) {
      this.notificationService.showError('Start time is required.');
    }
    if (!this.eventEditDto.endTime) {
      this.notificationService.showError('End time is required.');
    }
    if (!this.eventEditDto.name) {
      this.notificationService.showError('Name is required.');
    }
  }

  private formatDateTime() {
    const startDate = new Date(this.eventEditDto.startTime);
    this.startDateString = startDate.toISOString().slice(0, 16);

    const endDate = new Date(this.eventEditDto.endTime);
    this.endDateString = endDate.toISOString().slice(0, 16);
  }

  startTimeChange(event: any) {
    this.eventEditDto.startTime = event;
    this.formatDateTime();
  }

  endTimeChange(event: any) {
    this.eventEditDto.endTime = event;
    this.formatDateTime();
  }

}
