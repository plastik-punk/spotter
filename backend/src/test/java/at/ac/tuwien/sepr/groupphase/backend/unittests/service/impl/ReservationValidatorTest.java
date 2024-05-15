package at.ac.tuwien.sepr.groupphase.backend.unittests.service.impl;

import at.ac.tuwien.sepr.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.Reservation;
import at.ac.tuwien.sepr.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepr.groupphase.backend.service.impl.ReservationValidator;
import at.ac.tuwien.sepr.groupphase.backend.service.impl.UserDataValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class ReservationValidatorTest implements TestData {

    private ReservationValidator reservationValidator;

    @BeforeEach
    public void beforeEach() {
        this.reservationValidator = new ReservationValidator(new UserDataValidator());
    }

    // TODO: Implement tests
    @Test
    public void givenNoStartDate_whenValidateReservation_thenValidationException() {
        Reservation reservation = TEST_RESERVATION_1.copy();
        reservation.setStartTime(null);
        assertThrows(ValidationException.class, () -> reservationValidator.validateReservation(reservation));
    }

    @Test
    public void givenPastDate_whenValidateReservation_thenValidationException() {
        Reservation reservation = TEST_RESERVATION_1.copy();
        reservation.setDate(LocalDate.now().minusDays(1));
        assertThrows(ValidationException.class, () -> reservationValidator.validateReservation(reservation));
    }

    @Test
    public void givenNoStartDate_whenValidateReservationCreateDto_thenValidationException() {
        ReservationCreateDto dto = TEST_RESERVATION_CREATE_DTO_CUSTOMER.copy();
        dto.setStartTime(null);
        assertThrows(ValidationException.class, () -> reservationValidator.validateReservationCreateDto(dto));
    }

    @Test
    public void givenPastDate_whenValidateReservationCreateDto_thenValidationException() {
        ReservationCreateDto dto = TEST_RESERVATION_CREATE_DTO_CUSTOMER.copy();
        dto.setDate(LocalDate.now().minusDays(1));
        assertThrows(ValidationException.class, () -> reservationValidator.validateReservationCreateDto(dto));
    }

    // TODO: Implement full test coverage

}