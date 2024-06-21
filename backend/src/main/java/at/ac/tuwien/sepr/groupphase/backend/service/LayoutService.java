package at.ac.tuwien.sepr.groupphase.backend.service;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.AreaLayoutDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.AreaListDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.LayoutCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationLayoutCheckAvailabilityDto;
import at.ac.tuwien.sepr.groupphase.backend.exception.ConflictException;

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

    /**
     * Create a layout.
     *
     * @param layoutCreateDto the layout data
     */
    void createLayout(LayoutCreateDto layoutCreateDto);

    /**
     * Delete an area.
     *
     * @param id the id of the area
     */
    void deleteArea(Long id) throws ConflictException;

    /**
     * Get area by id.
     *
     * @param id the id of the area
     * @return the area
     */
    LayoutCreateDto.AreaCreateDto getAreaById(Long id);
}
