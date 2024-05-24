import { Component, OnInit } from '@angular/core';
import { NotificationService, NotificationMessage } from '../../services/notification.service';

@Component({
  selector: 'app-notification',
  templateUrl: './notification.component.html',
  styleUrls: ['./notification.component.scss']
})
export class NotificationComponent implements OnInit {
  notifications: NotificationMessage[] = [];

  constructor(private notificationService: NotificationService) { }

  ngOnInit(): void {
    this.notificationService.notifications$.subscribe(notification => {
      this.notifications.push(notification);
      setTimeout(() => this.addShowClass(notification), 10);
      setTimeout(() => this.removeNotification(notification), 3000); // Remove after 3 seconds
    });
  }

  addShowClass(notification: NotificationMessage): void {
    const index = this.notifications.indexOf(notification);
    if (index !== -1) {
      this.notifications[index].show = true;
    }
  }

  removeNotification(notification: NotificationMessage): void {
    const index = this.notifications.indexOf(notification);
    if (index !== -1) {
      this.notifications[index].show = false;
      setTimeout(() => {
        this.notifications = this.notifications.filter(n => n !== notification);
      }, 500); // Wait for the fade-out transition
    }
  }
}
