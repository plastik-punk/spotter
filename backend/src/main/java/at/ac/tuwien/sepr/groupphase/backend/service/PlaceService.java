package at.ac.tuwien.sepr.groupphase.backend.service;

import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;

/**
 * Service for operations on places (e.g. blocking a place).
 */
public interface PlaceService {
    void block(long id);

    void unblock(long id) throws NotFoundException;
}
