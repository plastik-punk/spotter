package at.ac.tuwien.sepr.groupphase.backend.unittests.repository;

import at.ac.tuwien.sepr.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepr.groupphase.backend.entity.OpeningHours;
import at.ac.tuwien.sepr.groupphase.backend.repository.RestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import at.ac.tuwien.sepr.groupphase.backend.repository.OpeningHoursRepository;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
public class OpeningHoursRepositoryTest implements TestData {

    @Autowired
    private OpeningHoursRepository openingHoursRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Test
    @Transactional
    public void givenLegalData_whenSaveOpeningHours_thenFindListWithOneElementAndFindOpeningHoursById() {
        OpeningHours openingHours = OpeningHours.OpeningHourBuilder.anOpeningHour()
            .withRestaurant(restaurantRepository.save(TEST_RESTAURANT_1))
            .withDayOfWeek(TEST_OPENING_HOURS_DAY_OF_WEEK)
            .withOpeningTime(TEST_OPENING_HOURS_OPENING_TIME)
            .withClosingTime(TEST_OPENING_HOURS_CLOSING_TIME)
            .build();
        openingHoursRepository.save(openingHours);

        assertAll(
            () -> assertEquals(1, openingHoursRepository.findAll().size()),
            () -> assertNotNull(openingHoursRepository.findById(openingHours.getId()))
        );
    }

    @Test
    @Transactional
    public void givenDayOfWeek_whenFindByDayOfWeek_thenFindOpeningHours() {
        OpeningHours openingHours = OpeningHours.OpeningHourBuilder.anOpeningHour()
            .withRestaurant(restaurantRepository.save(TEST_RESTAURANT_1))
            .withDayOfWeek(TEST_OPENING_HOURS_DAY_OF_WEEK)
            .withOpeningTime(TEST_OPENING_HOURS_OPENING_TIME)
            .withClosingTime(TEST_OPENING_HOURS_CLOSING_TIME)
            .build();
        openingHoursRepository.save(openingHours);

        assertAll(
            () -> assertEquals(openingHours, openingHoursRepository.findByDayOfWeek(TEST_OPENING_HOURS_DAY_OF_WEEK).get(0))
        );
    }
}