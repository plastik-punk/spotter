package at.ac.tuwien.sepr.groupphase.backend.service.mapper;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.PermanentReservationCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.PermanentReservationDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.PermanentReservationListDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationEditDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationListDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationModalDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.SpecialOfferListDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepr.groupphase.backend.entity.PermanentReservation;
import at.ac.tuwien.sepr.groupphase.backend.entity.Reservation;
import at.ac.tuwien.sepr.groupphase.backend.entity.SpecialOffer;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.List;


/**
 * Mapper interface for converting between reservation-related DTOs and entities.
 * This interface uses MapStruct for generating the implementation.
 */
@Mapper
public interface ReservationMapper {


    /**
     * Converts a {@link PermanentReservationCreateDto} to a {@link PermanentReservation}.
     * Ignores the {@code id} field during mapping.
     *
     * @param permanentReservationCreateDto the DTO to convert
     * @return the converted {@link PermanentReservation}
     */
    @Mapping(target = "id", ignore = true)
    //@Mapping(target = "user", ignore = true)
    PermanentReservation permanentReservationCreateDtoToPermanentReservation(PermanentReservationCreateDto permanentReservationCreateDto);


    /**
     * Converts a {@link PermanentReservation} to a {@link PermanentReservationCreateDto}.
     *
     * @param permanentReservation the entity to convert
     * @return the converted {@link PermanentReservationCreateDto}
     */
    PermanentReservationCreateDto permanentReservationToPermanentReservationCreateDto(PermanentReservation permanentReservation);


    /**
     * Converts a {@link ReservationCreateDto} to a {@link Reservation}.
     * Ignores the {@code id}, {@code hashValue}, and {@code confirmed} fields during mapping.
     *
     * @param reservationCreateDto the DTO to convert
     * @return the converted {@link Reservation}
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "hashValue", ignore = true)
    @Mapping(target = "confirmed", ignore = true)
    Reservation reservationCreateDtoToReservation(ReservationCreateDto reservationCreateDto);


    /**
     * Converts a {@link Reservation} to a {@link ReservationCreateDto}.
     * Ignores the {@code user}, {@code firstName}, {@code lastName}, {@code email},
     * {@code mobileNumber}, and {@code placeIds} fields during mapping.
     *
     * @param reservation the entity to convert
     * @return the converted {@link ReservationCreateDto}
     */
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "firstName", ignore = true)
    @Mapping(target = "lastName", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "mobileNumber", ignore = true)
    @Mapping(target = "placeIds", ignore = true)
    ReservationCreateDto reservationToReservationCreateDto(Reservation reservation);


    /**
     * Maps the user fields from a {@link Reservation} entity to a {@link ReservationCreateDto} after the main mapping.
     *
     * @param dto    the target DTO
     * @param entity the source entity
     */
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


    /**
     * Converts a {@link Reservation} to a {@link ReservationModalDetailDto}.
     *
     * @param reservation the entity to convert
     * @param placeIds    the list of place IDs associated with the reservation
     * @return the converted {@link ReservationModalDetailDto}
     */
    @Mapping(source = "reservation.applicationUser.firstName", target = "firstName")
    @Mapping(source = "reservation.applicationUser.lastName", target = "lastName")
    ReservationModalDetailDto reservationToReservationModalDetailDto(Reservation reservation, List<Long> placeIds);


    /**
     * Converts a {@link Reservation} to a {@link ReservationListDto}.
     *
     * @param reservation the entity to convert
     * @param placeIds    the list of place IDs associated with the reservation
     * @return the converted {@link ReservationListDto}
     */
    @Mapping(source = "reservation.applicationUser.firstName", target = "userFirstName")
    @Mapping(source = "reservation.applicationUser.lastName", target = "userLastName")
    @Mapping(source = "reservation.startTime", target = "startTime")
    @Mapping(source = "reservation.date", target = "date")
    @Mapping(source = "reservation.endTime", target = "endTime")
    @Mapping(source = "reservation.hashValue", target = "hashId")
    @Mapping(source = "reservation.confirmed", target = "confirmed")
    @Named("reservationList")
    ReservationListDto reservationToReservationListDto(Reservation reservation, List<Long> placeIds);


    /**
     * Converts a {@link Reservation} to a {@link ReservationListDto}.
     *
     * @param reservation the entity to convert
     * @return the converted {@link ReservationListDto}
     */
    @Mapping(source = "reservation.applicationUser.firstName", target = "userFirstName")
    @Mapping(source = "reservation.applicationUser.lastName", target = "userLastName")
    @Mapping(source = "reservation.startTime", target = "startTime")
    @Mapping(source = "reservation.date", target = "date")
    @Mapping(source = "reservation.endTime", target = "endTime")
    @Mapping(source = "reservation.hashValue", target = "hashId")
    @Mapping(source = "reservation.confirmed", target = "confirmed")
    @Named("reservationList")
    ReservationListDto reservationToReservationListDto(Reservation reservation);


    /**
     * Converts a {@link Reservation} to a {@link ReservationEditDto}.
     * Ignores the {@code placeIds} field during mapping.
     *
     * @param reservation the entity to convert
     * @return the converted {@link ReservationEditDto}
     */
    @Mapping(target = "placeIds", ignore = true)
    @Mapping(source = "reservation.applicationUser", target = "user")
    @Mapping(source = "reservation.id", target = "reservationId")
    @Mapping(source = "reservation.hashValue", target = "hashedId")
    @Mapping(source = "reservation.notes", target = "notes")
    ReservationEditDto reservationToReservationEditDto(Reservation reservation);

    /**
     * Converts a {@link PermanentReservation} to a {@link PermanentReservationListDto}.
     *
     * @param permanentReservation the entity to convert
     * @return the converted {@link PermanentReservationListDto}
     */
    @Mapping(source = "applicationUser.firstName", target = "userFirstName")
    @Mapping(source = "applicationUser.lastName", target = "userLastName")
    PermanentReservationListDto permanentReservationToPermanentReservationListDto(PermanentReservation permanentReservation);


    /**
     * Converts a {@link PermanentReservation} to a {@link PermanentReservationDetailDto}.
     * Ignores the {@code singleReservationList} field during mapping.
     *
     * @param permanentReservation the entity to convert
     * @return the converted {@link PermanentReservationDetailDto}
     */
    @Mapping(target = "id", source = "id")
    @Mapping(target = "userFirstName", source = "applicationUser.firstName")
    @Mapping(target = "userLastName", source = "applicationUser.lastName")
    @Mapping(target = "startTime", source = "startTime")
    @Mapping(target = "endTime", source = "endTime")
    @Mapping(target = "startDate", source = "startDate")
    @Mapping(target = "endDate", source = "endDate")
    @Mapping(target = "repetition", source = "repetition")
    @Mapping(target = "period", source = "period")
    @Mapping(target = "confirmed", source = "confirmed")
    @Mapping(target = "pax", source = "pax")
    @Mapping(target = "hashedId", source = "hashedId")
    @Mapping(target = "singleReservationList", ignore = true)
    PermanentReservationDetailDto permanentReservationToDetailDto(PermanentReservation permanentReservation);

    /**
     * Converts a {@link SpecialOffer} to a {@link SpecialOfferListDto}.
     *
     * @param offer the entity to convert
     * @return the converted {@link SpecialOfferListDto}
     */

    SpecialOfferListDto specialOfferToSpecialOfferListDto(SpecialOffer offer);
}