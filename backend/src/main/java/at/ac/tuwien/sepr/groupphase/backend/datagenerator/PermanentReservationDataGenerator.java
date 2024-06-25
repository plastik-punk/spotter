package at.ac.tuwien.sepr.groupphase.backend.datagenerator;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.PermanentReservationCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepr.groupphase.backend.entity.PermanentReservation;
import at.ac.tuwien.sepr.groupphase.backend.enums.RepetitionEnum;
import at.ac.tuwien.sepr.groupphase.backend.repository.ApplicationUserRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.PermanentReservationRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.HashService;
import at.ac.tuwien.sepr.groupphase.backend.service.ReservationService;
import at.ac.tuwien.sepr.groupphase.backend.service.mapper.ReservationMapper;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.time.LocalDate;
import java.time.LocalTime;

@Profile({"generateData", "test"})
@Component
public class PermanentReservationDataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final ApplicationUserRepository applicationUserRepository;
    private final ReservationService reservationService;
    private final ReservationMapper reservationMapper;
    private final PermanentReservationRepository permanentReservationRepository;
    private final HashService hashService;

    public PermanentReservationDataGenerator(ApplicationUserRepository applicationUserRepository,
                                             ReservationService reservationService,
                                             ReservationMapper reservationMapper,
                                             PermanentReservationRepository permanentReservationRepository,
                                             HashService hashService) {
        this.applicationUserRepository = applicationUserRepository;
        this.reservationService = reservationService;
        this.reservationMapper = reservationMapper;
        this.permanentReservationRepository = permanentReservationRepository;
        this.hashService = hashService;
    }

    @PostConstruct
    private void generatePermanentReservations() {
        LOGGER.trace("generatePermanentReservations");

        ApplicationUser user = applicationUserRepository.findById(3L).orElse(null);
        if (user == null) {
            LOGGER.debug("User with ID 3 not found");
            return;
        }
        LOGGER.debug("Found user for permanent reservation : {}", user);
        if (!permanentReservationRepository.findAll().isEmpty()) {
            LOGGER.debug("Permanent Reservations already generated");
        } else {

            createAndSavePermanentReservation(user, LocalDate.of(2024, 8, 25), LocalDate.of(2025, 1, 10), user, 5, RepetitionEnum.DAYS, 6L, false);
            createAndSavePermanentReservation(user, LocalDate.of(2024, 7, 12), LocalDate.of(2024, 11, 5), user, 1, RepetitionEnum.WEEKS, 4L, false);
            createAndSavePermanentReservation(user, LocalDate.of(2024, 6, 30), LocalDate.of(2024, 12, 8), user, 2, RepetitionEnum.WEEKS, 5L, true);
        }
    }

    private void createAndSavePermanentReservation(ApplicationUser applicationUser, LocalDate startDate, LocalDate endDate, ApplicationUser user, int period, RepetitionEnum repetition, Long pax, boolean confirm) {

        String hashValue = hashService.hashSha256(startDate
            + LocalTime.of(18, 00).toString() + LocalTime.of(20, 00).toString()
            + pax.toString() + endDate.toString());
        PermanentReservationCreateDto createDto = PermanentReservationCreateDto.PermanentReservationCreateDtoBuilder.aPermanentReservationCreateDto()
            .withStartDate(startDate)
            .withEndDate(endDate)
            .withEndTime(LocalTime.of(20, 0))   // Example end time
            .withStartTime(LocalTime.of(18, 0))
            .withPax(pax)                         // Example number of pax
            .withRepetition(repetition)
            .withPeriod(period)
            .withApplicationUser(applicationUser)
            .withConfirmed(false)
            .build();

        LOGGER.debug("Creating PermanentReservation: {}", createDto);
        PermanentReservationCreateDto savedDto = reservationService.createPermanent(createDto);
        PermanentReservation permanentReservation = permanentReservationRepository.findByHashedId(createDto.getHashedId());

        if (confirm) {
            try {
                reservationService.confirmPermanentReservation(permanentReservation.getId());
                LOGGER.debug("Confirmed PermanentReservation with ID: {}", permanentReservation.getId());
            } catch (Exception e) {
                LOGGER.error("Error confirming PermanentReservation with ID: {}", permanentReservation.getId(), e);
            }
        }
    }
}
