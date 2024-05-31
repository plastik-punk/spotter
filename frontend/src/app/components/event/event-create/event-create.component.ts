import {Component, OnInit} from '@angular/core';
import {AuthService} from "../../../services/auth.service";
import {EventService} from "../../../services/event.service";
import {NotificationService} from "../../../services/notification.service";
import {NgForm} from "@angular/forms";
import {Observable} from "rxjs";
import {EventCreateDto} from "../../../dtos/event";
import {Router} from "@angular/router";

@Component({
  selector: 'app-event-create',
  templateUrl: './event-create.component.html',
  styleUrl: './event-create.component.scss'
})
export class EventCreateComponent implements OnInit {

  eventCreateDto: EventCreateDto = {
    startDate: undefined,
    startTime: undefined,
    endDate: undefined,
    endTime: undefined,
    name: undefined,
    description: undefined
  };

  constructor(
    public authService: AuthService,
    private service: EventService,
    private notificationService: NotificationService,
    private router: Router
  ) {
  }

  ngOnInit() {
  }

  onSubmit(form: NgForm) {
    if (form.valid) {
      let observable: Observable<Event> = this.service.create(this.eventCreateDto);
      observable.subscribe(
        (event: Event) => {
          this.notificationService.showSuccess('Event created successfully.');
          this.router.navigate(['/event-overview'])

        },
        (error) => {
          this.notificationService.showError('Failed to create event. Please try again later.');
        }
      );
    } else {
      this.showFormErrors();
    }
  }

  private showFormErrors(): void {
    if (!this.eventCreateDto.startTime) {
      this.notificationService.showError('Start time is required.');
    }
    if (!this.eventCreateDto.endTime) {
      this.notificationService.showError('End time is required.');
    }
    if (!this.eventCreateDto.name) {
      this.notificationService.showError('Name is required.');
    }
  }

}
