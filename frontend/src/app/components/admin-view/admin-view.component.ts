import {Component, OnInit} from '@angular/core';
import {NgForm} from "@angular/forms";
import {AuthService} from "../../services/auth.service";
import {AdminViewDto, PredictionDto, ReservationForeCastDto} from "../../dtos/admin-view";
import {Router} from "@angular/router";

import {AdminViewService} from "../../services/adminView.service";
import {
  ApexAxisChartSeries,
  ApexChart,
  ApexDataLabels,
  ApexFill,
  ApexLegend,
  ApexNonAxisChartSeries,
  ApexPlotOptions,
  ApexResponsive,
  ApexStroke,
  ApexTooltip,
  ApexXAxis,
  ApexYAxis
} from "ng-apexcharts";
import {Observable} from "rxjs";
import {NotificationService} from "../../services/notification.service";
import {AreaDto, AreaListDto} from "../../dtos/layout";
import {ReservationService} from "../../services/reservation.service";
import {LayoutService} from "../../services/layout.service";
import {formatIsoDate} from "../../util/date-helper";
import {ToastrService} from "ngx-toastr";


export type ChartBarOptions = {
  series: ApexAxisChartSeries;
  chart: ApexChart;
  dataLabels: ApexDataLabels;
  plotOptions: ApexPlotOptions;
  yaxis: ApexYAxis;
  xaxis: ApexXAxis;
  fill: ApexFill;
  tooltip: ApexTooltip;
  stroke: ApexStroke;
  legend: ApexLegend;
  colors: string[];
};
export type ChartDonutOptions = {
  series: ApexNonAxisChartSeries;
  chart: ApexChart;
  responsive: ApexResponsive[];
  labels: any
};

@Component({
  selector: 'app-admin-view',
  templateUrl: './admin-view.component.html',
  styleUrl: './admin-view.component.scss'
})
export class AdminViewComponent implements OnInit {
  adminViewDto: AdminViewDto = {
    areaId: undefined,
    startTime: undefined,
    date: undefined
  }

  predictionDto: PredictionDto = {
    predictionText: "",
    areaNames: undefined,
    predictions: undefined
  };

  forecast: ReservationForeCastDto;

  public chartReservedTableOptions: Partial<ChartBarOptions>;
  public chartPredictionOptions: Partial<ChartDonutOptions>;


  currDate: any;
  currTime: any;

  isUnusual: any;
  tooltip: string = "Forecast is being calculated";
  barColors: string[] = ["#33b2df", "#33b2df", "#33b2df", "#33b2df", "#33b2df", "#33b2df", "#33b2df"];

  predictionTitle: string = "Prediction for Amount Of Employees needed";

  areas: AreaDto[] = [];
  selectedAreaId: number = 1;

  constructor(
    public authService: AuthService,
    private service: AdminViewService,
    private reservationService: ReservationService,
    private layoutService: LayoutService,
    private notificationService: NotificationService,
    private notification: ToastrService,
    private router: Router
  ) {

  }

  ngOnInit() {
    this.adminViewDto.areaId = this.selectedAreaId;

    this.initializeDateAndTime();
    this.fetchAllAreas();

    this.chartReservedTableOptions = {
      series: [
        {
          name: "# of reserved tables",
          data: [0, 0, 0, 0, 0, 0, 0]
        }
      ],
      chart: {
        height: 350,
        type: "bar"
      },
    };

    this.chartPredictionOptions = {
      series: [0],
      chart: {
        type: "donut"
      },
      labels: [""]
    };

    this.onFieldChange();


  }

  private initializeDateAndTime() {
    const now = new Date();
    this.currTime = now.toTimeString().slice(0, 5);
    this.currDate = formatIsoDate(now);
    this.adminViewDto.date = this.currDate;
    this.adminViewDto.startTime = this.currTime;
  }

  onSubmit(form: NgForm) {

  }

