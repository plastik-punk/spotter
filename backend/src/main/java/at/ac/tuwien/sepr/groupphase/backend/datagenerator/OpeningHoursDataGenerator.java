package at.ac.tuwien.sepr.groupphase.backend.datagenerator;

import at.ac.tuwien.sepr.groupphase.backend.entity.OpeningHours;
import at.ac.tuwien.sepr.groupphase.backend.entity.Restaurant;
import at.ac.tuwien.sepr.groupphase.backend.repository.OpeningHoursRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.RestaurantRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Optional;

@Profile({"generateData", "test"})
@Component
@Order(5)
public class OpeningHoursDataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final OpeningHoursRepository openingHoursRepository;
    private final RestaurantRepository restaurantRepository;


    public OpeningHoursDataGenerator(OpeningHoursRepository openingHoursRepository, RestaurantRepository restaurantRepository, RestaurantDataGenerator restaurantDataGenerator) {
        this.openingHoursRepository = openingHoursRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @PostConstruct
    private void generateOpeningHours() {
        LOGGER.trace("generateOpeningHours");

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

        if (this.openingHoursRepository.count() > 0) {
            LOGGER.debug("Opening hours have already been generated");
        } else {
            LOGGER.debug("Generating opening hours");

            // TODO
            OpeningHours openingHours1 = OpeningHours.OpeningHourBuilder.anOpeningHour()
                .withDayOfWeek(DayOfWeek.MONDAY)
                .withRestaurant(restaurant)
                .withOpeningTime(LocalTime.of(11, 30))
                .withClosingTime(LocalTime.of(15, 0))
                .build();

            OpeningHours openingHours2 = OpeningHours.OpeningHourBuilder.anOpeningHour()
                .withDayOfWeek(DayOfWeek.MONDAY)
                .withRestaurant(restaurant)
                .withOpeningTime(LocalTime.of(17, 0))
                .withClosingTime(LocalTime.of(22, 0))
                .build();

            OpeningHours openingHours3 = OpeningHours.OpeningHourBuilder.anOpeningHour()
                .withDayOfWeek(DayOfWeek.TUESDAY)
                .withRestaurant(restaurant)
                .withOpeningTime(LocalTime.of(11, 30))
                .withClosingTime(LocalTime.of(15, 0))
                .build();

            OpeningHours openingHours4 = OpeningHours.OpeningHourBuilder.anOpeningHour()
                .withDayOfWeek(DayOfWeek.TUESDAY)
                .withRestaurant(restaurant)
                .withOpeningTime(LocalTime.of(17, 0))
                .withClosingTime(LocalTime.of(22, 0))
                .build();

            OpeningHours openingHours5 = OpeningHours.OpeningHourBuilder.anOpeningHour()
                .withDayOfWeek(DayOfWeek.WEDNESDAY)
                .withRestaurant(restaurant)
                .withOpeningTime(LocalTime.of(11, 30))
                .withClosingTime(LocalTime.of(15, 0))
                .build();

            OpeningHours openingHours6 = OpeningHours.OpeningHourBuilder.anOpeningHour()
                .withDayOfWeek(DayOfWeek.WEDNESDAY)
                .withRestaurant(restaurant)
                .withOpeningTime(LocalTime.of(17, 0))
                .withClosingTime(LocalTime.of(22, 0))
                .build();

            OpeningHours openingHours7 = OpeningHours.OpeningHourBuilder.anOpeningHour()
                .withDayOfWeek(DayOfWeek.THURSDAY)
                .withRestaurant(restaurant)
                .withOpeningTime(LocalTime.of(11, 30))
                .withClosingTime(LocalTime.of(15, 0))
                .build();

            OpeningHours openingHours8 = OpeningHours.OpeningHourBuilder.anOpeningHour()
                .withDayOfWeek(DayOfWeek.THURSDAY)
                .withRestaurant(restaurant)
                .withOpeningTime(LocalTime.of(17, 0))
                .withClosingTime(LocalTime.of(22, 0))
                .build();

            OpeningHours openingHours9 = OpeningHours.OpeningHourBuilder.anOpeningHour()
                .withDayOfWeek(DayOfWeek.FRIDAY)
                .withRestaurant(restaurant)
                .withOpeningTime(LocalTime.of(11, 30))
                .withClosingTime(LocalTime.of(15, 0))
                .build();

            OpeningHours openingHours10 = OpeningHours.OpeningHourBuilder.anOpeningHour()
                .withDayOfWeek(DayOfWeek.FRIDAY)
                .withRestaurant(restaurant)
                .withOpeningTime(LocalTime.of(17, 0))
                .withClosingTime(LocalTime.of(23, 0))
                .build();

            OpeningHours openingHours11 = OpeningHours.OpeningHourBuilder.anOpeningHour()
                .withDayOfWeek(DayOfWeek.SATURDAY)
                .withRestaurant(restaurant)
                .withOpeningTime(LocalTime.of(11, 30))
                .withClosingTime(LocalTime.of(15, 0))
                .build();

            OpeningHours openingHours12 = OpeningHours.OpeningHourBuilder.anOpeningHour()
                .withDayOfWeek(DayOfWeek.SATURDAY)
                .withRestaurant(restaurant)
                .withOpeningTime(LocalTime.of(17, 0))
                .withClosingTime(LocalTime.of(23, 0))
                .build();

            OpeningHours openingHours13 = OpeningHours.OpeningHourBuilder.anOpeningHour()
                .withDayOfWeek(DayOfWeek.SUNDAY)
                .withRestaurant(restaurant)
                .withOpeningTime(LocalTime.of(11, 30))
                .withClosingTime(LocalTime.of(15, 0))
                .build();

            OpeningHours openingHours14 = OpeningHours.OpeningHourBuilder.anOpeningHour()
                .withDayOfWeek(DayOfWeek.SUNDAY)
                .withRestaurant(restaurant)
                .withOpeningTime(LocalTime.of(17, 0))
                .withClosingTime(LocalTime.of(22, 0))
                .build();

            openingHoursRepository.save(openingHours1);
            openingHoursRepository.save(openingHours2);
            openingHoursRepository.save(openingHours3);
            openingHoursRepository.save(openingHours4);
            openingHoursRepository.save(openingHours5);
            openingHoursRepository.save(openingHours6);
            openingHoursRepository.save(openingHours7);
            openingHoursRepository.save(openingHours8);
            openingHoursRepository.save(openingHours9);
            openingHoursRepository.save(openingHours10);
            openingHoursRepository.save(openingHours11);
            openingHoursRepository.save(openingHours12);
            openingHoursRepository.save(openingHours13);
            openingHoursRepository.save(openingHours14);
        }
    }
}