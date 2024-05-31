import { NgModule } from '@angular/core';
import {mapToCanActivate, RouterModule, Routes} from '@angular/router';
import { ReservationSimpleComponent } from './components/reservation/reservation-simple/reservation-simple.component';
import { LoginComponent } from './components/login/login.component';
import { AuthGuard } from './guards/auth.guard';
import { MessageComponent } from './components/message/message.component';
import { ReservationOverviewComponent } from './components/reservation/reservation-overview/reservation-overview.component';
import { RegistrationComponent } from "./components/registration/registration.component";
import { StaffAccountsComponent } from "./components/staff-accounts/staff-accounts.component";
import { ReservationDetailComponent } from "./components/reservation/reservation-detail/reservation-detail.component";
import { ReservationEditComponent } from "./components/reservation/reservation-edit/reservation-edit.component";
import {EventOverviewComponent} from "./components/event/event-overview/event-overview.component";
import {EventDetailComponent} from "./components/event/event-detail/event-detail.component";
import {EventEditComponent} from "./components/event/event-edit/event-edit.component";

import {EventCreateComponent} from "./components/event/event-create/event-create.component";

import {
    ReservationLayoutComponent
} from "./components/reservation/reservation-layout/reservation-layout.component";

const routes: Routes = [
  {path: '', redirectTo: 'login', pathMatch: 'full'},
  {path: 'login', component: LoginComponent},
  {path: 'reservation-simple', component: ReservationSimpleComponent},
  {path: 'message', canActivate: mapToCanActivate([AuthGuard]), component: MessageComponent},
  {path: 'reservation-overview', canActivate: mapToCanActivate([AuthGuard]), component: ReservationOverviewComponent},
  {path: 'message', canActivate: mapToCanActivate([AuthGuard]), component: MessageComponent},
  {path: 'registration', component: RegistrationComponent},
  {path: 'employees', component: StaffAccountsComponent},
  {path: 'reservation-detail/:id', component: ReservationDetailComponent},
  {path: 'reservation-edit/:id', component: ReservationEditComponent},
  {path: 'reservation-layout', component: ReservationLayoutComponent},
  {path: 'event-overview', canActivate: mapToCanActivate([AuthGuard]), component: EventOverviewComponent},
  {path: 'event-detail/:id', canActivate: mapToCanActivate([AuthGuard]), component: EventDetailComponent},
  {path: 'event-edit/:id', canActivate: mapToCanActivate([AuthGuard]), component: EventEditComponent},
  {path: 'event-create', canActivate: mapToCanActivate([AuthGuard]), component: EventCreateComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { useHash: true })],
  exports: [RouterModule]
})
export class AppRoutingModule { }
