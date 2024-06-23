package at.ac.tuwien.sepr.groupphase.backend.datagenerator;

import at.ac.tuwien.sepr.groupphase.backend.entity.Reservation;
import at.ac.tuwien.sepr.groupphase.backend.entity.ReservationOffer;
import at.ac.tuwien.sepr.groupphase.backend.entity.SpecialOffer;
import at.ac.tuwien.sepr.groupphase.backend.repository.ReservationOfferRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.ReservationPlaceRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.ReservationRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.SpecialOfferRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.Optional;

@Profile({"generateData", "test"})
@Component
@Order(14)
public class ReservationOfferDataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final ReservationOfferRepository reservationOfferRepository;
    private final ReservationRepository reservationRepository;
    private final SpecialOfferRepository specialOfferRepository;

    private final int[] reservationIds = {
        -29020,
        -27204,
        -24352,
        -23480,
        -21763,
        -20495,
        -19463,
        -14558,
        -11888,
        -11731,
        -8360,
        -8218,
        -4898,
        -4370,
        -27736,
        -26882,
        -25714,
        -25542,
        -24747,
        -23904,
        -18651,
        -16222,
        -15816,
        -15694,
        -9777,
        -9529,
        -8748,
        -8424,
        -7575,
        -671,
        -26940,
        -26223,
        -25884,
        -25596,
        -22009,
        -20426,
        -16382,
        -14222,
        -13050,
        -12405,
        -12279,
        -11341,
        -8873,
        -4100,
        -1755
    };

    private final int[] specialOfferIds = {1, 2, 3, 4, 5};


    public ReservationOfferDataGenerator(
        ReservationOfferRepository reservationOfferRepository,
        ReservationRepository reservationRepository,
        SpecialOfferRepository specialOfferRepository,
        ReservationDataGenerator reservationDataGenerator,
        SpecialOfferDataGenerator specialOfferDataGenerator,
        ReservationPlaceRepository reservationPlaceRepository) {
        this.reservationOfferRepository = reservationOfferRepository;
        this.reservationRepository = reservationRepository;
        this.specialOfferRepository = specialOfferRepository;
    }

    @PostConstruct
    private void generateReservationOffers() {
        LOGGER.trace("generateReservationOffers");

        if (reservationOfferRepository.count() > 0) {
            LOGGER.debug("ReservationOffers have already been generated");
        } else {
            LOGGER.debug("Generating reservation offer entries");
            int offerId = 0;
            for (int reservationId : reservationIds) {
                Optional<Reservation> reservation = reservationRepository.findById((long) reservationId);
                if (reservation.isPresent()) {
                    Optional<SpecialOffer> specialOffer = specialOfferRepository.findById((long) specialOfferIds[offerId++ % specialOfferIds.length]);
                    if (specialOffer.isPresent()) {
                        Reservation newReservation = reservation.get();
                        SpecialOffer newSpecialOffer = specialOffer.get();
                        ReservationOffer reservationOffer = ReservationOffer.ReservationOfferBuilder.aReservationOffer()
                            .withReservation(newReservation)
                            .withOffer(newSpecialOffer)
                            .withAmount(2)
                            .build();
                        LOGGER.info("Saving reservation offer: {}", reservationOffer);
                        reservationOfferRepository.save(reservationOffer);
                    } else {
                        LOGGER.warn("SpecialOffer with id {} does not exist", specialOfferIds[offerId % specialOfferIds.length]);
                    }
                } else {
                    LOGGER.warn("Reservation with id {} does not exist", reservationId);
                }
            }
        }
    }
}
