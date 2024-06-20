package at.ac.tuwien.sepr.groupphase.backend.service.mapper;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.LayoutCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.Area;
import at.ac.tuwien.sepr.groupphase.backend.entity.Place;
import at.ac.tuwien.sepr.groupphase.backend.entity.Segment;
import at.ac.tuwien.sepr.groupphase.backend.enums.StatusEnum;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper
public interface LayoutMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "open", target = "open")
    Area areaCreateDtoToArea(LayoutCreateDto.AreaCreateDto areaCreateDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "placeNumber", target = "number")
    @Mapping(source = "status", target = "status", qualifiedByName = "mapStatus")
    @Mapping(source = "numberOfSeats", target = "pax")
    Place placeVisualDtoToPlace(LayoutCreateDto.AreaCreateDto.PlaceVisualDto placeVisualDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "x1", target = "x")
    @Mapping(source = "y1", target = "y")
    Segment coordinateDtoToSegment(LayoutCreateDto.AreaCreateDto.PlaceVisualDto.CoordinateDto coordinateDto);

    @Named("mapStatus")
    default StatusEnum mapStatus(Boolean status) {
        return status ? StatusEnum.AVAILABLE : StatusEnum.OCCUPIED;
    }

}

