export interface ReservationLayoutCheckAvailabilityDto {
  startTime: string;
  date: string;
  areaId: number;
  idToExclude: number;
}

export interface AreaLayoutDto {
  width: number;
  height: number;
  placeVisuals: PlaceVisualDto[];
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

export interface AreaDto {
  id: number;
  name: string;
}

export interface AreaListDto {
  areas: AreaDto[];
}

export interface AreaCreateDto {
  name: string,
  isMainArea: boolean,
  closingTime?: string,
  openingTime?: string,
  isOpen: boolean,
  width: number,
  height: number
  places: PlaceVisualDto[]
}

export interface LayoutCreateDto{
  areas: AreaCreateDto[]
}
