package at.ac.tuwien.sepr.groupphase.backend.unittests.repository;

import at.ac.tuwien.sepr.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepr.groupphase.backend.entity.Event;
import at.ac.tuwien.sepr.groupphase.backend.repository.EventRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
public class EventRepositoryTest implements TestData {

    @Autowired
    private EventRepository repository;

    @Test
    @Transactional
    public void givenValidData_whenFindUpcomingEvents_thenReturnEvents() {
        repository.deleteAll();
        Event event1 = repository.save(TEST_EVENT_1);
        Event event2 = repository.save(TEST_EVENT_2);
        Event event3 = repository.save(TEST_EVENT_4);
        List<Event> actualEvents = repository.findUpcomingEvents();
        assertAll(
            () -> assertEquals(2, actualEvents.size(), "The size of the returned list should be 2"),
            () -> assertTrue(actualEvents.contains(event1), "The returned list should contain the first event"),
            () -> assertTrue(actualEvents.contains(event2), "The returned list should contain the second event")
        );
    }
}
