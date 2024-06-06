package at.ac.tuwien.sepr.groupphase.backend.service;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.AreaLayoutDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.AreaListDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationCheckAvailabilityDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationEditDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationLayoutCheckAvailabilityDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationListDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationSearchDto;
import at.ac.tuwien.sepr.groupphase.backend.enums.ReservationResponseEnum;
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
    ReservationCreateDto create(ReservationCreateDto reservationCreateDto) throws MessagingException;

    /**
     * Check if any tables are available for requested time and pax.
     *
     * @param reservationCheckAvailabilityDto the reservation data
     * @return the availability status
     */
    ReservationResponseEnum getAvailability(ReservationCheckAvailabilityDto reservationCheckAvailabilityDto);

    /**
     * Get the next three available tables for a reservation.
     *
     * @param reservationCheckAvailabilityDto the reservation data
     * @return the next three available tables
     */
    ReservationCheckAvailabilityDto[] getNextAvailableTables(ReservationCheckAvailabilityDto reservationCheckAvailabilityDto);

    /**
     * Get the details of a reservation specified by its Hashed id.
     *
     * @param id the Hashed id of the reservation
     * @return the reservation details
     */
    ReservationEditDto getByHashedId(String id);

    /**
     * Update a reservation.
     *
     * @param reservationEditDto the reservation data
     * @return the updated reservation
     */
    ReservationEditDto update(ReservationEditDto reservationEditDto);

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
     */
    void cancel(String hashId);


    /**
     * Get layout of area for requested time and pax.
     *
     * @param reservationLayoutCheckAvailabilityDto the reservation data
     * @return the layout of the area
     */

    AreaLayoutDto getAreaLayout(ReservationLayoutCheckAvailabilityDto reservationLayoutCheckAvailabilityDto);

    AreaListDto getAllAreas();
}
