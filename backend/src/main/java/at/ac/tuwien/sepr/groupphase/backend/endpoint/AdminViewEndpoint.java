package at.ac.tuwien.sepr.groupphase.backend.endpoint;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.AdminViewDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ForeCastDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.PredictionDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.UnusualReservationsDto;
import at.ac.tuwien.sepr.groupphase.backend.service.AdminViewService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;
import java.time.LocalDate;
import java.time.LocalTime;

@RestController
@RequestMapping(value = "/api/v1/adminView")
public class AdminViewEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final AdminViewService service;

    public AdminViewEndpoint(AdminViewService service) {
        this.service = service;
    }

    @ResponseStatus(HttpStatus.OK)
    @Secured("ROLE_ADMIN")
    @GetMapping({"/prediction"})
    @Operation(summary = "Get the prediction for the next week")
    public PredictionDto getPrediction(@Valid AdminViewDto adminViewDto) {
        LOGGER.info("GET /api/v1/admin-view/prediction");
        LOGGER.debug("GET /api/v1/admin-view/prediction body: {}", adminViewDto);
        return service.getPrediction(adminViewDto.getStartTime(), adminViewDto.getDate());
    }

    @ResponseStatus(HttpStatus.OK)
    @Secured("ROLE_ADMIN")
    @GetMapping({"/forecast"})
    @Operation(summary = "Get the forecast for the next week")
    public ForeCastDto getForeCast(@Valid AdminViewDto adminViewDto) {
        LOGGER.info("GET /api/v1/admin-view/forecast");
        LOGGER.debug("GET /api/v1/admin-view/forecast body: {} ", adminViewDto);
        return service.getForecast(adminViewDto.getAreaId(), adminViewDto.getDate());
    }

    @ResponseStatus(HttpStatus.OK)
    @Secured("ROLE_ADMIN")
    @GetMapping({"/unusualReservations"})
    @Operation(summary = "Get the unusual reservations for the next week")
    public UnusualReservationsDto getUnusualReservations(@Valid AdminViewDto adminViewDto) {
        LOGGER.info("GET /api/v1/admin-view/unusualReservations body {}: ", adminViewDto);
        return service.getUnusualReservations(adminViewDto.getDate());
    }
}