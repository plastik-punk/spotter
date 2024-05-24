package at.ac.tuwien.sepr.groupphase.backend.enums;

/**
 * Enum for the response of the reservation check (when user inputs a new value in fields for pax, date or startTime (simple view)).
 */
public enum ReservationResponseEnum {
    AVAILABLE,
    ALL_OCCUPIED,
    CLOSED,
    OUTSIDE_OPENING_HOURS,
    RESPECT_CLOSING_HOUR,
    TOO_MANY_PAX,
    DATE_IN_PAST,
}