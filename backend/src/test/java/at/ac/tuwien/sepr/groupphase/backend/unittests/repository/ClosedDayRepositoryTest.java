package at.ac.tuwien.sepr.groupphase.backend.unittests.repository;

import at.ac.tuwien.sepr.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepr.groupphase.backend.entity.ClosedDay;
import at.ac.tuwien.sepr.groupphase.backend.repository.RestaurantRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import at.ac.tuwien.sepr.groupphase.backend.repository.ClosedDayRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
public class ClosedDayRepositoryTest implements TestData {

    @Autowired
    private ClosedDayRepository closedDayRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Test
    @Transactional
    public void givenLegalData_whenSaveClosedDay_thenFindListWithOneElementAndFindClosedDayById() {
        ClosedDay closedDay = ClosedDay.ClosedDayBuilder.aClosedDay()
            .withRestaurant(restaurantRepository.save(TEST_RESTAURANT_1))
            .withDate(TEST_CLOSED_DAY_DATE)
            .build();
        closedDayRepository.save(closedDay);

        assertAll(
            () -> assertEquals(1, closedDayRepository.findAll().size()),
            () -> assertNotNull(closedDayRepository.findById(closedDay.getId()))
        );
    }

    @Test
    @Transactional
    public void givenDate_whenFindByDate_thenReturnClosedDay() {
        ClosedDay closedDay = ClosedDay.ClosedDayBuilder.aClosedDay()
            .withRestaurant(restaurantRepository.save(TEST_RESTAURANT_1))
            .withDate(TEST_CLOSED_DAY_DATE)
            .build();
        closedDayRepository.save(closedDay);

        assertAll(
            () -> assertNotNull(closedDayRepository.findByDate(TEST_CLOSED_DAY_DATE))
        );
    }
}