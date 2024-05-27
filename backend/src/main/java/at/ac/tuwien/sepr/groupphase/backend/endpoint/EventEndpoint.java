package at.ac.tuwien.sepr.groupphase.backend.endpoint;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.EventDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.EventListDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.EventSearchDto;
import at.ac.tuwien.sepr.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepr.groupphase.backend.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.annotation.security.PermitAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.lang.invoke.MethodHandles;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/events")
public class EventEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private EventService service;

    @Autowired
    public EventEndpoint(EventService service) {
        this.service = service;
    }

    @Operation(summary = "Get list of events that match the given parameters", security = @SecurityRequirement(name = "apiKey"))
    @GetMapping({"/search"})
    public List<EventListDto> searchEvents (EventSearchDto searchParameters) {
        LOGGER.info("GET /api/v1/events");
        LOGGER.debug("request parameters: {}", searchParameters.toString());
        return service.search(searchParameters);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping({"/detail"})
    @Operation(summary = "Get details of a specific event")
    public EventDetailDto getByHashId(@RequestParam("hashId") String hashId) throws ValidationException {
        LOGGER.info("GET /api/v1/events/detail body: {}", hashId);
        return service.getByHashId(hashId);
    }

    //TODO: delete

}
