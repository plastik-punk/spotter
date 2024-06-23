package at.ac.tuwien.sepr.groupphase.backend.basetest;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ApplicationUserRegistrationDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.EventListDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationCheckAvailabilityDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationEditDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.SpecialOfferAmountDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.SpecialOfferCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.SpecialOfferListDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepr.groupphase.backend.entity.Area;
import at.ac.tuwien.sepr.groupphase.backend.entity.AreaPlaceSegment;
import at.ac.tuwien.sepr.groupphase.backend.entity.ClosedDay;
import at.ac.tuwien.sepr.groupphase.backend.entity.Event;
import at.ac.tuwien.sepr.groupphase.backend.entity.Message;
import at.ac.tuwien.sepr.groupphase.backend.entity.OpeningHours;
import at.ac.tuwien.sepr.groupphase.backend.entity.Place;
import at.ac.tuwien.sepr.groupphase.backend.entity.Reservation;
import at.ac.tuwien.sepr.groupphase.backend.entity.ReservationOffer;
import at.ac.tuwien.sepr.groupphase.backend.entity.ReservationPlace;
import at.ac.tuwien.sepr.groupphase.backend.entity.Restaurant;
import at.ac.tuwien.sepr.groupphase.backend.entity.Segment;
import at.ac.tuwien.sepr.groupphase.backend.entity.SpecialOffer;
import at.ac.tuwien.sepr.groupphase.backend.enums.RoleEnum;
import at.ac.tuwien.sepr.groupphase.backend.enums.StatusEnum;
import at.ac.tuwien.sepr.groupphase.backend.repository.ApplicationUserRepository;

