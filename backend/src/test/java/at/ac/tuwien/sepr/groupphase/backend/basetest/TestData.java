package at.ac.tuwien.sepr.groupphase.backend.basetest;

import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepr.groupphase.backend.entity.Place;
import at.ac.tuwien.sepr.groupphase.backend.enums.RoleEnum;
import at.ac.tuwien.sepr.groupphase.backend.enums.StatusEnum;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * This interface provides test data for the Profile "test".
 * The data is saved explicitly like this for asserting the correctness of the test results by comparison.
 */
public interface TestData {

    // ---------------------------------------------
    // COMMON TEST DATA
    // ---------------------------------------------

    String BASE_URI = "/api/v1";

    // ---------------------------------------------
    // ROLE TEST DATA
    // ---------------------------------------------

    String ADMIN_USER = "admin@email.com";
    List<String> ADMIN_ROLES = new ArrayList<>() {
        {
            add("ROLE_ADMIN");
            add("ROLE_USER");
        }
    };
    String DEFAULT_USER = "admin@email.com";
    List<String> USER_ROLES = new ArrayList<>() {
        {
            add("ROLE_USER");
        }
    };
    String GUEST_USER = "guest@email.com";
    List<String> GUEST_ROLES = new ArrayList<>() {
        {
            add("ROLE_USER");
        }
    };

    // ---------------------------------------------
    // APPLICATION USER TEST DATA
    // ---------------------------------------------

    ApplicationUser TEST_RESERVATION_APPLICATION_USER_CUSTOMER = ApplicationUser.ApplicationUserBuilder.anApplicationUser()
        .withId(1L)
        .withFirstName("John")
        .withLastName("Doe")
        .withEmail("johndoe@mail.at")
        .withMobileNumber("06504222222")
        .withPassword("password")
        .withRole(RoleEnum.CUSTOMER)
        .build();
    ApplicationUser TEST_RESERVATION_APPLICATION_USER_GUEST = ApplicationUser.ApplicationUserBuilder.anApplicationUser()
        .withId(2L)
        .withFirstName("Guest")
        .withLastName("Otter")
        .withEmail("guest@mail.at")
        .withMobileNumber("06504111111")
        .withPassword("guest")
        .withRole(RoleEnum.GUEST)
        .build();

    // ---------------------------------------------
    // MESSAGE TEST DATA
    // ---------------------------------------------

    String MESSAGE_BASE_URI = BASE_URI + "/messages";
    Long TEST_MESSAGE_ID = 1L;
    String TEST_NEWS_TITLE = "Title";
    String TEST_NEWS_SUMMARY = "Summary";
    String TEST_NEWS_TEXT = "TestMessageText";
    LocalDateTime TEST_NEWS_PUBLISHED_AT =
        LocalDateTime.of(2019, 11, 13, 12, 15, 0, 0);

    // ---------------------------------------------
    // RESERVATION TEST DATA
    // ---------------------------------------------

    String RESERVATION_BASE_URI = BASE_URI + "/reservations";
    LocalTime TEST_RESERVATION_START_TIME = LocalDateTime.of(2021, 1, 1, 12, 0, 0, 0).toLocalTime();
    LocalTime TEST_RESERVATION_END_TIME = LocalDateTime.of(2021, 1, 1, 14, 0, 0, 0).toLocalTime();
    LocalDate TEST_RESERVATION_DATE = LocalDate.of(2021, 1, 1);
    Long TEST_RESERVATION_PAX = 4L;
    String TEST_RESERVATION_NOTES = "Test Notes";

    // ---------------------------------------------
    // PLACE TEST DATA
    // ---------------------------------------------

    // TODO

    // ---------------------------------------------
    // ... TEST DATA
    // ---------------------------------------------

    // TODO: write your test data here
    // use prefix "TEST_" for all your test data (except for baseUri suffix)
    // followed by context in which data is going to be used (e.g. reservation, message, ...)
    // followed by the name of the data
}
