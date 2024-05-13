package at.ac.tuwien.sepr.groupphase.backend.service;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.Reservation;
import jakarta.mail.MessagingException;

// TODO: JavaDoc

public interface ReservationService {
    Reservation create(ReservationCreateDto reservationCreateDto) throws MessagingException;
}
