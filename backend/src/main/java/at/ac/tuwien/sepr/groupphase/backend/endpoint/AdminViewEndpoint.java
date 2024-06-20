package at.ac.tuwien.sepr.groupphase.backend.endpoint;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ForeCastDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.PredictionDto;
import at.ac.tuwien.sepr.groupphase.backend.service.AdminViewService;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
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


    // TODO: use a DTO instead of RequestParam and then validate the DTO
    @ResponseStatus(HttpStatus.OK)
    @Secured("ROLE_ADMIN")
    @GetMapping({"/prediction"})
    @Operation(summary = "Get the prediction for the next week")
    public PredictionDto getPrediction(@RequestParam("areaId") Long areaId,
                                       @RequestParam("startTime") LocalTime startTime,
                                       @RequestParam("date") LocalDate date) {
        LOGGER.info("GET /api/v1/admin-view/prediction");
        LOGGER.debug("GET /api/v1/admin-view/prediction body: {} {}, {}", areaId, startTime, date);
        return service.getPrediction(startTime, date);
    }

    @ResponseStatus(HttpStatus.OK)
    @Secured("ROLE_ADMIN")
    @GetMapping({"/forecast"})
    @Operation(summary = "Get the forecast for the next week")
    public ForeCastDto getForeCast(@RequestParam("areaId") Long areaId,
                                   @RequestParam("startTime") LocalTime startTime,
                                   @RequestParam("date") LocalDate date) {
        LOGGER.info("GET /api/v1/admin-view/forecast");
        LOGGER.debug("GET /api/v1/admin-view/forecast body: {} {}, {}", areaId, startTime, date);
        return service.getForecast(areaId, date);
    }
}
