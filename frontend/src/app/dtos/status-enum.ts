// This is a copy of StatusEnum from the backend and should be used to mirror that enum in the frontend
export enum StatusEnum {
  occupied = 'OCCUPIED',
  available = 'AVAILABLE',
  reserved = 'RESERVED',
}

// This defines if a reservation in the simple view is available or not
// depending on time, date and pax specified by the user (and obviously the status of tables at that time)
// checking is used to indicate that user is still selecting the time, date and pax
export enum SimpleViewReservationStatusEnum {
  allOccupied = 'ALL_OCCUPIED',
  available = 'AVAILABLE',
  closed = 'CLOSED',
  outsideOpeningHours = 'OUTSIDE_OPENING_HOURS',
  respectClosingHour = 'RESPECT_CLOSING_HOUR',
  tooManyPax = 'TOO_MANY_PAX',
  dateInPast = 'DATE_IN_PAST',
}
