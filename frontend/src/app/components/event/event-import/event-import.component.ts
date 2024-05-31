import { Component } from '@angular/core';
import {EventService} from "../../../services/event.service";

@Component({
  selector: 'app-event-import',
  templateUrl: './event-import.component.html',
  styleUrls: ['./event-import.component.scss']
})
export class EventImportComponent {
  selectedFile: File | null = null;

  constructor(private eventService: EventService) {}

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.selectedFile = input.files[0];
    }
  }

  onUpload(): void {
    if (this.selectedFile) {
      this.eventService.uploadIcsFile(this.selectedFile).subscribe(
        response => {
          console.log('File uploaded successfully');
        },
        error => {
          console.error('Error uploading file', error);
        }
      );
    }
  }
}
