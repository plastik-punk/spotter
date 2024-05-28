import {Component, OnInit} from '@angular/core';
import {AdminViewDto, PredictionDto} from "../../../dtos/admin-view";
import {now} from "lodash";
import {NgForm} from "@angular/forms";
import {Observable} from "rxjs";
import {ReservationEditDto} from "../../../dtos/reservation";
import {ActivatedRoute, Router} from "@angular/router";
import {AdminViewService} from "../../../services/admin-view.service";
import {ToastrService} from "ngx-toastr";
import {NotificationService} from "../../../services/notification.service";
import {AuthService} from "../../../services/auth.service";

@Component({
  selector: 'app-prediction',
  templateUrl: './prediction.component.html',
  styleUrl: './prediction.component.scss'
})
export class PredictionComponent implements OnInit{
  adminViewDto: AdminViewDto={
    area: undefined,
    startTime: undefined,
    date: undefined
  }
  predictionDto: PredictionDto={
    prediction: undefined
  };

  constructor(
    public authService: AuthService,
    private service: AdminViewService,
    private notification: ToastrService,
    private notificationService: NotificationService,
    private router:Router,
    private route: ActivatedRoute
  ) {
  }
  ngOnInit() {
    this.adminViewDto.date = new Date(now());
    this.adminViewDto.startTime = new Date(now());
  }

  onFieldChange() {

  }

  onSubmit(form: NgForm) {
    if (form.valid) {
      let observable: Observable<PredictionDto>;
      observable = this.service.getPrediction(this.adminViewDto);
      observable.subscribe({
        next: (data) => {
          this.notification.success("Reservation updated successfully");
          if (this.authService.isLoggedIn()) {
            this.router.navigate(['/reservation-overview']);
          }
        },
        error: (error) => {
          this.notificationService.handleError(error);
        },
      });
    }
  }
}
