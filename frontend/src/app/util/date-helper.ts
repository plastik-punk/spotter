import {formatDate} from "@angular/common";

export function formatIsoDate(date: Date): string {
  return formatDate(date, 'yyyy-MM-dd', 'en-DK');
}

export function formatTime(date: Date | string): string {
  if (!date) {
    return '';
  }

  let dateObject: Date;

  if (typeof date === 'string') {
    dateObject = new Date(`1970-01-01T${date}Z`); // Convert the time string to a Date object
  } else {
    dateObject = date;
  }

  let hours = dateObject.getHours();
  let minutes = dateObject.getMinutes();
  return `${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}`;
}

export function formatDotDate(date: Date): string {
  return formatDate(date, 'dd.MM.yyyy', 'en-DK');
}

export function formatIsoTime(isoDateTime: Date): string {
  let date = new Date(isoDateTime);
  let hours = date.getUTCHours();
  return `${hours.toString().padStart(2, '0')}`;
}

export function formatDotDateShort(date: Date): string {
  return formatDate(date, 'dd.MM.', 'en-DK');
}

export function formatDay(date: Date): string {
  return formatDate(date, 'EEE', 'en-DK');
}
