package at.ac.tuwien.sepr.groupphase.backend.service.mapper;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepr.groupphase.backend.entity.Reservation;
import at.ac.tuwien.sepr.groupphase.backend.entity.Place;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
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

    ReservationDetailDto reservationToReservationDetailDto(Reservation reservation);

    @AfterMapping
    default void mapEntityPlaceToDtoPlaceId(@MappingTarget ReservationDetailDto dto, Reservation entity) {
        Place place = entity.getPlace();
        if (place != null) {
            dto.setPlaceId(place.getId());
        }
    }
}