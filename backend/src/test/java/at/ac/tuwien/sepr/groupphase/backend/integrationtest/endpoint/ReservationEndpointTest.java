package at.ac.tuwien.sepr.groupphase.backend.integrationtest.endpoint;

import at.ac.tuwien.sepr.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepr.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.Reservation;
import at.ac.tuwien.sepr.groupphase.backend.enums.ReservationResponseEnum;
import at.ac.tuwien.sepr.groupphase.backend.repository.ReservationRepository;
import at.ac.tuwien.sepr.groupphase.backend.security.JwtTokenizer;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class ReservationEndpointTest implements TestData {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtTokenizer jwtTokenizer;

    @Autowired
    private SecurityProperties securityProperties;

    @Test
    @Transactional
    public void givenReservationCreateDto_whenCreateForGuest_thenReservationAndGuestIsCreated() throws Exception {
        // when
        String body = objectMapper.writeValueAsString(TEST_RESERVATION_CREATE_DTO_GUEST);
        MvcResult mvcResult = this.mockMvc.perform(post(RESERVATION_BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andDo(print())
            .andReturn();

        // then
        int statusCode = mvcResult.getResponse().getStatus();
        ReservationCreateDto response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ReservationCreateDto.class);
        assertNotNull(response);

        assertAll(
            () -> assertEquals(201, statusCode),
            () -> assertEquals(TEST_APPLICATION_USER_FIRST_NAME, response.getFirstName()),
            () -> assertEquals(TEST_APPLICATION_USER_LAST_NAME, response.getLastName()),
            () -> assertEquals(TEST_RESERVATION_START_TIME, response.getStartTime()),
            () -> assertEquals(TEST_RESERVATION_END_TIME, response.getEndTime()),
            () -> assertEquals(TEST_RESERVATION_DATE, response.getDate()),
            () -> assertEquals(TEST_RESERVATION_PAX, response.getPax()),
            () -> assertEquals(TEST_RESERVATION_NOTES, response.getNotes()),
            () -> assertEquals(TEST_APPLICATION_USER_EMAIL, response.getEmail()),
            () -> assertEquals(TEST_APPLICATION_USER_MOBILE_NUMBER, response.getMobileNumber())
        );
    }

    @Test
    @Transactional
    public void givenReservationDetailDto_whenUpdate_thenReservationIsUpdated() throws Exception {
        Reservation savedReservationId = reservationRepository.save(TEST_RESERVATION_1);
        TEST_RESERVATION_DETAIL_DTO.setId(savedReservationId.getId());

        // when
        String body = objectMapper.writeValueAsString(TEST_RESERVATION_DETAIL_DTO);
        MvcResult mvcResult = this.mockMvc.perform(put(RESERVATION_BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(TEST_USER_CUSTOMER, TEST_ROLES_CUSTOMER)))
            .andDo(print())
            .andReturn();

        // then
        int statusCode = mvcResult.getResponse().getStatus();
        ReservationDetailDto response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ReservationDetailDto.class);
        assertNotNull(response);

        assertAll(
            () -> assertEquals(200, statusCode),
            () -> assertEquals(TEST_RESERVATION_DETAIL_ID, response.getId()),
            () -> assertEquals(TEST_RESERVATION_START_TIME, response.getStartTime()),
            () -> assertEquals(TEST_RESERVATION_END_TIME, response.getEndTime()),
            () -> assertEquals(TEST_RESERVATION_DATE, response.getDate()),
            () -> assertEquals(TEST_RESERVATION_PAX, response.getPax()),
            () -> assertEquals(TEST_RESERVATION_NOTES, response.getNotes()),
            () -> assertEquals(TEST_PLACE_AVAILABLE_1.getId(), response.getPlaceId())
        );
    }

    @Test
    @Transactional
    public void givenReservationCreateDto_whenCreateForCustomer_thenReservationIsCreated() throws Exception {
        // when
        String body = objectMapper.writeValueAsString(TEST_RESERVATION_CREATE_DTO_CUSTOMER);
        MvcResult mvcResult = this.mockMvc.perform(post(RESERVATION_BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(TEST_USER_CUSTOMER, TEST_ROLES_CUSTOMER)))
            .andDo(print())
            .andReturn();

        // then
        int statusCode = mvcResult.getResponse().getStatus();
        ReservationCreateDto response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ReservationCreateDto.class);
        assertNotNull(response);

        assertAll(
            () -> assertEquals(201, statusCode),
            () -> assertEquals(TEST_APPLICATION_USER_CUSTOMER_1.getFirstName(), response.getFirstName()),
            () -> assertEquals(TEST_APPLICATION_USER_CUSTOMER_1.getLastName(), response.getLastName()),
            () -> assertEquals(TEST_APPLICATION_USER_CUSTOMER_1.getEmail(), response.getEmail()),
            () -> assertEquals(TEST_APPLICATION_USER_CUSTOMER_1.getMobileNumber(), response.getMobileNumber()),
            () -> assertEquals(TEST_RESERVATION_START_TIME, response.getStartTime()),
            () -> assertEquals(TEST_RESERVATION_DATE, response.getDate()),
            () -> assertEquals(TEST_RESERVATION_END_TIME, response.getEndTime()),
            () -> assertEquals(TEST_RESERVATION_PAX, response.getPax()),
            () -> assertEquals(TEST_RESERVATION_NOTES, response.getNotes())
        );
    }

    @Test
    @Transactional
    public void givenValidData_whenGetAvailability_thenReturnAvailable() throws Exception {
        // when
        MvcResult mvcResult = this.mockMvc.perform(get(RESERVATION_BASE_URI)
                .param("startTime", TEST_RESERVATION_AVAILABILITY.getStartTime().toString())
                .param("endTime", TEST_RESERVATION_AVAILABILITY.getEndTime().toString())
                .param("date", TEST_RESERVATION_AVAILABILITY.getDate().toString())
                .param("pax", TEST_RESERVATION_AVAILABILITY.getPax().toString())
                .param("idToExclude", TEST_RESERVATION_AVAILABILITY.getIdToExclude().toString())
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(TEST_USER_CUSTOMER, TEST_ROLES_CUSTOMER)))
            .andDo(print())
            .andReturn();

        // then
        int statusCode = mvcResult.getResponse().getStatus();
        ReservationResponseEnum response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ReservationResponseEnum.class);
        assertNotNull(response);
        assertAll(
            () -> assertEquals(200, statusCode),
            () -> assertEquals(ReservationResponseEnum.AVAILABLE, response)
        );
    }
}