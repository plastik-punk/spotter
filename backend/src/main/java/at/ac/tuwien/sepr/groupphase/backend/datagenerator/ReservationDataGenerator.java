package at.ac.tuwien.sepr.groupphase.backend.datagenerator;

import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepr.groupphase.backend.entity.Place;
import at.ac.tuwien.sepr.groupphase.backend.entity.Reservation;
import at.ac.tuwien.sepr.groupphase.backend.repository.ApplicationUserRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.PlaceRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.ReservationRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.HashService;
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

@Profile({"generateData", "test"})
@Component
@Order(3)
public class ReservationDataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final int NUMBER_OF_RESERVATIONS_TO_GENERATE = 5;

    private final ReservationRepository reservationRepository;
    private final PlaceRepository placeRepository;
    private final ApplicationUserRepository applicationUserRepository;
    private final HashService hashService;

    public ReservationDataGenerator(ReservationRepository reservationRepository, PlaceRepository placeRepository,
                                    ApplicationUserRepository applicationUserRepository, PlaceDataGenerator placeDataGenerator,
                                    ApplicationUserDataGenerator applicationUserDataGenerator, HashService hashService) {
        this.reservationRepository = reservationRepository;
        this.placeRepository = placeRepository;
        this.applicationUserRepository = applicationUserRepository;
        this.hashService = hashService;
    }

    @PostConstruct
    private void generateReservations() {
        LOGGER.trace("generateReservations");

        List<Place> places = placeRepository.findAll();
        List<ApplicationUser> applicationUsers = applicationUserRepository.findAll();

        if (reservationRepository.count() > 0) {
            LOGGER.debug("Reservations have already been generated");
        } else if (places.isEmpty() || applicationUsers.isEmpty()) {
            LOGGER.warn("Cannot generate reservations: No places or users found");
        } else {
            LOGGER.debug("Generating reservations for the last month");

            LocalDate endDate = LocalDate.now(); // current date
            LocalDate startDate = endDate.minusMonths(1); // one month ago

            for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
                for (int i = 0; i < NUMBER_OF_RESERVATIONS_TO_GENERATE; i++) {
                    Place place = places.get(i % places.size());
                    ApplicationUser applicationUser = applicationUsers.get(i % applicationUsers.size());

                    Reservation reservation = Reservation.ReservationBuilder.aReservation()
                        .withUser(applicationUser)
                        .withStartTime(LocalTime.of(17, 0))
                        .withDate(date)
                        .withEndTime(LocalTime.of(19, 0))
                        .withPax(2L + (i % 2))
                        .withNotes("This is a note for reservation " + i)
                        .withHashValue(hashService.hashSha256("" + i))
                        .build();

                    LOGGER.debug("Saving reservation {}", reservation);
                    reservationRepository.save(reservation);
                }
            }
        }
    }
}
