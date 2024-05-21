package at.ac.tuwien.sepr.groupphase.backend.endpoint;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationCheckAvailabilityDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationListDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationSearchDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.enums.ReservationResponseEnum;
import at.ac.tuwien.sepr.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepr.groupphase.backend.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.annotation.security.PermitAll;
import jakarta.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
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
import java.time.LocalDate;
import java.time.LocalTime;

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
    public ReservationCreateDto create(@RequestBody ReservationCreateDto reservationCreateDto) throws MessagingException, ValidationException {
        LOGGER.info("POST /api/v1/reservations body: {}", reservationCreateDto.toString());
        return service.create(reservationCreateDto);
    }

    @ResponseStatus(HttpStatus.OK)
    @PermitAll
    @GetMapping
    @Operation(summary = "Check if any tables are available for requested time and pax")
    public ReservationResponseEnum getAvailability(@RequestParam("startTime") String startTime,
                                                   @RequestParam("date") String date,
                                                   @RequestParam("pax") Long pax)
        throws ValidationException {

        ReservationCheckAvailabilityDto reservationCheckAvailabilityDto = ReservationCheckAvailabilityDto.ReservationCheckAvailabilityDtoBuilder.aReservationCheckAvailabilityDto()
            .withStartTime(LocalTime.parse(startTime))
            .withEndTime(LocalTime.parse(startTime).plusHours(2))
            .withDate(LocalDate.parse(date))
            .withPax(pax)
            .build();

        LOGGER.info("GET /api/v1/reservations body: {}", reservationCheckAvailabilityDto.toString());
        return service.getAvailability(reservationCheckAvailabilityDto);
    }

    @ResponseStatus(HttpStatus.OK)
    @PermitAll
    @GetMapping({"/detail"})
    @Operation(summary = "Get detail information for a single reservation")
    public ReservationDetailDto getById(@RequestParam("id") Long id) throws ValidationException {
        LOGGER.info("GET /api/v1/reservations/detail body: {}", id);
        return service.getById(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @PermitAll
    @PutMapping
    @Operation(summary = "Update a reservation")
    public ReservationDetailDto update(@RequestBody ReservationDetailDto reservationDetailDto) throws ValidationException {
        LOGGER.info("PUT /api/v1/reservations body: {}", reservationDetailDto.toString());
        return service.update(reservationDetailDto);
    }
    @Secured("ROLE_USER")
    @Operation(summary = "Get list of reservations that match the given parameters", security = @SecurityRequirement(name = "apiKey"))
    @GetMapping
    public List<ReservationListDto> searchReservations(ReservationSearchDto searchParameters) {
        LOGGER.info("POST /api/v1/reservations");
        LOGGER.debug("request parameters: {}", searchParameters);
        return service.search(searchParameters);
    }
}