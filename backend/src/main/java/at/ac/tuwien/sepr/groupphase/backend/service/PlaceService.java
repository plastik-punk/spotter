package at.ac.tuwien.sepr.groupphase.backend.service;

import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;

import java.util.List;

/**
 * Service for operations on places (e.g. blocking a place).
 */
public interface PlaceService {
    void block(List<Long> ids) throws NotFoundException;
}
