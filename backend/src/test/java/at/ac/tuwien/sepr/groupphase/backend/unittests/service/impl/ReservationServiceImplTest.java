package at.ac.tuwien.sepr.groupphase.backend.unittests.service.impl;

import at.ac.tuwien.sepr.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationCheckAvailabilityDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationEditDto;
import at.ac.tuwien.sepr.groupphase.backend.enums.ReservationResponseEnum;
import at.ac.tuwien.sepr.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepr.groupphase.backend.service.ReservationService;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ReservationServiceImplTest implements TestData {

    @Autowired
    private ReservationService service;

    @Test
    @Transactional
    public void givenValidData_whenCreateGuestReservation_thenReturnDto() throws MessagingException, ValidationException {
        ReservationCreateDto response = service.create(TEST_RESERVATION_CREATE_DTO_GUEST);
        TEST_APPLICATION_USER_GUEST.setId(response.getUser().getId());

        assertAll(
            () -> assertEquals(TEST_APPLICATION_USER_GUEST, response.getUser()),
            () -> assertEquals(TEST_APPLICATION_USER_FIRST_NAME, response.getFirstName()),
            () -> assertEquals(TEST_APPLICATION_USER_LAST_NAME, response.getLastName()),
            () -> assertEquals(TEST_RESERVATION_START_TIME, response.getStartTime()),
            () -> assertEquals(TEST_RESERVATION_END_TIME, response.getEndTime()),
            () -> assertEquals(TEST_RESERVATION_DATE, response.getDate()),
            () -> assertEquals(TEST_RESERVATION_PAX, response.getPax()),
            () -> assertEquals(TEST_RESERVATION_NOTES, response.getNotes()),
            () -> assertEquals(TEST_APPLICATION_USER_EMAIL, response.getEmail()),
            () -> assertEquals(TEST_APPLICATION_USER_MOBILE_NUMBER, response.getMobileNumber())
        );
    }

    @Test
    @Transactional
    public void givenInvalidData_whenCreateGuestReservation_thenThrowValidationException() throws ValidationException, MessagingException {
        ReservationCreateDto dto = TEST_RESERVATION_CREATE_DTO_GUEST.copy();
        dto.setStartTime(null);

        assertThrows(ValidationException.class, () -> service.create(dto));
    }

    @Test
    @Transactional
    public void givenValidData_whenUpdate_thenReturnDto() throws ValidationException {
        ReservationEditDto response = service.update(TEST_RESERVATION_EDIT_DTO);

        assertAll(
            () -> assertEquals(TEST_RESERVATION_DETAIL_ID, response.getReservationId()),
            () -> assertEquals(TEST_RESERVATION_START_TIME, response.getStartTime()),
            () -> assertEquals(TEST_RESERVATION_END_TIME, response.getEndTime()),
            () -> assertEquals(TEST_RESERVATION_DATE, response.getDate()),
            () -> assertEquals(TEST_RESERVATION_PAX, response.getPax()),
            () -> assertEquals(TEST_RESERVATION_NOTES, response.getNotes()),
            () -> assertEquals(TEST_PLACE_AVAILABLE_1.getId(), response.getPlaceId()),
            () -> assertEquals(TEST_RESERVATION_HASH_VALUE_1, response.getHashedId())
        );
    }

    @Test
    @Transactional
    public void givenValidData_whenGetAvailability_thenReturnAvailable() throws ValidationException {
        ReservationResponseEnum response = service.getAvailability(TEST_RESERVATION_AVAILABILITY);

        assertEquals(ReservationResponseEnum.AVAILABLE, response);
    }

    @Test
    @Transactional
    public void givenValidData_whenGetAvailability_thenReturnClosed() throws ValidationException {
        ReservationCheckAvailabilityDto dto = TEST_RESERVATION_AVAILABILITY.copy();
        dto.setDate(LocalDate.of(2024, 6, 12));
        ReservationResponseEnum response = service.getAvailability(dto);

        assertEquals(ReservationResponseEnum.CLOSED, response);
    }

    @Test
    @Transactional
    public void givenValidData_whenGetAvailability_thenReturnOutsideOpeningHours() throws ValidationException {
        ReservationCheckAvailabilityDto dto = TEST_RESERVATION_AVAILABILITY.copy();
        dto.setStartTime(LocalDateTime.of(2024, 7, 1, 23, 0, 0, 0).toLocalTime());
        ReservationResponseEnum response = service.getAvailability(dto);

        assertEquals(ReservationResponseEnum.OUTSIDE_OPENING_HOURS, response);
    }

    @Test
    @Transactional
    public void givenValidData_whenGetAvailability_thenReturnTooManyPax() throws ValidationException {
        ReservationCheckAvailabilityDto dto = TEST_RESERVATION_AVAILABILITY.copy();
        dto.setPax(20L);
        ReservationResponseEnum response = service.getAvailability(dto);

        assertEquals(ReservationResponseEnum.TOO_MANY_PAX, response);
    }

    @Test
    @Transactional
    public void givenValidData_whenGetAvailability_thenReturnDateInPast() throws ValidationException {
        ReservationCheckAvailabilityDto dto = TEST_RESERVATION_AVAILABILITY.copy();
        dto.setDate(LocalDate.of(2023, 1, 1));
        ReservationResponseEnum response = service.getAvailability(dto);

        assertEquals(ReservationResponseEnum.DATE_IN_PAST, response);
    }

    @Test
    @Transactional
    public void givenValidData_whenGetAvailability_thenReturnRespectClosingHour() throws ValidationException {
        ReservationCheckAvailabilityDto dto = TEST_RESERVATION_AVAILABILITY.copy();
        dto.setStartTime(LocalDateTime.of(2024, 7, 1, 21, 30, 0, 0).toLocalTime());
        ReservationResponseEnum response = service.getAvailability(dto);

        assertEquals(ReservationResponseEnum.RESPECT_CLOSING_HOUR, response);
    }

    @Test
    @Transactional
    public void givenStartTime1HBeforeClosing_whenGetAvailability_thenReturnAvailable() throws ValidationException {
        ReservationCheckAvailabilityDto dto = TEST_RESERVATION_AVAILABILITY.copy();
        dto.setStartTime(LocalDateTime.of(2024, 7, 1, 21, 0, 0, 0).toLocalTime());
        ReservationResponseEnum response = service.getAvailability(dto);

        assertEquals(ReservationResponseEnum.AVAILABLE, response);
    }
}