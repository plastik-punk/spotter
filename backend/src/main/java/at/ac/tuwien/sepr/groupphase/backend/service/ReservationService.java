package at.ac.tuwien.sepr.groupphase.backend.service;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.*;
import at.ac.tuwien.sepr.groupphase.backend.enums.ReservationResponseEnum;
import at.ac.tuwien.sepr.groupphase.backend.exception.ValidationException;
import jakarta.mail.MessagingException;

import java.util.List;


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
     * Get the next three available tables for a reservation.
     *
     * @param reservationCheckAvailabilityDto the reservation data
     * @return the next three available tables
     */
    ReservationCheckAvailabilityDto[] getNextAvailableTables(ReservationCheckAvailabilityDto reservationCheckAvailabilityDto) throws ValidationException;

    /**
     * Get the details of a reservation specified by its Hashed id.
     *
     * @param id the Hashed id of the reservation
     * @return the reservation details
     */
    ReservationEditDto getByHashedId(String id) throws ValidationException;

    /**
     * Update a reservation.
     *
     * @param reservationEditDto the reservation data
     * @return the updated reservation
     */
    ReservationEditDto update(ReservationEditDto reservationEditDto) throws ValidationException;

    /**
     * Find all reservations that match the search parameters ordered by startDate (desc).
     *
     * @param reservationSearchDto the search parameters to use in filtering.
     * @return List of ReservationListDto that match the search parameters
     */
    List<ReservationListDto> search(ReservationSearchDto reservationSearchDto);


    /**
     * Cancel a reservation.
     *
     * @param hashId the Hashed id of the reservation.
     * @throws ValidationException if the reservation is not found.
     */
    void cancel(String hashId) throws ValidationException;


    /**
     * Get layout of area for requested time and pax.
     *
     * @param reservationLayoutCheckAvailabilityDto the reservation data
     * @return the layout of the area
     */

    AreaLayoutDto getAreaLayout(ReservationLayoutCheckAvailabilityDto reservationLayoutCheckAvailabilityDto) throws ValidationException;
}
