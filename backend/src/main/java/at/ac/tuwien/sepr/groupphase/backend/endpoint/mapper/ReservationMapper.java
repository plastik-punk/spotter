package at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationListDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.Reservation;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

@Mapper
public interface ReservationMapper {
    @Named("reservationList")
    ReservationListDto reservationToReservationListDto(Reservation reservation);

    @IterableMapping(qualifiedByName = "reservationList")
    List<ReservationListDto> reservationToReservationListDto(List<Reservation> reservation);
}
