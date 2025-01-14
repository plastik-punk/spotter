import {AfterViewInit, Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {FormArray, FormBuilder, FormGroup, Validators} from "@angular/forms";
import {ActivatedRoute, Router} from "@angular/router";
import {NotificationService} from "../../../services/notification.service";
import {LayoutService} from "../../../services/layout.service";
import * as d3 from "d3";
import * as bootstrap from 'bootstrap';
import {AreaDetailDto, AreaDto, AreaListDto} from "../../../dtos/layout";
import {Observable} from "rxjs";

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
  selector: 'app-edit-layout',
  templateUrl: './edit-layout.component.html',
  styleUrl: './edit-layout.component.scss'
})
export class EditLayoutComponent implements OnInit {

  id: number;
  areaEditDto: AreaDetailDto = {
    id: undefined,
    name: undefined,
    mainArea: false,
    closingTime: '',
    openingTime: '',
    open: false,
    width: undefined,
    height: undefined,
    places: []
  };

  @ViewChild('d3Container', {static: false}) d3Container: ElementRef<HTMLDivElement>;
  layoutForm: FormGroup;
  placeForm: FormGroup;
  layoutCreateDto: LayoutCreateDto = {areas: []};
  currentCell: { x: number, y: number } | null = null;
  createdTables: PlaceVisualDto[] = [];
  cellToDelete: { x: number, y: number, placeNumber: number } | null = null;

  confirmHeader = 'Delete Table';
  changeWhatFirstName = '';
  changeWhatLastName = '';
  makeWhat = 'delete';
  yesWhat = 'Delete';
  whatRole = 'Table';

