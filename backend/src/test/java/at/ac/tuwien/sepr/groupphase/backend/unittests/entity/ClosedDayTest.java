package at.ac.tuwien.sepr.groupphase.backend.unittests.entity;

import at.ac.tuwien.sepr.groupphase.backend.basetest.TestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
public class ClosedDayTest implements TestData {

    @Test
    void givenLegalData_whenBuildClosedDay_thenBuildClosedDay() {
        assertAll(
            () -> assertEquals(1L, TEST_CLOSED_DAY_1.getId()),
            () -> assertEquals(TEST_CLOSED_DAY_DATE, TEST_CLOSED_DAY_1.getDate()),
            () -> assertEquals(TEST_RESTAURANT_1, TEST_CLOSED_DAY_1.getRestaurant())
        );
    }

    @Test
    void givenEqualClosedDays_whenEquals_thenTrue() {
        assertAll(
            () -> assertTrue(TEST_CLOSED_DAY_1.equals(TEST_CLOSED_DAY_2) && TEST_CLOSED_DAY_2.equals(TEST_CLOSED_DAY_1)),
            () -> assertEquals(TEST_CLOSED_DAY_1.hashCode(), TEST_CLOSED_DAY_2.hashCode())
        );
    }

    @Test
    void givenLegalData_whenToString_thenReturnAsString() {
        assertEquals(TEST_CLOSED_DAY_EXPECTED_STRING, TEST_CLOSED_DAY_1.toString());
    }

    @Test
    void givenEqualData_whenHashCode_thenTrue() {
        assertEquals(TEST_CLOSED_DAY_1.hashCode(), TEST_CLOSED_DAY_2.hashCode());
    }
}