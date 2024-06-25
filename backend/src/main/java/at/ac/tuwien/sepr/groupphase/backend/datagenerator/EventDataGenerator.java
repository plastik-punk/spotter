package at.ac.tuwien.sepr.groupphase.backend.datagenerator;

import at.ac.tuwien.sepr.groupphase.backend.entity.Event;
import at.ac.tuwien.sepr.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.HashService;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.time.LocalDateTime;

@Profile({"generateData", "test"})
@Component
@Order(7)
public class EventDataGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final int NUMBER_OF_EVENTS_TO_GENERATE = 3;

    private final EventRepository eventRepository;
    private final HashService hashService;

    public EventDataGenerator(EventRepository eventRepository, HashService hashService) {
        this.eventRepository = eventRepository;
        this.hashService = hashService;
    }

    @PostConstruct
    private void generateEvents() {
        LOGGER.trace("generateEvents");

        if (eventRepository.count() > 0) {
            LOGGER.debug("Events have already been generated");
        } else {
            LOGGER.debug("Generating {} event entries", NUMBER_OF_EVENTS_TO_GENERATE);
            for (int i = 0; i < NUMBER_OF_EVENTS_TO_GENERATE; i++) {
                Event event = Event.EventBuilder.anEvent()
                    .withStartTime(LocalDateTime.of(2024, 6 + i, 1 + i, 12 + i, 0))
                    .withEndTime(LocalDateTime.of(2024, 6 + i, 5 + i, 14 + i, 0))
                    .withName("Event " + i)
                    .withDescription("Description of event " + i)
                    .withHashId(hashService.hashSha256("" + i))
                    .build();

                LOGGER.debug("Generated event: {}", event);
                eventRepository.save(event);
            }
        }
    }
}
