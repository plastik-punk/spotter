import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { SidebarComponent } from './components/sidebar/sidebar.component';
import { FooterComponent } from './components/footer/footer.component';
import { ReservationSimpleComponent } from './components/reservation/reservation-simple/reservation-simple.component';
import { LoginComponent } from './components/login/login.component';
import { MessageComponent } from './components/message/message.component';
import { ReservationOverviewComponent } from './components/reservation/reservation-overview/reservation-overview.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { httpInterceptorProviders } from './interceptors';
import { ReservationDetailComponent } from './components/reservation/reservation-detail/reservation-detail.component';
import { ConfirmationDialogDeleteComponent } from './components/confirmation-dialog/confirmation-dialog-delete/confirmation-dialog-delete.component';
import { ToastrModule } from 'ngx-toastr';
import { BrowserAnimationsModule, NoopAnimationsModule } from '@angular/platform-browser/animations';
import { NotificationComponent } from './components/notification/notification.component';
import {EventOverviewComponent} from "./components/event/event-overview/event-overview.component";
import {EventDetailComponent} from "./components/event/event-detail/event-detail.component";
import {EventEditComponent} from "./components/event/event-edit/event-edit.component";
import {EventCreateComponent} from "./components/event/event-create/event-create.component";
@NgModule({
    declarations: [
        AppComponent,
        SidebarComponent,
        FooterComponent,
        ReservationSimpleComponent,
        LoginComponent,
        MessageComponent,
        ReservationDetailComponent,
        ReservationOverviewComponent,
        ConfirmationDialogDeleteComponent,
        NotificationComponent,
        EventOverviewComponent,
        EventDetailComponent,
        EventEditComponent,
        EventCreateComponent
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
        NoopAnimationsModule
    ],
    providers: [httpInterceptorProviders],
    exports: [
        ConfirmationDialogDeleteComponent
    ],
    bootstrap: [AppComponent]
})
export class AppModule { }
