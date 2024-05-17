package at.ac.tuwien.sepr.groupphase.backend.unittests.service.impl;

import at.ac.tuwien.sepr.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationCheckAvailabilityDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.Reservation;
import at.ac.tuwien.sepr.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepr.groupphase.backend.service.impl.ReservationValidator;
import at.ac.tuwien.sepr.groupphase.backend.service.impl.UserDataValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
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

    @Test
    public void givenValidReservation_whenValidateReservation_thenNoException() {
        assertDoesNotThrow(() -> reservationValidator.validateReservation(TEST_RESERVATION_1));
    }

    @Test
    public void givenValidReservationCreateDto_whenValidateReservation_thenNoException() throws ValidationException {
        assertDoesNotThrow(() -> reservationValidator.validateReservationCreateDto(TEST_RESERVATION_CREATE_DTO_GUEST));
    }

    @Test
    public void givenNoStartDate_whenValidateReservation_thenValidationException() {
        Reservation reservation = TEST_RESERVATION_1.copy();
        reservation.setStartTime(null);
        assertThrows(ValidationException.class, () -> reservationValidator.validateReservation(reservation));
    }

    // TODO: activate after end time was implemented in frontend
    /*
    @Test
    public void givenNoEndDate_whenValidateReservation_thenValidationException() {
        Reservation reservation = TEST_RESERVATION_1.copy();
        reservation.setEndTime(null);
        assertThrows(ValidationException.class, () -> reservationValidator.validateReservation(reservation));
    }

    @Test
    public void givenEndBeforeStartDate_whenValidateReservation_thenValidationException() {
        Reservation reservation = TEST_RESERVATION_1.copy();
        reservation.setEndTime(reservation.getStartTime().minusHours(1));
        assertThrows(ValidationException.class, () -> reservationValidator.validateReservation(reservation));
    }
     */

    @Test
    public void givenNoDate_whenValidateReservation_thenValidationException() {
        Reservation reservation = TEST_RESERVATION_1.copy();
        reservation.setDate(null);
        assertThrows(ValidationException.class, () -> reservationValidator.validateReservation(reservation));
    }

    @Test
    public void givenPastDate_whenValidateReservation_thenValidationException() {
        Reservation reservation = TEST_RESERVATION_1.copy();
        reservation.setDate(LocalDate.now().minusDays(1));
        assertThrows(ValidationException.class, () -> reservationValidator.validateReservation(reservation));
    }

    @Test
    public void givenNoPax_whenValidateReservation_thenValidationException() {
        Reservation reservation = TEST_RESERVATION_1.copy();
        reservation.setPax(null);
        assertThrows(ValidationException.class, () -> reservationValidator.validateReservation(reservation));
    }

    @Test
    public void givenNegativePax_whenValidateReservation_thenValidationException() {
        Reservation reservation = TEST_RESERVATION_1.copy();
        reservation.setPax(-1L);
        assertThrows(ValidationException.class, () -> reservationValidator.validateReservation(reservation));
    }

    @Test
    public void givenNotesTooLong_whenValidateReservation_thenValidationException() {
        Reservation reservation = TEST_RESERVATION_1.copy();
        reservation.setNotes("a".repeat(100001));
        assertThrows(ValidationException.class, () -> reservationValidator.validateReservation(reservation));
    }

   @Test
   public void givenNoPlace_whenValidateReservation_thenValidationException() {
       Reservation reservation = TEST_RESERVATION_1.copy();
       reservation.setPlace(null);
       assertThrows(ValidationException.class, () -> reservationValidator.validateReservation(reservation));
    }

    @Test
    public void givenValidData_whenValidateReservationCheckAvailabilityDto_thenNoException() {
        assertDoesNotThrow(() -> reservationValidator.validateReservationCheckAvailabilityDto(TEST_RESERVATION_AVAILABILITY));
    }

    @Test
    public void givenNoStartTime_whenValidateReservationCheckAvailabilityDto_thenNoException() {
        ReservationCheckAvailabilityDto dto = TEST_RESERVATION_AVAILABILITY.copy();
        dto.setStartTime(null);
        assertThrows(ValidationException.class, () -> reservationValidator.validateReservationCheckAvailabilityDto(dto));
    }

    @Test
    public void givenNoDate_whenValidateReservationCheckAvailabilityDto_thenValidationException() {
        ReservationCheckAvailabilityDto dto = TEST_RESERVATION_AVAILABILITY.copy();
        dto.setDate(null);
        assertThrows(ValidationException.class, () -> reservationValidator.validateReservationCheckAvailabilityDto(dto));
    }

    @Test
    public void givenNoPax_whenValidateReservationCheckAvailabilityDto_thenValidationException() {
        ReservationCheckAvailabilityDto dto = TEST_RESERVATION_AVAILABILITY.copy();
        dto.setPax(null);
        assertThrows(ValidationException.class, () -> reservationValidator.validateReservationCheckAvailabilityDto(dto));
    }

    @Test
    public void givenNegativePax_whenValidateReservationCheckAvailabilityDto_thenValidationException() {
        ReservationCheckAvailabilityDto dto = TEST_RESERVATION_AVAILABILITY.copy();
        dto.setPax(-1L);
        assertThrows(ValidationException.class, () -> reservationValidator.validateReservationCheckAvailabilityDto(dto));
    }
}