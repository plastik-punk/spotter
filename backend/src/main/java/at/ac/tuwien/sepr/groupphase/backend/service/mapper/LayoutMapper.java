package at.ac.tuwien.sepr.groupphase.backend.service.mapper;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.LayoutCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.Area;
import at.ac.tuwien.sepr.groupphase.backend.entity.Place;
import at.ac.tuwien.sepr.groupphase.backend.entity.Segment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LayoutMapper {
    LayoutMapper INSTANCE = Mappers.getMapper(LayoutMapper.class);

    @Mapping(target = "id", ignore = true)
    Area areaCreateDtoToArea(LayoutCreateDto.AreaCreateDto areaCreateDto);

    @Mapping(target = "id", ignore = true)
    Place placeVisualDtoToPlace(LayoutCreateDto.AreaCreateDto.PlaceVisualDto placeVisualDto);

    @Mapping(target = "id", ignore = true)
    Segment coordinateDtoToSegment(LayoutCreateDto.AreaCreateDto.PlaceVisualDto.CoordinateDto coordinateDto);
}
