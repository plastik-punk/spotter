package at.ac.tuwien.sepr.groupphase.backend.service.impl;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.EventCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.EventDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.EventEditDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.EventListDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.EventSearchDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.Event;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.EventService;
import at.ac.tuwien.sepr.groupphase.backend.service.HashService;
import at.ac.tuwien.sepr.groupphase.backend.service.mapper.EventMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.component.VEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class EventServiceImpl implements EventService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final EventRepository eventRepository;
    private final EventMapper mapper;
    private final HashService hashService;

    @Autowired
    private Validator validator;

    @Autowired
    public EventServiceImpl(EventRepository eventRepository,
                            EventMapper mapper,
                            HashService hashService) {
        this.eventRepository = eventRepository;
        this.mapper = mapper;
        this.hashService = hashService;
    }

    @Override
    public List<EventListDto> search(EventSearchDto searchParameters) {
        LOGGER.trace("search({})", searchParameters);
        LocalDateTime startTime;
        if (searchParameters.getEarliestDate() == null || searchParameters.getEarliestStartTime() == null) {
            startTime = null;
        } else {
            startTime = LocalDateTime.of(searchParameters.getEarliestDate(), searchParameters.getEarliestStartTime());
        }

        LocalDateTime endTime;
        if (searchParameters.getLatestDate() == null || searchParameters.getLatestEndTime() == null) {
            endTime = null;
        } else {
            endTime = LocalDateTime.of(searchParameters.getLatestDate(), searchParameters.getLatestEndTime());
        }

        List<Event> events = null;
        if (searchParameters.getMaxResults() != null) {
            events = eventRepository.findEventsByDateMax(startTime, endTime, searchParameters.getMaxResults());
        } else {
            events = eventRepository.findEventsByDate(startTime, endTime);
        }
        return mapper.eventToEventListDto(events);
    }

    @Override
    public EventDetailDto getByHashId(String hashId) throws NotFoundException {
        LOGGER.trace("getByHashId({})", hashId);

        Event event = eventRepository.findByHashId(hashId);
        if (event == null) {
            throw new NotFoundException("Event not found");
        }

        return mapper.eventToEventDetailDto(event);
    }

    @Override
    public EventCreateDto create(EventCreateDto eventCreateDto) {
        LOGGER.trace("create({})", eventCreateDto);

        //if no time is set, set it to 00:00 and 23:59
        if (eventCreateDto.getStartTime() == null) {
            eventCreateDto.setStartTime(LocalTime.of(0, 0));
        }
        if (eventCreateDto.getEndTime() == null) {
            eventCreateDto.setEndTime(LocalTime.of(23, 59));
        }
        Set<ConstraintViolation<EventCreateDto>> eventCreateDtoViolations = validator.validate(eventCreateDto);
        if (!eventCreateDtoViolations.isEmpty()) {
            throw new ConstraintViolationException(eventCreateDtoViolations);
        }

        Event event = mapper.eventCreateDtoToEvent(eventCreateDto);
        event.setDescription(event.getDescription() != null ? event.getDescription().trim() : event.getDescription());
        String hashId = hashService.hashSha256(event.getName() + event.getStartTime().toString() + event.getEndTime().toString() + event.getDescription());
        event.setHashId(hashId);

        Event savedEvent = eventRepository.save(event);
        Set<ConstraintViolation<Event>> eventViolations = validator.validate(savedEvent);
        if (!eventViolations.isEmpty()) {
            throw new ConstraintViolationException(eventViolations);
        }

        return mapper.eventToEventCreateDto(savedEvent);
    }

    @Override
    public void importIcsFile(MultipartFile file) throws IllegalArgumentException {
        try {
            InputStream is = file.getInputStream();
            CalendarBuilder builder = new CalendarBuilder();
            Calendar calendar = builder.build(is);
            StringBuilder errorMessage = new StringBuilder();
            for (Component component : calendar.getComponents(Component.VEVENT)) {
                LOGGER.debug("Mapping event: {}", component);
                try {
                    Event event = mapper.vEventToEvent((VEvent) component);
                    event.setDescription(event.getDescription() != null ? event.getDescription().trim() : "");
                    String hashId = hashService.hashSha256(event.getName() + event.getStartTime().toString() + event.getEndTime().toString() + event.getDescription());
                    event.setHashId(hashId);
                    LOGGER.debug("Saving event: {}", event);
                    eventRepository.save(event);
                } catch (Exception e) {
                    LOGGER.warn("Error mapping event: ", e);
                    errorMessage.append(e.getMessage());
                }
            }
            if (!errorMessage.isEmpty()) {
                throw new IllegalArgumentException(errorMessage.toString());
            }
        } catch (IOException e) {
            LOGGER.error("Error reading file: ", e);
            throw new RuntimeException(e);
        } catch (ParserException e) {
            LOGGER.error("Error parsing file: ", e);
            throw new RuntimeException(e);
        }
    }

    public EventEditDto update(EventEditDto eventEditDto) {
        LOGGER.trace("update({})", eventEditDto);
        Set<ConstraintViolation<EventEditDto>> eventEditDtoViolations = validator.validate(eventEditDto);
        if (!eventEditDtoViolations.isEmpty()) {
            throw new ConstraintViolationException(eventEditDtoViolations);
        }

        Event event = eventRepository.findByHashId(eventEditDto.getHashId());
        event.setName(eventEditDto.getName());
        event.setDescription(eventEditDto.getDescription());
        event.setStartTime(eventEditDto.getStartTime());
        event.setEndTime(eventEditDto.getEndTime());

        Event savedEvent = eventRepository.save(event);
        Set<ConstraintViolation<Event>> eventViolations = validator.validate(savedEvent);
        if (!eventViolations.isEmpty()) {
            throw new ConstraintViolationException(eventViolations);
        }

        return mapper.eventToEventEditDto(savedEvent);
    }

    @Override
    public void delete(String hashId) throws NotFoundException {
        LOGGER.trace("delete({})", hashId);
        Event event = eventRepository.findByHashId(hashId);

        //TODO Validator

        Optional<Event> optionalEvent = eventRepository.findById(event.getId());
        if (optionalEvent.isEmpty()) {
            throw new NotFoundException("Event not found");
        }
        Event eventToDelete = optionalEvent.get();
        eventRepository.deleteById(eventToDelete.getId());
        LOGGER.debug("Event with id {} deleted", eventToDelete.getId());
    }
}