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
public class ApplicationUserTest implements TestData {

    @Test
    void givenLegalData_whenBuildApplicationUser_thenBuildApplicationUser() {
        assertAll(
            () -> assertEquals(1L, TEST_APPLICATION_USER_CUSTOMER_1.getId()),
            () -> assertEquals(TEST_APPLICATION_USER_FIRST_NAME, TEST_APPLICATION_USER_CUSTOMER_1.getFirstName()),
            () -> assertEquals(TEST_APPLICATION_USER_LAST_NAME, TEST_APPLICATION_USER_CUSTOMER_1.getLastName()),
            () -> assertEquals(TEST_APPLICATION_USER_EMAIL, TEST_APPLICATION_USER_CUSTOMER_1.getEmail()),
            () -> assertEquals(TEST_APPLICATION_USER_MOBILE_NUMBER, TEST_APPLICATION_USER_CUSTOMER_1.getMobileNumber()),
            () -> assertEquals(TEST_APPLICATION_USER_PASSWORD, TEST_APPLICATION_USER_CUSTOMER_1.getPassword()),
            () -> assertEquals(TEST_APPLICATION_USER_ROLE, TEST_APPLICATION_USER_CUSTOMER_1.getRole())
        );
    }

    @Test
    void givenEqualUsers_whenEquals_thenTrue() {
        assertAll(
            () -> assertTrue(TEST_APPLICATION_USER_CUSTOMER_1.equals(TEST_APPLICATION_USER_CUSTOMER_2) && TEST_APPLICATION_USER_CUSTOMER_2.equals(TEST_APPLICATION_USER_CUSTOMER_1)),
            () -> assertEquals(TEST_APPLICATION_USER_CUSTOMER_1.hashCode(), TEST_APPLICATION_USER_CUSTOMER_2.hashCode())
        );
    }

    @Test
    void givenLegalData_whenToString_thenReturnAsString() {
        assertEquals(TEST_APPLICATION_USER_EXPECTED_STRING, TEST_APPLICATION_USER_CUSTOMER_1.toString());
    }

    @Test
    void givenEqualData_whenHashCode_thenTrue() {
        assertEquals(TEST_APPLICATION_USER_CUSTOMER_1.hashCode(), TEST_APPLICATION_USER_CUSTOMER_2.hashCode());
    }
}