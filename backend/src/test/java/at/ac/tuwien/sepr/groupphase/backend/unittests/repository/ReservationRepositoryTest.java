package at.ac.tuwien.sepr.groupphase.backend.unittests.repository;

import at.ac.tuwien.sepr.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepr.groupphase.backend.entity.Reservation;
import at.ac.tuwien.sepr.groupphase.backend.repository.ApplicationUserRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.ReservationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
public class ReservationRepositoryTest implements TestData {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ApplicationUserRepository applicationUserRepository;

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
            .withHashValue(TEST_RESERVATION_HASH_VALUE)
            .build();
        reservationRepository.save(reservation);

        assertAll(
            () -> assertEquals(1, reservationRepository.findAll().size()),
            () -> assertNotNull(reservationRepository.findById(reservation.getId()))
        );
    }

    /*
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
            .withHashValue(TEST_RESERVATION_HASH_VALUE)
            .build();
        reservationRepository.save(reservation);
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
            .withHashValue(TEST_RESERVATION_HASH_VALUE)
            .build();
        reservationRepository.save(reservation);
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
            .withHashValue(TEST_RESERVATION_HASH_VALUE)
            .build();
        reservationRepository.save(reservation);
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
            .withHashValue(TEST_RESERVATION_HASH_VALUE)
            .build();
        reservationRepository.save(reservation);
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
            .withHashValue(TEST_RESERVATION_HASH_VALUE)
            .build();
        reservationRepository.save(reservation);
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
            .withHashValue(TEST_RESERVATION_HASH_VALUE)
            .build();
        reservationRepository.save(reservation);
    }

     */
}