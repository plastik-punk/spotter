package at.ac.tuwien.sepr.groupphase.backend.service.mapper;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationEditDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationListDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationModalDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.SpecialOfferListDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepr.groupphase.backend.entity.Reservation;
import at.ac.tuwien.sepr.groupphase.backend.entity.SpecialOffer;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.List;

@Mapper
public interface ReservationMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "hashValue", ignore = true)
    @Mapping(target = "confirmed", ignore = true)
    Reservation reservationCreateDtoToReservation(ReservationCreateDto reservationCreateDto);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "firstName", ignore = true)
    @Mapping(target = "lastName", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "mobileNumber", ignore = true)
    @Mapping(target = "placeIds", ignore = true)
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

    @Mapping(source = "reservation.applicationUser.firstName", target = "firstName")
    @Mapping(source = "reservation.applicationUser.lastName", target = "lastName")
    ReservationModalDetailDto reservationToReservationModalDetailDto(Reservation reservation, List<Long> placeIds);

    @Mapping(source = "reservation.applicationUser.firstName", target = "userFirstName")
    @Mapping(source = "reservation.applicationUser.lastName", target = "userLastName")
    @Mapping(source = "reservation.startTime", target = "startTime")
    @Mapping(source = "reservation.date", target = "date")
    @Mapping(source = "reservation.endTime", target = "endTime")
    @Mapping(source = "reservation.hashValue", target = "hashId")
    @Mapping(source = "reservation.confirmed", target = "confirmed")
    @Named("reservationList")
    ReservationListDto reservationToReservationListDto(Reservation reservation, List<Long> placeIds);

    @Mapping(target = "placeIds", ignore = true)
    @Mapping(source = "reservation.applicationUser", target = "user")
    @Mapping(source = "reservation.id", target = "reservationId")
    @Mapping(source = "reservation.hashValue", target = "hashedId")
    @Mapping(source = "reservation.notes", target = "notes")
    ReservationEditDto reservationToReservationEditDto(Reservation reservation);

    SpecialOfferListDto specialOfferToSpecialOfferListDto(SpecialOffer offer);
}