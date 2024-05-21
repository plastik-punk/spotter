package at.ac.tuwien.sepr.groupphase.backend.service;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationListDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationSearchDto;
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
     * Get the details of a reservation specified by its id.
     *
     * @param id the id of the reservation
     * @return the reservation details
     */
    ReservationDetailDto getById(Long id) throws ValidationException;

    /**
     * Update a reservation.
     *
     * @param reservationDetailDto the reservation data
     * @return the updated reservation
     */
    ReservationDetailDto update(ReservationDetailDto reservationDetailDto) throws ValidationException;

    /**
     * Find all reservations that match the search parameters ordered by startDate (desc).
     *
     * @param reservationSearchDto the search parameters to use in filtering.
     * @return List of ReservationListDto that match the search parameters
     */
    List<ReservationListDto> search(ReservationSearchDto reservationSearchDto);
}
