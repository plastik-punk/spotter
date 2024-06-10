package at.ac.tuwien.sepr.groupphase.backend.endpoint;

import at.ac.tuwien.sepr.groupphase.backend.service.PlaceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;

@RestController
@RequestMapping(value = "/api/v1/place")
public class PlaceEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final PlaceService placeService;

    public PlaceEndpoint(PlaceService placeService) {
        this.placeService = placeService;
    }

    @PutMapping("block/{id}")
    @Secured("ROLE_EMPLOYEE")
    public void block(@PathVariable("id") long id) {
        LOGGER.info("PUT /api/v1/place/block/{}", id);
        placeService.block(id);
    }

    @PutMapping("unblock/{id}")
    @Secured("ROLE_EMPLOYEE")
    public void unblock(@PathVariable("id") long id) {
        LOGGER.info("PUT /api/v1/place/unblock/{}", id);
        placeService.unblock(id);
    }
}