import java.time.DayOfWeek;
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
    String EVENT_BASE_URI = BASE_URI + "/events";
    String EMPLOYEES_BASE_URI = BASE_URI + "/employees";
    Long TEST_VALID_ID = 1L;
    Long TEST_INVALID_ID = -1L;

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
    LocalDateTime TEST_NEWS_PUBLISHED_AT_2 =
        LocalDateTime.of(2020, 1, 1, 1, 0, 0, 0);


    Message TEST_MESSAGE_1 = Message.MessageBuilder.aMessage()
        .withId(TEST_MESSAGE_ID)
        .withTitle(TEST_NEWS_TITLE)
        .withSummary(TEST_NEWS_SUMMARY)
        .withText(TEST_NEWS_TEXT)
        .withPublishedAt(TEST_NEWS_PUBLISHED_AT)
        .build();

    Message TEST_MESSAGE_2 = Message.MessageBuilder.aMessage()
        .withId(TEST_MESSAGE_ID)
        .withTitle(TEST_NEWS_TITLE)
        .withSummary(TEST_NEWS_SUMMARY)
        .withText(TEST_NEWS_TEXT)
        .withPublishedAt(TEST_NEWS_PUBLISHED_AT)
        .build();

    Message TEST_MESSAGE_3 = Message.MessageBuilder.aMessage()
        .withId(2L)
        .withTitle(TEST_NEWS_TITLE)
        .withSummary(TEST_NEWS_SUMMARY)
        .withText(TEST_NEWS_TEXT)
        .withPublishedAt(TEST_NEWS_PUBLISHED_AT_2)
        .build();

    // ---------------------------------------------
    // PLACE TEST DATA
    // ---------------------------------------------

    Place TEST_PLACE_AVAILABLE_1 = Place.PlaceBuilder.aPlace()
        .withId(1L)
        .withPax(4L)
        .withNumber(1)
        .withStatus(StatusEnum.AVAILABLE)
        .build();

    Place TEST_PLACE_AVAILABLE_2 = Place.PlaceBuilder.aPlace()
        .withId(2L)
        .withPax(4L)
        .withNumber(2)
        .withStatus(StatusEnum.AVAILABLE)
        .build();

    Place TEST_PLACE_RESERVED = Place.PlaceBuilder.aPlace()
        .withId(3L)
        .withPax(6L)
        .withNumber(3)
        .withStatus(StatusEnum.RESERVED)
        .build();

    Place TEST_PLACE_OCCUPIED = Place.PlaceBuilder.aPlace()
        .withId(4L)
        .withPax(6L)
        .withNumber(4)
        .withStatus(StatusEnum.OCCUPIED)
        .build();

    // ---------------------------------------------
    // APPLICATION USER TEST DATA
    // ---------------------------------------------

    String TEST_APPLICATION_USER_FIRST_NAME = "Otter";
    String TEST_APPLICATION_USER_LAST_NAME = "McOtterface";
    String TEST_APPLICATION_USER_EMAIL = "otter@example.at";
    String TEST_APPLICATION_USER_MOBILE_NUMBER = "06501234567";
    String TEST_APPLICATION_USER_PASSWORD = "naughtyotter";
    RoleEnum TEST_APPLICATION_USER_ROLE = RoleEnum.CUSTOMER;

    String TEST_APPLICATION_USER_FIRST_NAME_2 = "Biber";
    String TEST_APPLICATION_USER_LAST_NAME_2 = "McBiberface";
    String TEST_APPLICATION_USER_EMAIL_2 = "biber@example.at";
    String TEST_APPLICATION_USER_MOBILE_NUMBER_2 = "06501234568";
    String TEST_APPLICATION_USER_PASSWORD_2 = "naughtybiber";

    ApplicationUserRegistrationDto TEST_REGISTRATION_CUSTOMER = ApplicationUserRegistrationDto.ApplicationUserRegistrationDtoBuilder.anApplicationUserRegistrationDto()
        .withFirstName(TEST_APPLICATION_USER_FIRST_NAME)
        .withLastName(TEST_APPLICATION_USER_LAST_NAME)
        .withEmail(TEST_APPLICATION_USER_EMAIL)
        .withMobileNumber(TEST_APPLICATION_USER_MOBILE_NUMBER)
        .withPassword(TEST_APPLICATION_USER_PASSWORD)
        .withRole(TEST_APPLICATION_USER_ROLE)
        .build();

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

    ApplicationUser TEST_APPLICATION_USER_CUSTOMER_3 = ApplicationUser.ApplicationUserBuilder.anApplicationUser()
        .withId(2L)
        .withFirstName(TEST_APPLICATION_USER_FIRST_NAME_2)
        .withLastName(TEST_APPLICATION_USER_LAST_NAME_2)
        .withEmail(TEST_APPLICATION_USER_EMAIL_2)
        .withMobileNumber(TEST_APPLICATION_USER_MOBILE_NUMBER_2)
        .withPassword(TEST_APPLICATION_USER_PASSWORD_2)
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

    ApplicationUser TEST_APPLICATION_USER_GUEST_2 = ApplicationUser.ApplicationUserBuilder.anApplicationUser()
        .withId(22L)
        .withFirstName(TEST_APPLICATION_USER_FIRST_NAME)
        .withLastName(TEST_APPLICATION_USER_LAST_NAME)
        .withEmail(TEST_APPLICATION_USER_EMAIL)
        .withMobileNumber(TEST_APPLICATION_USER_MOBILE_NUMBER)
        .withoutPassword()
        .withRole(RoleEnum.GUEST)
        .build();

    ApplicationUser TEST_APPLICATION_USER_ADMIN = ApplicationUser.ApplicationUserBuilder.anApplicationUser()
        .withId(1L)
        .withFirstName(TEST_APPLICATION_USER_FIRST_NAME)
        .withLastName(TEST_APPLICATION_USER_LAST_NAME)
        .withEmail(TEST_APPLICATION_USER_EMAIL)
        .withMobileNumber(TEST_APPLICATION_USER_MOBILE_NUMBER)
        .withoutPassword()
        .withRole(RoleEnum.ADMIN)
        .build();

    // ---------------------------------------------
    // EVENT TEST DATA
    // ---------------------------------------------

    Long TEST_EVENT_ID_1 = 1L;
    String TEST_EVENT_HASH_1 = "eventhash1";
    String TEST_EVENT_HASH_2 = "eventhash2";
    LocalDateTime TEST_EVENT_START_TIME_1 = LocalDateTime.of(2024, 7, 1, 18, 0);
    LocalDateTime TEST_EVENT_START_TIME_2 = LocalDateTime.of(2024, 8, 2, 17, 0);
    LocalDateTime TEST_EVENT_START_TIME_3 = LocalDateTime.of(2027, 7, 1, 18, 0);
    LocalDateTime TEST_EVENT_END_TIME_1 = LocalDateTime.of(2024, 7, 1, 22, 0);
    LocalDateTime TEST_EVENT_END_TIME_2 = LocalDateTime.of(2024, 8, 2, 21, 0);
    LocalDateTime TEST_EVENT_END_TIME_3 = LocalDateTime.of(2027, 7, 1, 22, 0);
    String TEST_EVENT_NAME_1 = "Open Mic Night";
    String TEST_EVENT_NAME_2 = "Fussball EM";
    String TEST_EVENT_DESC_1 = "Let's laugh about some dad jokes.";
    String TEST_EVENT_DESC_2 = "Free shots!";

    Event TEST_EVENT_1 = Event.EventBuilder.anEvent()
        .withId(TEST_EVENT_ID_1)
        .withStartTime(TEST_EVENT_START_TIME_1)
        .withEndTime(TEST_EVENT_END_TIME_1)
        .withName(TEST_EVENT_NAME_1)
        .withDescription(TEST_EVENT_DESC_1)
        .withHashId(TEST_EVENT_HASH_1)
        .build();

    Event TEST_EVENT_2 = Event.EventBuilder.anEvent()
        .withStartTime(TEST_EVENT_START_TIME_2)
        .withEndTime(TEST_EVENT_END_TIME_2)
        .withName(TEST_EVENT_NAME_2)
        .withDescription(TEST_EVENT_DESC_2)
        .withHashId(TEST_EVENT_HASH_2)
        .build();

    Event TEST_EVENT_3 = Event.EventBuilder.anEvent()
        .withId(TEST_EVENT_ID_1)
        .withStartTime(TEST_EVENT_START_TIME_1)
        .withEndTime(TEST_EVENT_END_TIME_1)
        .withName(TEST_EVENT_NAME_1)
        .withDescription(TEST_EVENT_DESC_1)
        .withHashId(TEST_EVENT_HASH_1)
        .build();

    Event TEST_EVENT_4 = Event.EventBuilder.anEvent()
        .withStartTime(TEST_EVENT_START_TIME_3)
        .withEndTime(TEST_EVENT_END_TIME_3)
        .withName(TEST_EVENT_NAME_1)
        .withDescription(TEST_EVENT_DESC_1)
        .withHashId(TEST_EVENT_HASH_1)
        .build();

    EventListDto TEST_EVENT_LIST_1 = EventListDto.EventListDtoBuilder.anEventListDto()
        .withStartTime(TEST_EVENT_START_TIME_1)
        .withEndTime(TEST_EVENT_END_TIME_1)
        .withName(TEST_EVENT_NAME_1)
        .withHashId(TEST_EVENT_HASH_1)
        .build();

    EventListDto TEST_EVENT_LIST_2 = EventListDto.EventListDtoBuilder.anEventListDto()
        .withStartTime(TEST_EVENT_START_TIME_2)
        .withEndTime(TEST_EVENT_END_TIME_2)
        .withName(TEST_EVENT_NAME_2)
        .withHashId(TEST_EVENT_HASH_2)
        .build();

    // ---------------------------------------------
    // RESERVATION TEST DATA
    // ---------------------------------------------

    LocalTime TEST_RESERVATION_START_TIME = LocalDateTime.of(2025, 1, 2, 12, 0, 0, 0).toLocalTime();
    LocalTime TEST_RESERVATION_END_TIME = LocalDateTime.of(2025, 1, 2, 14, 0, 0, 0).toLocalTime();
    LocalDate TEST_RESERVATION_DATE = LocalDate.of(2025, 1, 2);
    Long TEST_RESERVATION_PAX = 4L;
    String TEST_RESERVATION_NOTES = "Test Notes";
    String TEST_RESERVATION_NOTES_EDIT = "Test Notes Edit";
    Long TEST_RESERVATION_DETAIL_ID = 1L;
    String TEST_RESERVATION_HASH_VALUE = "TestHashValue";
    String TEST_RESERVATION_HASH_VALUE_1 = "44A17E9E592AA9951A3A0853524BE799A333DFD8522182D79D990A24EB94A7FE";
    String TEST_RESERVATION_HASH_VALUE_2 = "44A17E9E592AA9951A3A0853524BE799A333DFD8522182D79D990A24EB94A7FEfalse";
    LocalDate TEST_RESERVATION_DATE_INVALID = LocalDate.of(2022, 1, 2);


    List<Long> TEST_PLACE_IDS = new ArrayList<>() {
        {
            add(TEST_PLACE_AVAILABLE_1.getId());
            add(TEST_PLACE_AVAILABLE_2.getId());
        }
    };

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

    ReservationEditDto TEST_RESERVATION_EDIT_DTO = ReservationEditDto.ReservationEditDtoBuilder.aReservationEditDto()
        .withReservationId(TEST_RESERVATION_DETAIL_ID)
        .withUser(TEST_APPLICATION_USER_CUSTOMER_1)
        .withStartTime(TEST_RESERVATION_START_TIME)
        .withEndTime(TEST_RESERVATION_END_TIME)
        .withDate(TEST_RESERVATION_DATE)
        .withPax(TEST_RESERVATION_PAX)
        .withNotes(TEST_RESERVATION_NOTES)
        .withPlaceIds(TEST_PLACE_IDS)
        .build();

    ReservationDetailDto TEST_RESERVATION_DETAIL_DTO = ReservationDetailDto.ReservationDetailDtoBuilder.aReservationDetailDto()
        .withId(TEST_RESERVATION_DETAIL_ID)
        .withStartTime(TEST_RESERVATION_START_TIME)
        .withEndTime(TEST_RESERVATION_END_TIME)
        .withDate(TEST_RESERVATION_DATE)
        .withPax(TEST_RESERVATION_PAX)
        .withNotes(TEST_RESERVATION_NOTES)
        .withPlaceIds(TEST_PLACE_IDS)
        .build();

    Reservation TEST_RESERVATION_1 = Reservation.ReservationBuilder.aReservation()
        .withId(1L)
        .withUser(TEST_APPLICATION_USER_CUSTOMER_1)
        .withStartTime(TEST_RESERVATION_START_TIME)
        .withEndTime(TEST_RESERVATION_END_TIME)
        .withDate(TEST_RESERVATION_DATE)
        .withPax(TEST_RESERVATION_PAX)
        .withNotes(TEST_RESERVATION_NOTES)
        .withHashValue(TEST_RESERVATION_HASH_VALUE_1)
        .withConfirmed(false)
        .build();

    Reservation TEST_RESERVATION_2 = Reservation.ReservationBuilder.aReservation()
        .withId(1L)
        .withUser(TEST_APPLICATION_USER_CUSTOMER_1)
        .withStartTime(TEST_RESERVATION_START_TIME)
        .withEndTime(TEST_RESERVATION_END_TIME)
        .withDate(TEST_RESERVATION_DATE)
        .withPax(TEST_RESERVATION_PAX)
        .withNotes(TEST_RESERVATION_NOTES)
        .withHashValue(TEST_RESERVATION_HASH_VALUE_1)
        .withConfirmed(false)
        .build();

    Reservation TEST_RESERVATION_3 = Reservation.ReservationBuilder.aReservation()
        .withId(2L)
        .withUser(TEST_APPLICATION_USER_CUSTOMER_3)
        .withStartTime(TEST_RESERVATION_START_TIME)
        .withEndTime(TEST_RESERVATION_END_TIME)
        .withDate(TEST_RESERVATION_DATE)
        .withPax(TEST_RESERVATION_PAX)
        .withNotes(TEST_RESERVATION_NOTES)
        .withConfirmed(false)
        .build();

    Reservation TEST_RESERVATION_TO_DELETE = Reservation.ReservationBuilder.aReservation()
        .withUser(TEST_APPLICATION_USER_CUSTOMER_1)
        .withStartTime(TEST_RESERVATION_START_TIME)
        .withEndTime(TEST_RESERVATION_END_TIME)
        .withDate(TEST_RESERVATION_DATE)
        .withPax(TEST_RESERVATION_PAX)
        .withNotes(TEST_RESERVATION_NOTES)
        .withHashValue(TEST_RESERVATION_HASH_VALUE)
        .build();

    String TEST_RESERVATION_EXPECTED_STRING = "Reservation{id=1"
        + ", applicationUser=" + TEST_APPLICATION_USER_CUSTOMER_1.toString()
        + ", startTime=" + TEST_RESERVATION_START_TIME
        + ", date=" + TEST_RESERVATION_DATE
        + ", endTime=" + TEST_RESERVATION_END_TIME
        + ", pax=" + TEST_RESERVATION_PAX
        + ", notes='" + TEST_RESERVATION_NOTES + '\''
        + ", confirmed='false'}";

    LocalTime TEST_RESERVATION_AVAILABILITY_START_TIME = LocalDateTime.of(2024, 7, 1, 18, 0, 0, 0).toLocalTime();
    LocalDate TEST_RESERVATION_AVAILABILITY_DATE = LocalDate.of(2024, 7, 1);

    ReservationCheckAvailabilityDto TEST_RESERVATION_AVAILABILITY = ReservationCheckAvailabilityDto.ReservationCheckAvailabilityDtoBuilder.aReservationCheckAvailabilityDto()
        .withStartTime(TEST_RESERVATION_AVAILABILITY_START_TIME)
        .withEndTime(TEST_RESERVATION_AVAILABILITY_START_TIME.plusHours(2))
        .withDate(TEST_RESERVATION_AVAILABILITY_DATE)
        .withPax(4L)
        .withIdToExclude(-1L)
        .build();

    // ---------------------------------------------
    // RESERVATIONPLACE TEST DATA
    // ---------------------------------------------

    ReservationPlace TEST_RESERVATIONPLACE = ReservationPlace.ReservationPlaceBuilder.aReservationPlace()
        .withReservation(TEST_RESERVATION_1)
        .withPlace(TEST_PLACE_AVAILABLE_1)
        .build();

    // ---------------------------------------------
    // RESTAURANT TEST DATA
    // ---------------------------------------------

    String TEST_RESTAURANT_NAME = "The Wet Otter";
    String TEST_RESTAURANT_ADDRESS = "Karlsplatz 13, 1040 Vienna";

    Restaurant TEST_RESTAURANT_1 = Restaurant.RestaurantBuilder.aRestaurant()
        .withId(1L)
        .withName(TEST_RESTAURANT_NAME)
        .withAddress(TEST_RESTAURANT_ADDRESS)
        .build();
    Restaurant TEST_RESTAURANT_2 = Restaurant.RestaurantBuilder.aRestaurant()
        .withId(1L)
        .withName(TEST_RESTAURANT_NAME)
        .withAddress(TEST_RESTAURANT_ADDRESS)
        .build();

    String TEST_RESTAURANT_EXPECTED_STRING = "Restaurant{id=1"
        + ", name='" + TEST_RESTAURANT_NAME
        + "', address='" + TEST_RESTAURANT_ADDRESS + "'}";

    // ---------------------------------------------
    // CLOSED DAY TEST DATA
    // ---------------------------------------------

    LocalDate TEST_CLOSED_DAY_DATE = LocalDate.of(2024, 6, 12);

    ClosedDay TEST_CLOSED_DAY_1 = ClosedDay.ClosedDayBuilder.aClosedDay()
        .withId(1L)
        .withRestaurant(TEST_RESTAURANT_1)
        .withDate(TEST_CLOSED_DAY_DATE)
        .build();
    ClosedDay TEST_CLOSED_DAY_2 = ClosedDay.ClosedDayBuilder.aClosedDay()
        .withId(1L)
        .withRestaurant(TEST_RESTAURANT_1)
        .withDate(TEST_CLOSED_DAY_DATE)
        .build();

    String TEST_CLOSED_DAY_EXPECTED_STRING = "ClosedDay{id=1"
        + ", restaurant=" + TEST_RESTAURANT_1.toString()
        + ", date=" + TEST_CLOSED_DAY_DATE + "}";

    // ---------------------------------------------
    // OPENING HOURS TEST DATA
    // ---------------------------------------------

    DayOfWeek TEST_OPENING_HOURS_DAY_OF_WEEK = DayOfWeek.MONDAY;
    DayOfWeek TEST_OPENING_HOURS_DAY_OF_WEEK_2 = DayOfWeek.TUESDAY;
    LocalTime TEST_OPENING_HOURS_OPENING_TIME = LocalTime.of(11, 30);
    LocalTime TEST_OPENING_HOURS_CLOSING_TIME = LocalTime.of(15, 0);

    OpeningHours TEST_OPENING_HOURS_1 = OpeningHours.OpeningHourBuilder.anOpeningHour()
        .withId(1L)
        .withDayOfWeek(TEST_OPENING_HOURS_DAY_OF_WEEK)
        .withRestaurant(TEST_RESTAURANT_1)
        .withOpeningTime(TEST_OPENING_HOURS_OPENING_TIME)
        .withClosingTime(TEST_OPENING_HOURS_CLOSING_TIME)
        .build();

    OpeningHours TEST_OPENING_HOURS_2 = OpeningHours.OpeningHourBuilder.anOpeningHour()
        .withId(1L)
        .withDayOfWeek(TEST_OPENING_HOURS_DAY_OF_WEEK)
        .withRestaurant(TEST_RESTAURANT_1)
        .withOpeningTime(TEST_OPENING_HOURS_OPENING_TIME)
        .withClosingTime(TEST_OPENING_HOURS_CLOSING_TIME)
        .build();

    String TEST_OPENING_HOURS_EXPECTED_STRING = "OpeningHour{id=1"
        + ", restaurant=" + TEST_RESTAURANT_1.toString()
        + ", dayOfWeek=" + TEST_OPENING_HOURS_DAY_OF_WEEK
        + ", openingTime=" + TEST_OPENING_HOURS_OPENING_TIME
        + ", closingTime=" + TEST_OPENING_HOURS_CLOSING_TIME + "}";
