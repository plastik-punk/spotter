package at.ac.tuwien.sepr.groupphase.backend.service.mapper;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.EventCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.EventDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.EventEditDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.EventListDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.Event;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.Description;
import net.fortuna.ical4j.model.property.DtEnd;
import net.fortuna.ical4j.model.property.DtStart;
import net.fortuna.ical4j.model.property.Summary;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
import java.util.List;


/**
 * Mapper interface for converting between {@link Event} entities and their corresponding DTOs.
 * This interface uses MapStruct for generating the implementation.
 */
@Mapper
public interface EventMapper {

    /**
     * Converts an {@link EventCreateDto} to an {@link Event}.
     * The {@code id} and {@code hashId} fields are ignored during mapping.
     * The {@code startTime} and {@code endTime} fields are combined from {@code startDate} and {@code startTime}, and {@code endDate} and {@code endTime} respectively.
     *
     * @param eventCreateDto the DTO to convert
     * @return the converted {@link Event}
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "hashId", ignore = true)
    @Mapping(target = "startTime", expression = "java(combineDateTime(eventCreateDto.getStartDate(), eventCreateDto.getStartTime()))")
    @Mapping(target = "endTime", expression = "java(combineDateTime(eventCreateDto.getEndDate(), eventCreateDto.getEndTime()))")
    Event eventCreateDtoToEvent(EventCreateDto eventCreateDto);


    /**
     * Converts an {@link Event} to an {@link EventCreateDto}.
     * The {@code startDate}, {@code startTime}, {@code endDate}, and {@code endTime} fields are extracted from the {@code startTime} and {@code endTime} fields of the event.
     *
     * @param event the entity to convert
     * @return the converted {@link EventCreateDto}
     */
    @Mapping(target = "startDate", source = "startTime", qualifiedByName = "extractDate")
    @Mapping(target = "startTime", source = "startTime", qualifiedByName = "extractTime")
    @Mapping(target = "endDate", source = "endTime", qualifiedByName = "extractDate")
    @Mapping(target = "endTime", source = "endTime", qualifiedByName = "extractTime")
    EventCreateDto eventToEventCreateDto(Event event);


    /**
     * Combines a {@link LocalDate} and {@link LocalTime} into a {@link LocalDateTime}.
     *
     * @param date the date to combine
     * @param time the time to combine
     * @return the combined {@link LocalDateTime}
     */
    default LocalDateTime combineDateTime(LocalDate date, LocalTime time) {
        return LocalDateTime.of(date, time);
    }


    /**
     * Extracts the {@link LocalDate} from a {@link LocalDateTime}.
     *
     * @param dateTime the date-time to extract from
     * @return the extracted {@link LocalDate}
     */
    @Named("extractDate")
    static LocalDate extractDate(LocalDateTime dateTime) {
        return dateTime.toLocalDate();
    }


    /**
     * Extracts the {@link LocalTime} from a {@link LocalDateTime}.
     *
     * @param dateTime the date-time to extract from
     * @return the extracted {@link LocalTime}
     */
    @Named("extractTime")
    static LocalTime extractTime(LocalDateTime dateTime) {
        return dateTime.toLocalTime();
    }


    /**
     * Converts an {@link Event} to an {@link EventDetailDto}.
     *
     * @param event the entity to convert
     * @return the converted {@link EventDetailDto}
     */
    EventDetailDto eventToEventDetailDto(Event event);


