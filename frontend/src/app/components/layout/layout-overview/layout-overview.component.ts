import {Component, OnInit} from '@angular/core';

import {Router} from "@angular/router";
import {NotificationService} from "../../../services/notification.service";
import {LayoutService} from "../../../services/layout.service";

import {AreaDetailDto, AreaDetailListDto, AreaDto, AreaListDto} from "../../../dtos/layout";


export interface AreaCreateDto {
  name: string;
  isMainArea: boolean;
  closingTime?: string;
  openingTime?: string;
  isOpen: boolean;
  width: number;
  height: number;
  places: PlaceVisualDto[];
}

export interface LayoutCreateDto {
  areas: AreaCreateDto[];
}

export interface PlaceVisualDto {
  placeNumber: number;
  status: boolean;
  reservation: boolean;
  numberOfSeats: number;
  coordinates: CoordinateDto[];
}

export interface CoordinateDto {
  x: number;
  y: number;
}

@Component({
  selector: 'app-layout-overview',
  templateUrl: './layout-overview.component.html',
  styleUrl: './layout-overview.component.scss'
})
export class LayoutOverviewComponent implements OnInit {

  areas: AreaDetailDto[] = [];
  deleteWhat: number | null = null;

  constructor(
    private router: Router,
    private notificationService: NotificationService,
    private layoutService: LayoutService
  ) {
  }


  ngOnInit(): void {

    this.fetchAllAreas();
  }

  private fetchAllAreas() {
    this.layoutService.getAllAreasDetailed().subscribe({
      next: (data: AreaDetailListDto) => {
        this.areas = data.areas;
      },
      error: () => {
        this.notificationService.showError('Failed to fetch areas. Please try again later.');
      },
    });
  }


  onDelete(): void {
    if (!this.deleteWhat) {
      this.notificationService.showError('No area selected.');
      return;
    }

    this.layoutService.deleteArea(this.deleteWhat).subscribe({
      next: () => {
        this.notificationService.showSuccess('Area deleted successfully');
        this.fetchAllAreas();
      },
      error: (error) => {
        this.notificationService.handleError(error);
      }
    });
  }

  openConfirmationDialog(areaId: number): void {
    this.deleteWhat = areaId;
  }

  toggleOpen(areaId: number, isOpen: boolean): void {
    this.layoutService.toggleOpen(areaId, !isOpen).subscribe({
      next: () => {
        this.notificationService.showSuccess('Area updated successfully');
        this.fetchAllAreas();
      },
      error: (error) => {
        this.notificationService.handleError(error);
      }
    });
  }

  toggleMain(areaId: number, isMainArea: boolean): void {
    if (!isMainArea) {
      this.notificationService.showError("There always has to be one main area");
      this.fetchAllAreas();
      return;
    }

    this.layoutService.toggleMain(areaId, isMainArea).subscribe({
      next: () => {
        this.notificationService.showSuccess('Area updated successfully');
        this.fetchAllAreas();
      },
      error: (error) => {
        this.notificationService.handleError(error);
      }
    });
  }

}

