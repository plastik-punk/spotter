package at.ac.tuwien.sepr.groupphase.backend.endpoint;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.AreaLayoutDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.AreaListDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationCheckAvailabilityDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationEditDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationLayoutCheckAvailabilityDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationListDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationSearchDto;
import at.ac.tuwien.sepr.groupphase.backend.enums.ReservationResponseEnum;
import at.ac.tuwien.sepr.groupphase.backend.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.annotation.security.PermitAll;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/reservations")
public class ReservationEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final ReservationService service;

    @Autowired
    public ReservationEndpoint(ReservationService service) {
        this.service = service;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PermitAll
    @PostMapping
    @Operation(summary = "Create a new reservation")
    public ReservationCreateDto create(@Valid @RequestBody ReservationCreateDto reservationCreateDto) throws MessagingException {
        LOGGER.info("POST /api/v1/reservations body: {}", reservationCreateDto.toString());
        return service.create(reservationCreateDto);
    }

    @ResponseStatus(HttpStatus.OK)
    @PermitAll
    @GetMapping
    @Operation(summary = "Check if any tables are available for requested time and pax")
    public ReservationResponseEnum getAvailability(@Valid ReservationCheckAvailabilityDto reservationCheckAvailabilityDto) {
        LOGGER.info("GET /api/v1/reservations body: {}", reservationCheckAvailabilityDto);
        return service.getAvailability(reservationCheckAvailabilityDto);
    }

    //Todo als dto
    @ResponseStatus(HttpStatus.OK)
    @PermitAll
    @GetMapping("/layout")
    @Operation(summary = "Get layout of area for requested time and pax")
    public AreaLayoutDto getAvailabilityLayout(@Valid ReservationLayoutCheckAvailabilityDto reservationLayoutCheckAvailabilityDto) {
        LOGGER.info("GET /api/v1/reservations/layout body: {}", reservationLayoutCheckAvailabilityDto);
        return service.getAreaLayout(reservationLayoutCheckAvailabilityDto);
    }

    @ResponseStatus(HttpStatus.OK)
    @PermitAll
    @GetMapping("/areas")
    @Operation(summary = "Get list of all areas")
    public AreaListDto getAllAreas() {
        LOGGER.info("GET /api/v1/reservations/areas");
        return service.getAllAreas();
    }

    @ResponseStatus(HttpStatus.OK)
    @PermitAll
    @GetMapping({"/next"})
    @Operation(summary = "Get the next three available reservations")
    public ReservationCheckAvailabilityDto[] getNextAvailableTables(@Valid ReservationCheckAvailabilityDto reservationCheckAvailabilityDto) {
        LOGGER.info("GET /api/v1/reservations body: {}", reservationCheckAvailabilityDto);
        return service.getNextAvailableTables(reservationCheckAvailabilityDto);
    }

    @ResponseStatus(HttpStatus.OK)
    @PermitAll
    @GetMapping({"/detail"})
    @Operation(summary = "Get detail information for a single reservation")
    public ReservationEditDto getByHashedId(@RequestParam("id") String id) {
        LOGGER.info("GET /api/v1/reservations/detail body: {}", id);
        return service.getByHashedId(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @PermitAll
    @PutMapping
    @Operation(summary = "Update a reservation")
    public ReservationEditDto update(@Valid @RequestBody ReservationEditDto reservationEditDto) {
        LOGGER.info("PUT /api/v1/reservations body: {}", reservationEditDto.toString());
        return service.update(reservationEditDto);
    }

    @Secured({"ROLE_ADMIN", "ROLE_EMPLOYEE", "ROLE_USER"})
    @Operation(summary = "Get list of all reservations for admins and employees", security = @SecurityRequirement(name = "apiKey"))
    @GetMapping({"/search"})
    public List<ReservationListDto> searchAllReservationsForAdmin(@Valid ReservationSearchDto searchParameters) {
        LOGGER.info("GET /api/v1/reservations/admin-search");
        LOGGER.debug("request parameters: {}", searchParameters);

        return service.search(searchParameters);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PermitAll
    @DeleteMapping
    @Operation(summary = "Delete a reservation")
    public ResponseEntity<Void> delete(@RequestBody String hashedId) {
        LOGGER.info("DELETE /api/v1/reservations body: {}", hashedId);
        service.cancel(hashedId);
        return ResponseEntity.noContent().build();
    }
}