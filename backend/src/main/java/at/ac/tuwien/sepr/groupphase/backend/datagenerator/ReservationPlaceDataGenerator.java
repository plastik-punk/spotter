package at.ac.tuwien.sepr.groupphase.backend.datagenerator;

import at.ac.tuwien.sepr.groupphase.backend.entity.Place;
import at.ac.tuwien.sepr.groupphase.backend.entity.Reservation;
import at.ac.tuwien.sepr.groupphase.backend.entity.ReservationPlace;
import at.ac.tuwien.sepr.groupphase.backend.repository.PlaceRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.ReservationPlaceRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.ReservationRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;

@Profile({"generateData", "test"})
@Order(6)
@Component
public class ReservationPlaceDataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final int NUMBER_OF_RESERVATIONPLACES_TO_GENERATE = 5;

    private final ReservationPlaceRepository reservationPlaceRepository;
    private final ReservationRepository reservationRepository;
    private final PlaceRepository placeRepository;

    public ReservationPlaceDataGenerator(ReservationPlaceRepository reservationPlaceRepository,
                                         ReservationRepository reservationRepository,
                                         PlaceRepository placeRepository,
                                         ReservationDataGenerator reservationDataGenerator,
                                         PlaceDataGenerator placeDataGenerator,
                                         UserDataGenerator userDataGenerator) {
        this.reservationPlaceRepository = reservationPlaceRepository;
        this.reservationRepository = reservationRepository;
        this.placeRepository = placeRepository;
    }

    @PostConstruct
    private void generateReservationPlaces() {
        LOGGER.trace("generateReservationPlaces");

        if (reservationPlaceRepository.count() > 0) {
            LOGGER.debug("ReservationPlaces have already been generated");
        } else {
            LOGGER.debug("Generating {} reservation place entries", NUMBER_OF_RESERVATIONPLACES_TO_GENERATE);
            int placeId = 1;
            long reservationId = 1;

            for (int i = 1; i <= 10; i++) {
                // ReservationPlace.ReservationPlaceId id = new ReservationPlace.ReservationPlaceId();
                // id.setPlaceId((long) placeId);
                // id.setReservationId(reservationId);

                // Fetch or create the Reservation and Place objects
                Reservation reservation = reservationRepository.findById(reservationId).orElse(null);
                Place place = placeRepository.findById((long) placeId).orElse(null);

                // Check if the Reservation and Place objects exist
                if (reservation != null && place != null) {
                    ReservationPlace reservationPlace = ReservationPlace.ReservationPlaceBuilder.aReservationPlace()
                        .withReservation(reservation)
                        .withPlace(place)
                        .build();
                    reservationPlaceRepository.save(reservationPlace);
                } else {
                    // Handle the case where the Reservation or Place objects do not exist
                    LOGGER.warn("Reservation or Place with id {} does not exist", i);
                }

                // Increment placeId and reset to 1 if it exceeds 5
                placeId++;
                if (placeId > 5) {
                    placeId = 1;
                }

                // Increment reservationId every other step
                if (i % 2 == 0) {
                    reservationId++;
                }
            }
        }
    }
}