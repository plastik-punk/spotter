package at.ac.tuwien.sepr.groupphase.backend.unittests.repository;

import at.ac.tuwien.sepr.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepr.groupphase.backend.entity.Place;
import at.ac.tuwien.sepr.groupphase.backend.entity.Reservation;
import at.ac.tuwien.sepr.groupphase.backend.entity.ReservationPlace;
import at.ac.tuwien.sepr.groupphase.backend.repository.ApplicationUserRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.PlaceRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.ReservationPlaceRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.ReservationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
public class ReservationPlaceRepositoryTest implements TestData {

    @Autowired
    private ReservationPlaceRepository reservationPlaceRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private ApplicationUserRepository applicationUserRepository;

    @Test
    @Transactional
    public void givenValidData_whenFindPlaceIdsByReservationIds_thenReturnPlaceIds() {
        // Create and save a few Reservation entities
        ApplicationUser user1 = ApplicationUser.ApplicationUserBuilder.anApplicationUser()
            .withFirstName(TEST_APPLICATION_USER_FIRST_NAME)
            .withLastName(TEST_APPLICATION_USER_LAST_NAME)
            .withEmail(TEST_APPLICATION_USER_EMAIL)
            .withMobileNumber(TEST_APPLICATION_USER_MOBILE_NUMBER)
            .withPassword(TEST_APPLICATION_USER_PASSWORD)
            .withRole(TEST_APPLICATION_USER_ROLE)
            .build();

        ApplicationUser user2 = ApplicationUser.ApplicationUserBuilder.anApplicationUser()
            .withFirstName(TEST_APPLICATION_USER_FIRST_NAME_2)
            .withLastName(TEST_APPLICATION_USER_LAST_NAME_2)
            .withEmail(TEST_APPLICATION_USER_EMAIL_2)
            .withMobileNumber(TEST_APPLICATION_USER_MOBILE_NUMBER_2)
            .withPassword(TEST_APPLICATION_USER_PASSWORD_2)
            .withRole(TEST_APPLICATION_USER_ROLE)
            .build();
        applicationUserRepository.save(user1);
        applicationUserRepository.save(user2);

        Reservation reservation1 = Reservation.ReservationBuilder.aReservation()
            .withId(1L)
            .withUser(user1)
            .withStartTime(TEST_RESERVATION_START_TIME)
            .withEndTime(TEST_RESERVATION_END_TIME)
            .withDate(TEST_RESERVATION_DATE)
            .withPax(TEST_RESERVATION_PAX)
            .withNotes(TEST_RESERVATION_NOTES)
            .withHashValue(TEST_RESERVATION_HASH_VALUE)
            .withConfirmed(false)
            .build();
        reservation1 = reservationRepository.save(reservation1);

        Reservation reservation2 = Reservation.ReservationBuilder.aReservation()
            .withId(2L)
            .withUser(user2)
            .withStartTime(TEST_RESERVATION_START_TIME)
            .withEndTime(TEST_RESERVATION_END_TIME)
            .withDate(TEST_RESERVATION_DATE)
            .withPax(TEST_RESERVATION_PAX)
            .withNotes(TEST_RESERVATION_NOTES)
            .withHashValue(TEST_RESERVATION_HASH_VALUE)
            .withConfirmed(false)
            .build();
        reservation2 = reservationRepository.save(reservation2);

        // Create and save a few Place entities
        Place place1 = TEST_PLACE_AVAILABLE_1;
        place1 = placeRepository.save(place1);

        Place place2 = TEST_PLACE_AVAILABLE_2;
        place2 = placeRepository.save(place2);

        // Create and save ReservationPlace entities that link the Reservation and Place entities
        ReservationPlace reservationPlace1 = ReservationPlace.ReservationPlaceBuilder.aReservationPlace()
            .withReservation(reservation1)
            .withPlace(place1)
            .build();
        reservationPlaceRepository.save(reservationPlace1);

        ReservationPlace reservationPlace2 = ReservationPlace.ReservationPlaceBuilder.aReservationPlace()
            .withReservation(reservation2)
            .withPlace(place2)
            .build();
        reservationPlaceRepository.save(reservationPlace2);

        // Call the findPlaceIdsByReservationIds method with the IDs of the Reservation entities
        List<Long> reservationIds = Arrays.asList(reservation1.getId(), reservation2.getId());
        List<Long> placeIds = reservationPlaceRepository.findPlaceIdsByReservationIds(reservationIds);

        // Assert that the returned list of place IDs matches the IDs of the Place entities linked to the Reservation entities
        assertThat(placeIds).containsExactlyInAnyOrder(place1.getId(), place2.getId());
    }

    @Test
    @Transactional
    public void givenValidData_whenDeleteByReservationId_thenDelete() {
        // Create and save a Reservation entity
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

        // Create and save a Place entity
        Place place = TEST_PLACE_AVAILABLE_1;
        place = placeRepository.save(place);

        // Create and save a ReservationPlace entity that links the Reservation and Place entities
        ReservationPlace reservationPlace = ReservationPlace.ReservationPlaceBuilder.aReservationPlace()
            .withReservation(reservation)
            .withPlace(place)
            .build();
        reservationPlace = reservationPlaceRepository.save(reservationPlace);

        // Call the deleteByReservationId method with the ID of the Reservation entity
        reservationPlaceRepository.deleteByReservationId(reservation.getId());

        // Assert that the ReservationPlace entity has been deleted
        List<ReservationPlace> reservationPlaces = reservationPlaceRepository.findByReservationId(reservation.getId());
        assertTrue(reservationPlaces.isEmpty());
    }

    @Test
    @Transactional
    public void givenValidData_whenFindByReservationId_thenReturnReservationPlaces() {
        // Create and save a Reservation entity
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

        // Create and save a Place entity
        Place place = TEST_PLACE_AVAILABLE_1;
        place = placeRepository.save(place);

        // Create and save a ReservationPlace entity that links the Reservation and Place entities
        ReservationPlace reservationPlace = ReservationPlace.ReservationPlaceBuilder.aReservationPlace()
            .withReservation(reservation)
            .withPlace(place)
            .build();
        reservationPlace = reservationPlaceRepository.save(reservationPlace);

        // Call the findByReservationId method with the ID of the Reservation entity
        List<ReservationPlace> reservationPlaces = reservationPlaceRepository.findByReservationId(reservation.getId());

        // Assert that the returned list of ReservationPlace entities contains the ReservationPlace entity you created
        assertTrue(reservationPlaces.contains(reservationPlace));
    }
}
