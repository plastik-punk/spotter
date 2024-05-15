package at.ac.tuwien.sepr.groupphase.backend.basetest;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepr.groupphase.backend.entity.Place;
import at.ac.tuwien.sepr.groupphase.backend.entity.Reservation;
import at.ac.tuwien.sepr.groupphase.backend.enums.RoleEnum;
import at.ac.tuwien.sepr.groupphase.backend.enums.StatusEnum;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

// ---------------------------------------------
// NAMING CONVENTIONS
// ---------------------------------------------

// use prefix "TEST_" for all your test data (except for baseUri suffix)
// followed by context in which data is going to be used (e.g. reservation, message, ...)
// followed by the name of the data

/**
 * This interface provides test data for the Profile "test".
 * The data is saved explicitly like this for asserting the correctness of the test results by comparison.
 */
public interface TestData {

    // ---------------------------------------------
    // COMMON TEST DATA
    // ---------------------------------------------

    String BASE_URI = "/api/v1";
    String RESERVATION_BASE_URI = BASE_URI + "/reservations";
    String MESSAGE_BASE_URI = BASE_URI + "/messages";

    // ---------------------------------------------
    // ROLE TEST DATA
    // ---------------------------------------------

    String TEST_USER_ADMIN = "admin@email.com";
    List<String> TEST_ROLES_ADMIN = new ArrayList<>() {
        {
            add("ROLE_ADMIN");
            add("ROLE_USER");
        }
    };
    String TEST_USER_CUSTOMER = "admin@email.com";
    List<String> TEST_ROLES_CUSTOMER = new ArrayList<>() {
        {
            add("ROLE_USER");
        }
    };
    String TEST_USER_GUEST = "guest@email.com";
    List<String> TEST_ROLES_GUEST = new ArrayList<>() {
        {
            add("ROLE_GUEST");
        }
    };

    // ---------------------------------------------
    // MESSAGE TEST DATA
    // ---------------------------------------------

    Long TEST_MESSAGE_ID = 1L;
    String TEST_NEWS_TITLE = "Title";
    String TEST_NEWS_SUMMARY = "Summary";
    String TEST_NEWS_TEXT = "TestMessageText";
    LocalDateTime TEST_NEWS_PUBLISHED_AT =
        LocalDateTime.of(2019, 11, 13, 12, 15, 0, 0);

    // ---------------------------------------------
    // PLACE TEST DATA
    // ---------------------------------------------

    Place TEST_PLACE_AVAILABLE_1 = Place.PlaceBuilder.aPlace()
        .withId(1L)
        .withPax(4L)
        .withStatus(StatusEnum.AVAILABLE)
        .build();

    Place TEST_PLACE_AVAILABLE_2 = Place.PlaceBuilder.aPlace()
        .withId(2L)
        .withPax(4L)
        .withStatus(StatusEnum.AVAILABLE)
        .build();

    Place TEST_PLACE_RESERVED = Place.PlaceBuilder.aPlace()
        .withId(3L)
        .withPax(6L)
        .withStatus(StatusEnum.RESERVED)
        .build();

    Place TEST_PLACE_OCCUPIED = Place.PlaceBuilder.aPlace()
        .withId(4L)
        .withPax(2L)
        .withStatus(StatusEnum.OCCUPIED)
        .build();

    // ---------------------------------------------
    // APPLICATION USER TEST DATA
    // ---------------------------------------------

    String TEST_APPLICATION_USER_FIRST_NAME = "Otter";
    String TEST_APPLICATION_USER_LAST_NAME = "McOtterface";
    String TEST_APPLICATION_USER_EMAIL = "otter@spotter.at";
    String TEST_APPLICATION_USER_MOBILE_NUMBER = "06501234567";
    String TEST_APPLICATION_USER_PASSWORD = "naughtyotter";
    RoleEnum TEST_APPLICATION_USER_ROLE = RoleEnum.CUSTOMER;

    ApplicationUser TEST_APPLICATION_USER_CUSTOMER_1 = ApplicationUser.ApplicationUserBuilder.anApplicationUser()
        .withId(1L)
        .withFirstName(TEST_APPLICATION_USER_FIRST_NAME)
        .withLastName(TEST_APPLICATION_USER_LAST_NAME)
        .withEmail(TEST_APPLICATION_USER_EMAIL)
        .withMobileNumber(TEST_APPLICATION_USER_MOBILE_NUMBER)
        .withPassword(TEST_APPLICATION_USER_PASSWORD)
        .withRole(TEST_APPLICATION_USER_ROLE)
        .build();

    ApplicationUser TEST_APPLICATION_USER_CUSTOMER_2 = ApplicationUser.ApplicationUserBuilder.anApplicationUser()
        .withId(1L)
        .withFirstName(TEST_APPLICATION_USER_FIRST_NAME)
        .withLastName(TEST_APPLICATION_USER_LAST_NAME)
        .withEmail(TEST_APPLICATION_USER_EMAIL)
        .withMobileNumber(TEST_APPLICATION_USER_MOBILE_NUMBER)
        .withPassword(TEST_APPLICATION_USER_PASSWORD)
        .withRole(TEST_APPLICATION_USER_ROLE)
        .build();

