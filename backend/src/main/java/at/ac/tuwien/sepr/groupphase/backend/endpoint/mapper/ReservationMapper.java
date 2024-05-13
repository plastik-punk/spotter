package at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepr.groupphase.backend.entity.Reservation;
import org.mapstruct.Mapper;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationCreateDto;
@Mapper
public interface ReservationMapper {
    /*
    @Named("reservationList")
    ReservationListDto reservationToReservationListDto(Reservation reservation);

    @IterableMapping(qualifiedByName = "reservationList")
    List<ReservationListDto> reservationToReservationListDto(List<Reservation> reservation);

     */
    Reservation reservationCreateDtoToReservation(ReservationCreateDto reservationCreateDto);
}
