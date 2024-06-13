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
import java.util.Random;

@Profile({"generateData", "test"})
@Component
@Order(3)
public class ReservationDataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final int NUMBER_OF_RESERVATIONS_TO_GENERATE = 20;

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
            LOGGER.debug("Generating {} reservation entries", NUMBER_OF_RESERVATIONS_TO_GENERATE);
            Random random = new Random();

            // Generate 5 reservations for today
            LocalDate today = LocalDate.now();
            for (int i = 0; i < 5; i++) {
                createReservation(today, places.get(i % places.size()), applicationUsers.get(i % applicationUsers.size()), i);
            }

            // Generate 10 reservations for the next 7 days
            for (int i = 5; i < 15; i++) {
                LocalDate date = today.plusDays(random.nextInt(7) + 1);
                createReservation(date, places.get(i % places.size()), applicationUsers.get(i % applicationUsers.size()), i);
            }

            // Generate 5 reservations for the next week
            for (int i = 15; i < 20; i++) {
                LocalDate date = today.plusWeeks(1).plusDays(random.nextInt(7) + 1);
                createReservation(date, places.get(i % places.size()), applicationUsers.get(i % applicationUsers.size()), i);
            }
            //10 Reservations in the past
//            for (int i = 0; i < 10; i++) {
//                LocalDate date = today.minusWeeks(3).minusDays(random.nextInt(7) + 1);
//                createReservation(date, places.get(i % places.size()), applicationUsers.get(i % applicationUsers.size()), i);
//            }
            //100 filler reservations in the future to test search limiting
            for (int i = 0; i < 100; i++) {
                LocalDate date = today.plusMonths(1 + random.nextInt(11)).plusDays(random.nextInt(7) + 1);
                createReservation(date, places.get(i % places.size()), applicationUsers.get(i % applicationUsers.size()), i);
            }
        }

    }

    private void createReservation(LocalDate date, Place place, ApplicationUser applicationUser, int index) {
        Random random = new Random();
        LocalTime startTime = LocalTime.of(12 + random.nextInt(8), random.nextInt(59));
        LocalTime endTime = LocalTime.of(20 + random.nextInt(3), random.nextInt(59));
        Long pax = 2L + (index % 2);

        String hashedValue = hashService.hashSha256(date.toString()
            + startTime.toString() + endTime.toString()
            + pax);

        Reservation reservation = Reservation.ReservationBuilder.aReservation()
            .withUser(applicationUser)
            .withStartTime(startTime)
            .withDate(date)
            .withEndTime(endTime)
            .withPax(pax)
            .withNotes("This is a note for reservation " + index)
            .withHashValue(hashedValue)
            .withConfirmed(false)
            .build();

        LOGGER.debug("Saving reservation {}", reservation);
        reservationRepository.save(reservation);
    }
}
