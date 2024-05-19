package at.ac.tuwien.sepr.groupphase.backend.endpoint;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.UserLoginDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.UserOverviewDto;
import at.ac.tuwien.sepr.groupphase.backend.service.UserService;
import at.ac.tuwien.sepr.groupphase.backend.service.mapper.UserMapper;
import jakarta.annotation.security.PermitAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.parameters.P;
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

    private final UserService userService;
    private final UserMapper userMapper;

    public LoginEndpoint(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @PermitAll
    @PostMapping
    public String login(@RequestBody UserLoginDto userLoginDto) {
        LOGGER.info("POST /api/v1/authentication body: {}", userLoginDto);
        return userService.login(userLoginDto);
    }

    @GetMapping
    @PermitAll
    public UserOverviewDto getCurrentUserDetails() {
        LOGGER.info("GET /api/v1/authentication");
        UserOverviewDto fetchedUser = userMapper.applicationUserToUserOverviewDto(userService.getCurrentUser());
        return fetchedUser;
    }

}

