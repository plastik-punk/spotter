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


@Mapper
public interface EventMapper {
    @Mapping(target = "startTime", expression = "java(combineDateTime(eventCreateDto.getStartDate(), eventCreateDto.getStartTime()))")
    @Mapping(target = "endTime", expression = "java(combineDateTime(eventCreateDto.getEndDate(), eventCreateDto.getEndTime()))")
    Event eventCreateDtoToEvent(EventCreateDto eventCreateDto);

    @Mapping(target = "startDate", source = "startTime", qualifiedByName = "extractDate")
    @Mapping(target = "startTime", source = "startTime", qualifiedByName = "extractTime")
    @Mapping(target = "endDate", source = "endTime", qualifiedByName = "extractDate")
    @Mapping(target = "endTime", source = "endTime", qualifiedByName = "extractTime")
    EventCreateDto eventToEventCreateDto(Event event);

    default LocalDateTime combineDateTime(LocalDate date, LocalTime time) {
        return LocalDateTime.of(date, time);
    }

    @Named("extractDate")
    static LocalDate extractDate(LocalDateTime dateTime) {
        return dateTime.toLocalDate();
    }

    @Named("extractTime")
    static LocalTime extractTime(LocalDateTime dateTime) {
        return dateTime.toLocalTime();
    }

    EventDetailDto eventToEventDetailDto(Event event);

    @Mapping(source = "startTime", target = "startTime")
    @Mapping(source = "endTime", target = "endTime")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "hashId", target = "hashId")
    @Named("eventList")
    EventListDto eventToEventListDto(Event event);

    @IterableMapping(qualifiedByName = "eventList")
    List<EventListDto> eventToEventListDto(List<Event> event);

    @Mapping(source = "startTime", target = "startTime")
    @Mapping(source = "endTime", target = "endTime")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "hashId", target = "hashId")
    EventEditDto eventToEventEditDto(Event event);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "hashId", ignore = true)
    @Mapping(target = "startTime", source = "vevent", qualifiedByName = "mapStartTime")
    @Mapping(target = "endTime", source = "vevent", qualifiedByName = "mapEndTime")
    @Mapping(target = "name", source = "vevent", qualifiedByName = "mapName")
    @Mapping(target = "description", source = "vevent", qualifiedByName = "mapDescription")
    Event vEventToEvent(VEvent vevent);

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
            System.out.println("Error mapping start time: " + e.getMessage() + "\n");
            return null;
        }
        //TODO: throw IllegalArgumentException
    }

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
            System.out.println("Error mapping end time: " + e.getMessage() + "\n");
            throw new IllegalArgumentException("Error mapping end time: " + e.getMessage());
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
