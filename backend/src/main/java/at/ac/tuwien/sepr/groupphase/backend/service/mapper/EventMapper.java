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

import java.util.List;

@Mapper
public interface EventMapper {
    @Mapping(source = "startDate", target = "startTime.date")
    @Mapping(source = "startTime", target = "startTime.time")
    @Mapping(source = "endDate", target = "endTime.date")
    @Mapping(source = "endTime", target = "endTime.time")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    Event eventCreateDtoToEvent(EventCreateDto eventCreateDto);

    @Mapping(source = "startTime.date", target = "startDate")
    @Mapping(source = "startTime.time", target = "startTime")
    @Mapping(source = "endTime.date", target = "endDate")
    @Mapping(source = "endTime.time", target = "endTime")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    EventCreateDto eventToEventCreateDto(Event event);

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
