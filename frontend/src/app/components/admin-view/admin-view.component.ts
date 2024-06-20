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
import {SpecialOfferCreateDto, SpecialOfferListDto} from "../../dtos/special-offer";
import {SpecialOfferService} from "../../services/special-offer.service";
import {formatDay, formatDotDate, formatTime} from "../../util/date-helper";

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
  specialOfferList: SpecialOfferListDto[];
  specialOfferCreateDto: SpecialOfferCreateDto = {
    name: undefined,
    pricePerPax: undefined,
    image: undefined
  }

  public chartOptions: Partial<ChartOptions>;
  currDate: any = new Date(now()).toISOString().split('T')[0];
  currTime: any = new Date(now()).toISOString().split('T')[1].substring(0, 5);

  constructor(
    public authService: AuthService,
    private adminViewService: AdminViewService,
    private specialOfferService: SpecialOfferService,
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
    this.loadSpecialOffers();

    let observable: Observable<ReservationForeCastDto>;
    observable = this.adminViewService.getForeCast(this.adminViewDto);
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

  loadSpecialOffers() {
    let observable: Observable<SpecialOfferListDto[]>;
    observable = this.specialOfferService.getSpecialOffers();
    observable.subscribe({
      next: (value) => {
        this.specialOfferList = value;
      },
      error: (error) => {
        this.notificationService.showError("Failed to load special offers");
      }
    });
  }

  onSubmit(form: NgForm) {

  }

  onFieldChange() {
    console.log(this.adminViewDto)
    let observable: Observable<ReservationForeCastDto>;
    observable = this.adminViewService.getForeCast(this.adminViewDto);
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

  onFileChange(event) {
    const file = event.target.files[0];
    if (file) {
      this.specialOfferCreateDto.image = file;
    }
  }

  createSpecialOffer(specialOfferForm: NgForm) {
    if (specialOfferForm.valid) {
      this.specialOfferService.createSpecialOffer(this.specialOfferCreateDto).subscribe({
        next: (data) => {
          this.notificationService.showSuccess('Special Offer created successfully.');
          this.loadSpecialOffers();
        },
        error: (error) => {
          this.notificationService.showError("Couldn't create Special Offer" + error);
        }
      });
    }
  }

  onClickDetailView() {
    this.router.navigate(['/admin-view/prediction'])
  }

  generateChart() {
    this.chartOptions = null;
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


  protected readonly formatDotDate = formatDotDate;
  protected readonly formatDay = formatDay;
  protected readonly formatTime = formatTime;
}
