package at.ac.tuwien.sepr.groupphase.backend.service.impl;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.AreaLayoutDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationCheckAvailabilityDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationEditDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationLayoutCheckAvailabilityDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationListDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationSearchDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepr.groupphase.backend.entity.AreaPlaceSegment;
import at.ac.tuwien.sepr.groupphase.backend.entity.ClosedDay;
import at.ac.tuwien.sepr.groupphase.backend.entity.OpeningHours;
import at.ac.tuwien.sepr.groupphase.backend.entity.Place;
import at.ac.tuwien.sepr.groupphase.backend.entity.Reservation;
import at.ac.tuwien.sepr.groupphase.backend.entity.ReservationPlace;
import at.ac.tuwien.sepr.groupphase.backend.entity.Segment;
import at.ac.tuwien.sepr.groupphase.backend.enums.ReservationResponseEnum;
import at.ac.tuwien.sepr.groupphase.backend.enums.RoleEnum;
import at.ac.tuwien.sepr.groupphase.backend.enums.StatusEnum;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.repository.ApplicationUserRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.AreaPlaceSegmentRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.AreaRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.ClosedDayRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.OpeningHoursRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.PlaceRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.ReservationPlaceRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.ReservationRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.EmailService;
import at.ac.tuwien.sepr.groupphase.backend.service.HashService;
import at.ac.tuwien.sepr.groupphase.backend.service.ReservationService;
import at.ac.tuwien.sepr.groupphase.backend.service.mapper.ReservationMapper;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ReservationServiceImpl implements ReservationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final ReservationRepository reservationRepository;
    private final ApplicationUserRepository applicationUserRepository;
    private final PlaceRepository placeRepository;
    private final ReservationMapper mapper;
    private final OpeningHoursRepository openingHoursRepository;
    private final ClosedDayRepository closedDayRepository;
    private final ReservationPlaceRepository reservationPlaceRepository;
    private final EmailService emailService;
    private final HashService hashService;
    private final ApplicationUserServiceImpl applicationUserService;
    private final AreaRepository areaRepository;
    private final AreaPlaceSegmentRepository areaPlaceSegmentRepository;

    @Autowired
    private Validator validator;

    @Autowired
    public ReservationServiceImpl(ReservationMapper mapper,
                                  ReservationRepository reservationRepository,
                                  ApplicationUserRepository applicationUserRepository,
                                  PlaceRepository placeRepository,
                                  EmailService emailService,
                                  OpeningHoursRepository openingHoursRepository,
                                  ReservationPlaceRepository reservationPlaceRepository,
                                  ClosedDayRepository closedDayRepository,
                                  HashService hashService,
                                  ApplicationUserServiceImpl applicationUserService,
                                  AreaRepository areaRepository,
                                  AreaPlaceSegmentRepository areaPlaceSegmentRepository) {
        this.mapper = mapper;
        this.reservationRepository = reservationRepository;
        this.applicationUserRepository = applicationUserRepository;
        this.placeRepository = placeRepository;
        this.emailService = emailService;
        this.openingHoursRepository = openingHoursRepository;
        this.reservationPlaceRepository = reservationPlaceRepository;
        this.closedDayRepository = closedDayRepository;
        this.hashService = hashService;
        this.applicationUserService = applicationUserService;
        this.areaRepository = areaRepository;
        this.areaPlaceSegmentRepository = areaPlaceSegmentRepository;
    }

    @Override
    public ReservationCreateDto create(@Valid ReservationCreateDto reservationCreateDto) throws MessagingException {
        LOGGER.trace("create ({})", reservationCreateDto.toString());
        Set<ConstraintViolation<ReservationCreateDto>> reservationCreateDtoViolations = validator.validate(reservationCreateDto);
        if (!reservationCreateDtoViolations.isEmpty()) {
            throw new ConstraintViolationException(reservationCreateDtoViolations);
        }

        // 1. if in simple view, no end time is given, so we set it to 2 hours after start time by default
        if (reservationCreateDto.getEndTime() == null) {
            reservationCreateDto.setEndTime(reservationCreateDto.getStartTime().plusHours(2));
        }

        // 2. check if a table is still available (via getAvailability) since last check and set endTime to closingHour if necessary
        ReservationCheckAvailabilityDto reservationCheckAvailabilityDto =
            ReservationCheckAvailabilityDto.ReservationCheckAvailabilityDtoBuilder.aReservationCheckAvailabilityDto()
                .withDate(reservationCreateDto.getDate())
                .withStartTime(reservationCreateDto.getStartTime())
                .withEndTime(reservationCreateDto.getEndTime())
                .withPax(reservationCreateDto.getPax())
                .build();
        ReservationResponseEnum tableStatus = getAvailability(reservationCheckAvailabilityDto);
        if (tableStatus != ReservationResponseEnum.AVAILABLE) {
            return null; // frontend should check for null and show notification accordingly
        }

        // 3. Create guest if this is a guest-reservation, otherwise set known customer data
        ApplicationUser currentUser = applicationUserService.getCurrentApplicationUser();
        if (currentUser == null) {
            ApplicationUser guestUser = ApplicationUser.ApplicationUserBuilder.anApplicationUser()
                .withFirstName(reservationCreateDto.getFirstName().trim())
                .withLastName(reservationCreateDto.getLastName().trim())
                .withEmail(reservationCreateDto.getEmail().trim())
                .withMobileNumber(
                    reservationCreateDto.getMobileNumber() != null ? reservationCreateDto.getMobileNumber().trim() : reservationCreateDto.getMobileNumber())
                .withoutPassword()
                .withRole(RoleEnum.GUEST)
                .build();
            ApplicationUser savedGuestUser = applicationUserRepository.save(guestUser);
            reservationCreateDto.setUser(savedGuestUser);
        } else {
            reservationCreateDto.setFirstName(currentUser.getFirstName());
            reservationCreateDto.setLastName(currentUser.getLastName());
            reservationCreateDto.setEmail(currentUser.getEmail());
            reservationCreateDto.setMobileNumber(currentUser.getMobileNumber());
            reservationCreateDto.setUser(currentUser);
        }

        // 4. map to Reservation entity
        Reservation reservation = mapper.reservationCreateDtoToReservation(reservationCreateDto);
        reservation.setNotes(reservation.getNotes() != null ? reservation.getNotes().trim() : reservation.getNotes());

        // 5. Choose the place for reservation
        Place selectedPlace = null;
        if (reservationCreateDto.getPlaceId() != null) {
            Optional<Place> optionalPlace = placeRepository.findById(reservationCreateDto.getPlaceId());
            if (optionalPlace.isPresent()) {
                selectedPlace = optionalPlace.get();
            }
        }
        if (selectedPlace == null) {
            List<Place> places = placeRepository.findAll();
            List<Long> reservationIds =
                reservationRepository.findReservationsAtSpecifiedTime(reservationCheckAvailabilityDto.getDate(), reservationCheckAvailabilityDto.getStartTime(),
                    reservationCheckAvailabilityDto.getEndTime());
            List<Long> placeIds = reservationPlaceRepository.findPlaceIdsByReservationIds(reservationIds);
            places.removeAll(placeRepository.findAllById(placeIds));
            if (places.isEmpty()) {
                return null; // No available places
            }
            selectedPlace = places.get(0);
        }

        // TODO: add Restaurant name to DTO

        String hashedValue = hashService.hashSha256(reservation.getDate().toString()
            + reservation.getStartTime().toString() + reservation.getEndTime().toString()
            + reservation.getPax().toString());
        reservation.setHashValue(hashedValue);

        // 6. save Reservation in database and return it mapped to a DTO
        Reservation savedReservation = reservationRepository.save(reservation);
        // Validate the Reservation object via javax.validation using the annotations in the entity class
        Set<ConstraintViolation<Reservation>> reservationViolations = validator.validate(savedReservation);
        if (!reservationViolations.isEmpty()) {
            throw new ConstraintViolationException(reservationViolations);
        }

        // 7. send conformation Mail
        Map<String, Object> templateModel = constructMailTemplateModel(savedReservation, reservationCreateDto.getUser());
        emailService.sendMessageUsingThymeleafTemplate(reservationCreateDto.getUser().getEmail(),
            "Reservation Confirmation", templateModel);

        // 8. save ReservationPlace in database
        ReservationPlace reservationPlace = ReservationPlace.ReservationPlaceBuilder.aReservationPlace()
            .withReservation(savedReservation)
            .withPlace(selectedPlace)
            .build();
        reservationPlaceRepository.save(reservationPlace);

        return mapper.reservationToReservationCreateDto(savedReservation);
    }


    @Override
    public ReservationResponseEnum getAvailability(ReservationCheckAvailabilityDto reservationCheckAvailabilityDto) {
        LOGGER.trace("getAvailability ({})", reservationCheckAvailabilityDto.toString());
        // reservationValidator.validateReservationCheckAvailabilityDto(reservationCheckAvailabilityDto);
        LocalDate date = reservationCheckAvailabilityDto.getDate();
        LocalTime startTime = reservationCheckAvailabilityDto.getStartTime();
        LocalTime endTime = reservationCheckAvailabilityDto.getEndTime();

        // 1. set endTime depending on context
        // a. if in simple view, no end time is given so we set it to 2 hours after start time by default
        if (endTime == null) {
            endTime = reservationCheckAvailabilityDto.getStartTime().plusHours(2);
        }
        // b. if endTime is after midnight, before 6am, set it to just before midnight to guarantee check-safety
        if (!endTime.isBefore(LocalTime.of(0, 0))
            && endTime.isBefore(LocalTime.of(6, 0))
            && startTime.isBefore(LocalTime.of(23, 59))
            && startTime.isAfter(LocalTime.of(6, 0))) {
            endTime = LocalTime.of(23, 59);
        }

        // 2. check if date is in the past
        if (date.isBefore(LocalDate.now())) {
            return ReservationResponseEnum.DATE_IN_PAST;
        }

        // 3. check if restaurant is open at specified date
        Optional<ClosedDay> optionalClosedDay = closedDayRepository.findByDate(date);
        if (optionalClosedDay.isPresent()) {
            return ReservationResponseEnum.CLOSED;
        }

        // 4. check if reservation is on regular closed day of the week
        boolean isOpen = false;
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        List<OpeningHours> openingHoursList = openingHoursRepository.findByDayOfWeek(dayOfWeek);
        if (openingHoursList.isEmpty()) {
            return ReservationResponseEnum.CLOSED; // if one weekday is always closed, this should return this enum instead of OUTSIDE_OPENING_HOURS
        }

        // 5. check if reservation is within opening hours
        for (OpeningHours openingHours : openingHoursList) {
            if (!startTime.isAfter(openingHours.getClosingTime())
                && !startTime.isBefore(openingHours.getOpeningTime())
                && !endTime.isBefore(openingHours.getOpeningTime())) {
                if (!startTime.plusHours(1).isAfter(openingHours.getClosingTime())) {
                    isOpen = true;
                    if (!endTime.isBefore(openingHours.getClosingTime())) {
                        reservationCheckAvailabilityDto.setEndTime(openingHours.getClosingTime());
                    }
                    break;
                } else {
                    return ReservationResponseEnum.RESPECT_CLOSING_HOUR;
                }
            }
        }
        if (!isOpen) {
            return ReservationResponseEnum.OUTSIDE_OPENING_HOURS;
        }

        // 6. check if any places are available for specified time
        List<Place> places = placeRepository.findAll();
        List<Long> reservationIds =
            reservationRepository.findReservationsAtSpecifiedTime(reservationCheckAvailabilityDto.getDate(), reservationCheckAvailabilityDto.getStartTime(),
                reservationCheckAvailabilityDto.getEndTime());
        List<Long> placeIds = reservationPlaceRepository.findPlaceIdsByReservationIds(reservationIds);
        places.removeAll(placeRepository.findAllById(placeIds));
        if (places.isEmpty()) {
            return ReservationResponseEnum.ALL_OCCUPIED;
        }

        // 7. check if any available table can host pax
        boolean isBigEnough = false;
        for (Place place : places) {
            if (place.getPax() >= reservationCheckAvailabilityDto.getPax()) {
                isBigEnough = true;
                break;
            }
        }
        if (!isBigEnough) {
            return ReservationResponseEnum.TOO_MANY_PAX;
        }

        // 8. if no check succeeded, return available
        return ReservationResponseEnum.AVAILABLE;
    }

    @Override
    public ReservationCheckAvailabilityDto[] getNextAvailableTables(ReservationCheckAvailabilityDto reservationCheckAvailabilityDto) {
        LOGGER.trace("getNextAvailableTables ({})", reservationCheckAvailabilityDto.toString());

        LocalDate date = reservationCheckAvailabilityDto.getDate();
        LocalTime startTime = reservationCheckAvailabilityDto.getStartTime();
        LocalTime endTime = reservationCheckAvailabilityDto.getEndTime();

        LocalDateTime start = LocalDateTime.of(date, startTime);
        LocalDateTime end;
        if (endTime == null) {
            end = start.plusHours(2);
        } else if (endTime.isBefore(startTime)) {
            end = LocalDateTime.of(date.plusDays(1), endTime);
        } else {
            end = LocalDateTime.of(date, endTime);
        }

        long duration = ChronoUnit.MINUTES.between(start, end);

        DayOfWeek dayOfWeek = date.getDayOfWeek();
        List<OpeningHours> openingHoursList = openingHoursRepository.findByDayOfWeek(dayOfWeek);
        if (openingHoursList.isEmpty()) {
            return new ReservationCheckAvailabilityDto[0];
        }

        LocalTime latest = openingHoursList.getFirst().getClosingTime();
        for (OpeningHours openingHours : openingHoursList) {
            if (openingHours.getClosingTime().isAfter(latest)) {
                latest = openingHours.getClosingTime();
            }
        }

        LocalDateTime endOfDay = LocalDateTime.of(date, latest);

        LocalDateTime tryStart = roundToNearest15Minutes(start).minusMinutes(60);

        List<ReservationCheckAvailabilityDto> nextTables = new ArrayList<>();

        while (nextTables.size() < 3 && tryStart.isBefore(endOfDay)) {
            LocalDateTime tryEnd = tryStart.plusMinutes(duration);
            ReservationCheckAvailabilityDto newReservationCheckAvailabilityDto =
                ReservationCheckAvailabilityDto.ReservationCheckAvailabilityDtoBuilder.aReservationCheckAvailabilityDto()
                    .withDate(tryStart.toLocalDate())
                    .withStartTime(tryStart.toLocalTime())
                    .withEndTime(tryEnd.toLocalTime())
                    .withPax(reservationCheckAvailabilityDto.getPax())
                    .build();
            ReservationResponseEnum tableStatus = getAvailability(newReservationCheckAvailabilityDto);

            if (tableStatus == ReservationResponseEnum.AVAILABLE) {
                nextTables.add(newReservationCheckAvailabilityDto);
            }

            tryStart = tryStart.plusMinutes(30);
        }

        return nextTables.toArray(new ReservationCheckAvailabilityDto[0]);
    }

    private LocalDateTime roundToNearest15Minutes(LocalDateTime time) {
        int minute = time.getMinute();
        int roundedMinute = (minute + 7) / 15 * 15;
        if (roundedMinute >= 60) {
            time = time.plusHours(1).withMinute(0);
        } else {
            time = time.withMinute(roundedMinute);
        }
        return time;
    }

    @Override
    public ReservationEditDto getByHashedId(String hashValue) throws NotFoundException {
        LOGGER.trace("getDetail ({})", hashValue);

        // TODO: this should not be a list, but a single reservation
        List<Reservation> reservationList = reservationRepository.findByHashValue(hashValue);

        if (reservationList.size() != 1) {
            throw new NotFoundException("Reservation with not found");
        }

        Reservation reservation = reservationList.getFirst();
        ApplicationUser currentUser = applicationUserService.getCurrentApplicationUser();

        if (currentUser == null || (!currentUser.getRole().equals(RoleEnum.ADMIN) && !currentUser.getRole().equals(RoleEnum.EMPLOYEE))) {
            if (!reservation.getApplicationUser().equals(currentUser)) {
                throw new AccessDeniedException("You are not allowed to view this reservation");
            }
        }

        // fetch places for reservation
        ReservationEditDto reservationEditDto = mapper.reservationToReservationEditDto(reservation);
        List<Long> reservationIds = Collections.singletonList(reservation.getId());
        List<Long> reservationPlaces = reservationPlaceRepository.findPlaceIdsByReservationIds(reservationIds);
        reservationEditDto.setPlaceIds(reservationPlaces);
        return reservationEditDto;
    }

    @Override
    public ReservationEditDto update(ReservationEditDto reservationEditDto) {
        LOGGER.trace("update ({})", reservationEditDto.toString());

        // 1. check if reservation exists and if so, fetch its data
        Optional<Reservation> optionalReservation = reservationRepository.findById(reservationEditDto.getReservationId());
        if (optionalReservation.isEmpty()) {
            // TODO: throw a fitting exception (create a new exception ideally)
            LOGGER.error("Reservation with id " + reservationEditDto.getReservationId() + " not found"); // TODO: remove after testing
            return null;
        }
        Reservation reservation = optionalReservation.get();

        // 2. check if current user is allowed to update this reservation
        ApplicationUser currentUser = reservationEditDto.getUser();
        if (currentUser == null || currentUser != reservation.getApplicationUser()) {
            // TODO: throw a fitting exception (create a new exception ideally)
        }

        // 3. update reservation data and save it in DB
        reservation.setNotes(reservationEditDto.getNotes());
        reservation.setPax(reservationEditDto.getPax());
        reservation.setStartTime(reservationEditDto.getStartTime());
        reservation.setEndTime(reservationEditDto.getEndTime());
        reservation.setDate(reservationEditDto.getDate());
        reservation.setHashValue(hashService.hashSha256(reservation.getDate().toString()
            + reservation.getStartTime().toString() + reservation.getEndTime().toString()
            + reservation.getPax().toString()));
        Reservation updatedReservation = reservationRepository.save(reservation);
        // Validate the Reservation object via javax.validation using the annotations in the entity class
        Set<ConstraintViolation<Reservation>> violations = validator.validate(updatedReservation);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        // 4. update user data if user is a guest
        assert currentUser != null;
        if (currentUser.getRole().equals(RoleEnum.GUEST)) {
            applicationUserRepository.save(currentUser);
        }

        // 5. update reservation places
        reservationPlaceRepository.deleteByReservationId(updatedReservation.getId());
        for (Long placeId : reservationEditDto.getPlaceIds()) {
            Optional<Place> optionalPlace = placeRepository.findById(placeId);
            if (optionalPlace.isPresent()) {
                ReservationPlace newReservationPlace = ReservationPlace.ReservationPlaceBuilder.aReservationPlace()
                    .withReservation(updatedReservation)
                    .withPlace(optionalPlace.get())
                    .build();
                reservationPlaceRepository.save(newReservationPlace);
            } else {
                // TODO: Handle the case where no place with the given ID exists.
            }
        }

        // 6. send confirmation mail
        Map<String, Object> templateModel = constructMailTemplateModel(updatedReservation, currentUser);
        try {
            emailService.sendUpdateMessageUsingThymeleafTemplate(currentUser.getEmail(),
                "Reservation Update Confirmation", templateModel);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        // 7. map updated reservation to DTO and return it
        ReservationEditDto dto = mapper.reservationToReservationEditDto(updatedReservation);
        List<ReservationPlace> reservationPlaces = reservationPlaceRepository.findByReservationId(updatedReservation.getId());
        List<Long> placeIds = reservationPlaces.stream()
            .map(reservationPlace -> reservationPlace.getPlace().getId())
            .collect(Collectors.toList());
        dto.setPlaceIds(placeIds);

        LOGGER.debug("Updated reservation: {}", dto.toString());

        return dto;
    }

    @Override
    public List<ReservationListDto> search(ReservationSearchDto reservationSearchDto) {
        LOGGER.trace("search ({})", reservationSearchDto.toString());

        if (applicationUserService.getCurrentApplicationUser().getRole().equals(RoleEnum.ADMIN)
            || applicationUserService.getCurrentApplicationUser().getRole().equals(RoleEnum.EMPLOYEE)) {
            List<Reservation> reservations = reservationRepository.findReservationsWithoutUserId(reservationSearchDto.getEarliestDate(),
                reservationSearchDto.getLatestDate(), reservationSearchDto.getEarliestStartTime(), reservationSearchDto.getLatestEndTime());
            LOGGER.debug("Found {} reservations for the given params", reservations.size());
            return mapper.reservationToReservationListDto(reservations);
        }
        String email = applicationUserService.getCurrentUserAuthentication().getName();
        List<Reservation> reservations = reservationRepository.findReservationsByDate(email, reservationSearchDto.getEarliestDate(),
            reservationSearchDto.getLatestDate(), reservationSearchDto.getEarliestStartTime(), reservationSearchDto.getLatestEndTime());
        LOGGER.debug("Found {} reservations for the given params", reservations.size());
        return mapper.reservationToReservationListDto(reservations);
    }

    @Override
    public void cancel(String hashId) {
        ReservationEditDto reservationEditDto = getByHashedId(hashId);
        Long id = reservationEditDto.getReservationId();
        LOGGER.trace("cancel ({})", id);

        // TODO: check if ID is positive, otherwise return a fitting exception for notification handler

        //fetch reservation
        Optional<Reservation> optionalReservation = reservationRepository.findById(id);
        if (optionalReservation.isEmpty()) {
            throw new NotFoundException("Reservation not found", null);
        }
        Reservation reservation = optionalReservation.get();

        //delete entries in join table
        reservationPlaceRepository.deleteByReservationId(reservation.getId());

        //delete reservation
        reservationRepository.deleteById(reservation.getId());

        //fetch reservation user
        ApplicationUser currentUser = reservation.getApplicationUser();

        //send confirmation mail
        Map<String, Object> templateModel = constructMailTemplateModel(reservation, currentUser);
        try {
            emailService.sendCancellationMessageUsingThymeleafTemplate(currentUser.getEmail(),
                "Cancellation Confirmation", templateModel);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        LOGGER.debug("Canceled reservation: {}", id);
    }

    @Override
    @Transactional
    public AreaLayoutDto getAreaLayout(ReservationLayoutCheckAvailabilityDto dto) {
        var area = areaRepository.getReferenceById(dto.getAreaId());

        List<AreaPlaceSegment> areaPlaceSegments = areaPlaceSegmentRepository.findByAreaId(area.getId());
        List<Long> placeIds = areaPlaceSegments.stream().map(aps -> aps.getPlace().getId()).collect(Collectors.toList());
        List<Place> places = placeRepository.findAllById(placeIds);
        List<Long> reservationIds = reservationRepository.findReservationsAtSpecifiedTime(
            dto.getDate(), dto.getStartTime(), dto.getEndTime());
        List<Long> reservedPlaceIds = reservationPlaceRepository.findPlaceIdsByReservationIds(reservationIds);

        List<AreaLayoutDto.PlaceVisualDto> placeVisuals = places.stream().map(place -> {
            AreaLayoutDto.PlaceVisualDto placeVisual = new AreaLayoutDto.PlaceVisualDto();
            placeVisual.setPlaceId(place.getId());
            placeVisual.setStatus(place.getStatus() == StatusEnum.AVAILABLE);
            placeVisual.setReservation(reservedPlaceIds.contains(place.getId()));
            placeVisual.setNumberOfSeats(place.getPax());

            List<Segment> segments = areaPlaceSegments.stream()
                .filter(aps -> aps.getPlace().getId().equals(place.getId()))
                .map(AreaPlaceSegment::getSegment)
                .toList();
            List<AreaLayoutDto.PlaceVisualDto.CoordinateDto> coordinates = segments.stream().map(segment -> {
                AreaLayoutDto.PlaceVisualDto.CoordinateDto coordinate = new AreaLayoutDto.PlaceVisualDto.CoordinateDto();
                coordinate.setX(segment.getX());
                coordinate.setY(segment.getY());
                return coordinate;
            }).collect(Collectors.toList());

            placeVisual.setCoordinates(coordinates);
            return placeVisual;
        }).collect(Collectors.toList());

        AreaLayoutDto areaLayoutDto = new AreaLayoutDto();
        areaLayoutDto.setWidth(area.getWidth());
        areaLayoutDto.setHeight(area.getHeight());
        areaLayoutDto.setPlaceVisuals(placeVisuals);
        LOGGER.debug("Built areaLayout: {}", areaLayoutDto.toString());

        return areaLayoutDto;
    }

    private Map<String, Object> constructMailTemplateModel(Reservation reservation, ApplicationUser currentUser) {
        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("recipientName", currentUser.getFirstName() + " " + currentUser.getLastName());
        templateModel.put("text", reservation.getNotes());
        templateModel.put("persons", reservation.getPax());
        templateModel.put("restaurantName", "--SpotterEssen--"); //TODO: change to restaurant name
        templateModel.put("reservationDate", reservation.getDate());
        templateModel.put("reservationTime", reservation.getStartTime());
        templateModel.put("link", "http://localhost:4200/#/reservation-detail/" + reservation.getHashValue()); //TODO: change away from localhost
        return templateModel;
    }
}