package at.ac.tuwien.sepr.groupphase.backend.service.mapper;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.AreaDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.LayoutCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.Area;
import at.ac.tuwien.sepr.groupphase.backend.entity.Place;
import at.ac.tuwien.sepr.groupphase.backend.entity.Segment;
import at.ac.tuwien.sepr.groupphase.backend.enums.StatusEnum;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface LayoutMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "open", ignore = true) //set inservice
    @Mapping(target = "name", source = "name")
    @Mapping(target = "openingTime", source = "openingTime")
    @Mapping(target = "closingTime", source = "closingTime")
    @Mapping(target = "width", source = "width")
    @Mapping(target = "height", source = "height")
    Area areaCreateDtoToArea(LayoutCreateDto.AreaCreateDto areaCreateDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "number", source = "placeNumber")
    @Mapping(target = "status", source = "status", qualifiedByName = "mapStatus")
    @Mapping(target = "pax", source = "numberOfSeats")
    Place placeVisualDtoToPlace(LayoutCreateDto.AreaCreateDto.PlaceVisualDto placeVisualDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "x", ignore = true) //set in service
    @Mapping(target = "y", ignore = true)
    Segment coordinateDtoToSegment(LayoutCreateDto.AreaCreateDto.PlaceVisualDto.CoordinateDto coordinateDto);

    @Named("mapStatus")
    default StatusEnum mapStatus(Boolean status) {
        return status ? StatusEnum.AVAILABLE : StatusEnum.OCCUPIED;
    }

}
