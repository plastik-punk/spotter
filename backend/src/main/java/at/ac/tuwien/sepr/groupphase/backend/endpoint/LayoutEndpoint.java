package at.ac.tuwien.sepr.groupphase.backend.endpoint;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.AreaLayoutDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.AreaListDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.LayoutCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationLayoutCheckAvailabilityDto;
import at.ac.tuwien.sepr.groupphase.backend.exception.ConflictException;
import at.ac.tuwien.sepr.groupphase.backend.service.LayoutService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;

@RestController
@RequestMapping(value = "/api/v1/layout")
public class LayoutEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final LayoutService layoutService;

    @Autowired
    public LayoutEndpoint(LayoutService layoutService) {
        this.layoutService = layoutService;
    }


    @ResponseStatus(HttpStatus.OK)
    @PermitAll
    @GetMapping
    @Operation(summary = "Get layout of area for requested time and pax")
    public AreaLayoutDto getAvailabilityLayout(@Valid ReservationLayoutCheckAvailabilityDto reservationLayoutCheckAvailabilityDto) {
        LOGGER.info("GET /api/v1/reservations/layout body: {}", reservationLayoutCheckAvailabilityDto);
        return layoutService.getAreaLayout(reservationLayoutCheckAvailabilityDto);
    }

    @ResponseStatus(HttpStatus.OK)
    @PermitAll
    @GetMapping("/areas")
    @Operation(summary = "Get list of all areas")
    public AreaListDto getAllAreas() {
        LOGGER.info("GET /api/v1/reservations/areas");
        return layoutService.getAllAreas();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PermitAll
    @PostMapping("/create")
    @Operation(summary = "Create layout")
    public void createLayout(@RequestBody @Valid LayoutCreateDto layoutCreateDto) throws ConflictException {
        layoutService.createLayout(layoutCreateDto);
    }
}
