package at.ac.tuwien.sepr.groupphase.backend.integrationtest.endpoint;

import at.ac.tuwien.sepr.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepr.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ApplicationUserOverviewDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepr.groupphase.backend.repository.ApplicationUserRepository;
import at.ac.tuwien.sepr.groupphase.backend.security.JwtTokenizer;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class StaffAccountEndpointTest implements TestData {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtTokenizer jwtTokenizer;

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private ApplicationUserRepository applicationUserRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Transactional
    public void givenNothing_whenFindAll_thenReturnAll() throws Exception {
        ApplicationUser employee1 = applicationUserRepository.save(TEST_REGISTRATION_EMPLOYEE_1);
        ApplicationUser employee2 = applicationUserRepository.save(TEST_REGISTRATION_EMPLOYEE_2);

        MvcResult mvcResult = mockMvc.perform(get(STAFF_ACCOUNT_URI)
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(TEST_USER_ADMIN, TEST_ROLES_ADMIN)))
            .andDo(print())
            .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        List<ApplicationUserOverviewDto> users = objectMapper.readValue(content, new TypeReference<List<ApplicationUserOverviewDto>>() {});

        // Filter the list to find the employees with the matching emails
        ApplicationUserOverviewDto employee1Dto = users.stream()
            .filter(user -> user.getEmail().equals(employee1.getEmail()))
            .findFirst()
            .orElse(null);

        ApplicationUserOverviewDto employee2Dto = users.stream()
            .filter(user -> user.getEmail().equals(employee2.getEmail()))
            .findFirst()
            .orElse(null);

        assertAll(
            () -> assertTrue(users.size() >= 2),
            () -> assertEquals(employee1.getEmail(), employee1Dto.getEmail()),
            () -> assertEquals(employee1.getRole(), employee1Dto.getRole()),
            () -> assertEquals(employee2.getEmail(), employee2Dto.getEmail()),
            () -> assertEquals(employee2.getRole(), employee2Dto.getRole())
        );
    }

    @Test
    @Transactional
    public void givenUser_whenUpdate_thenUserIsUpdated() throws Exception {
        ApplicationUser employee = applicationUserRepository.save(TEST_REGISTRATION_EMPLOYEE_1);

        ApplicationUserOverviewDto updatedEmployee = ApplicationUserOverviewDto.ApplicationUserOverviewDtoBuilder.anApplicationUserOverviewDto()
            .withId(employee.getId())
            .withFirstName("Hans")
            .withLastName("Baxter")
            .withEmail(employee.getEmail())
            .withMobileNumber(employee.getMobileNumber())
            .withRole(employee.getRole())
            .build();

        mockMvc.perform(put(STAFF_ACCOUNT_URI + "/" + employee.getId())
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(TEST_USER_ADMIN, TEST_ROLES_ADMIN))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        MvcResult mvcResult = mockMvc.perform(get(STAFF_ACCOUNT_URI)
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(TEST_USER_ADMIN, TEST_ROLES_ADMIN)))
            .andDo(print())
            .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        List<ApplicationUserOverviewDto> users = objectMapper.readValue(content, new TypeReference<List<ApplicationUserOverviewDto>>() {});

        ApplicationUserOverviewDto updatedEmployeeDto = users.stream()
            .filter(user -> user.getEmail().equals(employee.getEmail()))
            .findFirst()
            .orElse(null);

        assertAll(
            () -> assertEquals(updatedEmployee.getFirstName(), updatedEmployeeDto.getFirstName()),
            () -> assertEquals(updatedEmployee.getLastName(), updatedEmployeeDto.getLastName())
        );
    }

    @Test
    @Transactional
    public void givenUser_whenDelete_thenUserIsDeleted() throws Exception {
        ApplicationUser employee = applicationUserRepository.save(TEST_REGISTRATION_EMPLOYEE_1);

        MvcResult mvcResult = mockMvc.perform(delete(STAFF_ACCOUNT_URI + "/" + employee.getId())
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(TEST_USER_ADMIN, TEST_ROLES_ADMIN)))
            .andDo(print())
            .andReturn();
        int statusCode = mvcResult.getResponse().getStatus();

        MvcResult mvcResult2 = mockMvc.perform(get(STAFF_ACCOUNT_URI)
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(TEST_USER_ADMIN, TEST_ROLES_ADMIN)))
            .andDo(print())
            .andReturn();

        String content = mvcResult2.getResponse().getContentAsString();
        List<ApplicationUserOverviewDto> users = objectMapper.readValue(content, new TypeReference<List<ApplicationUserOverviewDto>>() {});

        ApplicationUserOverviewDto foundEmployee = users.stream()
            .filter(user -> user.getEmail().equals(employee.getEmail()))
            .findFirst()
            .orElse(null);

        assertAll(
            () -> assertNull(foundEmployee),
            () -> assertEquals(204, statusCode)
        );
    }
}