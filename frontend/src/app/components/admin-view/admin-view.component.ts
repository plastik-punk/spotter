import {Component, OnInit} from '@angular/core';
import {FormsModule, NgForm, ReactiveFormsModule} from "@angular/forms";
import {AuthService} from "../../services/auth.service";
import {AdminViewDto} from "../../dtos/admin-view";
import {ActivatedRoute, Router} from "@angular/router";
import {getLocaleDateFormat} from "@angular/common";
import {now} from "lodash";
import {AdminViewService} from "../../services/adminView.service";

@Component({
  selector: 'app-admin-view',
  templateUrl: './admin-view.component.html',
  styleUrl: './admin-view.component.scss'
})
export class AdminViewComponent implements OnInit{
  adminViewDto: AdminViewDto={
    area: undefined,
    startTime: new Date(now()),
    date: new Date(now())
  }

  constructor(
    public authService: AuthService,
    private service: AdminViewService,
    private router:Router
  ) {
  }
  ngOnInit() {
  }

  onSubmit(form: NgForm) {

  }

  onFieldChange() {

  }

  onClickDetailView() {
    this.router.navigate(['/admin-view/prediction'])
  }
}
