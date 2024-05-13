package at.ac.tuwien.sepr.groupphase.backend.service.impl;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.mapper.ReservationMapper;
import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepr.groupphase.backend.entity.Place;
import at.ac.tuwien.sepr.groupphase.backend.entity.Reservation;
import at.ac.tuwien.sepr.groupphase.backend.enums.RoleEnum;
import at.ac.tuwien.sepr.groupphase.backend.repository.ApplicationUserRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.PlaceRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.ReservationRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.ReservationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.Optional;

@Service
public class ReservationServiceImpl implements ReservationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final ReservationRepository reservationRepository;
    private final ApplicationUserRepository applicationUserRepository;
    private final PlaceRepository placeRepository;
    private final ReservationMapper mapper;

    @Autowired
    public ReservationServiceImpl(ReservationMapper mapper, ReservationRepository reservationRepository, ApplicationUserRepository applicationUserRepository,
                                  PlaceRepository placeRepository) {
        this.mapper = mapper;
        this.reservationRepository = reservationRepository;
        this.applicationUserRepository = applicationUserRepository;
        this.placeRepository = placeRepository;
    }

    @Override
    public Reservation create(ReservationCreateDto reservationCreateDto) {
        LOGGER.trace("create ({})", reservationCreateDto.toString());

        // 1. if guest user, create a new guest user, save it in DB and set returned user to reservationCreateDto
        if (reservationCreateDto.getUser() == null) {
            ApplicationUser guestUser = ApplicationUser.ApplicationUserBuilder.anApplicationUser()
                .withFirstName(reservationCreateDto.getFirstName())
                .withLastName(reservationCreateDto.getLastName())
                .withEmail(reservationCreateDto.getEmail())
                .withMobileNumber(reservationCreateDto.getMobileNumber())
                .withPassword("guest")
                .withRole(RoleEnum.GUEST)
                .build();
            reservationCreateDto.setUser(applicationUserRepository.save(guestUser));
        }

        // 2. map to Reservation entity
        Reservation reservation = mapper.reservationCreateDtoToReservation(reservationCreateDto);

        // TODO: remove this after implementing place selection in frontend
        Optional<Place> testPlace = placeRepository.findById(1L);
        reservation.setPlace(testPlace.get());

        // 3. save Reservation in database and return the new entity
        return reservationRepository.save(reservation);
    }
}