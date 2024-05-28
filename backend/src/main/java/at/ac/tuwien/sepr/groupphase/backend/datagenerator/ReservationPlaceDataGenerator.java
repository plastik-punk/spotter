package at.ac.tuwien.sepr.groupphase.backend.datagenerator;

import at.ac.tuwien.sepr.groupphase.backend.entity.Place;
import at.ac.tuwien.sepr.groupphase.backend.entity.Reservation;
import at.ac.tuwien.sepr.groupphase.backend.entity.ReservationPlace;
import at.ac.tuwien.sepr.groupphase.backend.enums.StatusEnum;
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
import java.util.Optional;

@Profile({"generateData", "test"})
@Order(6)
@Component
public class ReservationPlaceDataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final ReservationPlaceRepository reservationPlaceRepository;
    private final ReservationRepository reservationRepository;
    private final PlaceRepository placeRepository;


    public ReservationPlaceDataGenerator(ReservationPlaceRepository reservationPlaceRepository,
                                         ReservationRepository reservationRepository,
                                         PlaceRepository placeRepository,
                                         ReservationDataGenerator reservationDataGenerator,
                                         PlaceDataGenerator placeDataGenerator,
                                         ApplicationUserDataGenerator applicationUserDataGenerator) {
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
            LOGGER.debug("Generating reservation place entries");

            for (int i = 0; i < reservationRepository.findAll().size(); i++) {
                Optional<Reservation> reservation = reservationRepository.findById((long) i);
                Optional<Place> place;
                if (reservation.isPresent()) {
                    Reservation newReservation = reservation.get();
                    place = placeRepository.findFreePlaceForReservation(newReservation.getDate(),
                        newReservation.getStartTime(),
                        newReservation.getEndTime(),

                        newReservation.getPax(),
                        StatusEnum.AVAILABLE);
                    if (place.isPresent()) {
                        Place p = place.get();
                        ReservationPlace reservationPlace = ReservationPlace.ReservationPlaceBuilder.aReservationPlace()
                            .withReservation(newReservation)
                            .withPlace(p)
                            .build();
                        reservationPlaceRepository.save(reservationPlace);
                    } else {
                        LOGGER.warn("No fitting Place could be found for Reservation with ID {}", i);
                    }
                } else {
                    LOGGER.warn("Reservation with id {} does not exist", i);
                }
            }
        }
    }
}