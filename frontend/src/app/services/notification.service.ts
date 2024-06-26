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

  showValidationError(message: string) {
    this.notificationSubject.next({ type: 'validation', message });
  }

  showConflictError(message: string) {
    this.notificationSubject.next({ type: 'conflict', message });
  }

  showNotFoundError(message: string) {
    this.notificationSubject.next({ type: 'notFound', message });
  }

  showIllegalArgumentError(message: string) {
    this.notificationSubject.next({ type: 'illegalArgument', message });
  }

  showInternalServerError(message: string) {
    this.notificationSubject.next({ type: 'internalServer', message });
  }

  showUnauthorizedError(message: string) {
    this.notificationSubject.next({ type: 'unauthorized', message });
  }

  handleError(error: any) {
    const message = error.error.message || 'An error occurred';
    switch (error.status) {
      case 422:
        this.showValidationError(message);
        break;
      case 409:
        this.showConflictError(message);
        break;
      case 404:
        this.showNotFoundError(message);
        break;
      case 400:
        this.showIllegalArgumentError(message);
        break;
      case 500:
        this.showInternalServerError(message);
        break;
      case 401:
        this.showUnauthorizedError(message);
        break;
      default:
        this.showError(message);
        break;
    }
  }

  handleSuccess(message: string) {
    this.showSuccess(message);
  }
}

export interface NotificationMessage {
  type: 'success' | 'error' | 'validation' | 'conflict' | 'notFound' | 'illegalArgument' | 'internalServer' | 'unauthorized';
  message: string;
  show?: boolean;
}
