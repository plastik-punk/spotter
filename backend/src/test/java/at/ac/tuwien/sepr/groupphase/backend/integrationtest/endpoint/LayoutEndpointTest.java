package at.ac.tuwien.sepr.groupphase.backend.integrationtest.endpoint;

import at.ac.tuwien.sepr.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepr.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.AreaLayoutDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationLayoutCheckAvailabilityDto;
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
public class LayoutEndpointTest implements TestData {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtTokenizer jwtTokenizer;

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private EventRepository repository;

    @Autowired
    private EventService service;

    // TODO
    // @Test
    @Transactional
    public void givenValidData_whenGetAvailabilityLayout_thenReturnAreaLayout() throws Exception {
        ReservationLayoutCheckAvailabilityDto reservationLayoutCheckAvailabilityDto = TEST_LAYOUT_CHECK_AVAILABILITY_DTO;

        // TODO: Prepare the expected AreaLayoutDto
        AreaLayoutDto expectedAreaLayoutDto = new AreaLayoutDto();
        // TODO: Set the properties of expectedAreaLayoutDto as needed

        MvcResult mvcResult = this.mockMvc.perform(get("/api/v1/layout")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(TEST_USER_CUSTOMER, TEST_ROLES_CUSTOMER))
                .content(objectMapper.writeValueAsString(reservationLayoutCheckAvailabilityDto))
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andReturn();

        int statusCode = mvcResult.getResponse().getStatus();
        AreaLayoutDto response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), AreaLayoutDto.class);
        assertNotNull(response);
        assertAll(
            () -> assertEquals(200, statusCode),
            () -> assertEquals(expectedAreaLayoutDto, response)
        );
    }

    // TODO: getAllAreas


    // TODO: getAllAreasDetailed


    // TODO: createLayout


    // TODO: deleteArea


    // TODO: getAreaById


    // TODO: toggleOpen


    // TODO: updateArea


    // TODO: toggleMain

}
