package at.ac.tuwien.sepr.groupphase.backend.unittests.service.impl;

import at.ac.tuwien.sepr.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.EventListDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.Event;
import at.ac.tuwien.sepr.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.EventService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class EventServiceImplTest implements TestData {

    @Autowired
    private EventService service;

    @Autowired
    private EventRepository repository;

    @Test
    @Transactional
    public void givenValidData_whenGetUpcomingEvents_thenReturnEvents() {
        repository.deleteAll();

        Event event1 = repository.save(TEST_EVENT_1);
        Event event2 = repository.save(TEST_EVENT_2);

        List<EventListDto> actualDtos = service.getUpcomingEvents();
        EventListDto actualDto1 = actualDtos.get(0);
        EventListDto actualDto2 = actualDtos.get(1);

        assertAll(
            () -> assertEquals(2, actualDtos.size(), "The size of the returned list should be 2"),
            () -> assertEquals(event1.getName(), actualDto1.getName(), "The name of the first event does not match"),
            () -> assertEquals(event2.getName(), actualDto2.getName(), "The name of the second event does not match")
        );
    }
}