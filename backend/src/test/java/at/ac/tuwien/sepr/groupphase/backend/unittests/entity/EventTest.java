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
public class EventTest implements TestData {

    @Test
    void givevnValidData_whenBuildEvent_thenBuildEvent() {
        assertAll(
            () -> assertEquals(TEST_EVENT_NAME_1, TEST_EVENT_1.getName()),
            () -> assertEquals(TEST_EVENT_START_TIME_1, TEST_EVENT_1.getStartTime()),
            () -> assertEquals(TEST_EVENT_END_TIME_1, TEST_EVENT_1.getEndTime()),
            () -> assertEquals(TEST_EVENT_DESC_1, TEST_EVENT_1.getDescription()),
            () -> assertEquals(TEST_EVENT_HASH_1, TEST_EVENT_1.getHashId())
        );
    }

    @Test
    void givenEqualEvents_whenEquals_thenTrue() {
        assertAll(
            () -> assertTrue(TEST_EVENT_1.equals(TEST_EVENT_3) && TEST_EVENT_3.equals(TEST_EVENT_1))
        );
    }
}
