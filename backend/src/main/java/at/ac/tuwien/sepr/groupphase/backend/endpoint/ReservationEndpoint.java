package at.ac.tuwien.sepr.groupphase.backend.endpoint;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.PermanentReservationCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.PermanentReservationDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.PermanentReservationListDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.PermanentReservationSearchDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationCheckAvailabilityDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationEditDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationListDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationModalDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationSearchDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationWalkInDto;
import at.ac.tuwien.sepr.groupphase.backend.enums.ReservationResponseEnum;
import at.ac.tuwien.sepr.groupphase.backend.exception.ConflictException;
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
import org.springframework.web.bind.annotation.PathVariable;
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

    @ResponseStatus(HttpStatus.CREATED)
    @Secured({"ROLE_ADMIN", "ROLE_EMPLOYEE"})
    @PostMapping("/walk-in")
    @Operation(summary = "Create a new walk-in reservation")
    public ReservationCreateDto createWalkIn(@Valid @RequestBody ReservationWalkInDto reservationWalkInDto) throws ConflictException, MessagingException {
        LOGGER.info("POST /api/v1/reservations/walk-in body: {}", reservationWalkInDto.toString());
        return service.createWalkIn(reservationWalkInDto);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/permanent")
    @PermitAll
    @Operation(summary = "Create a new permanent reservation")
    public PermanentReservationCreateDto createPermanent(@Valid @RequestBody PermanentReservationCreateDto permanentReservationCreateDto) {
        LOGGER.info("POST /api/v1/reservations/permanent body: {}", permanentReservationCreateDto.toString());
        return service.createPermanent(permanentReservationCreateDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/permanent/confirmation/{id}")
    @Secured({"ROLE_ADMIN", "ROLE_EMPLOYEE"})
    @Operation(summary = "Confirm a new permanent reservation")
    public ResponseEntity<Void> confirmPermanentReservation(@PathVariable("id") Long id) throws MessagingException {
        LOGGER.info("POST /api/v1/reservations/permanent/{}", id);
        service.confirmPermanentReservation(id);
        return ResponseEntity.noContent().build();
    }

    @ResponseStatus(HttpStatus.OK)
    @PermitAll
    @GetMapping
    @Operation(summary = "Check if any tables are available for requested time and pax")
    public ReservationResponseEnum getAvailability(@Valid ReservationCheckAvailabilityDto reservationCheckAvailabilityDto) {
        LOGGER.info("GET /api/v1/reservations body: {}", reservationCheckAvailabilityDto.toString());
        return service.getAvailability(reservationCheckAvailabilityDto);
    }


    @ResponseStatus(HttpStatus.OK)
    @PermitAll
    @GetMapping({"/next"})
    @Operation(summary = "Get the next three available reservations")
    public ReservationCheckAvailabilityDto[] getNextAvailableTables(@Valid ReservationCheckAvailabilityDto reservationCheckAvailabilityDto) {
        LOGGER.info("GET /api/v1/reservations body: {}", reservationCheckAvailabilityDto.toString());
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
    @GetMapping("/modal")
    @Operation(summary = "Get details for the reservation modal")
    public ReservationModalDetailDto getModalDetail(@RequestParam("id") String id) {
        LOGGER.info("GET /api/v1/reservations/modal body: {}", id);
        return service.getModalDetail(id);
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
        LOGGER.info("GET /api/v1/reservations/admin-search body: {}", searchParameters.toString());
        return service.search(searchParameters);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PermitAll
    @DeleteMapping
    @Operation(summary = "Delete a reservation")
    public ResponseEntity<Void> delete(@RequestBody String hashedId) {
        LOGGER.info("DELETE /api/v1/reservations body: {}", hashedId);
        LOGGER.debug("hasedID: {}", hashedId);
        service.cancel(hashedId);
        return ResponseEntity.noContent().build();
    }

    @ResponseStatus(HttpStatus.OK)
    @Secured({"ROLE_EMPLOYEE"})
    @PutMapping("/confirm")
    @Operation(summary = "Confirm a reservation")
    public ResponseEntity<Void> confirm(@RequestBody String hashedId) {
        LOGGER.info("PUT /api/v1/reservations/confirm body: {}", hashedId);
        service.confirm(hashedId);
        return ResponseEntity.ok().build();
    }

    @ResponseStatus(HttpStatus.OK)
    @Secured({"ROLE_EMPLOYEE"})
    @PutMapping("/unconfirm")
    @Operation(summary = "Unconfirm a reservation")
    public ResponseEntity<Void> unconfirm(@RequestBody String hashedId) {
        LOGGER.info("PUT /api/v1/reservations/unconfirm body: {}", hashedId);
        service.unconfirm(hashedId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/permanent")
    @Operation(summary = "Get all permanent reservations")
    @Secured({"ROLE_ADMIN", "ROLE_EMPLOYEE", "ROLE_USER"})
    public List<PermanentReservationListDto> getPermanentReservations(
        PermanentReservationSearchDto searchParams) {
        LOGGER.info("GET /api/v1/reservations/permanent with search params: {}", searchParams);
        return service.searchPermanent(searchParams);
    }

    @ResponseStatus(HttpStatus.OK)
    @PermitAll
    @GetMapping({"/permanent/detail/{hashedId}"})
    @Operation(summary = "Get detail information for a permanent reservation")
    public PermanentReservationDetailDto getPermanentReservationDetailsByHashedId(@PathVariable("hashedId") String hashedId) {
        LOGGER.info("GET /api/v1/reservations/permanent/detail/{}", hashedId);
        return service.getPermanentDetails(hashedId);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Secured({"ROLE_ADMIN", "ROLE_EMPLOYEE", "ROLE_USER"})
    @DeleteMapping({"/permanent/delete/{hashedId}"})
    @Operation(summary = "Delete a permanent reservation ")
    public ResponseEntity<Void> deletePermanent(@PathVariable("hashedId") String hashedId) throws MessagingException {
        LOGGER.info("DELETE /api/v1/reservations/permanent/delete/{} ", hashedId);
        service.deletePermanentReservation(hashedId);
        return ResponseEntity.noContent().build();
    }

}