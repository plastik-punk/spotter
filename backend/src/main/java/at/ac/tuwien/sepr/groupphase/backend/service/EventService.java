package at.ac.tuwien.sepr.groupphase.backend.service;


import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.EventDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.EventListDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.EventEditDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.EventCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.EventSearchDto;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.exception.ValidationException;
import org.springframework.web.multipart.MultipartFile;

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

    /**
     * Create a new event.
     *
     * @param eventCreateDto the event to create
     * @return the created event
     * @throws ValidationException if the eventCreateDto is invalid
     */
    EventCreateDto create(EventCreateDto eventCreateDto) throws ValidationException;

    /**
     * Update an existing event.
     *
     * @param eventEditDto the event to update
     * @return the updated event
     * @throws ValidationException if the eventEditDto is invalid
     */
    EventEditDto update(EventEditDto eventEditDto) throws ValidationException;

    /**
     * Delete an event.
     *
     * @param hashId the Hashed id of the event to delete
     * @throws NotFoundException if the event with the given hashId does not exist
     */
    void delete(String hashId) throws NotFoundException;

    /**
     * Import an ICS file.
     *
     * @param file the ICS file
     * @throws Exception if the file could not be imported
     */
    void importIcsFile(MultipartFile file) throws Exception;
}
