package at.ac.tuwien.sepr.groupphase.backend.endpoint;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RestaurantOpeningHoursDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RestaurantDto;
import at.ac.tuwien.sepr.groupphase.backend.service.RestaurantService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.security.PermitAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;

@RestController
@RequestMapping(value = "/api/v1/restaurants")
public class RestaurantEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final RestaurantService service;

    @Autowired
    public RestaurantEndpoint(RestaurantService service) {
        this.service = service;
    }

    @ResponseStatus(HttpStatus.OK)
    @PermitAll
    @GetMapping("/opening-hours")
    @Operation(summary = "Get opening hours for the location")
    public RestaurantOpeningHoursDto getOpeningHours() {
        LOGGER.info("GET /api/v1/restaurant/opening-hours");
        return service.getOpeningHours();
    }

    @ResponseStatus(HttpStatus.OK)
    @PermitAll
    @GetMapping("/restaurant")
    @Operation(summary = "Get restaurant info")
    public RestaurantDto getRestaurantInfo() {
        LOGGER.info("GET /api/v1/restaurant/restaurant");
        return service.getRestaurantInfo();
    }
}