  onFieldChange() {
    this.adminViewDto.date = this.currDate;
    this.adminViewDto.startTime = this.currTime;
    let observable: Observable<ReservationForeCastDto>;
    observable = this.service.getForeCast(this.adminViewDto);
    observable.subscribe({
      next: (value) => {
        this.forecast = value;
        this.generateChart();
      },
      error: (error) => {
        this.notificationService.handleError(error);
      }
    });
    this.getUnusualReservationsNotification();
  }


  onClickGetPrediction() {

    let observable: Observable<PredictionDto>;
    observable = this.service.getPrediction(this.adminViewDto);
    observable.subscribe({
      next: (data) => {
        this.notificationService.handleSuccess("Prediction made successfully!");
        this.predictionDto.predictionText = data.predictionText;
        this.predictionDto.areaNames = data.areaNames;
        this.predictionDto.predictions = data.predictions;
        this.chartPredictionOptions = null;
        this.chartPredictionOptions = {
          series: data.predictions,
          chart: {
            type: "donut"
          },
          labels: data.areaNames
        };
        this.predictionTitle = data.predictionText;
      },
      error: (error) => {
        this.notificationService.handleError(error);
      },
    });
  }

  generateChart() {
    this.chartReservedTableOptions = null;
    this.chartReservedTableOptions = {
      series: [{
        name: "# of reserved tables",
        data: this.forecast.forecast,
      }],

      chart: {
        height: 350,
        type: "bar"
      },
      plotOptions: {
        bar: {
          distributed: true,
          dataLabels: {
            position: "top" // top, center, bottom
          },

        }
      },
      colors: ["#304758", "#304758", "#304758", "#33b2df", "#33b2df", "#33b2df", "#33b2df"],

      dataLabels: {
        enabled: true,
        formatter: function (val) {
          return val + "";
        },
        offsetY: -20,
        style: {
          fontSize: "12px",
          colors: ["#304758"]
        }
      },

      xaxis: {
        categories: this.forecast.days,
      },


      fill: {
        type: "gradient",
        gradient: {
          shade: "light",
          type: "horizontal",
          shadeIntensity: 0.25,
          gradientToColors: undefined,
          inverseColors: true,
          opacityFrom: 1,
          opacityTo: 1,
          stops: [50, 0, 100, 100]
        }
      },
      yaxis: {
        max: this.forecast.maxPlace,
        axisBorder: {
          show: false
        },
        axisTicks: {
          show: false
        },
        labels: {
          show: false,
          formatter: function (val) {
            return val + " Tables";
          }
        }
      },
    };
  }

  onAreaChange(event: Event) {
    const selectElement = event.target as HTMLSelectElement;
    this.selectedAreaId = Number(selectElement.value);
    //this.reservationLayoutCheckAvailabilityDto.areaId = this.selectedAreaId;
    //this.fetchLayoutAvailability();
  }

  private fetchAllAreas() {
    this.layoutService.getAllAreas().subscribe({
      next: (data: AreaListDto) => {
        this.areas = data.areas;
        if (this.areas.length > 0) {
          this.selectedAreaId = this.selectedAreaId || this.areas[0].id;
          //this.reservationLayoutCheckAvailabilityDto.areaId = this.selectedAreaId;
          //this.fetchLayoutAvailability(); TODO: Implement Layout to edit in Admin view
        }
      },
      error: () => {
        this.notificationService.showError('Failed to fetch areas. Please try again later.');
      },
    });
  }

  private getUnusualReservationsNotification() {
    this.isUnusual = false;
    this.service.getUnusualReservations(this.adminViewDto).subscribe({
      next: (data) => {
        if (data) {
          this.tooltip = '';
          this.isUnusual = data.unusual;
          for (let i = 0; i < data.days.length; i++) {
            if (data.days[i] && data.messages[i])
              this.tooltip += '' + data.days[i] + ': ' + data.messages[i] + '\r\r';
          }
        }
      },
      error: () => {
        this.notificationService.showError('Failed to fetch unusual reservations. Please try again later.');
      },
    });
  }
}
