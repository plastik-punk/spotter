package at.ac.tuwien.sepr.groupphase.backend.endpoint;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ApplicationUserLoginDto;
import at.ac.tuwien.sepr.groupphase.backend.service.ApplicationUserService;
import jakarta.annotation.security.PermitAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;

@RestController
@RequestMapping(value = "/api/v1/authentication")
public class LoginEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final ApplicationUserService applicationUserService;

    public LoginEndpoint(ApplicationUserService applicationUserService) {
        this.applicationUserService = applicationUserService;
    }

    @PermitAll
    @PostMapping
    public String login(@RequestBody ApplicationUserLoginDto applicationUserLoginDto) {
        LOGGER.info("POST /api/v1/authentication body: {}", applicationUserLoginDto);
        return applicationUserService.login(applicationUserLoginDto);
    }
}

