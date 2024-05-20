package at.ac.tuwien.sepr.groupphase.backend.datagenerator;

import at.ac.tuwien.sepr.groupphase.backend.entity.ClosedDay;
import at.ac.tuwien.sepr.groupphase.backend.entity.Restaurant;
import at.ac.tuwien.sepr.groupphase.backend.repository.ClosedDayRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.RestaurantRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.time.LocalDate;
import java.util.Optional;

@Profile({"generateData", "test"})
@Component
@Order(6)
public class ClosedDayDataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final ClosedDayRepository closedDayRepository;
    private final RestaurantRepository restaurantRepository;

    public ClosedDayDataGenerator(ClosedDayRepository closedDayRepository, RestaurantRepository restaurantRepository) {
        this.closedDayRepository = closedDayRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @PostConstruct
    private void generateClosedDays() {
        LOGGER.trace("generateClosedDays");

        Restaurant restaurant;
        Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(1L);
        if (optionalRestaurant.isPresent()) {
            restaurant = optionalRestaurant.get();
        } else {
            restaurant = Restaurant.RestaurantBuilder.aRestaurant()
                .withId(1L)
                .withName("The Wet Otter")
                .withAddress("Karlsplatz 13, 1040 Vienna")
                .build();
            restaurantRepository.save(restaurant);
        }

        if (this.closedDayRepository.count() > 0) {
            LOGGER.debug("Closed days have already been generated");
        } else {
            LOGGER.debug("Generating closed days");

            ClosedDay closedDay = ClosedDay.ClosedDayBuilder.aClosedDay()
                .withId(1L)
                .withRestaurant(restaurant)
                .withDate(LocalDate.of(2024, 6, 12))
                .build();

            closedDayRepository.save(closedDay);
        }
    }
}