package at.ac.tuwien.sepr.groupphase.backend.integrationtest.endpoint;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.EventCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.EventEditDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.EventSearchDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationEditDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepr.groupphase.backend.entity.Reservation;
import com.fasterxml.jackson.core.type.TypeReference;
import at.ac.tuwien.sepr.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepr.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.EventListDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.Event;
import at.ac.tuwien.sepr.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepr.groupphase.backend.security.JwtTokenizer;
import at.ac.tuwien.sepr.groupphase.backend.service.EventService;
import at.ac.tuwien.sepr.groupphase.backend.service.mapper.EventMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class EventEndpointTest implements TestData {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EventRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EventMapper mapper;

    @Autowired
    private EventService service;

    @Autowired
    private JwtTokenizer jwtTokenizer;

    @Autowired
    private SecurityProperties securityProperties;
    @Autowired
    private EventRepository eventRepository;

    @Test
    @Transactional
    public void givenValidData_whenGetUpcomingEvents_thenReturnEvents() throws Exception {
        repository.deleteAll();
        Event event1 = repository.save(TEST_EVENT_1);
        Event event2 = repository.save(TEST_EVENT_2);
        EventListDto dto1 = mapper.eventToEventListDto(event1);
        EventListDto dto2 = mapper.eventToEventListDto(event2);

        MvcResult mvcResult = this.mockMvc.perform(get(EVENT_BASE_URI + "/upcoming")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(TEST_USER_CUSTOMER, TEST_ROLES_CUSTOMER)))
            .andDo(print())
            .andReturn();

        int statusCode = mvcResult.getResponse().getStatus();
        List<EventListDto> response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<EventListDto>>(){});
        assertNotNull(response);
        assertAll(
            () -> assertEquals(200, statusCode),
            () -> assertEquals(dto1, response.get(0)),
            () -> assertEquals(dto2, response.get(1)),
            () -> assertEquals(2, response.size())
        );
    }

    // TODO: fix frontend for search (endpoint can't process request data)
    // @Test
    @Transactional
    public void givenValidData_whenSearchEvents_thenReturnEvents() throws Exception {
        repository.deleteAll();
        Event event1 = repository.save(TEST_EVENT_1);
        Event event2 = repository.save(TEST_EVENT_2);
        Event event3 = repository.save(TEST_EVENT_4);
        EventListDto dto1 = mapper.eventToEventListDto(event1);
        EventListDto dto2 = mapper.eventToEventListDto(event2);
        EventListDto dto3 = mapper.eventToEventListDto(event3);

        EventSearchDto searchParameters = TEST_EVENT_SEARCH_DTO;

        MvcResult mvcResult = this.mockMvc.perform(get(EVENT_BASE_URI + "/search")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(TEST_USER_CUSTOMER, TEST_ROLES_CUSTOMER))
                .content(objectMapper.writeValueAsString(searchParameters))
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andReturn();

        int statusCode = mvcResult.getResponse().getStatus();
        List<EventListDto> response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<EventListDto>>(){});

        assertNotNull(response);
        assertAll(
            () -> assertEquals(200, statusCode),
            () -> assertTrue(response.contains(dto1)),
            () -> assertTrue(response.contains(dto2)),
            () -> assertFalse(response.contains(dto3)),
            () -> assertEquals(2, response.size())
        );
    }

    @Test
    @Transactional
    public void givenEventCreateDto_whenCreate_thenReturnEventCreateDto() throws Exception {
        String body = objectMapper.writeValueAsString(TEST_EVENT_CREATE);
        MvcResult mvcResult = this.mockMvc.perform(post(EVENT_BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(TEST_USER_ADMIN, TEST_ROLES_ADMIN)))
            .andDo(print())
            .andReturn();

        int statusCode = mvcResult.getResponse().getStatus();
        EventCreateDto response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), EventCreateDto.class);
        assertNotNull(response);

        assertAll(
            () -> assertEquals(201, statusCode),
            () -> assertEquals(TEST_EVENT_CREATE.getName(), response.getName()),
            () -> assertEquals(TEST_EVENT_CREATE.getDescription(), response.getDescription()),
            () -> assertEquals(TEST_EVENT_CREATE.getStartDate(), response.getStartDate()),
            () -> assertEquals(TEST_EVENT_CREATE.getStartTime(), response.getStartTime()),
            () -> assertEquals(TEST_EVENT_CREATE.getEndDate(), response.getEndDate()),
            () -> assertEquals(TEST_EVENT_CREATE.getEndTime(), response.getEndTime())
        );
    }

    // TODO finish when update actually works
    // @Test
    @Transactional
    public void givenEventEditDto_whenUpdate_thenEventIsUpdated() throws Exception {
        Event savedEvent = eventRepository.save(TEST_EVENT_1);
        TEST_EVENT_EDIT.setHashId(savedEvent.getHashId());

        String body = objectMapper.writeValueAsString(TEST_EVENT_EDIT);
        MvcResult mvcResult = this.mockMvc.perform(put(EVENT_BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(TEST_USER_ADMIN, TEST_ROLES_ADMIN)))
            .andDo(print())
            .andReturn();

        int statusCode = mvcResult.getResponse().getStatus();
        EventEditDto response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), EventEditDto.class);
        assertNotNull(response);

        assertAll(
            () -> assertEquals(200, statusCode),
            () -> assertEquals(savedEvent.getHashId(), response.getHashId()),
            () -> assertEquals(savedEvent.getStartTime(), response.getStartTime()),
            () -> assertEquals(savedEvent.getEndTime(), response.getEndTime()),
            () -> assertEquals(savedEvent.getName(), response.getName()),
            () -> assertEquals(savedEvent.getDescription(), response.getDescription())
        );
    }

    @Test
    @Transactional
    public void givenValidId_whenDelete_thenNoContent() throws Exception {
        Event savedEvent = eventRepository.save(TEST_EVENT_1);

        MvcResult mvcResult = this.mockMvc.perform(delete(EVENT_BASE_URI)
                .content(savedEvent.getHashId())
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(TEST_USER_ADMIN, TEST_ROLES_ADMIN)))
            .andDo(print())
            .andReturn();
        int statusCode = mvcResult.getResponse().getStatus();

        mvcResult = this.mockMvc.perform(get(EVENT_BASE_URI + "/detail")
                .param("hashId", savedEvent.getHashId())
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(TEST_USER_ADMIN, TEST_ROLES_ADMIN)))
            .andDo(print())
            .andReturn();
        int statusCode2 = mvcResult.getResponse().getStatus();

        assertAll(
            () -> assertEquals(204, statusCode),
            () -> assertEquals(404, statusCode2)
        );
    }
}