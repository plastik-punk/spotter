package at.ac.tuwien.sepr.groupphase.backend.service;

import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;

import java.util.List;

/**
 * Service for operations on places (e.g. blocking a place).
 */
public interface PlaceService {

    /**
     * Blocks a list of places specified by their IDs.
     *
     * @param ids the list of place IDs to block
     * @throws NotFoundException if any of the place IDs are not found
     */
    void block(List<Long> ids) throws NotFoundException;
}
