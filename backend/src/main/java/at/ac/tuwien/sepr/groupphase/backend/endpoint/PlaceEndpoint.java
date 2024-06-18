package at.ac.tuwien.sepr.groupphase.backend.endpoint;

import at.ac.tuwien.sepr.groupphase.backend.service.PlaceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.lang.invoke.MethodHandles;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/places")
public class PlaceEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final PlaceService placeService;

    public PlaceEndpoint(PlaceService placeService) {
        this.placeService = placeService;
    }

    @PutMapping("block")
    @Secured("ROLE_EMPLOYEE")
    public void block(@RequestBody List<Long> ids) {
        LOGGER.info("PUT /api/v1/place/block/{}", ids);
        placeService.block(ids);
    }
}
