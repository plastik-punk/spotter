package at.ac.tuwien.sepr.groupphase.backend.service;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.*;
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
    AreaDetailDto getAreaById(Long id);

    /**
     * Get all areas with details.
     *
     * @return the list of all areas with details
     */
    AreaDetailListDto getAllAreasDetailed();

    /**
     * Toggle the open status of an area.
     *
     * @param id     the id of the area
     * @param isOpen the new open status
     */
    void toggleOpen(Long id, boolean isOpen);

    /**
     * Update an area.
     *
     * @param areaDetailDto the area data
     */
    void updateArea(AreaDetailDto areaDetailDto) throws ConflictException;

    /**
     * Toggle the main status of an area.
     *
     * @param id     the id of the area
     * @param isMain the new main status
     */
    void toggleMain(Long id, Boolean isMain);
}
