import {Component, OnInit} from '@angular/core';
import {NgForm} from "@angular/forms";
import {AuthService} from "../../services/auth.service";
import {AdminViewDto, ReservationForeCastDto} from "../../dtos/admin-view";
import {Router} from "@angular/router";
import {now} from "lodash";
import {AdminViewService} from "../../services/adminView.service";
import {
  ApexAxisChartSeries,
  ApexChart,
  ApexDataLabels,
  ApexFill,
  ApexLegend,
  ApexPlotOptions,
  ApexStroke,
  ApexTooltip,
  ApexXAxis,
  ApexYAxis
} from "ng-apexcharts";
import {Observable} from "rxjs";
import {NotificationService} from "../../services/notification.service";

export type ChartOptions = {
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
};

@Component({
  selector: 'app-admin-view',
  templateUrl: './admin-view.component.html',
  styleUrl: './admin-view.component.scss'
})
export class AdminViewComponent implements OnInit {
  adminViewDto: AdminViewDto = {
    area: undefined,
    startTime: undefined,
    date: undefined
  }

  forecast: ReservationForeCastDto;

  public chartOptions: Partial<ChartOptions>;
  currDate: any = new Date(now()).toISOString().split('T')[0];
  currTime: any = new Date(now()).toISOString().split('T')[1].substring(0, 5);

  constructor(
    public authService: AuthService,
    private service: AdminViewService,
    private notificationService: NotificationService,
    private router: Router
  ) {

  }

  ngOnInit() {
    this.adminViewDto.area = "Inside";

    this.adminViewDto.date = this.currDate;
    this.adminViewDto.startTime = this.currTime;
    this.chartOptions = {
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


  }

  onSubmit(form: NgForm) {

  }

  onFieldChange() {
    console.log(this.adminViewDto)
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
  }


  onClickDetailView() {
    this.router.navigate(['/admin-view/prediction'])
  }

  generateChart(){
    this.chartOptions=null;
    this.chartOptions = {
      series: [{
        name: "# of reserved tables",
        data: this.forecast.forecast
      }],

      chart: {
        height: 350,
        type: "bar"
      },
      plotOptions: {
        bar: {
          dataLabels: {
            position: "top" // top, center, bottom
          }
        }
      },

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


}