  isGridDrawn = false;  // Add this flag

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private notificationService: NotificationService,
    private layoutService: LayoutService,
    private route: ActivatedRoute
  ) {
    this.layoutForm = this.fb.group({});
    this.placeForm = this.fb.group({});
  }


  ngOnInit(): void {
    this.initializeForm();
    this.setupFormListeners();
    this.id = this.route.snapshot.params['id'];
    if (this.id) {
      let observable: Observable<AreaDetailDto>;
      observable = this.layoutService.getAreaById(this.id);
      observable.subscribe({
        next: (data) => {
          if (data != null) {
            this.areaEditDto = data;
            this.patchFormValues();
          }
        },
        error: (error) => {
          this.notificationService.handleError(error);
        },
      });
    }

  }

  initializeForm(): void {
    this.layoutForm = this.fb.group({
      name: ['', [Validators.required, Validators.maxLength(20)]],
      isMainArea: [false],
      closingTime: [''],
      openingTime: [''],
      isOpen: [false],
      width: [null, [Validators.required, Validators.min(1), Validators.max(16)]],
      height: [null, [Validators.required, Validators.min(1), Validators.max(9)]],
      places: this.fb.array([])
    });

    this.placeForm = this.fb.group({
      placeNumber: ['', Validators.required],
      status: ['', Validators.required],
      numberOfSeats: ['', Validators.required]
    });
  }

  patchFormValues(): void {
    this.layoutForm.patchValue({
      name: this.areaEditDto.name,
      isMainArea: this.areaEditDto.mainArea,
      closingTime: this.areaEditDto.closingTime,
      openingTime: this.areaEditDto.openingTime,
      isOpen: this.areaEditDto.open,
      width: this.areaEditDto.width + 1,
      height: this.areaEditDto.height + 1
    });

    // Initialize the form array with the coordinates
    const placesArray = this.layoutForm.get('places') as FormArray;
    this.areaEditDto.places.forEach(place => {
      const placeGroup = this.fb.group({
        placeNumber: [place.placeNumber],
        status: [place.status],
        numberOfSeats: [place.numberOfSeats],
        coordinates: this.fb.array(place.coordinates.map(coord => this.fb.group(coord)))
      });
      placesArray.push(placeGroup);
    });

    // Initialize the createdTables array with the places
    this.createdTables = this.areaEditDto.places;

    if (this.areaEditDto) {
      this.drawGrid();
    } else {
      this.notificationService.showError('Failed to load layout. Please try again later.');
    }
  }

  setupFormListeners(): void {
    this.layoutForm.valueChanges.subscribe(values => {
      this.updateAreaEditDto(values);
    });
  }

  updateAreaEditDto(values: any): void {
    this.areaEditDto.name = values.name;
    this.areaEditDto.mainArea = values.isMainArea;
    this.areaEditDto.closingTime = values.closingTime;
    this.areaEditDto.openingTime = values.openingTime;
    this.areaEditDto.open = values.isOpen;

    if (this.areaEditDto.width != values.width || this.areaEditDto.height != values.height) {
      this.areaEditDto.width = values.width;
      this.areaEditDto.height = values.height;
      this.drawGrid();
    }
  }

  showExplanation() {
    const infoModal = new bootstrap.Modal(document.getElementById('infoModal'));
    infoModal.show();
  }

  drawGrid(): void {

    const width = this.layoutForm.value.width;
    const height = this.layoutForm.value.height;
    const containerElement = this.d3Container.nativeElement as HTMLDivElement;
    const containerWidth = containerElement.getBoundingClientRect().width;
    const cellSize = containerWidth / width; // Adjust cell size based on container width

    // Clear previous drawings
    d3.select(containerElement).selectAll('*').remove();

    const svg = d3.select(containerElement).append('svg')
      .attr('width', containerWidth)
      .attr('height', height * cellSize);

    for (let x = 0; x < width; x++) {
      for (let y = 0; y < height; y++) {
        const cell = svg.append('rect')
          .attr('x', x * cellSize)
          .attr('y', y * cellSize)
          .attr('width', cellSize)
          .attr('height', cellSize)
          .attr('fill', '#d3d3d3')
          .attr('stroke', '#ffffff')
          .attr('class', `cell-${x}-${y}`)
          .attr('rx', 10)
          .attr('ry', 10)
          .on('click', () => this.onCellClick(x, y));

        svg.append('text')
          .attr('x', x * cellSize + cellSize / 2)
          .attr('y', y * cellSize + cellSize / 2)
          .attr('dy', '.35em')
          .attr('text-anchor', 'middle')
          .attr('class', `table-number-${x}-${y}`)
          .style('fill', 'white');
      }
    }

    this.drawPlaces();

    // Set the grid drawn flag to true
    this.isGridDrawn = true;

  }

  drawPlaces(): void {
    // fill in the segments with the place data stored in the areaEditDto
    this.areaEditDto.places.forEach(place => {
      place.coordinates.forEach(coord => {
        const container = d3.select(this.d3Container.nativeElement).select('svg');
        const cellSize = (this.d3Container.nativeElement as HTMLDivElement).getBoundingClientRect().width / this.layoutForm.value.width;
        container.select(`.table-number-${coord.x}-${coord.y}`)
          .text(place.placeNumber);
        container.select(`.cell-${coord.x}-${coord.y}`)
          .attr('fill', '#212529');
      });
    });
  }

  onCellClick(x: number, y: number): void {
    const table = this.findTableByCoordinates(x, y);
    if (table) {
      this.cellToDelete = {x, y, placeNumber: table.placeNumber};
      if (table.coordinates.length > 1) {
        // Directly delete the coordinate if it is not the last one
        this.deleteCoordinate(x, y, table.placeNumber);
      } else {
        // Show the delete confirmation dialog if it is the last coordinate
        this.showConfirmDialog();
      }
    } else {
      const adjacentTable = this.findAdjacentTable(x, y);
      if (adjacentTable) {
        this.addCoordinatesToAdjacentTable(adjacentTable, x, y);
      } else {
        this.currentCell = {x, y};
        this.placeForm.reset();
        const placeModal = new bootstrap.Modal(document.getElementById('placeModal'));
        placeModal.show();
      }
    }
  }

  deleteCoordinate(x: number, y: number, placeNumber: number): void {
    const table = this.createdTables.find(t => t.placeNumber === placeNumber);
    if (table) {
      table.coordinates = table.coordinates.filter(coord => coord.x !== x || coord.y !== y);

      // Update the form array with the new coordinates
      const placesArray = this.layoutForm.get('places') as FormArray;
      const placeGroup = placesArray.controls.find(control => control.value.placeNumber === placeNumber) as FormGroup;
      if (placeGroup) {
        const coordinatesArray = placeGroup.get('coordinates') as FormArray;
        const coordIndex = coordinatesArray.controls.findIndex(control => control.value.x === x && control.value.y === y);
        if (coordIndex !== -1) {
          coordinatesArray.removeAt(coordIndex);
        }
      }

      // Clear the cell in the grid
      this.clearGridCell(x, y);
    } else {
      this.notificationService.showError('Failed to delete the coordinate. Please try again.');
    }
  }

  finalizeDelete(x: number, y: number, placeNumber: number): void {
    // Remove the table from the createdTables array
    this.createdTables = this.createdTables.filter(t => t.placeNumber !== placeNumber);

    // Remove from form array
    const placesArray = this.layoutForm.get('places') as FormArray;
    const index = placesArray.controls.findIndex(control => control.value.placeNumber === placeNumber);
    if (index !== -1) {
      placesArray.removeAt(index);
    }

    // Clear the cells for the deleted place
    this.clearGridCell(x, y);
  }

  clearGridCell(x: number, y: number): void {
    const container = d3.select(this.d3Container.nativeElement).select('svg');
    container.select(`.table-number-${x}-${y}`).text('');
    container.select(`.cell-${x}-${y}`).attr('fill', '#d3d3d3');
  }

  showConfirmDialog(): void {
    const confirmModal = new bootstrap.Modal(document.getElementById('confirm-dialog'));
    confirmModal.show();
  }

  confirmDelete(): void {
    if (this.cellToDelete) {
      const {x, y, placeNumber} = this.cellToDelete;
      this.finalizeDelete(x, y, placeNumber);
    } else {
      this.notificationService.showError('No cell selected for deletion.');
    }

    // Hide the modal
    const confirmModal = bootstrap.Modal.getInstance(document.getElementById('confirm-dialog'));
    if (confirmModal) {
      confirmModal.hide();
    }
  }

  findTableByCoordinates(x: number, y: number): PlaceVisualDto | undefined {
    return this.createdTables.find(table =>
      table.coordinates.some(coord => coord.x === x && coord.y === y)
    );
  }

  findAdjacentTable(x: number, y: number): PlaceVisualDto | undefined {
    const directions = [
      {dx: -1, dy: 0},
      {dx: 1, dy: 0},
      {dx: 0, dy: -1},
      {dx: 0, dy: 1}
    ];

    for (const {dx, dy} of directions) {
      const adjacentCell = this.createdTables.find(table =>
        table.coordinates.some(coord => coord.x === x + dx && coord.y === y + dy)
      );
      if (adjacentCell) {
        return adjacentCell;
      }
    }

    return undefined;
  }

  addCoordinatesToAdjacentTable(table: PlaceVisualDto, x: number, y: number): void {
    table.coordinates.push({x, y});

    // Find the corresponding form group and update its coordinates array
    const placesArray = this.layoutForm.get('places') as FormArray;
    const placeGroup = placesArray.controls.find(control => control.value.placeNumber === table.placeNumber) as FormGroup;
    if (placeGroup) {
      const coordinatesArray = placeGroup.get('coordinates') as FormArray;
      coordinatesArray.push(this.fb.group({x, y}));
    }

    const container = d3.select(this.d3Container.nativeElement).select('svg');
    const cellSize = (this.d3Container.nativeElement as HTMLDivElement).getBoundingClientRect().width / this.layoutForm.value.width;
    container.select(`.table-number-${x}-${y}`)
      .text(table.placeNumber);
    container.select(`.cell-${x}-${y}`)
      .attr('fill', '#212529');
  }

  savePlace(): void {
    if (this.placeForm.valid && this.currentCell) {
      const placeNumber = this.placeForm.value.placeNumber;

      // Check if the table number already exists in any area
      const isDuplicate = this.layoutCreateDto.areas.some(area =>
        area.places.some(place => place.placeNumber === placeNumber)
      );

      if (isDuplicate || this.createdTables.some(table => table.placeNumber === placeNumber)) {
        this.notificationService.showError('Place number is already taken.');
        return;
      }

      const placeData: PlaceVisualDto = {
        placeNumber: this.placeForm.value.placeNumber,
        status: this.placeForm.value.status === 'true',
        reservation: false,
        numberOfSeats: this.placeForm.value.numberOfSeats,
        coordinates: [{x: this.currentCell.x, y: this.currentCell.y}]
      };

      // Initialize the form group for the place with coordinates as a form array
      const placeGroup = this.fb.group({
        placeNumber: [placeData.placeNumber],
        status: [placeData.status],
        numberOfSeats: [placeData.numberOfSeats],
        coordinates: this.fb.array(placeData.coordinates.map(coord => this.fb.group(coord)))
      });

      const placesArray = this.layoutForm.get('places') as FormArray;
      placesArray.push(placeGroup);
      this.createdTables.push(placeData);

      // Update the grid with the table number and change color
      const container = d3.select(this.d3Container.nativeElement).select('svg');
      const cellSize = (this.d3Container.nativeElement as HTMLDivElement).getBoundingClientRect().width / this.layoutForm.value.width;
      container.select(`.table-number-${this.currentCell.x}-${this.currentCell.y}`)
        .text(placeData.placeNumber);
      container.select(`.cell-${this.currentCell.x}-${this.currentCell.y}`)
        .attr('fill', '#212529');

      const placeModal = bootstrap.Modal.getInstance(document.getElementById('placeModal'));
      placeModal.hide();
      this.placeForm.reset();
    } else {
      this.notificationService.showError('Please fill out all required fields for the place.');
    }
  }

  getCurrentAreaTables(): PlaceVisualDto[] {
    return this.createdTables;
  }

  isMainAreaDisabled(): boolean {
    return this.layoutCreateDto.areas.some(area => area.isMainArea);
  }

  onSubmit(): void {
    if (this.layoutForm.valid) {
      // Show the confirm save layout modal
      const confirmSaveLayoutModal = new bootstrap.Modal(document.getElementById('confirmSaveLayoutModal'));
      confirmSaveLayoutModal.show();
    } else {
      this.notificationService.showError('Please fill out all required fields before submitting.');
    }
  }

  confirmSaveLayout(): void {
    if (this.layoutForm.valid) {
      this.areaEditDto.places = this.createdTables;
      this.layoutService.updateArea(this.areaEditDto).subscribe({
        next: () => {
          this.notificationService.showSuccess('Area saved successfully.');
          // Hide the modal after saving
          const confirmSaveLayoutModal = bootstrap.Modal.getInstance(document.getElementById('confirmSaveLayoutModal'));
          if (confirmSaveLayoutModal) {
            confirmSaveLayoutModal.hide();
          }
          this.router.navigate(['layout-overview']);
        },
        error: () => {
          this.notificationService.showError('Failed to save area. Please try again later.');
        }
      });


    } else {
      this.notificationService.showError('Please fill out all required fields before saving.');
    }
  }

}
