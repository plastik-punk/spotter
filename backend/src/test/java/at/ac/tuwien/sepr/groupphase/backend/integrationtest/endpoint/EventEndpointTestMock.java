package at.ac.tuwien.sepr.groupphase.backend.integrationtest.endpoint;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.EventDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.EventSearchDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationEditDto;
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
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class EventEndpointTestMock implements TestData {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EventMapper mapper;

    @MockBean
    private EventService mockService;

    @Autowired
    private JwtTokenizer jwtTokenizer;

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private EventRepository repository;

    @Test
    @Transactional
    public void givenValidId_whenGetByHashedId_thenReturnEventDetailDto() throws Exception {
        String validId = "valid id";
        Event saved = repository.save(TEST_EVENT_1);

        EventDetailDto expectedDto = mapper.eventToEventDetailDto(saved);

        Mockito.when(mockService.getByHashId(validId)).thenReturn(expectedDto);

        MvcResult mvcResult = this.mockMvc.perform(get(EVENT_BASE_URI + "/detail")
                .param("hashId", validId)
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(TEST_USER_ADMIN, TEST_ROLES_ADMIN)))
            .andDo(print())
            .andReturn();

        int statusCode = mvcResult.getResponse().getStatus();
        EventDetailDto actualDto = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), EventDetailDto.class);

        assertAll (
            () -> assertEquals(200, statusCode),
            () -> assertEquals(expectedDto, actualDto)
        );
    }

}