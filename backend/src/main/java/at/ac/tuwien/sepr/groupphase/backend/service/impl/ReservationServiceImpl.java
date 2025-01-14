package at.ac.tuwien.sepr.groupphase.backend.service.impl;

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
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.SpecialOfferAmountDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.SpecialOfferListDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepr.groupphase.backend.entity.OpeningHours;
import at.ac.tuwien.sepr.groupphase.backend.entity.PermanentReservation;
import at.ac.tuwien.sepr.groupphase.backend.entity.PermanentReservationMapper;
import at.ac.tuwien.sepr.groupphase.backend.entity.Place;
import at.ac.tuwien.sepr.groupphase.backend.entity.Reservation;
import at.ac.tuwien.sepr.groupphase.backend.entity.ReservationOffer;
import at.ac.tuwien.sepr.groupphase.backend.entity.ReservationPlace;
import at.ac.tuwien.sepr.groupphase.backend.enums.RepetitionEnum;
import at.ac.tuwien.sepr.groupphase.backend.entity.SpecialOffer;
import at.ac.tuwien.sepr.groupphase.backend.enums.ReservationResponseEnum;
import at.ac.tuwien.sepr.groupphase.backend.enums.RoleEnum;
import at.ac.tuwien.sepr.groupphase.backend.enums.StatusEnum;
import at.ac.tuwien.sepr.groupphase.backend.exception.ConflictException;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.repository.ApplicationUserRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.OpeningHoursRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.PermanentReservationMapperRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.PermanentReservationRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.PlaceRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.ReservationOfferRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.ReservationPlaceRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.ReservationRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.SpecialOfferRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.EmailService;
import at.ac.tuwien.sepr.groupphase.backend.service.HashService;
import at.ac.tuwien.sepr.groupphase.backend.service.ReservationService;
import at.ac.tuwien.sepr.groupphase.backend.service.mapper.ReservationMapper;
import jakarta.mail.MessagingException;
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
import java.util.Objects;
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
    private final ReservationPlaceRepository reservationPlaceRepository;
    private final EmailService emailService;
    private final HashService hashService;
    private final ApplicationUserServiceImpl applicationUserService;
    private final PermanentReservationRepository permanentReservationRepository;
    private final PermanentReservationMapperRepository permanentReservationMapperRepository;
    private final SpecialOfferRepository specialOfferRepository;
    private final ReservationOfferRepository reservationOfferRepository;

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
                                  PermanentReservationRepository permanentReservationRepository,
                                  PermanentReservationMapperRepository permanentReservationMapperRepository,
                                  HashService hashService,
                                  ApplicationUserServiceImpl applicationUserService,
                                  SpecialOfferRepository specialOfferRepository,
                                  ReservationOfferRepository reservationOfferRepository) {
        this.mapper = mapper;
        this.reservationRepository = reservationRepository;
        this.applicationUserRepository = applicationUserRepository;
        this.placeRepository = placeRepository;
        this.emailService = emailService;
        this.openingHoursRepository = openingHoursRepository;
        this.reservationPlaceRepository = reservationPlaceRepository;
        this.permanentReservationRepository = permanentReservationRepository;
        this.permanentReservationMapperRepository = permanentReservationMapperRepository;
        this.hashService = hashService;
        this.applicationUserService = applicationUserService;
        this.specialOfferRepository = specialOfferRepository;
        this.reservationOfferRepository = reservationOfferRepository;
    }

    @Override
    public ReservationCreateDto create(@Valid ReservationCreateDto reservationCreateDto) throws MessagingException {
        LOGGER.trace("create ({})", reservationCreateDto.toString());
        if (reservationCreateDto.getEndTime() == null) { // 1. if in simple view, no end time is given, so we set it to 2 hours after start time by default
            reservationCreateDto.setEndTime(reservationCreateDto.getStartTime().plusHours(2));
        }

        // 2. Check if all specified tables are available
        if (reservationCreateDto.getPlaceIds() != null && !reservationCreateDto.getPlaceIds().isEmpty()) {
            for (Long placeId : reservationCreateDto.getPlaceIds()) {
                ReservationCheckAvailabilityDto reservationCheckAvailabilityDto = ReservationCheckAvailabilityDto.ReservationCheckAvailabilityDtoBuilder.aReservationCheckAvailabilityDto()
                    .withDate(reservationCreateDto.getDate())
                    .withStartTime(reservationCreateDto.getStartTime())
                    .withEndTime(reservationCreateDto.getEndTime())
                    .withPax(reservationCreateDto.getPax()).withIdToExclude(null) // Assuming we're not excluding any reservations
                    .build();
                if (!isPlaceAvailable(placeId)) {
                    return null; // frontend checks and notifies
                }
            }
        } else {
            // If no place IDs are provided, perform the default availability check
            ReservationCheckAvailabilityDto reservationCheckAvailabilityDto = ReservationCheckAvailabilityDto.ReservationCheckAvailabilityDtoBuilder.aReservationCheckAvailabilityDto()
                .withDate(reservationCreateDto.getDate())
                .withStartTime(reservationCreateDto.getStartTime())
                .withEndTime(reservationCreateDto.getEndTime())
                .withPax(reservationCreateDto.getPax()).withIdToExclude(null) // Assuming we're not excluding any reservations
                .build();
            ReservationResponseEnum tableStatus = getAvailability(reservationCheckAvailabilityDto);
            if (tableStatus != ReservationResponseEnum.AVAILABLE) {
                return null; // frontend should check for null and show notification accordingly
            }
        }

        // 3. Create guest if this is a guest-reservation, otherwise set known customer data
        ApplicationUser currentUser = applicationUserService.getCurrentApplicationUser();
        if (currentUser == null || (!Objects.equals(currentUser.getEmail(), reservationCreateDto.getEmail()) && (currentUser.getRole().equals(RoleEnum.ADMIN) || currentUser.getRole().equals(RoleEnum.EMPLOYEE)))) {
            ApplicationUser guestUser = ApplicationUser.ApplicationUserBuilder.anApplicationUser()
                .withFirstName(reservationCreateDto.getFirstName().trim())
                .withLastName(reservationCreateDto.getLastName().trim())
                .withEmail(reservationCreateDto.getEmail().trim())
                .withMobileNumber(reservationCreateDto.getMobileNumber() != null ? reservationCreateDto.getMobileNumber().trim() : reservationCreateDto.getMobileNumber())
                .withoutPassword().withRole(RoleEnum.GUEST).build();
            ApplicationUser savedGuestUser = applicationUserRepository.save(guestUser);
            reservationCreateDto.setUser(savedGuestUser);
        } else {
            reservationCreateDto.setFirstName(currentUser.getFirstName());
            reservationCreateDto.setLastName(currentUser.getLastName());
            reservationCreateDto.setEmail(currentUser.getEmail());
            reservationCreateDto.setMobileNumber(currentUser.getMobileNumber());
            reservationCreateDto.setUser(currentUser);
        }

        // 4. Map to Reservation entity
        Reservation reservation = mapper.reservationCreateDtoToReservation(reservationCreateDto);
        reservation.setNotes(reservation.getNotes() != null ? reservation.getNotes().trim() : reservation.getNotes());
        reservation.setConfirmed(false);

        // 5. Choose the places for reservation
        List<Place> selectedPlaces = new ArrayList<>();
        if (reservationCreateDto.getPlaceIds() != null && !reservationCreateDto.getPlaceIds().isEmpty()) {
            for (Long placeId : reservationCreateDto.getPlaceIds()) {
                Optional<Place> optionalPlace = placeRepository.findById(placeId);
                if (optionalPlace.isPresent()) {
                    selectedPlaces.add(optionalPlace.get());
                }
            }
            // If any specified place is not found, return null
            if (selectedPlaces.size() != reservationCreateDto.getPlaceIds().size()) {
                return null;
            }
        } else {
            List<Place> places = placeRepository.findAll();
            List<Long> reservationIds = reservationRepository.findReservationsAtSpecifiedTime(reservationCreateDto.getDate(),
                reservationCreateDto.getStartTime(), reservationCreateDto.getEndTime());
            List<Long> placeIds = reservationPlaceRepository.findPlaceIdsByReservationIds(reservationIds);
            places.removeAll(placeRepository.findAllById(placeIds));
            if (places.isEmpty()) {
                return null; // No available places
            }
            selectedPlaces.add(places.get(0));
        }

        String hashedValue = hashService.hashSha256(reservation.getDate().toString() + reservation.getStartTime().toString()
            + reservation.getEndTime().toString() + reservation.getPax().toString()) + reservation.isConfirmed();
        reservation.setHashValue(hashedValue);

        // 6. Save Reservation in database and return it mapped to a DTO
        Reservation savedReservation = reservationRepository.save(reservation);

        // Add special offers if there are any
        if (reservationCreateDto.getSpecialOffers() != null && !reservationCreateDto.getSpecialOffers().isEmpty()) {
            // Map special offers to ReservationOffers
            for (SpecialOfferAmountDto specialOfferAmountDto : reservationCreateDto.getSpecialOffers()) {
                Optional<SpecialOffer> specialOffer = specialOfferRepository.findById(specialOfferAmountDto.getSpecialOffer().getId());
                if (specialOffer.isPresent()) {
                    ReservationOffer reservationOffer = ReservationOffer.ReservationOfferBuilder.aReservationOffer()
                        .withReservation(savedReservation)
                        .withOffer(specialOffer.get())
                        .withAmount(specialOfferAmountDto.getAmount())
                        .build();
                    reservationOfferRepository.save(reservationOffer);
                } else {
                    throw new NotFoundException("The chosen Special offer was not found");
                }
            }
        }
        // Validate after changes being made as an additional layer of defense
        Set<ConstraintViolation<Reservation>> reservationViolations = validator.validate(savedReservation);
        if (!reservationViolations.isEmpty()) {
            throw new ConstraintViolationException(reservationViolations);
        }

        // 7. Send confirmation Mail
        Map<String, Object> templateModel = constructMailTemplateModel(savedReservation, reservationCreateDto.getUser());
        emailService.sendMessageUsingThymeleafTemplate(reservationCreateDto.getUser().getEmail(), "Reservation Confirmation",
            templateModel);

        // 8. Save ReservationPlace for all selected places in the database
        for (Place place : selectedPlaces) {
            ReservationPlace reservationPlace = ReservationPlace.ReservationPlaceBuilder.aReservationPlace().withReservation(savedReservation).withPlace(place).build();
            reservationPlaceRepository.save(reservationPlace);
        }

        return mapper.reservationToReservationCreateDto(savedReservation);
    }

    private boolean isPlaceAvailable(Long placeId) {
        StatusEnum placeStatus = placeRepository.findStatusById(placeId);
        return placeStatus == StatusEnum.AVAILABLE;
    }

    @Override
    public ReservationResponseEnum getAvailability(ReservationCheckAvailabilityDto reservationCheckAvailabilityDto) {
        LOGGER.trace("getAvailability ({})", reservationCheckAvailabilityDto.toString());
        LocalDate date = reservationCheckAvailabilityDto.getDate();
        LocalTime startTime = reservationCheckAvailabilityDto.getStartTime();
        LocalTime endTime = reservationCheckAvailabilityDto.getEndTime();

        // 1. set endTime depending on context
        // a. if in simple view, no end time is given so we set it to 2 hours after start time by default
        if (endTime == null) {
            endTime = reservationCheckAvailabilityDto.getStartTime().plusHours(2);
        }
        // b. if endTime is after midnight, before 6am, set it to just before midnight to guarantee check-safety
        if (!endTime.isBefore(LocalTime.of(0, 0)) && endTime.isBefore(LocalTime.of(6, 0))
            && startTime.isBefore(LocalTime.of(23, 59)) && startTime.isAfter(LocalTime.of(6, 0))) {
            endTime = LocalTime.of(23, 59);
        }

        // 2. check if date is in the past
        if (date.isBefore(LocalDate.now())) {
            return ReservationResponseEnum.DATE_IN_PAST;
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
            if (!startTime.isAfter(openingHours.getClosingTime()) && !startTime.isBefore(openingHours.getOpeningTime())
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
        List<Long> reservationIds = reservationRepository.findReservationsAtSpecifiedTime(reservationCheckAvailabilityDto.getDate(),
            reservationCheckAvailabilityDto.getStartTime(), reservationCheckAvailabilityDto.getEndTime());
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
                    .withPax(reservationCheckAvailabilityDto.getPax()).build();
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

        ReservationEditDto reservationEditDto = mapper.reservationToReservationEditDto(reservation);

        // fetch ReservationOffers
        List<ReservationOffer> reservationOffers = reservationOfferRepository.findByReservationId(reservation.getId());
        List<SpecialOfferAmountDto> specialOffers = new ArrayList<>();
        for (ReservationOffer reservationOffer : reservationOffers) {
            SpecialOfferListDto specialOfferListDto = mapper.specialOfferToSpecialOfferListDto(reservationOffer.getOffer());
            SpecialOfferAmountDto specialOfferAmountDto =
                SpecialOfferAmountDto.SpecialOfferAmountDtoBuilder.aSpecialOfferAmountDto()
                    .withSpecialOffer(specialOfferListDto)
                    .withAmount(reservationOffer.getAmount())
                    .build();
            specialOffers.add(specialOfferAmountDto);
        }
        reservationEditDto.setSpecialOffers(specialOffers);

        // fetch places for reservation
        List<Long> reservationIds = Collections.singletonList(reservation.getId());
        List<Long> reservationPlaces = reservationPlaceRepository.findPlaceIdsByReservationIds(reservationIds);
        reservationEditDto.setPlaceIds(reservationPlaces);

        return reservationEditDto;
    }

    @Override
    public ReservationModalDetailDto getModalDetail(String hashValue) throws NotFoundException {
        LOGGER.trace("getModalDetail ({})", hashValue);

        List<Reservation> reservations = reservationRepository.findByHashValue(hashValue);

        if (reservations.size() != 1) {
            throw new NotFoundException("Reservation with not found");
        }

        Reservation reservation = reservations.getFirst();
        List<Long> placeIds = reservationPlaceRepository.findPlaceIdsByReservationIds(Collections.singletonList(reservation.getId()));
        ReservationModalDetailDto reservationModalDetailDto = mapper.reservationToReservationModalDetailDto(reservation, placeIds);

        //go through the Reservation Offers and add the special offers to the list
        List<ReservationOffer> reservationOffers = reservationOfferRepository.findByReservationId(reservation.getId());
        List<SpecialOfferAmountDto> specialOffers = new ArrayList<>();
        for (ReservationOffer reservationOffer : reservationOffers) {
            SpecialOfferListDto specialOfferListDto = mapper.specialOfferToSpecialOfferListDto(reservationOffer.getOffer());
            SpecialOfferAmountDto specialOfferAmountDto =
                SpecialOfferAmountDto.SpecialOfferAmountDtoBuilder.aSpecialOfferAmountDto()
                    .withSpecialOffer(specialOfferListDto)
                    .withAmount(reservationOffer.getAmount())
                    .build();
            specialOffers.add(specialOfferAmountDto);
        }

        reservationModalDetailDto.setSpecialOffers(specialOffers);

        LOGGER.debug("Found reservation: {}", reservationModalDetailDto.toString());

        return reservationModalDetailDto;
    }

    // TODO: find reservation to update via hash?
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
            + reservation.getStartTime().toString() + reservation.getEndTime().toString() + reservation.getPax().toString()));
        Reservation updatedReservation = reservationRepository.save(reservation);

        // Validate after changes being made as an additional layer of defense
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
                ReservationPlace newReservationPlace = ReservationPlace.ReservationPlaceBuilder.aReservationPlace().withReservation(updatedReservation)
                    .withPlace(optionalPlace.get()).build();
                reservationPlaceRepository.save(newReservationPlace);
            } else {
                // TODO: Handle the case where no place with the given ID exists.
            }
        }

        // update special offers
        List<ReservationOffer> reservationOffers = reservationOfferRepository.findByReservationId(updatedReservation.getId());
        reservationOfferRepository.deleteAll(reservationOffers);
        if (reservationEditDto.getSpecialOffers() != null && !reservationEditDto.getSpecialOffers().isEmpty()) {
            Map<Long, Integer> specialOfferAmounts = new HashMap<>();
            for (SpecialOfferAmountDto specialOffer : reservationEditDto.getSpecialOffers()) {
                specialOfferAmounts.put(specialOffer.getSpecialOffer().getId(), specialOffer.getAmount());
            }
            for (Map.Entry<Long, Integer> entry : specialOfferAmounts.entrySet()) {
                Optional<SpecialOffer> specialOffer = specialOfferRepository.findById(entry.getKey());
                if (specialOffer.isPresent()) {
                    ReservationOffer reservationOffer = ReservationOffer.ReservationOfferBuilder.aReservationOffer()
                        .withReservation(updatedReservation)
                        .withOffer(specialOffer.get())
                        .withAmount(entry.getValue())
                        .build();
                    reservationOfferRepository.save(reservationOffer);
                } else {
                    throw new NotFoundException("Special offer not found");
                }
            }
        }

        // 6. send confirmation mail
        Map<String, Object> templateModel = constructMailTemplateModel(updatedReservation, currentUser);
        try {
            emailService.sendUpdateMessageUsingThymeleafTemplate(currentUser.getEmail(), "Reservation Update Confirmation", templateModel);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }


        // 7. map updated reservation to DTO and return it
        ReservationEditDto dto = mapper.reservationToReservationEditDto(updatedReservation);
        List<ReservationPlace> reservationPlaces = reservationPlaceRepository.findByReservationId(updatedReservation.getId());
        List<Long> placeIds = reservationPlaces.stream().map(reservationPlace -> reservationPlace.getPlace().getId()).collect(Collectors.toList());
        dto.setPlaceIds(placeIds);

        LOGGER.debug("Updated reservation: {}", dto.toString());

        return dto;
    }

    @Override
    public List<ReservationListDto> search(ReservationSearchDto reservationSearchDto) {
        LOGGER.trace("search ({})", reservationSearchDto.toString());

        List<ReservationListDto> reservationListDtos = new ArrayList<>();

        if (applicationUserService.getCurrentApplicationUser().getRole().equals(RoleEnum.ADMIN) || applicationUserService.getCurrentApplicationUser().getRole().equals(RoleEnum.EMPLOYEE)) {
            List<Reservation> reservations = reservationRepository.findReservationsWithoutUserId(reservationSearchDto.getEarliestDate(),
                reservationSearchDto.getLatestDate(), reservationSearchDto.getEarliestStartTime(), reservationSearchDto.getLatestEndTime());
            for (Reservation reservation : reservations) {
                List<Long> placeIds = reservationPlaceRepository.findPlaceIdsByReservationIds(Collections.singletonList(reservation.getId()));
                reservationListDtos.add(mapper.reservationToReservationListDto(reservation, placeIds));
            }
            LOGGER.debug("Found {} reservations for the given params", reservations.size());
            return reservationListDtos;
        }
        String email = applicationUserService.getCurrentUserAuthentication().getName();
        List<Reservation> reservations = reservationRepository.findReservationsByDate(email, reservationSearchDto.getEarliestDate(), reservationSearchDto.getLatestDate(),
            reservationSearchDto.getEarliestStartTime(), reservationSearchDto.getLatestEndTime());
        LOGGER.debug("Found {} reservations for the given params", reservations.size());
        for (Reservation reservation : reservations) {
            List<Long> placeIds = reservationPlaceRepository.findPlaceIdsByReservationIds(Collections.singletonList(reservation.getId()));
            LOGGER.debug("Found {} places for reservation {}", placeIds.size(), reservation.getId());
            ReservationListDto dto = mapper.reservationToReservationListDto(reservation, placeIds);
            LOGGER.debug("Mapped reservation to DTO: {}", dto.toString());
            reservationListDtos.add(mapper.reservationToReservationListDto(reservation, placeIds));
        }
        return reservationListDtos;
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
        permanentReservationMapperRepository.deleteByReservationId(reservation.getId());

        //delete reservation
        reservationRepository.deleteById(reservation.getId());

        //fetch reservation user
        ApplicationUser currentUser = reservation.getApplicationUser();

        //send confirmation mail
        Map<String, Object> templateModel = constructMailTemplateModel(reservation, currentUser);
        try {
            emailService.sendCancellationMessageUsingThymeleafTemplate(currentUser.getEmail(), "Cancellation Confirmation", templateModel);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        LOGGER.debug("Canceled reservation: {}", id);
    }


    @Override
    public ReservationCreateDto createWalkIn(ReservationWalkInDto reservationWalkInDto) throws ConflictException, MessagingException {
        LOGGER.trace("createWalkIn ({})", reservationWalkInDto.toString());
        List<ApplicationUser> walkInUserList = applicationUserRepository.findAll();
        ApplicationUser walkInUser = null;
        for (ApplicationUser user : walkInUserList) {
            if (user.getId() == 0) {
                walkInUser = user;
                break;
            }
        }
        if (walkInUser == null) {
            List<String> errors = new ArrayList<>();
            errors.add("Walk-in user not found");
            throw new ConflictException("Walk-in user not found", errors);
        }


        ReservationCreateDto reservationCreateDto = ReservationCreateDto.ReservationCreateDtoBuilder.aReservationCreateDto()
            .withDate(reservationWalkInDto.getDate())
            .withStartTime(reservationWalkInDto.getStartTime())
            .withEndTime(reservationWalkInDto.getStartTime().plusHours(2))
            .withPax(reservationWalkInDto.getPax()).withPlaceIds(reservationWalkInDto.getPlaceIds())
            .withFirstName(walkInUser.getFirstName()).withLastName(walkInUser.getLastName())
            .withEmail(walkInUser.getEmail()).withApplicationUser(walkInUser).build();
        LOGGER.debug("Created reservationCreateDto: {}", reservationCreateDto);
        Reservation reservation = mapper.reservationCreateDtoToReservation(reservationCreateDto);
        reservation.setHashValue(hashService.hashSha256(reservation.getDate().toString() + reservation.getStartTime().toString() + reservation.getEndTime().toString() + reservation.getPax().toString()));
        Reservation createdReservation = reservationRepository.save(reservation);
        LOGGER.debug("Created reservation: {}", createdReservation);
        List<Place> selectedPlaces = new ArrayList<>();
        for (Long placeId : reservationCreateDto.getPlaceIds()) {
            Optional<Place> optionalPlace = placeRepository.findById(placeId);
            if (optionalPlace.isPresent()) {
                selectedPlaces.add(optionalPlace.get());
            } else {
                List<String> errors = new ArrayList<>();
                errors.add("Place #" + placeId + " not found");
                throw new ConflictException("Place not found", errors);
            }
        }
        for (Place place : selectedPlaces) {
            ReservationPlace reservationPlace = ReservationPlace.ReservationPlaceBuilder.aReservationPlace()
                .withReservation(createdReservation).withPlace(place).build();
            reservationPlaceRepository.save(reservationPlace);
        }
        return mapper.reservationToReservationCreateDto(createdReservation);
    }

    @Override
    public PermanentReservationCreateDto createPermanent(PermanentReservationCreateDto createDto) {
        LOGGER.trace("createPermanent {}", createDto);


        if (createDto.getApplicationUser() == null) {
            createDto.setApplicationUser(applicationUserService.getCurrentApplicationUser());
        }
        createDto.setEndDate(createDto.getEndDate() == null ? createDto.getStartDate().plusYears(1) : createDto.getEndDate());
        createDto.setEndTime(createDto.getStartTime().plusHours(2));

        String hashedValue = hashService.hashSha256(createDto.getStartDate().toString() + createDto.getStartTime().toString() + createDto.getEndTime().toString() + createDto.getPax().toString() + createDto.getEndDate());
        createDto.setHashedId(hashedValue);

        LOGGER.debug("MAPPED Perma: {}", mapper.permanentReservationCreateDtoToPermanentReservation(createDto));
        PermanentReservation savedPermanentReservation = permanentReservationRepository.save(mapper.permanentReservationCreateDtoToPermanentReservation(createDto));
        return mapper.permanentReservationToPermanentReservationCreateDto(savedPermanentReservation);
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

    @Override
    public void confirm(String hashId) throws NotFoundException {
        ReservationEditDto reservationEditDto = getByHashedId(hashId);
        Long id = reservationEditDto.getReservationId();
        LOGGER.trace("confirm ({})", id);

        Optional<Reservation> optionalReservation = reservationRepository.findById(id);
        if (optionalReservation.isEmpty()) {
            throw new NotFoundException("Reservation not found", null);
        }
        Reservation reservation = optionalReservation.get();
        reservation.setConfirmed(true);
        reservationRepository.save(reservation);
    }

    @Override
    public void unconfirm(String hashId) throws NotFoundException {
        ReservationEditDto reservationEditDto = getByHashedId(hashId);
        Long id = reservationEditDto.getReservationId();
        LOGGER.trace("unconfirm ({})", id);

        Optional<Reservation> optionalReservation = reservationRepository.findById(id);
        if (optionalReservation.isEmpty()) {
            throw new NotFoundException("Reservation not found", null);
        }
        Reservation reservation = optionalReservation.get();
        reservation.setConfirmed(false);
        reservationRepository.save(reservation);
    }

    public void confirmPermanentReservation(Long permanentReservationId) throws MessagingException {
        Optional<PermanentReservation> optionalPermanent = permanentReservationRepository.findById(permanentReservationId);
        if (optionalPermanent.isEmpty()) {
            throw new NotFoundException("Permanent reservation not found");
        }

        PermanentReservation permanentReservation = optionalPermanent.get();
        permanentReservation.setConfirmed(true);
        permanentReservationRepository.save(permanentReservation);

        LocalDate currentDate = permanentReservation.getStartDate();
        LocalDate endDate = permanentReservation.getEndDate();
        List<LocalDate> skippedDates = new ArrayList<>();
        int period = permanentReservation.getPeriod();
        RepetitionEnum repetition = permanentReservation.getRepetition();

        while (currentDate.isBefore(endDate) || currentDate.isEqual(endDate)) {
            ReservationCheckAvailabilityDto reservationCheckAvailabilityDto = ReservationCheckAvailabilityDto.ReservationCheckAvailabilityDtoBuilder.aReservationCheckAvailabilityDto()
                .withDate(currentDate)
                .withStartTime(permanentReservation.getStartTime())
                .withEndTime(permanentReservation.getEndTime())
                .withPax(permanentReservation.getPax())
                .build();

            ReservationResponseEnum tableStatus = getAvailability(reservationCheckAvailabilityDto);

            if (tableStatus == ReservationResponseEnum.AVAILABLE) {
                Reservation newReservation = createSingleReservationFromPermanent(permanentReservation, currentDate);

                Reservation createdReservation = reservationRepository.save(newReservation);
                LOGGER.debug("Single Reservation for Permanent Reservation: {}", createdReservation);

                PermanentReservationMapper mapping = PermanentReservationMapper.PermanentReservationMapperBuilder.aPermanentReservationMapper()
                    .withReservation(newReservation)
                    .withPermanentReservation(permanentReservation).build();
                permanentReservationMapperRepository.save(mapping);
                LOGGER.debug("Saved mapping between PermanentReservation and new Reservation.");
            } else {
                skippedDates.add(currentDate);
            }

            if (repetition == RepetitionEnum.DAYS) {
                currentDate = currentDate.plusDays(period);
            } else if (repetition == RepetitionEnum.WEEKS) {
                currentDate = currentDate.plusWeeks(period);
            }
        }
        LOGGER.debug("Skipped Dates: {}", skippedDates);
        sendPermanentMail(permanentReservation, skippedDates, 0);
    }

    @Override
    public List<PermanentReservationListDto> searchPermanent(PermanentReservationSearchDto searchParams) {
        LOGGER.trace("searchPermanent ({})", searchParams);

        if (searchParams.getUserId() == null) {
            List<PermanentReservation> reservations = permanentReservationRepository.findPermanentReservationsWithoutUserId(searchParams.getEarliestDate(),
                searchParams.getLatestDate(), searchParams.getEarliestStartTime(), searchParams.getLatestEndTime());

            List<PermanentReservationListDto> dtos = reservations.stream().map(mapper::permanentReservationToPermanentReservationListDto).collect(Collectors.toList());

            LOGGER.debug("Found {} reservations for admin/employee without a specific user", dtos.size());
            return dtos;
        }

        List<PermanentReservation> reservations = permanentReservationRepository.findPermanentReservationsByUserId(searchParams.getUserId(), searchParams.getEarliestDate(),
            searchParams.getLatestDate(), searchParams.getEarliestStartTime(), searchParams.getLatestEndTime());

        List<PermanentReservationListDto> dtos = reservations.stream().map(mapper::permanentReservationToPermanentReservationListDto).collect(Collectors.toList());

        LOGGER.debug("Found {} reservations for user id {}", dtos.size(), searchParams.getUserId());
        return dtos;
    }

    @Override
    public PermanentReservationDetailDto getPermanentDetails(String hashedId) {
        LOGGER.trace("getPermanentDetails");

        PermanentReservation permanentReservation = permanentReservationRepository.findByHashedId(hashedId);

        if (permanentReservation == null) {
            throw new NotFoundException("Permanent reservation not found with id: " + hashedId);
        }
        List<PermanentReservationMapper> mappers = permanentReservationMapperRepository.findByPermanentReservationId(permanentReservation.getId());
        List<Long> reservationIds = mappers.stream().map(mapper -> mapper.getReservation().getId()).collect(Collectors.toList());

        // Fetch reservations by  IDs
        List<Reservation> reservations = reservationRepository.findAllById(reservationIds);

        // Map reservations to ReservationListDto
        List<ReservationListDto> reservationListDtos = reservations.stream().map(mapper::reservationToReservationListDto).collect(Collectors.toList());
        for (Reservation reservation : reservations) {
            List<Long> placeIds = reservationPlaceRepository.findPlaceIdsByReservationIds(Collections.singletonList(reservation.getId()));
            reservationListDtos.add(mapper.reservationToReservationListDto(reservation, placeIds));
        }

        PermanentReservationDetailDto detailDto = mapper.permanentReservationToDetailDto(permanentReservation);
        detailDto.setSingleReservationList(reservationListDtos);
        LOGGER.debug("Found ReservationDetailDto: {}", detailDto);
        return detailDto;
    }

    private Reservation createSingleReservationFromPermanent(PermanentReservation perm, LocalDate date) throws MessagingException {
        LOGGER.trace("createSingleReservationFromPermanent");

        ReservationCreateDto reservationCreateDto = new ReservationCreateDto();
        reservationCreateDto.setDate(date);
        reservationCreateDto.setStartTime(perm.getStartTime());
        reservationCreateDto.setEndTime(perm.getEndTime() == null ? perm.getStartTime().plusHours(2) : perm.getEndTime());
        reservationCreateDto.setPax(perm.getPax());
        reservationCreateDto.setFirstName(perm.getApplicationUser().getFirstName());
        reservationCreateDto.setLastName(perm.getApplicationUser().getLastName());
        reservationCreateDto.setEmail(perm.getApplicationUser().getEmail());
        reservationCreateDto.setUser(perm.getApplicationUser());

        // Check if all specified tables are available
        if (reservationCreateDto.getPlaceIds() != null && !reservationCreateDto.getPlaceIds().isEmpty()) {
            for (Long placeId : reservationCreateDto.getPlaceIds()) {
                ReservationCheckAvailabilityDto reservationCheckAvailabilityDto = ReservationCheckAvailabilityDto.ReservationCheckAvailabilityDtoBuilder.aReservationCheckAvailabilityDto()
                    .withDate(reservationCreateDto.getDate())
                    .withStartTime(reservationCreateDto.getStartTime())
                    .withEndTime(reservationCreateDto.getEndTime())
                    .withPax(reservationCreateDto.getPax())
                    .withIdToExclude(null) // Assuming we're not excluding any reservations
                    .build();
                if (!isPlaceAvailable(placeId)) {
                    return null; // frontend checks and notifies
                }
            }
        } else {
            // If no place IDs are provided, perform the default availability check
            ReservationCheckAvailabilityDto reservationCheckAvailabilityDto = ReservationCheckAvailabilityDto.ReservationCheckAvailabilityDtoBuilder.aReservationCheckAvailabilityDto()
                .withDate(reservationCreateDto.getDate())
                .withStartTime(reservationCreateDto.getStartTime())
                .withEndTime(reservationCreateDto.getEndTime())
                .withPax(reservationCreateDto.getPax())
                .withIdToExclude(null) // Assuming we're not excluding any reservations
                .build();
            ReservationResponseEnum tableStatus = getAvailability(reservationCheckAvailabilityDto);
            if (tableStatus != ReservationResponseEnum.AVAILABLE) {
                return null; // frontend should check for null and show notification accordingly
            }
        }

        // Choose the places for reservation
        List<Place> selectedPlaces = new ArrayList<>();
        if (reservationCreateDto.getPlaceIds() != null && !reservationCreateDto.getPlaceIds().isEmpty()) {
            for (Long placeId : reservationCreateDto.getPlaceIds()) {
                Optional<Place> optionalPlace = placeRepository.findById(placeId);
                if (optionalPlace.isPresent()) {
                    selectedPlaces.add(optionalPlace.get());
                }
            }
            // If any specified place is not found, return null
            if (selectedPlaces.size() != reservationCreateDto.getPlaceIds().size()) {
                return null;
            }
        } else {
            List<Place> places = placeRepository.findAll();
            List<Long> reservationIds = reservationRepository.findReservationsAtSpecifiedTime(reservationCreateDto.getDate(),
                reservationCreateDto.getStartTime(), reservationCreateDto.getEndTime());
            List<Long> placeIds = reservationPlaceRepository.findPlaceIdsByReservationIds(reservationIds);
            places.removeAll(placeRepository.findAllById(placeIds));
            if (places.isEmpty()) {
                return null; // No available places
            }
            selectedPlaces.add(places.get(0));
        }

        // Map to Reservation entity
        Reservation reservation = mapper.reservationCreateDtoToReservation(reservationCreateDto);
        reservation.setNotes(reservation.getNotes() != null ? reservation.getNotes().trim() : reservation.getNotes());
        reservation.setConfirmed(false);

        String hashedValue = hashService.hashSha256(reservation.getDate().toString() + reservation.getStartTime().toString()
            + reservation.getEndTime().toString() + reservation.getPax().toString() + reservation.isConfirmed());
        reservation.setHashValue(hashedValue);

        // Save Reservation in database and return it mapped to a DTO
        Reservation savedReservation = reservationRepository.save(reservation);

        // Validate after changes being made as an additional layer of defense
        Set<ConstraintViolation<Reservation>> reservationViolations = validator.validate(savedReservation);
        if (!reservationViolations.isEmpty()) {
            throw new ConstraintViolationException(reservationViolations);
        }

        // Save ReservationPlace for all selected places in the database
        for (Place place : selectedPlaces) {
            ReservationPlace reservationPlace = ReservationPlace.ReservationPlaceBuilder.aReservationPlace().withReservation(savedReservation).withPlace(place).build();
            reservationPlaceRepository.save(reservationPlace);
        }

        return savedReservation;
    }


    @Override
    public void deletePermanentReservation(String permanentReservationHashId) throws MessagingException {
        LOGGER.trace("deletePermanentReservation ({})", permanentReservationHashId);
        
        PermanentReservation permanentReservation = permanentReservationRepository.findByHashedId(permanentReservationHashId);

        if (permanentReservation == null) {
            throw new NotFoundException("Permanent reservation not found");
        }

        List<PermanentReservationMapper> mappers = permanentReservationMapperRepository.findByPermanentReservationId(permanentReservation.getId());
        List<Long> reservationIds = mappers.stream().map(mapper -> mapper.getReservation().getId()).toList();
        ;

        // Collect dates for email notification
        List<LocalDate> deletedReservationDates = new ArrayList<>();

        for (Long reservationId : reservationIds) {
            Optional<Reservation> optionalReservation = reservationRepository.findById(reservationId);
            if (optionalReservation.isPresent()) {
                Reservation reservation = optionalReservation.get();
                deletedReservationDates.add(reservation.getDate());

                // Delete the mapper entries
                permanentReservationMapperRepository.deleteByReservationId(reservationId);
                reservationPlaceRepository.deleteByReservationId(reservationId);

                // Delete the reservation
                reservationRepository.deleteById(reservationId);
            }
        }

        // Delete the permanent reservation
        permanentReservationRepository.delete(permanentReservation);

        // Send email notification
        sendPermanentMail(permanentReservation, deletedReservationDates, 1);

        LOGGER.debug("Deleted permanent reservation: {}", permanentReservationHashId);
    }

    private void sendPermanentMail(PermanentReservation permanentReservation, List<LocalDate> deletedReservationDates, int mode) throws MessagingException {
        LOGGER.trace("sendPermanentDeletionEmail");
        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("recipientName", permanentReservation.getApplicationUser().getFirstName() + " " + permanentReservation.getApplicationUser().getLastName());
        templateModel.put("persons", permanentReservation.getPax());
        templateModel.put("restaurantName", "The Wet otter");
        templateModel.put("deletedReservationDates", deletedReservationDates);
        templateModel.put("link", "http://localhost:4200/#/permanent-reservation-details/" + permanentReservation.getHashedId());
        templateModel.put("startDate", permanentReservation.getStartDate());
        templateModel.put("endDate", permanentReservation.getEndDate());
        templateModel.put("startTime", permanentReservation.getStartTime());
        templateModel.put("endTime", permanentReservation.getEndTime());
        templateModel.put("period", permanentReservation.getPeriod());
        String repetition;
        templateModel.put("repetition", repetition = (permanentReservation.getRepetition() == RepetitionEnum.DAYS ? "days" : "weeks"));
        if (mode == 0) { //confirm
            emailService.sendPermanentConfirmationMessageUsingThymeleafTemplate(permanentReservation, deletedReservationDates, templateModel);
        } else if (mode == 1) { //delete
            emailService.sendPermanentDeletionMessageUsingThymeleafTemplate(permanentReservation, deletedReservationDates, templateModel);
        } else { //update
            return;
        }
    }
}