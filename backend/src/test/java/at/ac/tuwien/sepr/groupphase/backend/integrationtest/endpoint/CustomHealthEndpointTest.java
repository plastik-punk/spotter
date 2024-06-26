package at.ac.tuwien.sepr.groupphase.backend.integrationtest.endpoint;

import at.ac.tuwien.sepr.groupphase.backend.basetest.TestData;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class CustomHealthEndpointTest implements TestData {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void givenNothing_whenGetHealth_thenReturnOK() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/health")
                .accept(MediaType.APPLICATION_JSON))
            .andReturn();

        assertAll(
            () -> assertEquals(200, mvcResult.getResponse().getStatus()),
            () -> assertEquals("OK", mvcResult.getResponse().getContentAsString())
        );
    }

    @Test
    public void givenNothing_whenPreShutdown_thenGetHealthReturns500() throws Exception {
        // Call the preShutdown method
        MvcResult mvcResultPreShutdown = mockMvc.perform(MockMvcRequestBuilders.get("/health/prepareShutdown")
                .accept(MediaType.APPLICATION_JSON))
            .andReturn();

        // Call the getHealth method again
        MvcResult mvcResultGetHealth = mockMvc.perform(MockMvcRequestBuilders.get("/health")
                .accept(MediaType.APPLICATION_JSON))
            .andReturn();

        assertAll(
            () -> assertEquals(200, mvcResultPreShutdown.getResponse().getStatus()),
            () -> assertEquals(500, mvcResultGetHealth.getResponse().getStatus())
        );
    }
}