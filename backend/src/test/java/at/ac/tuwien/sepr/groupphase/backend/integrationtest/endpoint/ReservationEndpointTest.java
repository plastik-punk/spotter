package at.ac.tuwien.sepr.groupphase.backend.integrationtest.endpoint;

import at.ac.tuwien.sepr.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepr.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationCheckAvailabilityDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationEditDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepr.groupphase.backend.entity.Reservation;
import at.ac.tuwien.sepr.groupphase.backend.enums.ReservationResponseEnum;
import at.ac.tuwien.sepr.groupphase.backend.repository.ApplicationUserRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.PlaceRepository;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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
    private ApplicationUserRepository userRepository;

    @Autowired
    private JwtTokenizer jwtTokenizer;

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private PlaceRepository placeRepository;

    @Test
    @Transactional
    public void givenReservationCreateDto_whenCreateForGuest_thenReservationAndGuestIsCreated() throws Exception {
        String body = objectMapper.writeValueAsString(TEST_RESERVATION_CREATE_DTO_GUEST);
        MvcResult mvcResult = this.mockMvc.perform(post(RESERVATION_BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andDo(print())
            .andReturn();

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
    public void givenReservationEditDto_whenUpdate_thenReservationIsUpdated() throws Exception {
        placeRepository.save(TEST_PLACE_AVAILABLE_1);
        placeRepository.save(TEST_PLACE_AVAILABLE_2);
        Reservation savedReservationId = reservationRepository.save(TEST_RESERVATION_1);
        TEST_RESERVATION_EDIT_DTO.setReservationId(savedReservationId.getId());
        TEST_RESERVATION_EDIT_DTO.setHashedId(savedReservationId.getHashValue());

        String body = objectMapper.writeValueAsString(TEST_RESERVATION_EDIT_DTO);
        MvcResult mvcResult = this.mockMvc.perform(put(RESERVATION_BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(TEST_USER_CUSTOMER, TEST_ROLES_CUSTOMER)))
            .andDo(print())
            .andReturn();

        int statusCode = mvcResult.getResponse().getStatus();
        ReservationEditDto response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ReservationEditDto.class);
        assertNotNull(response);

        assertAll(
            () -> assertEquals(200, statusCode),
            () -> assertEquals(savedReservationId.getId(), response.getReservationId()),
            () -> assertEquals(savedReservationId.getStartTime(), response.getStartTime()),
            () -> assertEquals(savedReservationId.getEndTime(), response.getEndTime()),
            () -> assertEquals(savedReservationId.getDate(), response.getDate()),
            () -> assertEquals(savedReservationId.getPax(), response.getPax()),
            () -> assertEquals(savedReservationId.getNotes(), response.getNotes()),
            () -> assertEquals(TEST_RESERVATION_HASH_VALUE_1, response.getHashedId()),
            () -> assertEquals(TEST_PLACE_IDS, response.getPlaceIds())
        );
    }

    @Test
    @Transactional
    public void givenReservationCreateDto_whenCreateForCustomer_thenReservationIsCreated() throws Exception {
        String body = objectMapper.writeValueAsString(TEST_RESERVATION_CREATE_DTO_CUSTOMER);
        MvcResult mvcResult = this.mockMvc.perform(post(RESERVATION_BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(TEST_USER_CUSTOMER, TEST_ROLES_CUSTOMER)))
            .andDo(print())
            .andReturn();

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

    @Test
    @Transactional
    public void givenValidId_whenDelete_thenNoContent() throws Exception {
        ApplicationUser user = userRepository.save(TEST_APPLICATION_USER_CUSTOMER_1);

        TEST_RESERVATION_TO_DELETE.setUser(user);
        TEST_RESERVATION_TO_DELETE.setHashValue(TEST_RESERVATION_HASH_VALUE_2);
        Reservation savedReservation = reservationRepository.save(TEST_RESERVATION_TO_DELETE);

        MvcResult mvcResult = this.mockMvc.perform(delete(RESERVATION_BASE_URI)
                .content(savedReservation.getHashValue())
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(user.getEmail(), TEST_ROLES_CUSTOMER)))
            .andDo(print())
            .andReturn();
        int statusCode = mvcResult.getResponse().getStatus();

        mvcResult = this.mockMvc.perform(get(RESERVATION_BASE_URI + "/detail")
                .param("id", savedReservation.getHashValue())
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(user.getEmail(), TEST_ROLES_CUSTOMER)))
            .andDo(print())
            .andReturn();
        int statusCode2 = mvcResult.getResponse().getStatus();

        assertAll(
            () -> assertEquals(204, statusCode),
            () -> assertEquals(404, statusCode2)
        );
    }

    @Test
    @Transactional
    public void givenValidReservation_whenGetNextAvailableTables_thenReturnAvailableTables() throws Exception {
        // Given Valid Reservation
        ReservationCheckAvailabilityDto reservationCheckAvailabilityDto = ReservationCheckAvailabilityDto.ReservationCheckAvailabilityDtoBuilder.aReservationCheckAvailabilityDto()
            .withStartTime(TEST_RESERVATION_AVAILABILITY.getStartTime())
            .withDate(TEST_RESERVATION_AVAILABILITY.getDate())
            .withPax(TEST_RESERVATION_AVAILABILITY.getPax())
            .withIdToExclude(TEST_RESERVATION_AVAILABILITY.getIdToExclude())
            .build();

        // When a get request is made with the valid reservation
        MvcResult mvcResult = this.mockMvc.perform(get(RESERVATION_BASE_URI + "/next")
                .param("startTime", reservationCheckAvailabilityDto.getStartTime().toString())
                .param("date", reservationCheckAvailabilityDto.getDate().toString())
                .param("pax", reservationCheckAvailabilityDto.getPax().toString())
                .param("idToExclude", reservationCheckAvailabilityDto.getIdToExclude().toString())
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(TEST_USER_CUSTOMER, TEST_ROLES_CUSTOMER)))
            .andDo(print())
            .andReturn();

        // Then the response status should be 200 (OK) and there should be 3 available tables
        ReservationCheckAvailabilityDto[] response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ReservationCheckAvailabilityDto[].class);
        assertAll(
            () -> assertEquals(200, mvcResult.getResponse().getStatus()),
            () -> assertEquals(3, response.length)
        );
    }

    @Test
    @Transactional
    public void givenInvalidReservation_whenGetNextAvailableTables_thenReturnBadRequest() throws Exception {
        // Given Invalid Reservation
        ReservationCheckAvailabilityDto reservationCheckAvailabilityDto =
            ReservationCheckAvailabilityDto.ReservationCheckAvailabilityDtoBuilder.aReservationCheckAvailabilityDto()
                .withStartTime(TEST_RESERVATION_AVAILABILITY.getStartTime())
                .withDate(null)
                .withPax(TEST_RESERVATION_AVAILABILITY.getPax())
                .withIdToExclude(TEST_RESERVATION_AVAILABILITY.getIdToExclude())
                .build();

        // When a get request is made with the invalid reservation
        MvcResult mvcResult = this.mockMvc.perform(get(RESERVATION_BASE_URI + "/next")
                .param("startTime", reservationCheckAvailabilityDto.getStartTime().toString())
                .param("pax", reservationCheckAvailabilityDto.getPax().toString())
                .param("idToExclude", reservationCheckAvailabilityDto.getIdToExclude().toString())
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(TEST_USER_CUSTOMER, TEST_ROLES_CUSTOMER)))
            .andDo(print())
            .andReturn();

        // Then the response status should be 400 (Bad Request)
        int statusCode = mvcResult.getResponse().getStatus();
        assertEquals(422, statusCode);
    }
}