    /**
     * Converts an {@link Event} to an {@link EventListDto}.
     * This method is qualified by the {@code eventList} name.
     *
     * @param event the entity to convert
     * @return the converted {@link EventListDto}
     */
    @Mapping(source = "startTime", target = "startTime")
    @Mapping(source = "endTime", target = "endTime")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "hashId", target = "hashId")
    @Named("eventList")
    EventListDto eventToEventListDto(Event event);

    /**
     * Converts a list of {@link Event} entities to a list of {@link EventListDto}.
     * This method uses the {@code eventList} qualified method for individual conversions.
     *
     * @param event the list of entities to convert
     * @return the converted list of {@link EventListDto}
     */
    @IterableMapping(qualifiedByName = "eventList")
    List<EventListDto> eventToEventListDto(List<Event> event);


    /**
     * Converts an {@link Event} to an {@link EventEditDto}.
     *
     * @param event the entity to convert
     * @return the converted {@link EventEditDto}
     */
    @Mapping(source = "startTime", target = "startTime")
    @Mapping(source = "endTime", target = "endTime")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "hashId", target = "hashId")
    EventEditDto eventToEventEditDto(Event event);


    /**
     * Converts a {@link VEvent} to an {@link Event}.
     * The {@code id} and {@code hashId} fields are ignored during mapping.
     * The {@code startTime}, {@code endTime}, {@code name}, and {@code description} fields are mapped from the VEvent.
     *
     * @param vevent the VEvent to convert
     * @return the converted {@link Event}
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "hashId", ignore = true)
    @Mapping(target = "startTime", source = "vevent", qualifiedByName = "mapStartTime")
    @Mapping(target = "endTime", source = "vevent", qualifiedByName = "mapEndTime")
    @Mapping(target = "name", source = "vevent", qualifiedByName = "mapName")
    @Mapping(target = "description", source = "vevent", qualifiedByName = "mapDescription")
    Event vEventToEvent(VEvent vevent);


    /**
     * Maps the start time from a {@link VEvent} to a {@link LocalDateTime}.
     *
     * @param vevent the VEvent to map from
     * @return the mapped {@link LocalDateTime}
     */
    @Named("mapStartTime")
    default LocalDateTime mapStartTime(VEvent vevent) {
        try {
            DtStart<Temporal> dtStart = vevent.getStartDate().get();
            Temporal temporal = dtStart.getDate();
            LocalDateTime localDateTime;
            if (temporal instanceof java.time.LocalDate) {
                localDateTime = LocalDateTime.of((LocalDate) temporal, LocalTime.of(0, 0));
                return localDateTime;
            } else if (temporal instanceof java.time.Instant) {
                localDateTime = LocalDateTime.ofInstant((Instant) temporal, ZoneId.systemDefault());
                return localDateTime;
            } else if (temporal instanceof java.time.ZonedDateTime) {
                localDateTime = ((ZonedDateTime) temporal).toLocalDateTime();
                return localDateTime;
            }
            throw new Exception("unknown type of startDate Class: " + temporal.getClass());
        } catch (Exception e) {
            throw new IllegalArgumentException("Error mapping start time: " + e.getMessage() + "\n");
        }
    }

    /**
     * Maps the end time from a {@link VEvent} to a {@link LocalDateTime}.
     *
     * @param vevent the VEvent to map from
     * @return the mapped {@link LocalDateTime}
     */
    @Named("mapEndTime")
    default LocalDateTime mapEndTime(VEvent vevent) {
        try {
            DtEnd<?> dtEnd = vevent.getEndDate().get();
            Temporal temporal = dtEnd.getDate();
            LocalDateTime localDateTime;
            if (temporal instanceof java.time.LocalDate) {
                localDateTime = LocalDateTime.of((LocalDate) temporal, LocalTime.of(0, 0));
                return localDateTime;
            } else if (temporal instanceof java.time.Instant) {
                localDateTime = LocalDateTime.ofInstant((Instant) temporal, ZoneId.systemDefault());
                return localDateTime;
            } else if (temporal instanceof java.time.ZonedDateTime) {
                localDateTime = ((ZonedDateTime) temporal).toLocalDateTime();
                return localDateTime;
            }
            throw new Exception("unknown type of endDate Class: " + temporal.getClass());
        } catch (Exception e) {
            try {
                LocalDateTime startTime = mapStartTime(vevent);
                return startTime.plusDays(1);
            } catch (Exception e2) {
                throw new IllegalArgumentException("Error mapping end time: " + e.getMessage());
            }
        }
    }

    @Named("mapName")
    default String mapName(VEvent vevent) {
        try {
            Summary summary = vevent.getSummary().get();
            return summary.getValue();
        } catch (Exception e) {
            throw new IllegalArgumentException("Error mapping name: " + e.getMessage());
        }
    }

    @Named("mapDescription")
    default String mapDescription(VEvent vevent) {
        try {
            Description description = vevent.getDescription().get();
            return description.getValue();
        } catch (Exception e) {
            return null;
        }
    }
}
