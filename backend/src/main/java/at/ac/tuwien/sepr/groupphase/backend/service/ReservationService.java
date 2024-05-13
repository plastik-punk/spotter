package at.ac.tuwien.sepr.groupphase.backend.service;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.Reservation;

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
}