package at.ac.tuwien.sepr.groupphase.backend.endpoint;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationListDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationSearchDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.Reservation;
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
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;
import java.util.List;

// todo: use correct endpoint path, activate LoginEndpoint, fix authentication

@RestController
@RequestMapping(value = "/api/v1/reservations")
public class ReservationEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final ReservationService service;

    @Autowired
    public ReservationEndpoint(ReservationService service) {
        this.service = service;
    }

    // todo: authentication
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new reservation")
    @PermitAll
    @PostMapping
    public Reservation create(@Valid @RequestBody ReservationCreateDto reservationCreateDto) throws MessagingException {
        LOGGER.info("POST /api/v1/reservations body: {}", reservationCreateDto.toString());
        return service.create(reservationCreateDto);
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