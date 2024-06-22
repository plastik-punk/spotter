package at.ac.tuwien.sepr.groupphase.backend.endpoint;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ApplicationUserLoginDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ApplicationUserOverviewDto;
import at.ac.tuwien.sepr.groupphase.backend.service.ApplicationUserService;
import at.ac.tuwien.sepr.groupphase.backend.service.mapper.ApplicationUserMapper;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
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
    private final ApplicationUserMapper applicationUserMapper;

    public LoginEndpoint(ApplicationUserService applicationUserService, ApplicationUserMapper applicationUserMapper) {
        this.applicationUserService = applicationUserService;
        this.applicationUserMapper = applicationUserMapper;
    }

    @PermitAll
    @PostMapping
    public String login(@Valid @RequestBody ApplicationUserLoginDto applicationUserLoginDto) {
        LOGGER.info("POST /api/v1/authentication body: {}", applicationUserLoginDto);
        return applicationUserService.login(applicationUserLoginDto);
    }

    @GetMapping
    @PermitAll
    public ApplicationUserOverviewDto getCurrentUserDetails() {
        LOGGER.info("GET /api/v1/authentication");
        ApplicationUserOverviewDto fetchedUser = applicationUserMapper.applicationUserToUserOverviewDto(applicationUserService.getCurrentApplicationUser());
        return fetchedUser;
    }

}

