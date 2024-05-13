package at.ac.tuwien.sepr.groupphase.backend.endpoint;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper.ReservationMapper;
import at.ac.tuwien.sepr.groupphase.backend.entity.Reservation;
import at.ac.tuwien.sepr.groupphase.backend.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;

// todo: use correct endpoint path, activate LoginEndpoint, fix authentication

@RestController
@RequestMapping(value = "/api/v1/authentication")
public class ReservationEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final ReservationService service;
    private final ReservationMapper reservationMapper;

    @Autowired
    public ReservationEndpoint(ReservationService service, ReservationMapper reservationMapper) {
        this.service = service;
        this.reservationMapper = reservationMapper;
    }

    // todo: authentication
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new reservation")
    @PermitAll
    @PostMapping
    public Reservation create(@Valid @RequestBody ReservationCreateDto reservationCreateDto) {
        LOGGER.info("POST /api/v1/reservations body: {}", reservationCreateDto.toString());
        return service.create(reservationCreateDto);
    }
}