package at.ac.tuwien.sepr.groupphase.backend.datagenerator;

import at.ac.tuwien.sepr.groupphase.backend.enums.RoleEnum;
import at.ac.tuwien.sepr.groupphase.backend.repository.ApplicationUserRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class DefaultDataGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final PasswordEncoder passwordEncoder;
    private final ApplicationUserRepository applicationUserRepository;

    public DefaultDataGenerator(ApplicationUserRepository applicationUserRepository, PasswordEncoder passwordEncoder) {
        this.applicationUserRepository = applicationUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    private void generateDefaultData() {
        LOGGER.trace("generateDefaultData");
        if (applicationUserRepository.existsById(0L)) {
            LOGGER.debug("Walk-in User already generated");
        } else {
            LOGGER.debug("Generating Walk-in User");
            String firstName = "WalkIn";
            String lastName = "Customers";
            String email = "walk-in-customers@example.com";
            String mobileNumber = null;
            String password = passwordEncoder.encode("password123");
            RoleEnum role = RoleEnum.GUEST;

            LOGGER.debug("Saving applicationUser with ID 0");
            applicationUserRepository.insertUserWithId(0L, firstName, lastName, email, mobileNumber, password, role);
        }
    }
}
