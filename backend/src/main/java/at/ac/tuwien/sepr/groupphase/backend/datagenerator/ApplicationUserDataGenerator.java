package at.ac.tuwien.sepr.groupphase.backend.datagenerator;

import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepr.groupphase.backend.enums.RoleEnum;
import at.ac.tuwien.sepr.groupphase.backend.repository.ApplicationUserRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.List;

@Profile({"generateData", "test"})
@Component
@Order(2)
public class ApplicationUserDataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final PasswordEncoder passwordEncoder;
    private static final int NUMBER_OF_USERS_TO_GENERATE = 6;
    private static final String[] FIRST_NAMES = {"AdminSpotter", "EmployeeSpotter", "CustomerSpotter", "GuestSpotter",
        "UnconfirmedA", "UnconfirmedE"};
    private static final String[] LAST_NAMES = {"Smith", "Johnson", "Williams", "Brown", "Jones", "Gitti"};
    private static final String EMAIL_DOMAIN = "@example.com";
    private static final String[] MOBILE_NUMBERS = {"123456789", "987654321", "112233445", "556677889", "998877665", null};
    private static final String DEFAULT_PASSWORD = "password123";

    private static final RoleEnum[] ROLES = {RoleEnum.ADMIN, RoleEnum.EMPLOYEE, RoleEnum.CUSTOMER, RoleEnum.GUEST,
        RoleEnum.UNCONFIRMED_ADMIN, RoleEnum.UNCONFIRMED_EMPLOYEE};

    private final ApplicationUserRepository applicationUserRepository;

    public ApplicationUserDataGenerator(ApplicationUserRepository applicationUserRepository, PasswordEncoder passwordEncoder) {
        this.applicationUserRepository = applicationUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    private void generateUsers() {
        LOGGER.trace("generateUsers");
        List<ApplicationUser> existingUsers = applicationUserRepository.findAll();
        if (!applicationUserRepository.findAll().isEmpty() && !(existingUsers.size() == 1 && existingUsers.get(0).getId() == 0)) {
            LOGGER.debug("Users already generated");
        } else {
            LOGGER.debug("Generating {} user entries", NUMBER_OF_USERS_TO_GENERATE);
            for (int i = 0; i < NUMBER_OF_USERS_TO_GENERATE; i++) {
                ApplicationUser applicationUser = ApplicationUser.ApplicationUserBuilder.anApplicationUser()
                    .withFirstName(FIRST_NAMES[i % FIRST_NAMES.length])
                    .withLastName(LAST_NAMES[i % LAST_NAMES.length])
                    .withEmail(FIRST_NAMES[i % FIRST_NAMES.length].toLowerCase() + EMAIL_DOMAIN)
                    .withMobileNumber(MOBILE_NUMBERS[i % MOBILE_NUMBERS.length])
                    .withRole(ROLES[i % ROLES.length])
                    .withPassword(passwordEncoder.encode(DEFAULT_PASSWORD))
                    .build();

                LOGGER.debug("Saving applicationUser {}", applicationUser);
                applicationUserRepository.save(applicationUser);
            }
        }
    }
}