    String TEST_APPLICATION_USER_EXPECTED_STRING = "User{id=1, firstName='" + TEST_APPLICATION_USER_FIRST_NAME
        + "', lastName='" + TEST_APPLICATION_USER_LAST_NAME
        + "', eMail='" + TEST_APPLICATION_USER_EMAIL
        + "', mobileNumber='" + TEST_APPLICATION_USER_MOBILE_NUMBER
        + "', password='" + TEST_APPLICATION_USER_PASSWORD
        + "', role='" + TEST_APPLICATION_USER_ROLE + "'}";

    ApplicationUser TEST_APPLICATION_USER_GUEST = ApplicationUser.ApplicationUserBuilder.anApplicationUser()
        .withId(1L)
        .withFirstName(TEST_APPLICATION_USER_FIRST_NAME)
        .withLastName(TEST_APPLICATION_USER_LAST_NAME)
        .withEmail(TEST_APPLICATION_USER_EMAIL)
        .withMobileNumber(TEST_APPLICATION_USER_MOBILE_NUMBER)
        .withoutPassword()
        .withRole(RoleEnum.GUEST)
        .build();

    // ---------------------------------------------
    // RESERVATION TEST DATA
    // ---------------------------------------------

    LocalTime TEST_RESERVATION_START_TIME = LocalDateTime.of(2025, 1, 1, 12, 0, 0, 0).toLocalTime();
    LocalTime TEST_RESERVATION_END_TIME = LocalDateTime.of(2025, 1, 1, 14, 0, 0, 0).toLocalTime();
    LocalDate TEST_RESERVATION_DATE = LocalDate.of(2025, 1, 1);
    Long TEST_RESERVATION_PAX = 4L;
    String TEST_RESERVATION_NOTES = "Test Notes";

    ReservationCreateDto TEST_RESERVATION_CREATE_DTO_CUSTOMER = ReservationCreateDto.ReservationCreateDtoBuilder.aReservationCreateDto()
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

    ReservationCreateDto TEST_RESERVATION_CREATE_DTO_GUEST = ReservationCreateDto.ReservationCreateDtoBuilder.aReservationCreateDto()
        .withApplicationUser(null)
        .withFirstName(TEST_APPLICATION_USER_GUEST.getFirstName())
        .withLastName(TEST_APPLICATION_USER_GUEST.getLastName())
        .withStartTime(TEST_RESERVATION_START_TIME)
        .withEndTime(TEST_RESERVATION_END_TIME)
        .withDate(TEST_RESERVATION_DATE)
        .withPax(TEST_RESERVATION_PAX)
        .withNotes(TEST_RESERVATION_NOTES)
        .withEmail(TEST_APPLICATION_USER_GUEST.getEmail())
        .withMobileNumber(TEST_APPLICATION_USER_GUEST.getMobileNumber())
        .build();

    Reservation TEST_RESERVATION_1 = Reservation.ReservationBuilder.aReservation()
        .withId(1L)
        .withUser(TEST_APPLICATION_USER_CUSTOMER_1)
        .withStartTime(TEST_RESERVATION_START_TIME)
        .withEndTime(TEST_RESERVATION_END_TIME)
        .withDate(TEST_RESERVATION_DATE)
        .withPax(TEST_RESERVATION_PAX)
        .withNotes(TEST_RESERVATION_NOTES)
        .withPlace(TEST_PLACE_AVAILABLE_1)
        .build();

    Reservation TEST_RESERVATION_2 = Reservation.ReservationBuilder.aReservation()
        .withId(1L)
        .withUser(TEST_APPLICATION_USER_CUSTOMER_1)
        .withStartTime(TEST_RESERVATION_START_TIME)
        .withEndTime(TEST_RESERVATION_END_TIME)
        .withDate(TEST_RESERVATION_DATE)
        .withPax(TEST_RESERVATION_PAX)
        .withNotes(TEST_RESERVATION_NOTES)
        .withPlace(TEST_PLACE_AVAILABLE_1)
        .build();

    String TEST_RESERVATION_EXPECTED_STRING = "Reservation{id=1"
        + ", applicationUser=" + TEST_APPLICATION_USER_CUSTOMER_1.toString()
        + ", startTime=" + TEST_RESERVATION_START_TIME
        + ", date=" + TEST_RESERVATION_DATE
        + ", endTime=" + TEST_RESERVATION_END_TIME
        + ", pax=" + TEST_RESERVATION_PAX
        + ", notes='" + TEST_RESERVATION_NOTES
        + "', place=" +  TEST_PLACE_AVAILABLE_1.toString() + "}";

    // ---------------------------------------------
    // ... TEST DATA
    // ---------------------------------------------

    // TODO
}
