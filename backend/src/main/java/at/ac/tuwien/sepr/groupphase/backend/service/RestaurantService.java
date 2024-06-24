package at.ac.tuwien.sepr.groupphase.backend.service;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RestaurantOpeningHoursDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RestaurantDto;

/**
 * Service for operations on the restaurant (e.g. getting the opening hours).
 */
public interface RestaurantService {

    /**
     * Get the opening hours of the restaurant.
     *
     * @return the opening hours
     */
    RestaurantOpeningHoursDto getOpeningHours();

    /**
     * Get the restaurant information.
     *
     * @return the restaurant information
     */
    RestaurantDto getRestaurantInfo();
}
