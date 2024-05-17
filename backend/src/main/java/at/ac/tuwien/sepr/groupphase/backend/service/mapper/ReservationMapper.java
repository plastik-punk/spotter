package at.ac.tuwien.sepr.groupphase.backend.service.mapper;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationListDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepr.groupphase.backend.entity.Reservation;
import org.mapstruct.IterableMapping;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

import org.mapstruct.MappingTarget;

@Mapper
public interface ReservationMapper {

    Reservation reservationCreateDtoToReservation(ReservationCreateDto reservationCreateDto);

    ReservationCreateDto reservationToReservationCreateDto(Reservation reservation);

    @AfterMapping
    default void mapEntityUserToDtoUser(@MappingTarget ReservationCreateDto dto, Reservation entity) {
        ApplicationUser applicationUser = entity.getApplicationUser();
        if (applicationUser != null) {
            dto.setUser(applicationUser);
            dto.setFirstName(applicationUser.getFirstName());
            dto.setLastName(applicationUser.getLastName());
            dto.setEmail(applicationUser.getEmail());
            dto.setMobileNumber(applicationUser.getMobileNumber());
        }
    }

    @Mapping(source = "applicationUser.firstName", target = "userFirstName")
    @Mapping(source = "applicationUser.lastName", target = "userLastName")
    @Mapping(source = "startTime", target = "startTime")
    @Mapping(source = "date", target = "date")
    @Mapping(source = "endTime", target = "endTime")
    @Mapping(source = "place.id", target = "placeId")
    @Named("reservationList")
    ReservationListDto reservationToReservationListDto(Reservation reservation);

    @IterableMapping(qualifiedByName = "reservationList")
    List<ReservationListDto> reservationToReservationListDto(List<Reservation> reservation);
}