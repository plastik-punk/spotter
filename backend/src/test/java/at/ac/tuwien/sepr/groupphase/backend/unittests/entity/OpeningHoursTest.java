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
public class OpeningHoursTest implements TestData {

    @Test
    void givenLegalData_whenBuildOpeningHours_thenBuildOpeningHours() {
        assertAll(
            () -> assertEquals(1L, TEST_OPENING_HOURS_1.getId()),
            () -> assertEquals(TEST_OPENING_HOURS_DAY_OF_WEEK, TEST_OPENING_HOURS_1.getDayOfWeek()),
            () -> assertEquals(TEST_OPENING_HOURS_OPENING_TIME, TEST_OPENING_HOURS_1.getOpeningTime()),
            () -> assertEquals(TEST_OPENING_HOURS_CLOSING_TIME, TEST_OPENING_HOURS_1.getClosingTime()),
            () -> assertEquals(TEST_RESTAURANT_1, TEST_OPENING_HOURS_1.getRestaurant())
        );
    }

    @Test
    void givenEqualOpeningHours_whenEquals_thenTrue() {
        assertAll(
            () -> assertTrue(TEST_OPENING_HOURS_1.equals(TEST_OPENING_HOURS_2) && TEST_OPENING_HOURS_2.equals(TEST_OPENING_HOURS_1)),
            () -> assertEquals(TEST_OPENING_HOURS_1.hashCode(), TEST_OPENING_HOURS_2.hashCode())
        );
    }

    @Test
    void givenLegalData_whenToString_thenReturnAsString() {
        assertEquals(TEST_OPENING_HOURS_EXPECTED_STRING, TEST_OPENING_HOURS_1.toString());
    }

    @Test
    void givenEqualData_whenHashCode_thenTrue() {
        assertEquals(TEST_OPENING_HOURS_1.hashCode(), TEST_OPENING_HOURS_2.hashCode());
    }
}