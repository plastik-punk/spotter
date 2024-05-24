import {Component, EventEmitter, HostBinding, Input, OnInit, Output} from '@angular/core';

@Component({
  selector: 'app-confirm-dialog',
  templateUrl: './confirm-dialog.component.html',
  styleUrls: ['./confirm-dialog.component.scss'],
  standalone: true
})
export class ConfirmDialogComponent implements OnInit {

  @Input() confirmHeader='?';
  @Input() changeWhatFirstName = '?';
  @Input() changeWhatLastName = '?';
  @Input() makeWhat = '?';
  @Input() yesWhat='?';
  @Input() whatRole='?';
  @Output() confirm = new EventEmitter<void>();

  @HostBinding('class') cssClass = 'modal fade';
  constructor() {
  }

  ngOnInit(): void {
  }

}
