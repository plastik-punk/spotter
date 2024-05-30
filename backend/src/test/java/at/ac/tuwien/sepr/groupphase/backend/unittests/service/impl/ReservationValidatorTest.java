package at.ac.tuwien.sepr.groupphase.backend.unittests.service.impl;

import at.ac.tuwien.sepr.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationCheckAvailabilityDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepr.groupphase.backend.entity.Reservation;
import at.ac.tuwien.sepr.groupphase.backend.enums.RoleEnum;
import at.ac.tuwien.sepr.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepr.groupphase.backend.repository.ReservationRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.ApplicationUserService;
import at.ac.tuwien.sepr.groupphase.backend.service.impl.ReservationValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

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
    public void givenValidReservation_whenValidateReservation_thenNoException() {
        assertDoesNotThrow(() -> reservationValidator.validateReservation(TEST_RESERVATION_1));
    }

    @Test
    public void givenValidReservationCreateDto_whenValidateReservation_thenNoException() throws ValidationException {
        assertDoesNotThrow(() -> reservationValidator.validateReservationCreateDto(TEST_RESERVATION_CREATE_DTO_GUEST));
    }

    @Test
    public void givenValidReservationDetailDto_whenValidateReservation_thenNoException() throws ValidationException {
        assertDoesNotThrow(() -> reservationValidator.validateReservationDetailDto(TEST_RESERVATION_DETAIL_DTO));
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
    public void givenNoStartTime_whenValidateReservationDetailDto_thenValidationException() {
        ReservationDetailDto dto = TEST_RESERVATION_DETAIL_DTO.copy();
        dto.setStartTime(null);
        assertThrows(ValidationException.class, () -> reservationValidator.validateReservationDetailDto(dto));
    }

    @Test
    public void givenNoDate_whenValidateReservationDetailDto_thenValidationException() {
        ReservationDetailDto dto = TEST_RESERVATION_DETAIL_DTO.copy();
        dto.setDate(null);
        assertThrows(ValidationException.class, () -> reservationValidator.validateReservationDetailDto(dto));
    }

    @Test
    public void givenNoPax_whenValidateReservationDetailDto_thenValidationException() {
        ReservationDetailDto dto = TEST_RESERVATION_DETAIL_DTO.copy();
        dto.setPax(null);
        assertThrows(ValidationException.class, () -> reservationValidator.validateReservationDetailDto(dto));
    }

    @Test
    public void givenNegativePax_whenValidateReservationDetailDto_thenValidationException() {
        ReservationDetailDto dto = TEST_RESERVATION_DETAIL_DTO.copy();
        dto.setPax(-1L);
        assertThrows(ValidationException.class, () -> reservationValidator.validateReservationDetailDto(dto));
    }

    @Test
    public void givenNoNotes_whenValidateReservationDetailDto_thenNoException() {
        ReservationDetailDto dto = TEST_RESERVATION_DETAIL_DTO.copy();
        dto.setNotes(null);
        assertDoesNotThrow(() -> reservationValidator.validateReservationDetailDto(dto));
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

    @Test
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

    @Test
    public void givenReservationIsInThePast_whenValidateReservationDelete_thenValidationException() {
        Reservation reservation = TEST_RESERVATION_1.copy();
        reservation.setId(TEST_VALID_ID);
        reservation.setDate(LocalDate.now().minusDays(1));
        when(reservationRepository.findById(TEST_VALID_ID)).thenReturn(Optional.of(reservation));
        when(applicationUserService.getCurrentApplicationUser()).thenReturn(reservation.getApplicationUser());
        assertThrows(ValidationException.class, () -> reservationValidator.validateReservationDelete(TEST_VALID_ID));
    }

    @Test
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