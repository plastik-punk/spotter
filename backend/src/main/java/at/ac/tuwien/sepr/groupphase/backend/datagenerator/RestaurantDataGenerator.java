package at.ac.tuwien.sepr.groupphase.backend.datagenerator;

import at.ac.tuwien.sepr.groupphase.backend.entity.Restaurant;
import at.ac.tuwien.sepr.groupphase.backend.repository.RestaurantRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;

@Profile({"test"})
@Component
@Order(4)
public class RestaurantDataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final RestaurantRepository restaurantRepository;

    public RestaurantDataGenerator(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @PostConstruct
    private void generateRestaurants() {
        LOGGER.trace("generateRestaurants");

        if (this.restaurantRepository.count() > 0) {
            LOGGER.debug("Restaurants have already been generated");
        } else {
            LOGGER.debug("Generating restaurants");

            // TODO
            Restaurant restaurant = Restaurant.RestaurantBuilder.aRestaurant()
                .withId(1L)
                .withName("The Wet Otter")
                .withAddress("Karlsplatz 13, 1040 Vienna")
                .build();

            restaurantRepository.save(restaurant);
        }
    }
}