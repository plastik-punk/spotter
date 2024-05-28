package at.ac.tuwien.sepr.groupphase.backend.service;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.EventDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.EventListDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.EventSearchDto;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.exception.ValidationException;

import java.util.List;

/**
 * Service for operations on events (e.g. creating a new Event).
 */
public interface EventService {

    /**
     * Search for events fitting the parameters..
     *
     * @param searchParameters the search parameters
     * @return the list of events that match the search parameters
     */
    List<EventListDto> search(EventSearchDto searchParameters);

    /**
     * Get the details of an event specified by its Hashed id.
     *
     * @param hashId the Hashed id of the event
     * @return the event details
     * @throws NotFoundException if the event with the given hashId does not exist
     */
    EventDetailDto getByHashId(String hashId) throws NotFoundException;
}
