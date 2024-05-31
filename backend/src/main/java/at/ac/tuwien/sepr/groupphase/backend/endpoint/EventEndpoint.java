package at.ac.tuwien.sepr.groupphase.backend.endpoint;


import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.EventCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.EventDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.EventListDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.EventSearchDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.EventEditDto;
import at.ac.tuwien.sepr.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepr.groupphase.backend.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.annotation.security.PermitAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.multipart.MultipartFile;

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

    @PermitAll
    @Operation(summary = "Get list of events that match the given parameters", security = @SecurityRequirement(name = "apiKey"))
    @GetMapping({"/search"})
    public List<EventListDto> searchEvents(EventSearchDto searchParameters) {
        LOGGER.info("GET /api/v1/events");
        LOGGER.debug("request parameters: {}", searchParameters.toString());
        return service.search(searchParameters);
    }

    @PermitAll
    @ResponseStatus(HttpStatus.OK)
    @GetMapping({"/detail"})
    @Operation(summary = "Get details of a specific event")
    public EventDetailDto getByHashId(@RequestParam("hashId") String hashId) {
        LOGGER.info("GET /api/v1/events/detail body: {}", hashId);
        return service.getByHashId(hashId);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @Secured("ROLE_ADMIN")
    @PostMapping
    @Operation(summary = "Create a new event")
    public EventCreateDto create(@RequestBody EventCreateDto eventCreateDto) throws ValidationException {
        LOGGER.info("POST /api/v1/events body: {}", eventCreateDto.toString());
        return service.create(eventCreateDto);
    }

    @Secured("ROLE_ADMIN")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping
    @Operation(summary = "Update an existing event")
    public EventEditDto update(@RequestBody EventEditDto eventEditDto) throws ValidationException {
        LOGGER.info("PUT /api/v1/events body: {}", eventEditDto.toString());
        return service.update(eventEditDto);
    }

    @Secured("ROLE_ADMIN")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping
    @Operation(summary = "Delete an existing event")
    public ResponseEntity<Void> delete(@RequestBody String hashId) {
        LOGGER.info("DELETE /api/v1/events body: {}", hashId);
        service.delete(hashId);
        return ResponseEntity.noContent().build();
    }


    @Secured("ROLE_ADMIN")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/import-ics")
    @Operation(summary = "Import events from an ICS file")
    public ResponseEntity<String> importIcsFile(@RequestParam("file") MultipartFile file) throws IllegalArgumentException {
        LOGGER.info("POST /api/v1/events/import-ics");
        service.importIcsFile(file);
        return ResponseEntity.ok("File imported successfully");
    }

}
