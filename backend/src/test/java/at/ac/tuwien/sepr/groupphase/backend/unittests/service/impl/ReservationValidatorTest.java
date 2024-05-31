package at.ac.tuwien.sepr.groupphase.backend.unittests.service.impl;

import at.ac.tuwien.sepr.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepr.groupphase.backend.entity.Reservation;
import at.ac.tuwien.sepr.groupphase.backend.enums.RoleEnum;
import at.ac.tuwien.sepr.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepr.groupphase.backend.repository.ReservationRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.ApplicationUserService;
import at.ac.tuwien.sepr.groupphase.backend.service.impl.ReservationValidator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class ReservationValidatorTest implements TestData {

    private ReservationValidator reservationValidator;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ApplicationUserService applicationUserService;

    @BeforeEach
    public void beforeEach() {
        MockitoAnnotations.openMocks(this);
        this.reservationValidator = new ReservationValidator(reservationRepository, applicationUserService);
    }

    @Test
    public void givenValidId_whenValidateReservationDelete_thenNoException() {
        Reservation reservation = TEST_RESERVATION_1.copy();
        reservation.setId(TEST_VALID_ID);
        when(reservationRepository.findById(TEST_VALID_ID)).thenReturn(Optional.of(reservation));
        when(applicationUserService.getCurrentApplicationUser()).thenReturn(reservation.getApplicationUser());
        assertDoesNotThrow(() -> reservationValidator.validateReservationDelete(TEST_VALID_ID));
    }

    @Test
    public void givenInvalidId_whenValidateReservationDelete_thenValidationException() {
        assertThrows(ValidationException.class, () -> reservationValidator.validateReservationDelete(TEST_INVALID_ID));
    }

    @Test
    public void givenNonExistingId_whenValidateReservationDelete_thenValidationException() {
        when(reservationRepository.findById(TEST_INVALID_ID)).thenReturn(Optional.empty());
        assertThrows(ValidationException.class, () -> reservationValidator.validateReservationDelete(TEST_INVALID_ID));
    }

    // @Test
    public void givenReservationBelongsToAnotherUser_whenValidateReservationDelete_thenValidationException() {
        Reservation reservation = TEST_RESERVATION_1.copy();
        reservation.setId(TEST_VALID_ID);
        ApplicationUser anotherUser = new ApplicationUser();
        anotherUser.setRole(RoleEnum.CUSTOMER);
        anotherUser.setId(2L);
        when(reservationRepository.findById(TEST_VALID_ID)).thenReturn(Optional.of(reservation));
        when(applicationUserService.getCurrentApplicationUser()).thenReturn(anotherUser);
        assertThrows(ValidationException.class, () -> reservationValidator.validateReservationDelete(TEST_VALID_ID));
    }

    // @Test
    public void givenReservationIsInThePast_whenValidateReservationDelete_thenValidationException() {
        Reservation reservation = TEST_RESERVATION_1.copy();
        reservation.setId(TEST_VALID_ID);
        reservation.setDate(LocalDate.now().minusDays(1));
        when(reservationRepository.findById(TEST_VALID_ID)).thenReturn(Optional.of(reservation));
        when(applicationUserService.getCurrentApplicationUser()).thenReturn(reservation.getApplicationUser());
        assertThrows(ValidationException.class, () -> reservationValidator.validateReservationDelete(TEST_VALID_ID));
    }

    // @Test
    public void givenReservationStartsInLessThanOneHour_whenValidateReservationDelete_thenValidationException() {
        Reservation tempReservation = TEST_RESERVATION_1.copy();
        tempReservation.setId(TEST_VALID_ID);
        tempReservation.setStartTime(LocalTime.now()); // set start time to 30 minutes from now
        tempReservation.setDate(LocalDate.now());
        when(reservationRepository.findById(TEST_VALID_ID)).thenReturn(Optional.of(tempReservation));
        when(applicationUserService.getCurrentApplicationUser()).thenReturn(tempReservation.getApplicationUser());
        assertThrows(ValidationException.class, () -> reservationValidator.validateReservationDelete(TEST_VALID_ID));
    }
}