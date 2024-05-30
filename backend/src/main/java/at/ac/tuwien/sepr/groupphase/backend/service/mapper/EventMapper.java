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
    Event eventCreateDtoToEvent(EventCreateDto eventCreateDto);

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
