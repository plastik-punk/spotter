package at.ac.tuwien.sepr.groupphase.backend.service.impl;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.EventCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.Event;
import at.ac.tuwien.sepr.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepr.groupphase.backend.repository.EventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class EventValidator {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    private final EventRepository eventRepository;

    public EventValidator(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public void validateEvent(Event event) throws ValidationException {
        LOGGER.trace("validateEvent({})", event);
        List<String> validationErrors = new ArrayList<>();

        validateName(validationErrors, event.getName());
        validateDescription(validationErrors, event.getDescription());
        validateStartTime(validationErrors, event.getStartTime());
        validateEndTime(validationErrors, event.getEndTime(), event.getStartTime());

        if (!validationErrors.isEmpty()) {
            throw new ValidationException("Validation of event failed", validationErrors);
        }
    }

    public void validateEventCreateDto(EventCreateDto eventCreateDto) throws ValidationException {
        LOGGER.trace("validateEventCreateDto({})", eventCreateDto);
        List<String> validationErrors = new ArrayList<>();

        validateName(validationErrors, eventCreateDto.getName());
        validateDescription(validationErrors, eventCreateDto.getDescription());
        validateStartTime(validationErrors, LocalDateTime.of(eventCreateDto.getStartDate(), eventCreateDto.getStartTime()));
        validateEndTime(validationErrors, LocalDateTime.of(eventCreateDto.getEndDate(), eventCreateDto.getEndTime()),
            LocalDateTime.of(eventCreateDto.getStartDate(), eventCreateDto.getStartTime()));

        if (!validationErrors.isEmpty()) {
            throw new ValidationException("Validation of event failed", validationErrors);
        }
    }

    private void validateName(List<String> validationErrors, String name) {
        if (name == null || name.isBlank()) {
            validationErrors.add("Name must not be empty");
        }
    }

    private void validateDescription(List<String> validationErrors, String description) {
        if (description == null || description.isBlank()) {
            validationErrors.add("Description must not be empty");
        }
    }

    private void validateStartTime(List<String> validationErrors, LocalDateTime startTime) {
        if (startTime == null) {
            validationErrors.add("Start time must not be empty");
        }
    }

    private void validateEndTime(List<String> validationErrors, LocalDateTime endTime, LocalDateTime startTime) {
        if (endTime == null) {
            validationErrors.add("End time must not be empty");
        } else if (endTime.isBefore(startTime)) {
            validationErrors.add("End time must not be before start time");
        }
    }


}
