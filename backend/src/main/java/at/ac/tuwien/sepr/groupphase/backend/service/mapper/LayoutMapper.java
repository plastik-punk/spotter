package at.ac.tuwien.sepr.groupphase.backend.service.mapper;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.LayoutCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.Area;
import at.ac.tuwien.sepr.groupphase.backend.entity.Place;
import at.ac.tuwien.sepr.groupphase.backend.entity.Segment;
import at.ac.tuwien.sepr.groupphase.backend.enums.StatusEnum;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;


/**
 * Mapper interface for converting between layout-related DTOs and entities.
 * This interface uses MapStruct for generating the implementation.
 */
@Mapper(componentModel = "spring")
public interface LayoutMapper {


    /**
     * Converts an {@link LayoutCreateDto.AreaCreateDto} to an {@link Area}.
     * The {@code id} and {@code open} fields are ignored during mapping.
     * Other fields are mapped directly.
     *
     * @param areaCreateDto the DTO to convert
     * @return the converted {@link Area}
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "open", ignore = true) //set inservice
    @Mapping(target = "name", source = "name")
    @Mapping(target = "openingTime", source = "openingTime")
    @Mapping(target = "closingTime", source = "closingTime")
    @Mapping(target = "width", source = "width")
    @Mapping(target = "height", source = "height")
    Area areaCreateDtoToArea(LayoutCreateDto.AreaCreateDto areaCreateDto);


    /**
     * Converts an {@link LayoutCreateDto.AreaCreateDto.PlaceVisualDto} to a {@link Place}.
     * The {@code id} field is ignored during mapping.
     * The {@code number} field is mapped from {@code placeNumber}.
     * The {@code status} field is mapped using the {@code mapStatus} method.
     * The {@code pax} field is mapped from {@code numberOfSeats}.
     *
     * @param placeVisualDto the DTO to convert
     * @return the converted {@link Place}
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "number", source = "placeNumber")
    @Mapping(target = "status", source = "status", qualifiedByName = "mapStatus")
    @Mapping(target = "pax", source = "numberOfSeats")
    Place placeVisualDtoToPlace(LayoutCreateDto.AreaCreateDto.PlaceVisualDto placeVisualDto);


    /**
     * Converts an {@link LayoutCreateDto.AreaCreateDto.PlaceVisualDto.CoordinateDto} to a {@link Segment}.
     * The {@code id}, {@code x}, and {@code y} fields are ignored during mapping.
     *
     * @param coordinateDto the DTO to convert
     * @return the converted {@link Segment}
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "x", ignore = true) //set in service
    @Mapping(target = "y", ignore = true)
    Segment coordinateDtoToSegment(LayoutCreateDto.AreaCreateDto.PlaceVisualDto.CoordinateDto coordinateDto);

    /**
     * Maps a boolean status to a {@link StatusEnum}.
     * Returns {@link StatusEnum#AVAILABLE} if {@code status} is true, otherwise {@link StatusEnum#OCCUPIED}.
     *
     * @param status the boolean status to map
     * @return the mapped {@link StatusEnum}
     */
    @Named("mapStatus")
    default StatusEnum mapStatus(Boolean status) {
        return status ? StatusEnum.AVAILABLE : StatusEnum.OCCUPIED;
    }

}
