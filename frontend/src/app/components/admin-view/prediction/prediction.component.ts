import {Component, OnInit} from '@angular/core';
import {AdminViewDto, PredictionDto} from "../../../dtos/admin-view";
import {now} from "lodash";
import {NgForm} from "@angular/forms";
import {Observable} from "rxjs";
import {ActivatedRoute, Router} from "@angular/router";
import {AdminViewService} from "../../../services/adminView.service";
import {ToastrService} from "ngx-toastr";
import {NotificationService} from "../../../services/notification.service";
import {AuthService} from "../../../services/auth.service";

@Component({
  selector: 'app-prediction',
  templateUrl: './prediction.component.html',
  styleUrl: './prediction.component.scss'
})
export class PredictionComponent implements OnInit {
  adminViewDto: AdminViewDto = {
    area: undefined,
    startTime: undefined,
    date: undefined
  }
  predictionDto: PredictionDto = {
    prediction: undefined
  };

  currDate: any = new Date(now()).toISOString().split('T')[0];
  currTime: any = new Date(now()).toISOString().split('T')[1].substring(0, 5);

  constructor(
    public authService: AuthService,
    private service: AdminViewService,
    private notification: ToastrService,
    private notificationService: NotificationService,
    private router: Router,
    private route: ActivatedRoute
  ) {
  }

  ngOnInit() {
    this.adminViewDto.date = this.currDate;
    this.adminViewDto.startTime = this.currTime;
    this.adminViewDto.area = "Inside";
  }

  onFieldChange() {

  }

  onSubmit(form: NgForm) {
    console.log(this.adminViewDto)
    if (form.valid) {
      let observable: Observable<PredictionDto>;
      observable = this.service.getPrediction(this.adminViewDto);
      observable.subscribe({
        next: (data) => {
          this.notification.success("Prediction made successfully!");
          this.predictionDto = data;
        },
        error: (error) => {
          this.notificationService.handleError(error);
        },
      });
    }
  }

  getBack() {
    this.router.navigate(['/admin-view']);
  }
}
