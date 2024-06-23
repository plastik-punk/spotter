package at.ac.tuwien.sepr.groupphase.backend.endpoint;


import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.AreaDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.AreaDetailListDto;
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
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

    @ResponseStatus(HttpStatus.OK)
    @PermitAll
    @GetMapping("/areas/detailed")
    @Operation(summary = "Get list of all areas with details")
    public AreaDetailListDto getAllAreasDetailed() {
        LOGGER.info("GET /api/v1/reservations/areas/detailed");
        return layoutService.getAllAreasDetailed();
    }

    // TODO: logging
    @ResponseStatus(HttpStatus.CREATED)
    @PermitAll
    @PostMapping("/create")
    @Operation(summary = "Create layout")
    public void createLayout(@RequestBody @Valid LayoutCreateDto layoutCreateDto) {
        layoutService.createLayout(layoutCreateDto);
    }

    // TODO: logging
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Secured({"ROLE_ADMIN"})
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete area")
    public void deleteArea(@PathVariable("id") Long id) throws ConflictException {
        layoutService.deleteArea(id);
    }

    // TODO: logging
    @ResponseStatus(HttpStatus.OK)
    @PermitAll
    @GetMapping("/area/{id}")
    @Operation(summary = "Get layout of area")
    public AreaDetailDto getAreaById(@PathVariable("id") Long id) {
        return layoutService.getAreaById(id);
    }

    // TODO: logging
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Secured({"ROLE_ADMIN", "ROLE_EMPLOYEE"})
    @PutMapping("/toggleOpen/{id}")
    @Operation(summary = "Toggle open status of area")
    public void toggleOpen(@PathVariable("id") Long id, @RequestBody Boolean isOpen) {
        layoutService.toggleOpen(id, isOpen);
    }

    // TODO: logging
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Secured({"ROLE_ADMIN"})
    @PutMapping("/area/update")
    @Operation(summary = "Update area")
    public void updateArea(@RequestBody AreaDetailDto areaDetailDto) throws ConflictException {
        layoutService.updateArea(areaDetailDto);
    }

    // TODO: logging
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Secured({"ROLE_ADMIN"})
    @PutMapping("/toggleMain/{id}")
    @Operation(summary = "Toggle main status of area")
    public void toggleMain(@PathVariable("id") Long id, @RequestBody Boolean isMain) {
        layoutService.toggleMain(id, isMain);
    }
}