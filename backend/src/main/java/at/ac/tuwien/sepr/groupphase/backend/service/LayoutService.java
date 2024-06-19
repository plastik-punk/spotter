package at.ac.tuwien.sepr.groupphase.backend.service;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.AreaLayoutDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.AreaListDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.LayoutCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationLayoutCheckAvailabilityDto;

/**
 * Service for operations on Layout (graphic representation of areas).
 */
public interface LayoutService {

    /**
     * Get all areas name and id.
     *
     * @return the list of all areas name and id
     */
    AreaListDto getAllAreas();

    /**
     * Get layout of area for requested time and pax.
     *
     * @param reservationLayoutCheckAvailabilityDto the reservation data
     * @return the layout of the area
     */
    AreaLayoutDto getAreaLayout(ReservationLayoutCheckAvailabilityDto reservationLayoutCheckAvailabilityDto);

    void createLayout(LayoutCreateDto layoutCreateDto);
}
