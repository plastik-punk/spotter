package at.ac.tuwien.sepr.groupphase.backend.unittests.repository;

import at.ac.tuwien.sepr.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepr.groupphase.backend.entity.Restaurant;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import at.ac.tuwien.sepr.groupphase.backend.repository.RestaurantRepository;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
public class RestaurantRepositoryTest implements TestData {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Test
    @Transactional
    public void givenLegalData_whenSaveRestaurant_thenFindListWithOneElementAndFindRestaurantById() {
        Restaurant restaurant = Restaurant.RestaurantBuilder.aRestaurant()
            .withName(TEST_RESTAURANT_NAME)
            .withAddress(TEST_RESTAURANT_ADDRESS)
            .build();
        restaurantRepository.save(restaurant);

        assertAll(
            () -> assertEquals(1, restaurantRepository.findAll().size()),
            () -> assertNotNull(restaurantRepository.findById(restaurant.getId()))
        );
    }
}