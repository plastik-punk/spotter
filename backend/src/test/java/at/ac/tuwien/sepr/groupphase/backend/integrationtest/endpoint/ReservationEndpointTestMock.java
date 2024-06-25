package at.ac.tuwien.sepr.groupphase.backend.integrationtest.endpoint;

import at.ac.tuwien.sepr.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepr.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationEditDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationListDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationWalkInDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepr.groupphase.backend.entity.Reservation;
import at.ac.tuwien.sepr.groupphase.backend.enums.RoleEnum;
import at.ac.tuwien.sepr.groupphase.backend.repository.ReservationRepository;
import at.ac.tuwien.sepr.groupphase.backend.security.JwtTokenizer;
import at.ac.tuwien.sepr.groupphase.backend.service.ReservationService;
import at.ac.tuwien.sepr.groupphase.backend.service.impl.ApplicationUserServiceImpl;
import at.ac.tuwien.sepr.groupphase.backend.service.mapper.ReservationMapper;
import com.fasterxml.jackson.core.type.TypeReference;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class ReservationEndpointTestMock implements TestData {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ReservationMapper mapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtTokenizer jwtTokenizer;

    @Autowired
    private SecurityProperties securityProperties;

    @MockBean
    private ApplicationUserServiceImpl mockApplicationUserService;

    @MockBean
    private ReservationService mockService;

    @Test
    @Transactional
    public void givenValidId_whenGetByHashedId_thenReturnReservationEditDto() throws Exception {
        String validId = "valid id";
        ReservationEditDto expectedDto = TEST_RESERVATION_EDIT_DTO;

        Mockito.when(mockService.getByHashedId(validId)).thenReturn(expectedDto);

        MvcResult mvcResult = this.mockMvc.perform(get(RESERVATION_BASE_URI + "/detail")
                .param("id", validId)
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(TEST_USER_ADMIN, TEST_ROLES_ADMIN)))
            .andDo(print())
            .andReturn();

        int statusCode = mvcResult.getResponse().getStatus();
        ReservationEditDto actualDto = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ReservationEditDto.class);

        assertAll (
            () -> assertEquals(200, statusCode),
            () -> assertEquals(expectedDto, actualDto)
        );
    }

    @Test
    @Transactional
    public void givenValidSearchParameters_whenSearchAllReservationsForAdmin_thenReturnReservations() throws Exception {
        Reservation reservation = mapper.reservationCreateDtoToReservation(TEST_RESERVATION_CREATE_DTO_CUSTOMER);
        reservation.setHashValue("asdhasdhasdh");
        reservationRepository.save(reservation);

        ApplicationUser admin = ApplicationUser.ApplicationUserBuilder.anApplicationUser()
            .withFirstName(TEST_APPLICATION_USER_FIRST_NAME_EMPLOYEE_1)
            .withLastName(TEST_APPLICATION_USER_LAST_NAME_EMPLOYEE_1)
            .withEmail(TEST_APPLICATION_USER_EMAIL_EMPLOYEE_1)
            .withMobileNumber(TEST_APPLICATION_USER_MOBILE_NUMBER_EMPLOYEE_1)
            .withPassword(TEST_APPLICATION_USER_PASSWORD_EMPLOYEE_1)
            .withRole(RoleEnum.ADMIN)
            .build();

        Mockito.when(mockApplicationUserService.getCurrentApplicationUser()).thenReturn(admin);

        MvcResult mvcResult = mockMvc.perform(get(RESERVATION_BASE_URI + "/search")
                .contentType(MediaType.APPLICATION_JSON)
                .param("earliestDate", TEST_RESERVATION_SEARCH_DTO.getEarliestDate().toString())
                .param("latestDate", TEST_RESERVATION_SEARCH_DTO.getLatestDate().toString())
                .param("earliestStartTime", TEST_RESERVATION_SEARCH_DTO.getEarliestStartTime().toString())
                .param("latestEndTime", TEST_RESERVATION_SEARCH_DTO.getLatestEndTime().toString())
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(TEST_USER_ADMIN, TEST_ROLES_ADMIN)))
            .andDo(print())
            .andReturn();

        int statusCode = mvcResult.getResponse().getStatus();
        List<ReservationListDto> response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<ReservationListDto>>() {});

        assertAll(
            () -> assertEquals(200, statusCode),
            () -> assertNotNull(response)
        );
    }

    @Test
    @Transactional
    public void givenValidReservationWalkInDto_whenCreateWalkIn_thenReturnReservationCreateDto() throws Exception {
        ReservationWalkInDto validDto = TEST_RESERVATION_WALK_IN;
        ReservationCreateDto expectedDto = TEST_RESERVATION_CREATE_DTO_CUSTOMER;

        Mockito.when(mockService.createWalkIn(validDto)).thenReturn(expectedDto);

        MvcResult mvcResult = this.mockMvc.perform(post(RESERVATION_BASE_URI + "/walk-in")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validDto))
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(TEST_USER_ADMIN, TEST_ROLES_ADMIN)))
            .andDo(print())
            .andReturn();

        int statusCode = mvcResult.getResponse().getStatus();
        ReservationCreateDto actualDto = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ReservationCreateDto.class);

        assertAll(
            () -> assertEquals(201, statusCode),
            () -> assertEquals(expectedDto, actualDto)
        );
    }
}