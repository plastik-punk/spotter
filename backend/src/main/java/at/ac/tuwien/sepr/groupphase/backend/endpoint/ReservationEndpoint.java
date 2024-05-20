package at.ac.tuwien.sepr.groupphase.backend.endpoint;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationCheckAvailabilityDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.enums.ReservationResponseEnum;
import at.ac.tuwien.sepr.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepr.groupphase.backend.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.security.PermitAll;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;
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

    // TODO: exception handling through all layers
    @ResponseStatus(HttpStatus.CREATED)
    @PermitAll
    @PostMapping
    @Operation(summary = "Create a new reservation")
    public ReservationCreateDto create(@Valid @RequestBody ReservationCreateDto reservationCreateDto) throws MessagingException, ValidationException {
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
}