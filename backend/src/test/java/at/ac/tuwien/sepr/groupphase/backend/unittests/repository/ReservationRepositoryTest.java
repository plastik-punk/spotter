package at.ac.tuwien.sepr.groupphase.backend.unittests.repository;

import at.ac.tuwien.sepr.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    public void givenValidData_whenFindReservationsAtSpecifiedTime_thenReturnListOfIds() {
        ApplicationUser user = ApplicationUser.ApplicationUserBuilder.anApplicationUser()
            .withFirstName(TEST_APPLICATION_USER_FIRST_NAME)
            .withLastName(TEST_APPLICATION_USER_LAST_NAME)
            .withEmail(TEST_APPLICATION_USER_EMAIL)
            .withMobileNumber(TEST_APPLICATION_USER_MOBILE_NUMBER)
            .withPassword(TEST_APPLICATION_USER_PASSWORD)
            .withRole(TEST_APPLICATION_USER_ROLE)
            .build();
        user = applicationUserRepository.save(user);

        Reservation reservation = Reservation.ReservationBuilder.aReservation()
            .withId(1L)
            .withUser(user)
            .withStartTime(TEST_RESERVATION_START_TIME)
            .withEndTime(TEST_RESERVATION_END_TIME)
            .withDate(TEST_RESERVATION_DATE)
            .withPax(TEST_RESERVATION_PAX)
            .withNotes(TEST_RESERVATION_NOTES)
            .withHashValue(TEST_RESERVATION_HASH_VALUE)
            .withConfirmed(false)
            .build();
        reservation = reservationRepository.save(reservation);
        List<Long> reservationIds = reservationRepository.findReservationsAtSpecifiedTime(TEST_RESERVATION_DATE, TEST_RESERVATION_START_TIME, TEST_RESERVATION_END_TIME);
        assertTrue(reservationIds.contains(reservation.getId()));
    }

    @Test
    @Transactional
    public void givenValidData_whenFindReservationsByDate_thenReturnListOfReservations() {
        ApplicationUser user = ApplicationUser.ApplicationUserBuilder.anApplicationUser()
            .withFirstName(TEST_APPLICATION_USER_FIRST_NAME)
            .withLastName(TEST_APPLICATION_USER_LAST_NAME)
            .withEmail(TEST_APPLICATION_USER_EMAIL)
            .withMobileNumber(TEST_APPLICATION_USER_MOBILE_NUMBER)
            .withPassword(TEST_APPLICATION_USER_PASSWORD)
            .withRole(TEST_APPLICATION_USER_ROLE)
            .build();
        user = applicationUserRepository.save(user);

        Reservation reservation = Reservation.ReservationBuilder.aReservation()
            .withId(1L)
            .withUser(user)
            .withStartTime(TEST_RESERVATION_START_TIME)
            .withEndTime(TEST_RESERVATION_END_TIME)
            .withDate(TEST_RESERVATION_DATE)
            .withPax(TEST_RESERVATION_PAX)
            .withNotes(TEST_RESERVATION_NOTES)
            .withHashValue(TEST_RESERVATION_HASH_VALUE)
            .withConfirmed(false)
            .build();
        reservation = reservationRepository.save(reservation);
        List<Reservation> reservations = reservationRepository.findReservationsByDate(TEST_APPLICATION_USER_EMAIL, TEST_RESERVATION_DATE, TEST_RESERVATION_DATE, TEST_RESERVATION_START_TIME, TEST_RESERVATION_END_TIME);
        assertTrue(reservations.contains(reservation));
    }

    @Test
    @Transactional
    public void givenValidData_whenFindByHashValue_thenReturnReservation() {
        ApplicationUser user = ApplicationUser.ApplicationUserBuilder.anApplicationUser()
            .withFirstName(TEST_APPLICATION_USER_FIRST_NAME)
            .withLastName(TEST_APPLICATION_USER_LAST_NAME)
            .withEmail(TEST_APPLICATION_USER_EMAIL)
            .withMobileNumber(TEST_APPLICATION_USER_MOBILE_NUMBER)
            .withPassword(TEST_APPLICATION_USER_PASSWORD)
            .withRole(TEST_APPLICATION_USER_ROLE)
            .build();
        user = applicationUserRepository.save(user);

        Reservation reservation = Reservation.ReservationBuilder.aReservation()
            .withId(1L)
            .withUser(user)
            .withStartTime(TEST_RESERVATION_START_TIME)
            .withEndTime(TEST_RESERVATION_END_TIME)
            .withDate(TEST_RESERVATION_DATE)
            .withPax(TEST_RESERVATION_PAX)
            .withNotes(TEST_RESERVATION_NOTES)
            .withHashValue(TEST_RESERVATION_HASH_VALUE)
            .withConfirmed(false)
            .build();
        reservation = reservationRepository.save(reservation);
        List<Reservation> reservations = reservationRepository.findByHashValue(TEST_RESERVATION_HASH_VALUE);
        assertTrue(reservations.contains(reservation));
    }

    @Test
    @Transactional
    public void givenValidData_whenFindAllByDate_thenReturnListOfReservations() {
        ApplicationUser user = ApplicationUser.ApplicationUserBuilder.anApplicationUser()
            .withFirstName(TEST_APPLICATION_USER_FIRST_NAME)
            .withLastName(TEST_APPLICATION_USER_LAST_NAME)
            .withEmail(TEST_APPLICATION_USER_EMAIL)
            .withMobileNumber(TEST_APPLICATION_USER_MOBILE_NUMBER)
            .withPassword(TEST_APPLICATION_USER_PASSWORD)
            .withRole(TEST_APPLICATION_USER_ROLE)
            .build();
        user = applicationUserRepository.save(user);

        Reservation reservation = Reservation.ReservationBuilder.aReservation()
            .withId(1L)
            .withUser(user)
            .withStartTime(TEST_RESERVATION_START_TIME)
            .withEndTime(TEST_RESERVATION_END_TIME)
            .withDate(TEST_RESERVATION_DATE)
            .withPax(TEST_RESERVATION_PAX)
            .withNotes(TEST_RESERVATION_NOTES)
            .withHashValue(TEST_RESERVATION_HASH_VALUE)
            .withConfirmed(false)
            .build();
        reservation = reservationRepository.save(reservation);
        List<Reservation> reservations = reservationRepository.findAllByDate(TEST_RESERVATION_DATE);
        assertTrue(reservations.contains(reservation));
    }

    @Test
    @Transactional
    public void givenValidData_whenFindByApplicationUserId_thenReturnListOfReservations() {
        ApplicationUser user = ApplicationUser.ApplicationUserBuilder.anApplicationUser()
            .withFirstName(TEST_APPLICATION_USER_FIRST_NAME)
            .withLastName(TEST_APPLICATION_USER_LAST_NAME)
            .withEmail(TEST_APPLICATION_USER_EMAIL)
            .withMobileNumber(TEST_APPLICATION_USER_MOBILE_NUMBER)
            .withPassword(TEST_APPLICATION_USER_PASSWORD)
            .withRole(TEST_APPLICATION_USER_ROLE)
            .build();
        user = applicationUserRepository.save(user);

        Reservation reservation = Reservation.ReservationBuilder.aReservation()
            .withId(1L)
            .withUser(user)
            .withStartTime(TEST_RESERVATION_START_TIME)
            .withEndTime(TEST_RESERVATION_END_TIME)
            .withDate(TEST_RESERVATION_DATE)
            .withPax(TEST_RESERVATION_PAX)
            .withNotes(TEST_RESERVATION_NOTES)
            .withHashValue(TEST_RESERVATION_HASH_VALUE)
            .withConfirmed(false)
            .build();
        reservation = reservationRepository.save(reservation);
        List<Reservation> reservations = reservationRepository.findByApplicationUserId(user.getId());
        assertTrue(reservations.contains(reservation));
    }

    @Test
    @Transactional
    public void givenValidData_whenFindReservationsWithoutUserId_thenReturnListOfReservations() {
        ApplicationUser user = ApplicationUser.ApplicationUserBuilder.anApplicationUser()
            .withFirstName(TEST_APPLICATION_USER_FIRST_NAME)
            .withLastName(TEST_APPLICATION_USER_LAST_NAME)
            .withEmail(TEST_APPLICATION_USER_EMAIL)
            .withMobileNumber(TEST_APPLICATION_USER_MOBILE_NUMBER)
            .withPassword(TEST_APPLICATION_USER_PASSWORD)
            .withRole(TEST_APPLICATION_USER_ROLE)
            .build();
        user = applicationUserRepository.save(user);

        Reservation reservation = Reservation.ReservationBuilder.aReservation()
            .withId(1L)
            .withUser(user)
            .withStartTime(TEST_RESERVATION_START_TIME)
            .withEndTime(TEST_RESERVATION_END_TIME)
            .withDate(TEST_RESERVATION_DATE)
            .withPax(TEST_RESERVATION_PAX)
            .withNotes(TEST_RESERVATION_NOTES)
            .withHashValue(TEST_RESERVATION_HASH_VALUE)
            .withConfirmed(false)
            .build();
        reservation = reservationRepository.save(reservation);
        List<Reservation> reservations = reservationRepository.findReservationsWithoutUserId(TEST_RESERVATION_DATE, TEST_RESERVATION_DATE, TEST_RESERVATION_START_TIME, TEST_RESERVATION_END_TIME);
        assertTrue(reservations.contains(reservation));
    }
}