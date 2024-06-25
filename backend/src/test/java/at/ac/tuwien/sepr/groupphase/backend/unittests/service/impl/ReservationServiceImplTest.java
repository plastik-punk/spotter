package at.ac.tuwien.sepr.groupphase.backend.unittests.service.impl;

import at.ac.tuwien.sepr.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationCheckAvailabilityDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationEditDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationModalDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.Reservation;
import at.ac.tuwien.sepr.groupphase.backend.enums.ReservationResponseEnum;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.repository.ReservationRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.ReservationService;
import jakarta.mail.MessagingException;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ReservationServiceImplTest implements TestData {

    @Autowired
    private ReservationService service;
    @Autowired
    private ReservationRepository reservationRepository;

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
    public void givenInvalidData_whenCreateGuestReservation_thenThrowConstraintViolationException() {
        ReservationCreateDto dto = TEST_RESERVATION_CREATE_DTO_GUEST.copy();
        dto.setPax(-10L);

        assertThrows(ConstraintViolationException.class, () -> service.create(dto));
    }


    @Test
    @Transactional
    public void givenValidData_whenGetModalDetail_thenReturnDto() throws ValidationException, MessagingException {
        ReservationCreateDto dto = ReservationCreateDto.ReservationCreateDtoBuilder.aReservationCreateDto()
            .withApplicationUser(TEST_APPLICATION_USER_CUSTOMER_1)
            .withFirstName(TEST_APPLICATION_USER_CUSTOMER_1.getFirstName())
            .withLastName(TEST_APPLICATION_USER_CUSTOMER_1.getLastName())
            .withStartTime(TEST_RESERVATION_START_TIME)
            .withEndTime(TEST_RESERVATION_END_TIME)
            .withDate(TEST_RESERVATION_DATE)
            .withPax(TEST_RESERVATION_PAX)
            .withNotes(TEST_RESERVATION_NOTES)
            .withEmail(TEST_APPLICATION_USER_CUSTOMER_1.getEmail())
            .withMobileNumber(TEST_APPLICATION_USER_CUSTOMER_1.getMobileNumber())
            .build();
        service.create(dto);

        ReservationModalDetailDto response = service.getModalDetail(TEST_RESERVATION_HASH_VALUE_2);

        assertAll(
            () -> assertEquals(TEST_APPLICATION_USER_FIRST_NAME, response.getFirstName()),
            () -> assertEquals(TEST_APPLICATION_USER_LAST_NAME, response.getLastName()),
            () -> assertEquals(TEST_RESERVATION_START_TIME, response.getStartTime()),
            () -> assertEquals(TEST_RESERVATION_END_TIME, response.getEndTime()),
            () -> assertEquals(TEST_RESERVATION_DATE, response.getDate()),
            () -> assertEquals(TEST_RESERVATION_NOTES, response.getNotes())
        );
    }

    // TODO: test update

    @Test
    @Transactional
    public void givenValidData_whenGetAvailability_thenReturnAvailable() throws ValidationException {
        ReservationResponseEnum response = service.getAvailability(TEST_RESERVATION_AVAILABILITY);

        assertEquals(ReservationResponseEnum.AVAILABLE, response);
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

    //@Test
    //@Transactional
    public void givenValidHashId_whenConfirm_thenReservationIsConfirmed() throws ValidationException {
        Reservation reservation = TEST_RESERVATION_1.copy();

        //TODO: nicht testbar, da bei getByHashId der User abgeglichen wird

        reservation.setHashValue("hash");
        reservation = reservationRepository.save(reservation);
        service.confirm("hash");

        Optional<Reservation> confirmedReservation = reservationRepository.findById(reservation.getId());
        assertTrue(confirmedReservation.get().isConfirmed());
    }

    @Test
    @Transactional
    public void givenInvalidHashId_whenConfirm_thenThrowNotFoundException() {
        assertThrows(NotFoundException.class, () -> service.confirm("invalid"));
    }

    //@Test
    //@Transactional
    public void givenValidHashId_whenUnconfirm_thenReservationIsUnconfirmed() throws ValidationException {
        Reservation reservation = TEST_RESERVATION_2.copy();

        //TODO: nicht testbar, da bei getByHashId der User abgeglichen wird

        reservation.setConfirmed(true);
        reservation.setHashValue("hash");
        reservation = reservationRepository.save(reservation);
        service.unconfirm("hash");

        Optional<Reservation> unconfirmedReservation = reservationRepository.findById(reservation.getId());
        assertFalse(unconfirmedReservation.get().isConfirmed());
    }

    @Test
    @Transactional
    public void givenInvalidHashId_whenUnconfirm_thenThrowNotFoundException() {
        assertThrows(NotFoundException.class, () -> service.unconfirm("invalid"));
    }
}