package at.ac.tuwien.sepr.groupphase.backend.service.mapper;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.Reservation;
import org.mapstruct.Mapper;

@Mapper
public interface ReservationMapper {
    Reservation reservationCreateDtoToReservation(ReservationCreateDto reservationCreateDto);
}