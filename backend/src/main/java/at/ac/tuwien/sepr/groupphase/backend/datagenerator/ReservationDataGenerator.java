package at.ac.tuwien.sepr.groupphase.backend.datagenerator;

import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepr.groupphase.backend.entity.Place;
import at.ac.tuwien.sepr.groupphase.backend.entity.Reservation;
import at.ac.tuwien.sepr.groupphase.backend.repository.ApplicationUserRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.PlaceRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.ReservationRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Profile("generateData")
@Component
@Order(13)
public class ReservationDataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final int NUMBER_OF_RESERVATIONS_TO_GENERATE = 5;

    private final PlaceDataGenerator placeDataGenerator;
    private final UserDataGenerator userDataGenerator;

    private final ReservationRepository reservationRepository;
    private final PlaceRepository placeRepository;
    private final ApplicationUserRepository applicationUserRepository;

    public ReservationDataGenerator(ReservationRepository reservationRepository, PlaceRepository placeRepository,
                                    ApplicationUserRepository applicationUserRepository, PlaceDataGenerator placeDataGenerator,
                                    UserDataGenerator userDataGenerator) {
        this.reservationRepository = reservationRepository;
        this.placeRepository = placeRepository;
        this.applicationUserRepository = applicationUserRepository;
        this.placeDataGenerator = placeDataGenerator;
        this.userDataGenerator = userDataGenerator;
    }

    @PostConstruct
    private void generateReservations() {
        LOGGER.warn("generate Reservations"); // TODO: remove after testing

        List<Place> places = placeRepository.findAll();
        List<ApplicationUser> applicationUsers = applicationUserRepository.findAll();

        if (reservationRepository.count() > 0) {
            LOGGER.debug("Reservations have already been generated");
        } else if (places.isEmpty() || applicationUsers.isEmpty()) {
            LOGGER.warn("Cannot generate reservations: No places or users found");
        } else {
            LOGGER.debug("Generating {} reservation entries", NUMBER_OF_RESERVATIONS_TO_GENERATE);
            for (int i = 0; i < NUMBER_OF_RESERVATIONS_TO_GENERATE; i++) {
                Place place = places.get(i % places.size());
                ApplicationUser applicationUser = applicationUsers.get(i % applicationUsers.size());

                Reservation reservation = Reservation.ReservationBuilder.aReservation()
                    .withUser(applicationUser)
                    .withStartTime(LocalTime.of(19, 0))
                    .withDate(LocalDate.of(2022, 1, 1))
                    .withEndTime(LocalTime.of(21, 0))
                    .withPax(4L)
                    .withNotes("This is a note for reservation " + i)
                    .withPlace(place)
                    .build();

                LOGGER.debug("Saving reservation {}", reservation);
                reservationRepository.save(reservation);
            }
        }
    }
}
