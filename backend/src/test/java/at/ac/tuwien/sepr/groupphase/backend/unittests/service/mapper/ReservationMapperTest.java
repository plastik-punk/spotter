package at.ac.tuwien.sepr.groupphase.backend.unittests.service.mapper;

import at.ac.tuwien.sepr.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.Reservation;
import at.ac.tuwien.sepr.groupphase.backend.service.mapper.ReservationMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ReservationMapperTest implements TestData {

    @Autowired
    private ReservationMapper reservationMapper;

    @Test
    public void givenNothing_whenMapReservationCreateDtoToEntity_thenEntityHasAllProperties() {
        Reservation reservation = reservationMapper.reservationCreateDtoToReservation(TEST_RESERVATION_CREATE_DTO_CUSTOMER);

        assertAll(
            () -> assertNull(reservation.getId()),
            () -> assertEquals(TEST_APPLICATION_USER_CUSTOMER_1, reservation.getApplicationUser()),
            () -> assertEquals(TEST_RESERVATION_START_TIME, reservation.getStartTime()),
            () -> assertEquals(TEST_RESERVATION_END_TIME, reservation.getEndTime()),
            () -> assertEquals(TEST_RESERVATION_DATE, reservation.getDate()),
            () -> assertEquals(TEST_RESERVATION_PAX, reservation.getPax()),
            () -> assertEquals(TEST_RESERVATION_NOTES, reservation.getNotes())
        );
    }

    @Test
    public void givenNothing_whenMapEntityToReservationCreateDto_thenDtoHasAllProperties() {
        ReservationCreateDto dto = reservationMapper.reservationToReservationCreateDto(TEST_RESERVATION_1);

        assertAll(
            () -> assertEquals(TEST_APPLICATION_USER_CUSTOMER_1, dto.getUser()),
            () -> assertEquals(TEST_APPLICATION_USER_FIRST_NAME, dto.getFirstName()),
            () -> assertEquals(TEST_APPLICATION_USER_LAST_NAME, dto.getLastName()),
            () -> assertEquals(TEST_RESERVATION_START_TIME, dto.getStartTime()),
            () -> assertEquals(TEST_RESERVATION_END_TIME, dto.getEndTime()),
            () -> assertEquals(TEST_RESERVATION_DATE, dto.getDate()),
            () -> assertEquals(TEST_RESERVATION_PAX, dto.getPax()),
            () -> assertEquals(TEST_RESERVATION_NOTES, dto.getNotes()),
            () -> assertEquals(TEST_APPLICATION_USER_CUSTOMER_1.getEmail(), dto.getEmail()),
            () -> assertEquals(TEST_APPLICATION_USER_CUSTOMER_1.getMobileNumber(), dto.getMobileNumber())
        );
    }
}