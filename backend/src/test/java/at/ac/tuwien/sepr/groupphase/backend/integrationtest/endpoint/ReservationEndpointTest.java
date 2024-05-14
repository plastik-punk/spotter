package at.ac.tuwien.sepr.groupphase.backend.integrationtest.endpoint;

import at.ac.tuwien.sepr.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepr.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.repository.ReservationRepository;
import at.ac.tuwien.sepr.groupphase.backend.security.JwtTokenizer;
import at.ac.tuwien.sepr.groupphase.backend.service.mapper.ReservationMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
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

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    private ReservationMapper reservationMapper;

    @Autowired
    private JwtTokenizer jwtTokenizer;

    @Autowired
    private SecurityProperties securityProperties;

    private ReservationCreateDto customerReservationCreateDto;
    private ReservationCreateDto guestReservationCreateDto;

    @BeforeEach
    public void setup() {
        reservationRepository.deleteAll();

        customerReservationCreateDto = ReservationCreateDto.ReservationCreateDtoBuilder.aReservationCreateDto()
            .withApplicationUser(TEST_RESERVATION_APPLICATION_USER_CUSTOMER)
            .withFirstName(TEST_RESERVATION_APPLICATION_USER_CUSTOMER.getFirstName())
            .withLastName(TEST_RESERVATION_APPLICATION_USER_CUSTOMER.getLastName())
            .withStartTime(TEST_RESERVATION_START_TIME)
            .withEndTime(TEST_RESERVATION_END_TIME)
            .withDate(TEST_RESERVATION_DATE)
            .withPax(TEST_RESERVATION_PAX)
            .withNotes(TEST_RESERVATION_NOTES)
            .withEmail(TEST_RESERVATION_APPLICATION_USER_CUSTOMER.getEmail())
            .withMobileNumber(TEST_RESERVATION_APPLICATION_USER_CUSTOMER.getMobileNumber())
            .build();

        guestReservationCreateDto = ReservationCreateDto.ReservationCreateDtoBuilder.aReservationCreateDto()
            .withApplicationUser(TEST_RESERVATION_APPLICATION_USER_GUEST)
            .withFirstName(TEST_RESERVATION_APPLICATION_USER_GUEST.getFirstName())
            .withLastName(TEST_RESERVATION_APPLICATION_USER_GUEST.getLastName())
            .withStartTime(TEST_RESERVATION_START_TIME)
            .withEndTime(TEST_RESERVATION_END_TIME)
            .withDate(TEST_RESERVATION_DATE)
            .withPax(TEST_RESERVATION_PAX)
            .withNotes(TEST_RESERVATION_NOTES)
            .withEmail(TEST_RESERVATION_APPLICATION_USER_GUEST.getEmail())
            .withMobileNumber(TEST_RESERVATION_APPLICATION_USER_GUEST.getMobileNumber())
            .build();
    }

    @Test
    public void givenReservationCreateDto_whenCreateForCustomer_thenReservationIsCreated() throws Exception {
        // given: s. TestData

        // when
        String body = objectMapper.writeValueAsString(customerReservationCreateDto);
        MvcResult mvcResult = this.mockMvc.perform(post(RESERVATION_BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andDo(print())
            .andReturn();

        // then
        ReservationCreateDto response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ReservationCreateDto.class);
        assertNotNull(response);

        assertAll(
            () -> assertEquals(TEST_RESERVATION_APPLICATION_USER_CUSTOMER.getFirstName(), response.getFirstName()),
            () -> assertEquals(TEST_RESERVATION_APPLICATION_USER_CUSTOMER.getLastName(), response.getLastName()),
            () -> assertEquals(TEST_RESERVATION_APPLICATION_USER_CUSTOMER.getEmail(), response.getEmail()),
            () -> assertEquals(TEST_RESERVATION_APPLICATION_USER_CUSTOMER.getMobileNumber(), response.getMobileNumber()),
            () -> assertEquals(TEST_RESERVATION_START_TIME, response.getStartTime()),
            () -> assertEquals(TEST_RESERVATION_DATE, response.getDate()),
            () -> assertEquals(TEST_RESERVATION_END_TIME, response.getEndTime()),
            () -> assertEquals(TEST_RESERVATION_PAX, response.getPax()),
            () -> assertEquals(TEST_RESERVATION_NOTES, response.getNotes())
        );
    }
}