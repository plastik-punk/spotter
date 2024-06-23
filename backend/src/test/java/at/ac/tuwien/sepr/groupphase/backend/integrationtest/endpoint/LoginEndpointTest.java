package at.ac.tuwien.sepr.groupphase.backend.integrationtest.endpoint;

import at.ac.tuwien.sepr.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ApplicationUserOverviewDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepr.groupphase.backend.repository.ApplicationUserRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.ApplicationUserService;
import at.ac.tuwien.sepr.groupphase.backend.service.mapper.ApplicationUserMapper;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class LoginEndpointTest implements TestData {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ApplicationUserService applicationUserService;

    @Autowired
    private ApplicationUserRepository userRepository;

    @Autowired
    private ApplicationUserMapper applicationUserMapper;

    @Test
    @Transactional
    public void givenValidData_whenLogin_thenReturnToken() throws Exception {
        // Create user in database
        ApplicationUser user = TEST_APPLICATION_USER_CUSTOMER_1.copy();
        user.setPassword("$2a$10$o4DxqCDqMIZD/2qjI7MamON3JmZDOrIz3f/DkdH80Z3JK2j59bRRm");
        userRepository.save(user);

        MvcResult mvcResult = this.mockMvc.perform(post(LOGIN_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(TEST_LOGIN_DTO_1)))
            .andDo(print())
            .andReturn();

        int statusCode = mvcResult.getResponse().getStatus();
        String response = mvcResult.getResponse().getContentAsString();
        assertAll(
            () -> assertNotNull(response),
            () -> assertEquals(200, statusCode)
        );
    }

    @Test
    @Transactional
    public void givenValidData_whenLoginEmployee_thenReturnToken() throws Exception {
        // Create user in database
        ApplicationUser user = TEST_APPLICATION_USER_ADMIN.copy();
        user.setPassword("$2a$10$o4DxqCDqMIZD/2qjI7MamON3JmZDOrIz3f/DkdH80Z3JK2j59bRRm");
        userRepository.save(user);

        MvcResult mvcResult = this.mockMvc.perform(post(LOGIN_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(TEST_LOGIN_DTO_1)))
            .andDo(print())
            .andReturn();

        int statusCode = mvcResult.getResponse().getStatus();
        String response = mvcResult.getResponse().getContentAsString();
        assertAll(
            () -> assertNotNull(response),
            () -> assertEquals(200, statusCode)
        );
    }

    // TODO: Fix this test
    // @Test
    @Transactional
    public void givenNothing_whenGetCurrentUserDetails_thenReturnUserDetails() throws Exception {
        // Create user in database
        ApplicationUser user = TEST_APPLICATION_USER_CUSTOMER_1;
        user.setPassword("$2a$10$o4DxqCDqMIZD/2qjI7MamON3JmZDOrIz3f/DkdH80Z3JK2j59bRRm");
        userRepository.save(user);

        // log user in
        this.mockMvc.perform(post(LOGIN_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(TEST_LOGIN_DTO_1)))
            .andDo(print())
            .andReturn();

        ApplicationUserOverviewDto fetchedUser = applicationUserMapper.applicationUserToUserOverviewDto(applicationUserService.getCurrentApplicationUser());

        assertAll(
            () -> assertEquals(TEST_APPLICATION_USER_CUSTOMER_1.getFirstName(), fetchedUser.getFirstName()),
            () -> assertEquals(TEST_APPLICATION_USER_CUSTOMER_1.getLastName(), fetchedUser.getLastName()),
            () -> assertEquals(TEST_APPLICATION_USER_CUSTOMER_1.getEmail(), fetchedUser.getEmail())
        );
    }
}