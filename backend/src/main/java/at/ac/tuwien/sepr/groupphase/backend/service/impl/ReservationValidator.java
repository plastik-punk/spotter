package at.ac.tuwien.sepr.groupphase.backend.service.impl;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationCheckAvailabilityDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepr.groupphase.backend.entity.Place;
import at.ac.tuwien.sepr.groupphase.backend.entity.Reservation;
import at.ac.tuwien.sepr.groupphase.backend.repository.ReservationRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import at.ac.tuwien.sepr.groupphase.backend.exception.ValidationException;

import java.lang.invoke.MethodHandles;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class ReservationValidator {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    private final UserDataValidator userDataValidator;

    @Autowired
    private final ReservationRepository reservationRepository;

    @Autowired
    private final UserService userService;

    @Autowired
    public ReservationValidator(ReservationRepository reservationRepository, UserService userService) {
        this.userDataValidator = new UserDataValidator();
        this.reservationRepository = reservationRepository;
        this.userService = userService;
    }

    public void validateReservation(Reservation reservation) throws ValidationException {
        LOGGER.trace("validateReservation({})", reservation);
        List<String> validationErrors = new ArrayList<>();

        this.userDataValidator.validateApplicationUser(validationErrors, reservation.getApplicationUser());

        validateStartTime(validationErrors, reservation.getStartTime());
        // validateEndTime(validationErrors, reservation.getEndTime(), reservation.getStartTime()); // TODO: activate after end time was implemented in frontend
        validateDate(validationErrors, reservation.getDate());
        validatePax(validationErrors, reservation.getPax());
        validateNotes(validationErrors, reservation.getNotes());
        validatePlace(validationErrors, reservation.getPlace());

        if (!validationErrors.isEmpty()) {
            throw new ValidationException("Validation of reservation failed", validationErrors);
        }
    }

    public void validateReservationDetailDto(ReservationDetailDto dto) throws ValidationException {
        LOGGER.trace("validateReservationDetailDto({})", dto);
        List<String> validationErrors = new ArrayList<>();

        validateStartTime(validationErrors, dto.getStartTime());
        validateEndTime(validationErrors, dto.getEndTime(), dto.getStartTime());
        validateDate(validationErrors, dto.getDate());
        validatePax(validationErrors, dto.getPax());
        validateNotes(validationErrors, dto.getNotes());
        validatePlaceId(validationErrors, dto.getPlaceId());

        if (!validationErrors.isEmpty()) {
            throw new ValidationException("Validation of reservationDetailDto failed", validationErrors);
        }
    }

    public void validateReservationCreateDto(ReservationCreateDto dto) throws ValidationException {
        LOGGER.trace("validateReservationCreateDto({})", dto);
        List<String> validationErrors = new ArrayList<>();

        if (dto.getUser() != null) {
            this.userDataValidator.validateApplicationUser(validationErrors, dto.getUser());
        }
        this.userDataValidator.validateFirstName(validationErrors, dto.getFirstName());
        this.userDataValidator.validateEmail(validationErrors, dto.getEmail());
        this.userDataValidator.validatePhoneNumber(validationErrors, dto.getMobileNumber());

        validateStartTime(validationErrors, dto.getStartTime());
        // validateEndTime(validationErrors, dto.getEndTime(), dto.getStartTime()); // TODO: activate after end time was implemented in frontend
        validateDate(validationErrors, dto.getDate());
        validatePax(validationErrors, dto.getPax());
        validateNotes(validationErrors, dto.getNotes());

        if (!validationErrors.isEmpty()) {
            throw new ValidationException("Validation of reservationCreateDto failed", validationErrors);
        }
    }

    public void validateReservationCheckAvailabilityDto(ReservationCheckAvailabilityDto dto) throws ValidationException {
        LOGGER.trace("validateReservationCheckAvailabilityDto({})", dto);
        List<String> validationErrors = new ArrayList<>();

        validateStartTime(validationErrors, dto.getStartTime());
        // validateEndTime(validationErrors, dto.getEndTime(), dto.getStartTime()); // TODO: activate after end time was implemented in frontend
        validatePax(validationErrors, dto.getPax());
        if (dto.getDate() == null) {
            validationErrors.add("No date given");
        }

        if (!validationErrors.isEmpty()) {
            throw new ValidationException("Validation of reservationCheckAvailabilityDto failed", validationErrors);
        }
    }

    public void validateReservationDelete(Long id) throws ValidationException {
        LOGGER.trace("validateReservationDelete({})", id);
        List<String> validationErrors = new ArrayList<>();

        // 1. validate id
        if (id <= 0) {
            validationErrors.add("ID is invalid");
            throw new ValidationException("Validation for delete failed", validationErrors);
        }

        // 2. fetch reservation with id for validation of deletion
        Optional<Reservation> optionalReservation = reservationRepository.findById(id);
        if (optionalReservation.isEmpty()) {
            validationErrors.add("Reservation not found");
            throw new ValidationException("Validation of delete failed", validationErrors);
        }
        Reservation reservation = optionalReservation.get();

        // 3. fetch current user
        ApplicationUser currentUser = userService.getCurrentUser();

        // 4. validate reservation
        if (reservation.getApplicationUser() != null && !reservation.getApplicationUser().equals(currentUser)) {
            // this way an unauthorized user does not get any information about the existence of a reservation
            throw new ValidationException("Only the customer booking a reservation can delete it", validationErrors);
        } else if (reservation.getDate().isBefore(LocalDate.now())) {
            validationErrors.add("Reservation is in the past and cannot be deleted");
        } else if (Objects.equals(reservation.getDate(), LocalDate.now()) && reservation.getStartTime().isBefore(LocalTime.now().plusHours(1))) {
            validationErrors.add("You can not cancel a reservation less than 1 hour before it starts");
        }

        if (!validationErrors.isEmpty()) {
            throw new ValidationException("Validation of delete failed", validationErrors);
        }
    }

    private void validateStartTime(List<String> validationErrors, LocalTime startTime) {
        LOGGER.trace("validateStartTime({})", startTime);

        if (startTime == null) {
            validationErrors.add("No start time given");
        }
    }

    private void validateEndTime(List<String> validationErrors, LocalTime endTime, LocalTime startTime) {
        LOGGER.trace("validateEndTime({})", endTime);
        if (endTime == null) {
            validationErrors.add("No end time given");
        } else if (startTime != null && endTime.isBefore(startTime)) {
            validationErrors.add("End time cannot be before start time");
        }
    }

    private void validateDate(List<String> validationErrors, LocalDate date) {
        LOGGER.trace("validateDate({})", date);
        if (date == null) {
            validationErrors.add("No date given");
        } else if (date.isBefore(LocalDate.now())) {
            validationErrors.add("Date cannot be in the past");
        }
    }

    private void validatePax(List<String> validationErrors, Long pax) {
        LOGGER.trace("validatePax({})", pax);
        if (pax == null) {
            validationErrors.add("No pax given");
        } else if (pax <= 0) {
            validationErrors.add("Pax should be greater than 0");
        }
    }

    private void validateNotes(List<String> validationErrors, String notes) {
        LOGGER.trace("validateNotes({})", notes);
        if (notes != null) {
            if (notes.length() > 100000) {
                validationErrors.add("Notes shouldn't be longer than 100000 characters");
            }
        }
    }

    private void validatePlace(List<String> validationErrors, Place place) {
        LOGGER.trace("validatePlace({})", place);
        if (place == null) {
            validationErrors.add("No place given");
        }
    }

    private void validatePlaceId(List<String> validationErrors, Long placeId) {
        LOGGER.trace("validatePlaceId({})", placeId);
        if (placeId == null) {
            validationErrors.add("No place ID given");
        } else if (placeId <= 0) {
            validationErrors.add("Place ID is invalid");
        }
    }
}