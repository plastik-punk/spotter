package at.ac.tuwien.sepr.groupphase.backend.unittests.repository;

import at.ac.tuwien.sepr.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepr.groupphase.backend.entity.Place;
import at.ac.tuwien.sepr.groupphase.backend.entity.Reservation;
import at.ac.tuwien.sepr.groupphase.backend.repository.ApplicationUserRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.PlaceRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.ReservationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.lang.invoke.MethodHandles;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
public class ReservationRepositoryTest implements TestData {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ApplicationUserRepository applicationUserRepository;

    @Autowired
    private PlaceRepository placeRepository;

    @Test
    @Transactional
    public void givenLegalData_whenSaveReservation_thenFindListWithOneElementAndFindReservationById() {
        Reservation reservation = Reservation.ReservationBuilder.aReservation()
            .withUser(applicationUserRepository.save(TEST_APPLICATION_USER_CUSTOMER_1))
            .withStartTime(TEST_RESERVATION_START_TIME)
            .withDate(TEST_RESERVATION_DATE)
            .withEndTime(TEST_RESERVATION_END_TIME)
            .withPax(TEST_RESERVATION_PAX)
            .withNotes(TEST_RESERVATION_NOTES)
            .withPlace(placeRepository.save(TEST_PLACE_AVAILABLE_1))
            .build();
        reservationRepository.save(reservation);

        assertAll(
            () -> assertEquals(1, reservationRepository.findAll().size()),
            () -> assertNotNull(reservationRepository.findById(reservation.getId()))
        );
    }

    @Test
    @Transactional
    public void givenLongerReservation_whenFindOccupiedPlacesAtSpecifiedTime_thenFindOccupiedPlaces() {
            Reservation reservation = Reservation.ReservationBuilder.aReservation()
            .withId(1L)
            .withUser(applicationUserRepository.save(TEST_APPLICATION_USER_CUSTOMER_1))
            .withStartTime(TEST_RESERVATION_START_TIME)
            .withDate(TEST_RESERVATION_DATE)
            .withEndTime(TEST_RESERVATION_END_TIME)
            .withPax(TEST_RESERVATION_PAX)
            .withNotes(TEST_RESERVATION_NOTES)
            .withPlace(placeRepository.save(TEST_PLACE_OCCUPIED))
            .build();
        reservationRepository.save(reservation);
        List<Place> occupiedPlaces = reservationRepository.findOccupiedPlacesAtSpecifiedTime(TEST_RESERVATION_DATE, TEST_RESERVATION_START_TIME, TEST_RESERVATION_END_TIME);
        assertFalse(occupiedPlaces.isEmpty(), "The list of occupied places should not be empty");
    }

    @Test
    @Transactional
    public void givenShorterReservation_whenFindOccupiedPlacesAtSpecifiedTime_thenFindOccupiedPlaces() {
        Reservation reservation = Reservation.ReservationBuilder.aReservation()
            .withId(1L)
            .withUser(applicationUserRepository.save(TEST_APPLICATION_USER_CUSTOMER_1))
            .withStartTime(TEST_RESERVATION_START_TIME)
            .withDate(TEST_RESERVATION_DATE)
            .withEndTime(TEST_RESERVATION_END_TIME)
            .withPax(TEST_RESERVATION_PAX)
            .withNotes(TEST_RESERVATION_NOTES)
            .withPlace(placeRepository.save(TEST_PLACE_OCCUPIED))
            .build();
        reservationRepository.save(reservation);
        List<Place> occupiedPlaces = reservationRepository.findOccupiedPlacesAtSpecifiedTime(TEST_RESERVATION_DATE, TEST_RESERVATION_START_TIME, TEST_RESERVATION_END_TIME);
        assertFalse(occupiedPlaces.isEmpty(), "The list of occupied places should not be empty");
    }

