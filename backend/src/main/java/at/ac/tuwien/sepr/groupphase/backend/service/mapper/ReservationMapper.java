package at.ac.tuwien.sepr.groupphase.backend.service.mapper;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationEditDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationListDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationModalDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepr.groupphase.backend.entity.Reservation;
import org.mapstruct.AfterMapping;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.List;

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

    @Mapping(source = "applicationUser.firstName", target = "firstName")
    @Mapping(source = "applicationUser.lastName", target = "lastName")
    ReservationModalDetailDto reservationToReservationModalDetailDto(Reservation reservation);

    // TODO: annotation only needed for the applicationUser fields (but test this first)
    @Mapping(source = "applicationUser.firstName", target = "userFirstName")
    @Mapping(source = "applicationUser.lastName", target = "userLastName")
    @Mapping(source = "startTime", target = "startTime")
    @Mapping(source = "date", target = "date")
    @Mapping(source = "endTime", target = "endTime")
    @Mapping(source = "hashValue", target = "hashId")
    @Mapping(source = "confirmed", target = "confirmed")
    @Named("reservationList")
    ReservationListDto reservationToReservationListDto(Reservation reservation);

    @IterableMapping(qualifiedByName = "reservationList")
    List<ReservationListDto> reservationToReservationListDto(List<Reservation> reservation);

    @Mapping(source = "reservation.applicationUser", target = "user")
    @Mapping(source = "reservation.id", target = "reservationId")
    @Mapping(source = "reservation.hashValue", target = "hashedId")
    @Mapping(source = "reservation.notes", target = "notes")
    ReservationEditDto reservationToReservationEditDto(Reservation reservation);
}