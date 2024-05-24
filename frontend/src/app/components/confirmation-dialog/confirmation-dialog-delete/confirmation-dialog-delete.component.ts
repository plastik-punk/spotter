import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';

@Component({
  selector: 'app-confirmation-dialog-delete',
  templateUrl: './confirmation-dialog-delete.component.html',
  styleUrl: './confirmation-dialog-delete.component.scss'
})
export class ConfirmationDialogDeleteComponent implements OnInit {
  @Input() deleteThis: any;
  @Input() dialogTitle: string;
  @Input() dialogText: string;
  @Output() confirm = new EventEmitter<void>();

  constructor() {}

  ngOnInit(): void {}
}