    @Test
    @Transactional
    public void givenEndOnStartTime_whenFindOccupiedPlacesAtSpecifiedTime_thenFindNone() {
        Reservation reservation = Reservation.ReservationBuilder.aReservation()
            .withId(1L)
            .withUser(applicationUserRepository.save(TEST_APPLICATION_USER_CUSTOMER_1))
            .withStartTime(TEST_RESERVATION_START_TIME)
            .withDate(TEST_RESERVATION_DATE)
            .withEndTime(TEST_RESERVATION_END_TIME)
            .withPax(TEST_RESERVATION_PAX)
            .withNotes(TEST_RESERVATION_NOTES)
            .withPlace(placeRepository.save(TEST_PLACE_OCCUPIED))
            .build();
        reservationRepository.save(reservation);
        List<Place> occupiedPlaces = reservationRepository.findOccupiedPlacesAtSpecifiedTime(TEST_RESERVATION_DATE, TEST_RESERVATION_START_TIME.minusHours(2), TEST_RESERVATION_START_TIME);
        assertTrue(occupiedPlaces.isEmpty(), "The list of occupied places should be empty");
    }

    @Test
    @Transactional
    public void givenStartOnEndTime_whenFindOccupiedPlacesAtSpecifiedTime_thenFindNone() {
        Reservation reservation = Reservation.ReservationBuilder.aReservation()
            .withId(1L)
            .withUser(applicationUserRepository.save(TEST_APPLICATION_USER_CUSTOMER_1))
            .withStartTime(TEST_RESERVATION_START_TIME)
            .withDate(TEST_RESERVATION_DATE)
            .withEndTime(TEST_RESERVATION_END_TIME)
            .withPax(TEST_RESERVATION_PAX)
            .withNotes(TEST_RESERVATION_NOTES)
            .withPlace(placeRepository.save(TEST_PLACE_OCCUPIED))
            .build();
        reservationRepository.save(reservation);
        List<Place> occupiedPlaces = reservationRepository.findOccupiedPlacesAtSpecifiedTime(TEST_RESERVATION_DATE, TEST_RESERVATION_END_TIME, TEST_RESERVATION_END_TIME.plusHours(2));
        assertTrue(occupiedPlaces.isEmpty(), "The list of occupied places should be empty");
    }

    @Test
    @Transactional
    public void givenStartBeforeAndEndAfterStartTime_whenFindOccupiedPlacesAtSpecifiedTime_thenFindOccupiedPlaces() {
        Reservation reservation = Reservation.ReservationBuilder.aReservation()
            .withId(1L)
            .withUser(applicationUserRepository.save(TEST_APPLICATION_USER_CUSTOMER_1))
            .withStartTime(TEST_RESERVATION_START_TIME)
            .withDate(TEST_RESERVATION_DATE)
            .withEndTime(TEST_RESERVATION_END_TIME)
            .withPax(TEST_RESERVATION_PAX)
            .withNotes(TEST_RESERVATION_NOTES)
            .withPlace(placeRepository.save(TEST_PLACE_OCCUPIED))
            .build();
        reservationRepository.save(reservation);
        List<Place> occupiedPlaces = reservationRepository.findOccupiedPlacesAtSpecifiedTime(TEST_RESERVATION_DATE, TEST_RESERVATION_START_TIME.minusHours(1), TEST_RESERVATION_END_TIME.minusHours(1));
        assertFalse(occupiedPlaces.isEmpty(), "The list of occupied places should not be empty");
    }

    @Test
    @Transactional
    public void givenStartBeforeAndEndAfterEndTime_whenFindOccupiedPlacesAtSpecifiedTime_thenFindOccupiedPlaces() {
        Reservation reservation = Reservation.ReservationBuilder.aReservation()
            .withId(1L)
            .withUser(applicationUserRepository.save(TEST_APPLICATION_USER_CUSTOMER_1))
            .withStartTime(TEST_RESERVATION_START_TIME)
            .withDate(TEST_RESERVATION_DATE)
            .withEndTime(TEST_RESERVATION_END_TIME)
            .withPax(TEST_RESERVATION_PAX)
            .withNotes(TEST_RESERVATION_NOTES)
            .withPlace(placeRepository.save(TEST_PLACE_OCCUPIED))
            .build();
        reservationRepository.save(reservation);
        List<Place> occupiedPlaces = reservationRepository.findOccupiedPlacesAtSpecifiedTime(TEST_RESERVATION_DATE, TEST_RESERVATION_START_TIME.plusHours(1), TEST_RESERVATION_END_TIME.plusHours(1));
        assertFalse(occupiedPlaces.isEmpty(), "The list of occupied places should not be empty");
    }
}