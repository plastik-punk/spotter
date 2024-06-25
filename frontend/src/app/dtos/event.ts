import {Time} from "@angular/common";

export interface Event {
  hashId: string;
  startTime: Date;
  endTime: Date;
  name: string;
  description: string;
}

export interface EventCreateDto {
  startDate: Date;
  startTime: Time;
  endDate: Date;
  endTime: Time;
  name: string;
  description: string;
}
export interface EventListDto {
  hashId: string;
  startTime: Date;
  endTime: Date;
  name: string;
}

export interface EventSearchDto {
  name?: string;
  earliestStartDate?: Date;
  latestEndDate?: Date;
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
  name: string;
  description: string;
}
