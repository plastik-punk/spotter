package at.ac.tuwien.sepr.groupphase.backend.datagenerator;

import at.ac.tuwien.sepr.groupphase.backend.entity.Place;
import at.ac.tuwien.sepr.groupphase.backend.entity.Reservation;
import at.ac.tuwien.sepr.groupphase.backend.entity.User;
import at.ac.tuwien.sepr.groupphase.backend.repository.IUserRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.PlaceRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.ReservationRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Profile("generateData")
@Component
public class ReservationDataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final int NUMBER_OF_RESERVATIONS_TO_GENERATE = 5;

    private final ReservationRepository reservationRepository;
    private final PlaceRepository placeRepository;
    private final IUserRepository userRepository;

    public ReservationDataGenerator(ReservationRepository reservationRepository, PlaceRepository placeRepository, IUserRepository userRepository) {
        this.reservationRepository = reservationRepository;
        this.placeRepository = placeRepository;
        this.userRepository = userRepository;
    }

    @PostConstruct
    private void generateReservations() {
        List<Place> places = placeRepository.findAll();
        List<User> users = userRepository.findAll();

        if (reservationRepository.count() > 0) {
            LOGGER.debug("Reservations have already been generated");
        } else if (places.isEmpty() || users.isEmpty()) {
            LOGGER.warn("Cannot generate reservations: No places or users found");
        } else {
            LOGGER.debug("Generating {} reservation entries", NUMBER_OF_RESERVATIONS_TO_GENERATE);
            for (int i = 0; i < NUMBER_OF_RESERVATIONS_TO_GENERATE; i++) {
                Place place = places.get(i % places.size());
                User user = users.get(i % users.size());

                Reservation reservation = Reservation.ReservationBuilder.aReservation()
                    .withUser(user)
                    .withDate(LocalDate.now().plusDays(i))
                    .withStartTime(LocalTime.of(19, 0))
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
