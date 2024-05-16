package at.ac.tuwien.sepr.groupphase.backend.service.impl;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationCheckAvailabilityDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepr.groupphase.backend.entity.ClosedDay;
import at.ac.tuwien.sepr.groupphase.backend.entity.OpeningHours;
import at.ac.tuwien.sepr.groupphase.backend.entity.Place;
import at.ac.tuwien.sepr.groupphase.backend.entity.Reservation;
import at.ac.tuwien.sepr.groupphase.backend.enums.ReservationResponseEnum;
import at.ac.tuwien.sepr.groupphase.backend.enums.RoleEnum;
import at.ac.tuwien.sepr.groupphase.backend.enums.StatusEnum;
import at.ac.tuwien.sepr.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepr.groupphase.backend.repository.ApplicationUserRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.ClosedDayRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.OpeningHoursRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.PlaceRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.ReservationRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.ReservationService;
import at.ac.tuwien.sepr.groupphase.backend.service.mail.EmailService;
import at.ac.tuwien.sepr.groupphase.backend.service.mapper.ReservationMapper;
import jakarta.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ReservationServiceImpl implements ReservationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final ReservationRepository reservationRepository;
    private final ApplicationUserRepository applicationUserRepository;
    private final PlaceRepository placeRepository;
    private final ReservationMapper mapper;
    private final OpeningHoursRepository openingHoursRepository;
    private final ClosedDayRepository closedDayRepository;

    private final EmailService emailService;
    private final ReservationValidator reservationValidator;

    @Autowired
    public ReservationServiceImpl(ReservationMapper mapper, ReservationRepository reservationRepository, ApplicationUserRepository applicationUserRepository,
                                  PlaceRepository placeRepository, EmailService emailService, ReservationValidator reservationValidator, OpeningHoursRepository openingHoursRepository, ClosedDayRepository closedDayRepository) {
        this.mapper = mapper;
        this.reservationRepository = reservationRepository;
        this.applicationUserRepository = applicationUserRepository;
        this.placeRepository = placeRepository;
        this.emailService = emailService;
        this.reservationValidator = reservationValidator;
        this.openingHoursRepository = openingHoursRepository;
        this.closedDayRepository = closedDayRepository;
    }

    @Override
    public ReservationCreateDto create(ReservationCreateDto reservationCreateDto) throws MessagingException, ValidationException {
        LOGGER.trace("create ({})", reservationCreateDto.toString());
        reservationValidator.validateReservationCreateDto(reservationCreateDto);

        // 1. if guest user, create a new guest user, save it in DB and set returned user to reservationCreateDto
        if (reservationCreateDto.getUser() == null) {
            ApplicationUser guestUser = ApplicationUser.ApplicationUserBuilder.anApplicationUser()
                .withFirstName(reservationCreateDto.getFirstName().trim())
                .withLastName(reservationCreateDto.getLastName().trim())
                .withEmail(reservationCreateDto.getEmail().trim())
                .withMobileNumber(reservationCreateDto.getMobileNumber().trim())
                .withoutPassword()
                .withRole(RoleEnum.GUEST)
                .build();

            ApplicationUser savedGuestUser = applicationUserRepository.save(guestUser);
            reservationCreateDto.setUser(savedGuestUser);
        }

        // 2. map to Reservation entity and trim strings
        Reservation reservation = mapper.reservationCreateDtoToReservation(reservationCreateDto);
        reservation.setNotes(reservation.getNotes().trim());

        // TODO: change this after implementing place selection in frontend
        Optional<Place> testPlace = placeRepository.findById(1L);
        if (testPlace.isPresent()) {
            reservation.setPlace(testPlace.get());
        } else {
            Place dummyPlace = Place.PlaceBuilder.aPlace()
                .withId(1L)
                .withPax(4L)
                .withStatus(StatusEnum.AVAILABLE)
                .build();
            placeRepository.save(dummyPlace);
            reservation.setPlace(dummyPlace);
        }

        // TODO: add Restaurant name to DTO
        // 3. send conformation Mail
        // TODO: activate mail sending for production
        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("recipientName", reservationCreateDto.getFirstName() + " " + reservationCreateDto.getLastName());
        templateModel.put("text", reservationCreateDto.getNotes());
        templateModel.put("persons", reservationCreateDto.getPax());
        templateModel.put("restaurantName", "--SpotterEssen--"); //TODO: change to restaurant name
        templateModel.put("reservationDate", reservationCreateDto.getDate());
        templateModel.put("reservationTime", reservationCreateDto.getStartTime());
        templateModel.put("link", "--link here--"); //TODO: change to the link
        // emailService.sendMessageUsingThymeleafTemplate(reservationCreateDto.getUser().getEmail(),
        //     "Reservation Confirmation", templateModel);

        // 4. save Reservation in database and return it mapped to a DTO
        Reservation savedReservation = reservationRepository.save(reservation);
        reservationValidator.validateReservation(savedReservation);

        return mapper.reservationToReservationCreateDto(savedReservation);
    }

    @Override
    public ReservationResponseEnum getAvailability(ReservationCheckAvailabilityDto reservationCheckAvailabilityDto) throws ValidationException {
        LOGGER.trace("getAvailability ({})", reservationCheckAvailabilityDto.toString());
        reservationValidator.validateReservationCheckAvailabilityDto(reservationCheckAvailabilityDto);

        // 1. check if date is in the past
        if (reservationCheckAvailabilityDto.getDate().isBefore(java.time.LocalDate.now())) {
            return ReservationResponseEnum.DATE_IN_PAST;
        }

        // 2. check if restaurant is open at specified date
        Optional<ClosedDay> optionalClosedDay = closedDayRepository.findByDate(reservationCheckAvailabilityDto.getDate());
        if (optionalClosedDay.isPresent()) {
            return ReservationResponseEnum.CLOSED;
        }

        // 3. check if reservation is during opening hours
        boolean isOpen = false;
        DayOfWeek dayOfWeek = reservationCheckAvailabilityDto.getDate().getDayOfWeek();
        List<OpeningHours> openingHoursList = openingHoursRepository.findByDayOfWeek(dayOfWeek);
        if (openingHoursList.isEmpty()) {
            return ReservationResponseEnum.CLOSED; // if one weekday is always closed, this should return this enum instead of OUTSIDE_OPENING_HOURS
        }
        // TODO: if startTime is in opening hours but endTime is not, return meaningful notification
        for (OpeningHours openingHours : openingHoursList) {
            if (reservationCheckAvailabilityDto.getStartTime().isBefore(openingHours.getClosingTime())
                && reservationCheckAvailabilityDto.getStartTime().plusHours(2).isAfter(openingHours.getOpeningTime())
                && reservationCheckAvailabilityDto.getStartTime().plusHours(2).isBefore(openingHours.getClosingTime())
                && reservationCheckAvailabilityDto.getStartTime().isAfter(openingHours.getOpeningTime())) {
                isOpen = true;
                break;
            }
        }
        if (!isOpen) {
            return ReservationResponseEnum.OUTSIDE_OPENING_HOURS;
        }

        // 4. check if any places are available for specified time
        // TODO: findOccupiedPlaces seems not to work as expected
        List<Place> places = placeRepository.findAll();
        List<Place> occupiedPlaces = reservationRepository.findOccupiedPlaces(reservationCheckAvailabilityDto.getDate(),
                reservationCheckAvailabilityDto.getStartTime(),
                reservationCheckAvailabilityDto.getStartTime().plusHours(2));
        places.removeAll(occupiedPlaces);
        if (places.isEmpty()) {
            return ReservationResponseEnum.ALL_OCCUPIED;
        }

        // 5. check if any available table can host pax
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

        // 6. if no check succeeded, return available
        return ReservationResponseEnum.AVAILABLE;
    }
}