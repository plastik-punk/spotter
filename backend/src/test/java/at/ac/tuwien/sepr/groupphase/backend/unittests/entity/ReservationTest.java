package at.ac.tuwien.sepr.groupphase.backend.unittests.entity;

import at.ac.tuwien.sepr.groupphase.backend.basetest.TestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
public class ReservationTest implements TestData {

    @Test
    void givenLegalData_whenBuildReservation_thenBuildReservation() {
        assertAll(
            () -> assertEquals(1L, TEST_RESERVATION_1.getId()),
            () -> assertEquals(TEST_APPLICATION_USER_CUSTOMER_1, TEST_RESERVATION_1.getApplicationUser()),
            () -> assertEquals(TEST_RESERVATION_START_TIME, TEST_RESERVATION_1.getStartTime()),
            () -> assertEquals(TEST_RESERVATION_END_TIME, TEST_RESERVATION_1.getEndTime()),
            () -> assertEquals(TEST_RESERVATION_DATE, TEST_RESERVATION_1.getDate()),
            () -> assertEquals(TEST_RESERVATION_PAX, TEST_RESERVATION_1.getPax()),
            () -> assertEquals(TEST_RESERVATION_NOTES, TEST_RESERVATION_1.getNotes())
        );
    }

    @Test
    void givenEqualReservations_whenEquals_thenTrue() {
        assertAll(
            () -> assertTrue(TEST_RESERVATION_1.equals(TEST_RESERVATION_2) && TEST_RESERVATION_2.equals(TEST_RESERVATION_1))
        );
    }

    @Test
    void givenLegalData_whenToString_thenReturnAsString() {
        assertEquals(TEST_RESERVATION_EXPECTED_STRING, TEST_RESERVATION_1.toString());
    }

}