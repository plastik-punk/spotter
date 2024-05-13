package at.ac.tuwien.sepr.groupphase.backend.service;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationListDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationSearchDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.Reservation;
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
    Reservation create(ReservationCreateDto reservationCreateDto);

    /**
     * Find all reservations that match the search parameters ordered by startDate (desc).
     *
     * @param reservationSearchDto the search parameters to use in filtering.
     * @return List of ReservationListDto that match the search parameters
     */
    List<ReservationListDto> search(ReservationSearchDto reservationSearchDto);
}
