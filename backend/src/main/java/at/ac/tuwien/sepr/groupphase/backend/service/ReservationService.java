package at.ac.tuwien.sepr.groupphase.backend.service;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationCheckAvailabilityDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.enums.ReservationResponseEnum;
import at.ac.tuwien.sepr.groupphase.backend.exception.ValidationException;
import jakarta.mail.MessagingException;


/**
 * Service for operations on reservations (e.g. creating a new Reservation).
 */
public interface ReservationService {

    /**
     * Create a reservation.
     *
     * @param reservationCreateDto the reservation data
     * @return the reservation as provided from the Repository layer after creation in the database
     */
    ReservationCreateDto create(ReservationCreateDto reservationCreateDto) throws MessagingException, ValidationException;

    /**
     * Check if any tables are available for requested time and pax.
     *
     * @param reservationCheckAvailabilityDto the reservation data
     * @return the availability status
     */
    ReservationResponseEnum getAvailability(ReservationCheckAvailabilityDto reservationCheckAvailabilityDto) throws ValidationException;

    /**
     * Get the details of a reservation specified by its Hashed id.
     *
     * @param id the Hashed id of the reservation
     * @return the reservation details
     */
    ReservationDetailDto getByHashedId(String id) throws ValidationException;

    /**
     * Update a reservation.
     *
     * @param reservationDetailDto the reservation data
     * @return the updated reservation
     */
    ReservationDetailDto update(ReservationDetailDto reservationDetailDto) throws ValidationException;
}
