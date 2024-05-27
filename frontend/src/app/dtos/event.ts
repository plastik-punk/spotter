export interface Event {
  hashId: string;
  startTime: Date;
  endTime: Date;
  name: string;
  description: string;
}

export interface EventCreateDto {
  startTime: Date;
  endTime: Date;
  name: string;
  description: string;
}
export interface EventListDto {
  hashId: string;
  startTime: Date;
  endTime: Date;
  name: string;
}

export interface EventSearch {
  name?: string;
  earliestDate?: Date;
  latestDate?: Date;
  earliestStartTime?: Date;
  latestEndTime?: Date;
}

export interface EventDetailDto {
  hashId: string;
  startTime: Date;
  endTime: Date;
  name: string;
  description: string;
}

export interface EventEditDto {
  hashId: string;
  startTime: Date;
  endTime: Date;
  notes: string;
  description: string;
}
