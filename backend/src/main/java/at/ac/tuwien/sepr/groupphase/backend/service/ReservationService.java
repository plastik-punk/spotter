package at.ac.tuwien.sepr.groupphase.backend.service;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationListDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationSearchDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.Reservation;

import java.util.List;

// TODO: JavaDoc

public interface ReservationService {
    Reservation create(ReservationCreateDto reservationCreateDto);

    /**
     * Find all reservations that match the search parameters ordered by startDate (desc).
     *
     * @param reservationSearchDto the search parameters to use in filtering.
     * @return List of ReservationListDto that match the search parameters
     */
    List<ReservationListDto> search(ReservationSearchDto reservationSearchDto);
}
