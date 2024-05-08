import {Component, OnInit} from '@angular/core';
import {AuthService} from '../../services/auth.service';
import {NgForm} from "@angular/forms";
import {Reservation} from "../../dtos/reservation";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
  reservation: Reservation = {
    id: undefined,
    startTime: undefined,
    endTime: undefined,
    pax: undefined,
    notes: undefined,
    placeId: undefined,
    userId: undefined
};

  constructor(public authService: AuthService) { }

  ngOnInit() {
  }

  onSubmit(form: NgForm) {


  }
}
