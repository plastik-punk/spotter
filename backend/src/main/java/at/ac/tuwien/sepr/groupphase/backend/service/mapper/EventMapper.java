package at.ac.tuwien.sepr.groupphase.backend.service.mapper;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.EventCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.EventDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.EventEditDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.EventListDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.Event;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
}