// ---------------------------------------------
// AREA TEST DATA
// ---------------------------------------------

    Area TEST_AREA_1 = Area.AreaBuilder.anArea()
        .withId(1L)
        .withName("Main Area")
        .withWidth(15)
        .withHeight(8)
        .withOpen(true)
        .build();

    Area TEST_AREA_2 = Area.AreaBuilder.anArea()
        .withId(2L)
        .withName("Second Area")
        .withWidth(19)
        .withHeight(4)
        .withOpen(false)
        .build();

    // ---------------------------------------------
    // SEGMENT TEST DATA
    // ---------------------------------------------

    Segment TEST_SEGMENT_1 = Segment.SegmentBuilder.aSegment()
        .withX(0)
        .withY(0)
        .build();

    // ---------------------------------------------
    // AREAPLACESEGMENT TEST DATA
    // ---------------------------------------------

    AreaPlaceSegment TEST_LOCATION_1 = AreaPlaceSegment.AreaPlaceSegmentBuilder.anAreaPlaceSegment()
        .withArea(TEST_AREA_1)
        .withPlace(TEST_PLACE_AVAILABLE_1)
        .withSegment(TEST_SEGMENT_1)
        .build();

    // ---------------------------------------------
    // SPECIAL OFFER TEST DATA
    // ---------------------------------------------

    SpecialOffer TEST_SPECIAL_OFFER_1 = SpecialOffer.SpecialOfferBuilder.aSpecialOffer()
        .withId(1L)
        .withName("Test Special Offer 1")
        .withPricePerPax(10.0f)
        .build();

    SpecialOffer TEST_SPECIAL_OFFER_2 = SpecialOffer.SpecialOfferBuilder.aSpecialOffer()
        .withId(2L)
        .withName("Test Special Offer 2")
        .withPricePerPax(15.0f)
        .build();

    List<Long> TEST_SPECIAL_OFFER_IDS = new ArrayList<>() {
        {
            add(1L);
            add(2L);
        }
    };

    SpecialOfferListDto TEST_OFFER_LIST_DTO_1 = SpecialOfferListDto.SpecialOfferListBuilder.aSpecialOfferListDto()
        .withId(1L)
        .withName("Test Special Offer 1")
        .withPricePerPax(10.0f)
        .build();

    SpecialOfferListDto TEST_OFFER_LIST_DTO_2 = SpecialOfferListDto.SpecialOfferListBuilder.aSpecialOfferListDto()
        .withId(2L)
        .withName("Test Special Offer 2")
        .withPricePerPax(15.0f)
        .build();

    SpecialOfferAmountDto TEST_OFFER_AMOUNT_DTO_1 = SpecialOfferAmountDto.SpecialOfferAmountDtoBuilder.aSpecialOfferAmountDto()
        .withAmount(1)
        .withSpecialOffer(TEST_OFFER_LIST_DTO_1)
        .build();

    SpecialOfferAmountDto TEST_OFFER_AMOUNT_DTO_2 = SpecialOfferAmountDto.SpecialOfferAmountDtoBuilder.aSpecialOfferAmountDto()
        .withAmount(2)
        .withSpecialOffer(TEST_OFFER_LIST_DTO_2)
        .build();

    List<SpecialOfferAmountDto> TEST_RESERVATION_SPECIAL_OFFERS = new ArrayList<>() {
        {
            add(TEST_OFFER_AMOUNT_DTO_1);
            add(TEST_OFFER_AMOUNT_DTO_2);
        }
    };

    SpecialOfferCreateDto TEST_SPECIAL_OFFER_CREATE_DTO_1 = SpecialOfferCreateDto.SpecialOfferCreateBuilder.aSpecialOfferCreateDto()
        .withName("Test Special Offer 1")
        .withPricePerPax(10.0f)
        .build();

    SpecialOfferCreateDto TEST_SPECIAL_OFFER_CREATE_DTO_2 = SpecialOfferCreateDto.SpecialOfferCreateBuilder.aSpecialOfferCreateDto()
        .withName("Test Special Offer 2")
        .withPricePerPax(15.0f)
        .build();

    // ---------------------------------------------
    // ... TEST DATA
    // ---------------------------------------------

    // TODO
}
