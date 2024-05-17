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
public class RestaurantTest implements TestData {

    @Test
    void givenLegalData_whenBuildRestaurant_thenBuildRestaurant() {
        assertAll(
            () -> assertEquals(1L, TEST_RESTAURANT_1.getId()),
            () -> assertEquals(TEST_RESTAURANT_NAME, TEST_RESTAURANT_1.getName()),
            () -> assertEquals(TEST_RESTAURANT_ADDRESS, TEST_RESTAURANT_1.getAddress())
        );
    }

    @Test
    void givenEqualRestaurants_whenEquals_thenTrue() {
        assertAll(
            () -> assertTrue(TEST_RESTAURANT_1.equals(TEST_RESTAURANT_2) && TEST_RESTAURANT_2.equals(TEST_RESTAURANT_1)),
            () -> assertEquals(TEST_RESTAURANT_1.hashCode(), TEST_RESTAURANT_2.hashCode())
        );
    }

    @Test
    void givenLegalData_whenToString_thenReturnAsString() {
        assertEquals(TEST_RESTAURANT_EXPECTED_STRING, TEST_RESTAURANT_1.toString());
    }

    @Test
    void givenEqualData_whenHashCode_thenTrue() {
        assertEquals(TEST_RESTAURANT_1.hashCode(), TEST_RESTAURANT_2.hashCode());
    }
}
