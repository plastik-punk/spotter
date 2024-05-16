package at.ac.tuwien.sepr.groupphase.backend.datagenerator;

import at.ac.tuwien.sepr.groupphase.backend.entity.OpeningHours;
import at.ac.tuwien.sepr.groupphase.backend.repository.OpeningHoursRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.time.DayOfWeek;
import java.time.LocalTime;

@Profile({"generateData"})
@Component
@Order(4)
public class OpeningHoursDataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final OpeningHoursRepository openingHoursRepository;

    public OpeningHoursDataGenerator(OpeningHoursRepository openingHoursRepository) {
        this.openingHoursRepository = openingHoursRepository;
    }

    @PostConstruct
    private void generateOpeningHours() {
        LOGGER.trace("generateOpeningHours");

        if (this.openingHoursRepository.count() > 0) {
            LOGGER.debug("Opening hours have already been generated");
        } else {
            LOGGER.debug("Generating opening hours");

            // TODO
            OpeningHours openingHours1 = OpeningHours.OpeningHourBuilder.anOpeningHour()
                .withDayOfWeek(DayOfWeek.MONDAY)
                .withOpeningTime(LocalTime.of(11, 30))
                .withClosingTime(LocalTime.of(15, 0))
                .build();

            OpeningHours openingHours2 = OpeningHours.OpeningHourBuilder.anOpeningHour()
                .withDayOfWeek(DayOfWeek.MONDAY)
                .withOpeningTime(LocalTime.of(17, 0))
                .withClosingTime(LocalTime.of(22, 0))
                .build();

            OpeningHours openingHours3 = OpeningHours.OpeningHourBuilder.anOpeningHour()
                .withDayOfWeek(DayOfWeek.TUESDAY)
                .withOpeningTime(LocalTime.of(11, 30))
                .withClosingTime(LocalTime.of(15, 0))
                .build();

            OpeningHours openingHours4 = OpeningHours.OpeningHourBuilder.anOpeningHour()
                .withDayOfWeek(DayOfWeek.TUESDAY)
                .withOpeningTime(LocalTime.of(17, 0))
                .withClosingTime(LocalTime.of(22, 0))
                .build();

            OpeningHours openingHours6 = OpeningHours.OpeningHourBuilder.anOpeningHour()
                .withDayOfWeek(DayOfWeek.THURSDAY)
                .withOpeningTime(LocalTime.of(11, 30))
                .withClosingTime(LocalTime.of(15, 0))
                .build();

            OpeningHours openingHours7 = OpeningHours.OpeningHourBuilder.anOpeningHour()
                .withDayOfWeek(DayOfWeek.THURSDAY)
                .withOpeningTime(LocalTime.of(17, 0))
                .withClosingTime(LocalTime.of(22, 0))
                .build();

            OpeningHours openingHours8 = OpeningHours.OpeningHourBuilder.anOpeningHour()
                .withDayOfWeek(DayOfWeek.FRIDAY)
                .withOpeningTime(LocalTime.of(11, 30))
                .withClosingTime(LocalTime.of(15, 0))
                .build();

            OpeningHours openingHours9 = OpeningHours.OpeningHourBuilder.anOpeningHour()
                .withDayOfWeek(DayOfWeek.FRIDAY)
                .withOpeningTime(LocalTime.of(17, 0))
                .withClosingTime(LocalTime.of(23, 0))
                .build();

            OpeningHours openingHours10 = OpeningHours.OpeningHourBuilder.anOpeningHour()
                .withDayOfWeek(DayOfWeek.SATURDAY)
                .withOpeningTime(LocalTime.of(11, 30))
                .withClosingTime(LocalTime.of(15, 0))
                .build();

            OpeningHours openingHours11 = OpeningHours.OpeningHourBuilder.anOpeningHour()
                .withDayOfWeek(DayOfWeek.SATURDAY)
                .withOpeningTime(LocalTime.of(17, 0))
                .withClosingTime(LocalTime.of(23, 0))
                .build();

            OpeningHours openingHours12 = OpeningHours.OpeningHourBuilder.anOpeningHour()
                .withDayOfWeek(DayOfWeek.SUNDAY)
                .withOpeningTime(LocalTime.of(11, 30))
                .withClosingTime(LocalTime.of(15, 0))
                .build();

            OpeningHours openingHours13 = OpeningHours.OpeningHourBuilder.anOpeningHour()
                .withDayOfWeek(DayOfWeek.SUNDAY)
                .withOpeningTime(LocalTime.of(17, 0))
                .withClosingTime(LocalTime.of(22, 0))
                .build();

            openingHoursRepository.save(openingHours1);
            openingHoursRepository.save(openingHours2);
            openingHoursRepository.save(openingHours3);
            openingHoursRepository.save(openingHours4);
            openingHoursRepository.save(openingHours6);
            openingHoursRepository.save(openingHours7);
            openingHoursRepository.save(openingHours8);
            openingHoursRepository.save(openingHours9);
            openingHoursRepository.save(openingHours10);
            openingHoursRepository.save(openingHours11);
            openingHoursRepository.save(openingHours12);
            openingHoursRepository.save(openingHours13);

        }
    }
}
