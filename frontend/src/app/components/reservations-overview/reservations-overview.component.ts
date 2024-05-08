import {ChangeDetectorRef, Component, OnInit, TemplateRef, ViewChild, ViewChildren} from '@angular/core';
import {MessageService} from '../../services/message.service';
import {Message} from '../../dtos/message';
import {NgbModal, NgbPaginationConfig} from '@ng-bootstrap/ng-bootstrap';
import {UntypedFormBuilder, NgForm} from '@angular/forms';
import {AuthService} from '../../services/auth.service';

@Component({
  selector: 'app-reservations-overview',
  templateUrl: './reservations-overview.component.html',
  styleUrl: './reservations-overview.component.scss'
})
export class ReservationsOverviewComponent implements OnInit{
  error = false;
  errorMessage = '';

  constructor(
              private authService: AuthService,
              private modalService: NgbModal) {
  }
  ngOnInit(): void {
  }
  /**
   * Error flag will be deactivated, which clears the error message
   */
  vanishError() {
    this.error = false;
  }
  /**
   * Returns true if the authenticated user is an admin
   */
  isAdmin(): boolean {
    return this.authService.getUserRole() === 'ADMIN';
  }
}
