package at.ac.tuwien.sepr.groupphase.backend.integrationtest.endpoint;

import at.ac.tuwien.sepr.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepr.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.AdminViewDto;
import at.ac.tuwien.sepr.groupphase.backend.security.JwtTokenizer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class AdminViewEndpointTest implements TestData {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtTokenizer jwtTokenizer;

    @Autowired
    private SecurityProperties securityProperties;

    @Test
    @Transactional
    public void givenNothing_whenGetPrediction_thenOk() throws Exception {
        AdminViewDto adminViewDto = TEST_ADMIN_VIEW_DTO;
        MvcResult mvcResult = mockMvc.perform(get(ADMIN_VIEW_URI + "/prediction")
                .param("date", adminViewDto.getDate().toString())
                .param("areaId", adminViewDto.getAreaId().toString())
                .param("startTime", adminViewDto.getStartTime().toString())
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(TEST_USER_ADMIN, TEST_ROLES_ADMIN)))
            .andDo(print())
            .andReturn();
        int statusCode = mvcResult.getResponse().getStatus();
        assertEquals(200, statusCode);
    }

    @Test
    @Transactional
    public void givenNothing_whenGetForeCast_thenOk() throws Exception {
        AdminViewDto adminViewDto = TEST_ADMIN_VIEW_DTO;
        MvcResult mvcResult = mockMvc.perform(get(ADMIN_VIEW_URI + "/forecast")
                .param("date", adminViewDto.getDate().toString())
                .param("areaId", adminViewDto.getAreaId().toString())
                .param("startTime", adminViewDto.getStartTime().toString())
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(TEST_USER_ADMIN, TEST_ROLES_ADMIN)))
            .andDo(print())
            .andReturn();
        int statusCode = mvcResult.getResponse().getStatus();
        assertEquals(200, statusCode);
    }

    @Test
    @Transactional
    public void givenNothing_whenGetUnusualReservations_thenOk() throws Exception {
        AdminViewDto adminViewDto = TEST_ADMIN_VIEW_DTO;
        MvcResult mvcResult = mockMvc.perform(get(ADMIN_VIEW_URI + "/unusualReservations")
                .param("date", adminViewDto.getDate().toString())
                .param("areaId", adminViewDto.getAreaId().toString())
                .param("startTime", adminViewDto.getStartTime().toString())
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(TEST_USER_ADMIN, TEST_ROLES_ADMIN)))
            .andDo(print())
            .andReturn();
        int statusCode = mvcResult.getResponse().getStatus();
        assertEquals(200, statusCode);
    }
}