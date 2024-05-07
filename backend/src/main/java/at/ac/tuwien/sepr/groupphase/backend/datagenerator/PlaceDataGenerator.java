package at.ac.tuwien.sepr.groupphase.backend.datagenerator;

import at.ac.tuwien.sepr.groupphase.backend.entity.Place;
import at.ac.tuwien.sepr.groupphase.backend.enums.StatusEnum;
import at.ac.tuwien.sepr.groupphase.backend.repository.PlaceRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;

@Profile("generateData")
@Component
public class PlaceDataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final int NUMBER_OF_PLACES_TO_GENERATE = 5;

    private final PlaceRepository placeRepository;

    public PlaceDataGenerator(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    @PostConstruct
    private void generatePlaces() {
        if (placeRepository.findAll().size() > 0) {
            LOGGER.debug("Places already generated");
        } else {
            LOGGER.debug("Generating {} place entries", NUMBER_OF_PLACES_TO_GENERATE);
            for (int i = 0; i < NUMBER_OF_PLACES_TO_GENERATE; i++) {
                Place place = Place.PlaceBuilder.aPlace()
                    .withPax((long) (2 + i))
                    .withStatus(StatusEnum.AVAILABLE)
                    .build();
                LOGGER.debug("Saving place {}", place);
                placeRepository.save(place);
            }
        }
    }
}
