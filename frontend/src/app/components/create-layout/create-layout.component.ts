import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { FormBuilder, FormGroup, Validators, FormArray } from '@angular/forms';
import * as d3 from 'd3';
import * as bootstrap from 'bootstrap';

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
  selector: 'app-create-layout',
  templateUrl: './create-layout.component.html',
  styleUrls: ['./create-layout.component.scss']
})
export class CreateLayoutComponent implements OnInit {
  @ViewChild('d3Container', { static: false }) d3Container: ElementRef<HTMLDivElement>;
  layoutForm: FormGroup;
  placeForm: FormGroup;
  layoutCreateDto: LayoutCreateDto = { areas: [] };
  currentCell: { x: number, y: number } | null = null;
  createdTables: PlaceVisualDto[] = [];

  constructor(private fb: FormBuilder) {}

  ngOnInit(): void {
    this.layoutForm = this.fb.group({
      name: ['', [Validators.required, Validators.maxLength(20)]],
      isMainArea: [false],
      closingTime: [''],
      openingTime: [''],
      isOpen: [false],
      width: [1, [Validators.required, Validators.min(1), Validators.max(16)]],
      height: [1, [Validators.required, Validators.min(1), Validators.max(9)]],
      places: this.fb.array([])  // Initially empty, can be populated later
    });

    this.placeForm = this.fb.group({
      placeNumber: ['', Validators.required],
      status: ['', Validators.required],
      numberOfSeats: ['', Validators.required]
    });
  }

  addArea(): void {
    if (this.layoutForm.valid) {
      const area: AreaCreateDto = {
        ...this.layoutForm.value,
        places: this.layoutForm.value.places
      };
      this.layoutCreateDto.areas.push(area);
      this.layoutForm.reset();
      this.layoutForm.patchValue({ isMainArea: false, isOpen: false }); // Reset main area and open checkboxes
      this.ensureSingleMainArea();
    }
  }

  ensureSingleMainArea(): void {
    const mainAreaExists = this.layoutCreateDto.areas.some(area => area.isMainArea);
    if (mainAreaExists) {
      this.layoutForm.patchValue({ isMainArea: false });
    }
  }

  onMainAreaChange(index: number): void {
    if (this.layoutForm.get('isMainArea').value) {
      this.layoutCreateDto.areas.forEach(area => {
        if (area.isMainArea) {
          area.isMainArea = false;
        }
      });
    }
  }

  drawGrid(): void {
    if (this.layoutForm.valid) {
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
            .on('click', (event) => this.onCellClick(x, y));

          svg.append('text')
            .attr('x', x * cellSize + cellSize / 2)
            .attr('y', y * cellSize + cellSize / 2)
            .attr('dy', '.35em')
            .attr('text-anchor', 'middle')
            .attr('class', `table-number-${x}-${y}`);
        }
      }
    }
  }

  onCellClick(x: number, y: number): void {
    const adjacentTable = this.findAdjacentTable(x, y);
    if (adjacentTable) {
      this.addCoordinatesToAdjacentTable(adjacentTable, x, y);
    } else {
      this.currentCell = { x, y };
      const placeModal = new bootstrap.Modal(document.getElementById('placeModal'));
      placeModal.show();
    }
  }

  findAdjacentTable(x: number, y: number): PlaceVisualDto | undefined {
    const directions = [
      { dx: -1, dy: 0 },
      { dx: 1, dy: 0 },
      { dx: 0, dy: -1 },
      { dx: 0, dy: 1 }
    ];

    for (const { dx, dy } of directions) {
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
    table.coordinates.push({ x, y });

    const container = d3.select(this.d3Container.nativeElement).select('svg');
    const cellSize = (this.d3Container.nativeElement as HTMLDivElement).getBoundingClientRect().width / this.layoutForm.value.width;
    container.select(`.table-number-${x}-${y}`)
      .text(table.placeNumber);
    container.select(`.cell-${x}-${y}`)
      .attr('fill', '#4daf4a');
  }

  savePlace(): void {
    if (this.placeForm.valid && this.currentCell) {
      const placeData: PlaceVisualDto = {
        placeNumber: this.placeForm.value.placeNumber,
        status: this.placeForm.value.status === 'true',
        reservation: false,
        numberOfSeats: this.placeForm.value.numberOfSeats,
        coordinates: [{ x: this.currentCell.x, y: this.currentCell.y }]
      };
      const placesArray = this.layoutForm.get('places') as FormArray;
      placesArray.push(this.fb.group(placeData));
      this.createdTables.push(placeData);

      // Update the grid with the table number and change color
      const container = d3.select(this.d3Container.nativeElement).select('svg');
      const cellSize = (this.d3Container.nativeElement as HTMLDivElement).getBoundingClientRect().width / this.layoutForm.value.width;
      container.select(`.table-number-${this.currentCell.x}-${this.currentCell.y}`)
        .text(placeData.placeNumber);
      container.select(`.cell-${this.currentCell.x}-${this.currentCell.y}`)
        .attr('fill', '#4daf4a');

      const placeModal = bootstrap.Modal.getInstance(document.getElementById('placeModal'));
      placeModal.hide();
    }
  }

  onSubmit(): void {
    if (this.layoutForm.valid) {
      this.addArea(); // Save the current area before submitting
      console.log(this.layoutCreateDto);
      // handle the submission logic here
    }
  }
}
