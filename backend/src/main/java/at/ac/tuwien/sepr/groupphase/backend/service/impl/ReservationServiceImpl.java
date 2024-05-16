package at.ac.tuwien.sepr.groupphase.backend.service.impl;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationCheckAvailabilityDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepr.groupphase.backend.entity.Place;
import at.ac.tuwien.sepr.groupphase.backend.entity.Reservation;
import at.ac.tuwien.sepr.groupphase.backend.enums.RoleEnum;
import at.ac.tuwien.sepr.groupphase.backend.enums.StatusEnum;
import at.ac.tuwien.sepr.groupphase.backend.repository.ApplicationUserRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.PlaceRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.ReservationRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.ReservationService;
import at.ac.tuwien.sepr.groupphase.backend.service.mail.EmailService;
import jakarta.mail.MessagingException;
import at.ac.tuwien.sepr.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepr.groupphase.backend.service.mapper.ReservationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
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

    private final EmailService emailService;
    private final ReservationValidator reservationValidator;

    @Autowired
    public ReservationServiceImpl(ReservationMapper mapper, ReservationRepository reservationRepository, ApplicationUserRepository applicationUserRepository,
                                  PlaceRepository placeRepository, EmailService emailService, ReservationValidator reservationValidator) {
        this.mapper = mapper;
        this.reservationRepository = reservationRepository;
        this.applicationUserRepository = applicationUserRepository;
        this.placeRepository = placeRepository;
        this.emailService = emailService;
        this.reservationValidator = reservationValidator;
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
    public Boolean getAvailability(ReservationCheckAvailabilityDto reservationCheckAvailabilityDto) {
        LOGGER.trace("getAvailability ({})", reservationCheckAvailabilityDto.toString());

        // 1. fetch all places from DB that have a pax of greater or equal to the requested pax
        List<Place> places = placeRepository.findPlacesWithSufficientPax(reservationCheckAvailabilityDto.getPax());

        // 2. fetch all occupied places for the requested time
        List<Place> occupiedPlaces = reservationRepository.findOccupiedPlaces(reservationCheckAvailabilityDto.getDate(), reservationCheckAvailabilityDto.getStartTime(), reservationCheckAvailabilityDto.getStartTime().plusHours(2));

        // 3. remove occupied places from all places
        places.removeAll(occupiedPlaces);

        // 4. return true if there are any available places
        return !places.isEmpty();
    }
}