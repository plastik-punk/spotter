package at.ac.tuwien.sepr.groupphase.backend.endpoint;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.UserRegistrationDto;
import at.ac.tuwien.sepr.groupphase.backend.service.UserService;
import jakarta.annotation.security.PermitAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;

@RestController
@RequestMapping(value = "/api/v1/registration")
public class RegistrationEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final UserService userService;

    public RegistrationEndpoint(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @PermitAll
    public ResponseEntity<Void> register(@RequestBody UserRegistrationDto userRegistrationDto) {
        LOGGER.info("POST /api/v1/registration body: {}", userRegistrationDto);
        userService.register(userRegistrationDto);
        return ResponseEntity.status(201).build();
    }
}

