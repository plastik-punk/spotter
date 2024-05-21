import {NgModule} from '@angular/core';
import {mapToCanActivate, RouterModule, Routes} from '@angular/router';
import {HomeComponent} from './components/home/home.component';
import {LoginComponent} from './components/login/login.component';
import {AuthGuard} from './guards/auth.guard';
import {MessageComponent} from './components/message/message.component';
import {RegistrationComponent} from "./components/registration/registration.component";
import {StaffAccountsComponent} from "./components/staff-accounts/staff-accounts.component";
import {ReservationDetailComponent} from "./components/reservation/reservation-detail/reservation-detail.component";

const routes: Routes = [
  {path: '', redirectTo: 'login', pathMatch: 'full'},
  {path: 'login', component: LoginComponent},
  {path: 'home', component: HomeComponent},
  {path: 'message', canActivate: mapToCanActivate([AuthGuard]), component: MessageComponent},
  {path: 'registration', component: RegistrationComponent},
  {path: 'employees', component: StaffAccountsComponent},
  {path: 'reservation-detail', component: ReservationDetailComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {useHash: true})],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
