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

const routes: Routes = [
  {path: '', redirectTo: 'login', pathMatch: 'full'},
  {path: 'login', component: LoginComponent},
  {path: 'reservation-simple', component: ReservationSimpleComponent},
  {path: 'reservation-overview', canActivate: mapToCanActivate([AuthGuard]), component: ReservationOverviewComponent},
  {path: 'registration', component: RegistrationComponent},
  {path: 'employees', component: StaffAccountsComponent},
  {path: 'reservation-detail/:id', component: ReservationDetailComponent},
  {path: 'reservation-edit/:id', component: ReservationEditComponent}

];

@NgModule({
  imports: [RouterModule.forRoot(routes, { useHash: true })],
  exports: [RouterModule]
})
export class AppRoutingModule { }
