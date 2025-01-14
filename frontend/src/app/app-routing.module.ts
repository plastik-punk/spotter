import {NgModule} from '@angular/core';
import {mapToCanActivate, RouterModule, Routes} from '@angular/router';
import {ReservationSimpleComponent} from './components/reservation/reservation-simple/reservation-simple.component';
import {LoginComponent} from './components/login/login.component';
import {AuthGuard} from './guards/auth.guard';
import {
  ReservationOverviewComponent
} from './components/reservation/reservation-overview/reservation-overview.component';
import {RegistrationComponent} from "./components/registration/registration.component";
import {StaffAccountsComponent} from "./components/staff-accounts/staff-accounts.component";
import {ReservationEditComponent} from "./components/reservation/reservation-edit/reservation-edit.component";
import {EventOverviewComponent} from "./components/event/event-overview/event-overview.component";
import {EventDetailComponent} from "./components/event/event-detail/event-detail.component";
import {EventEditComponent} from "./components/event/event-edit/event-edit.component";

import {EventCreateComponent} from "./components/event/event-create/event-create.component";

import {ReservationLayoutComponent} from "./components/reservation/reservation-layout/reservation-layout.component";
import {AdminViewComponent} from "./components/admin-view/admin-view.component";
import {CreateLayoutComponent} from "./components/layout/create-layout/create-layout.component";
import {EmployeeViewComponent} from "./components/employee-view/employee-view.component";
import {EditLayoutComponent} from "./components/layout/edit-layout/edit-layout.component";
import {LayoutOverviewComponent} from "./components/layout/layout-overview/layout-overview.component";
import {
  PermanentReservationDetailsComponent
} from "./components/reservation/permanent-reservation-details/permanent-reservation-details.component";

const routes: Routes = [
  {path: '', redirectTo: 'login', pathMatch: 'full'},
  {path: 'login', component: LoginComponent},
  {path: 'reservation-simple', component: ReservationSimpleComponent},
  {path: 'reservation-overview', canActivate: mapToCanActivate([AuthGuard]), component: ReservationOverviewComponent},
  {path: 'registration', component: RegistrationComponent},
  {path: 'employees', component: StaffAccountsComponent},
  {path: 'reservation-edit/:id', component: ReservationEditComponent},
  {path: 'reservation-layout', component: ReservationLayoutComponent},
  {path: 'event-overview', canActivate: mapToCanActivate([AuthGuard]), component: EventOverviewComponent},
  {path: 'event-detail/:id', canActivate: mapToCanActivate([AuthGuard]), component: EventDetailComponent},
  {path: 'event-edit/:id', canActivate: mapToCanActivate([AuthGuard]), component: EventEditComponent},
  {path: 'event-create', canActivate: mapToCanActivate([AuthGuard]), component: EventCreateComponent},
  {path: 'reservation-edit/:id', component: ReservationEditComponent},
  {path: 'admin-view', component: AdminViewComponent},

  {path: 'create-layout', canActivate: mapToCanActivate([AuthGuard]), component: CreateLayoutComponent},
  {path: 'edit-layout/:id', canActivate: mapToCanActivate([AuthGuard]), component: EditLayoutComponent},
  {path: 'layout-overview', canActivate: mapToCanActivate([AuthGuard]), component: LayoutOverviewComponent},

  {path: 'employee-view', component: EmployeeViewComponent},

  { path: 'permanent-reservation-details/:hashedId', component: PermanentReservationDetailsComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {useHash: true})],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
