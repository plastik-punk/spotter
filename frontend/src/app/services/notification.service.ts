import { Injectable } from '@angular/core';
import {ToastrService} from "ngx-toastr";

@Injectable({
  providedIn: 'root'
})

export class NotificationService {
    constructor(
      private notification: ToastrService,
    ) { }

    public handleError(error: any): void {
      this.notification.error(
        error.error.errors,
        error.error.message,
        {
          enableHtml: true,
          timeOut: 5000,
        },
      );
    }

    public handleSuccess(message: string): void {
      this.notification.success(
        message,
        null,
        {
          timeOut: 5000,
          enableHtml: true,
        },
      );
    }

    public handleInfo(message: string): void {
      this.notification.info(
        message,
        null,
        {
          timeOut: 5000,
          enableHtml: true,
        },
      );
    }
}
