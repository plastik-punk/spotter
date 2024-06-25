import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {
  private notificationSubject = new Subject<NotificationMessage>();
  notifications$ = this.notificationSubject.asObservable();

  showSuccess(message: string) {
    this.notificationSubject.next({ type: 'success', message });
  }

  showError(message: string) {
    this.notificationSubject.next({ type: 'error', message });
  }

  handleError(error: any) {
    this.showError(error.error.message || 'An error occurred');
  }

  handleSuccess(message: string) {
    this.showSuccess(message);
  }
}

export interface NotificationMessage {
  type: 'success' | 'error';
  message: string;
  show?: boolean;
}
