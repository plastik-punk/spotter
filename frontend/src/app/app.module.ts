import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {HttpClientModule} from '@angular/common/http';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {SidebarComponent} from './components/sidebar/sidebar.component';
import {FooterComponent} from './components/footer/footer.component';
import {ReservationSimpleComponent} from './components/reservation/reservation-simple/reservation-simple.component';
import {LoginComponent} from './components/login/login.component';
import {
  ReservationOverviewComponent
} from './components/reservation/reservation-overview/reservation-overview.component';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {httpInterceptorProviders} from './interceptors';
import {
  ConfirmationDialogDeleteComponent
} from './components/confirmation-dialog/confirmation-dialog-delete/confirmation-dialog-delete.component';
import {ReservationLayoutComponent} from './components/reservation/reservation-layout/reservation-layout.component';
import {ToastrModule} from 'ngx-toastr';
import {BrowserAnimationsModule, NoopAnimationsModule} from '@angular/platform-browser/animations';
import {NotificationComponent} from './components/notification/notification.component';
import {EventOverviewComponent} from "./components/event/event-overview/event-overview.component";
import {EventDetailComponent} from "./components/event/event-detail/event-detail.component";
import {EventEditComponent} from "./components/event/event-edit/event-edit.component";
import {EventCreateComponent} from "./components/event/event-create/event-create.component";
import {GroupByDatePipe} from "./pipes/group-by-date.pipe";
import {NgIf} from "@angular/common";
import {RouterLink} from "@angular/router";
import {AdminViewComponent} from "./components/admin-view/admin-view.component";
import {ReservationEditComponent} from "./components/reservation/reservation-edit/reservation-edit.component";
import {NgApexchartsModule} from "ng-apexcharts";
import {CreateLayoutComponent} from "./components/layout/create-layout/create-layout.component";
import {ConfirmDialogComponent} from "./components/confirm-dialog/confirm-dialog.component";
import {EmployeeViewComponent} from "./components/employee-view/employee-view.component";
import {EditLayoutComponent} from "./components/layout/edit-layout/edit-layout.component";
import {LayoutOverviewComponent} from "./components/layout/layout-overview/layout-overview.component";
import {
  PermanentReservationDetailsComponent
} from "./components/reservation/permanent-reservation-details/permanent-reservation-details.component";

@NgModule({

  declarations: [
    AppComponent,
    SidebarComponent,
    FooterComponent,
    ReservationSimpleComponent,
    LoginComponent,
    ReservationEditComponent,
    ReservationOverviewComponent,
    ConfirmationDialogDeleteComponent,
    NotificationComponent,
    EventOverviewComponent,
    EventDetailComponent,
    EventEditComponent,
    EventCreateComponent,
    ReservationLayoutComponent,
    AdminViewComponent,
    CreateLayoutComponent,
    EmployeeViewComponent,
    EditLayoutComponent,
    LayoutOverviewComponent,
    PermanentReservationDetailsComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule,
    HttpClientModule,
    NgbModule,
    FormsModule,
    ToastrModule.forRoot(),
    BrowserAnimationsModule,
    NoopAnimationsModule,
    GroupByDatePipe,
    FormsModule,
    NgIf,
    ReactiveFormsModule,
    RouterLink,
    NoopAnimationsModule,
    NgApexchartsModule,
    ConfirmDialogComponent
  ],
  providers: [httpInterceptorProviders],
  exports: [
    ConfirmationDialogDeleteComponent
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
