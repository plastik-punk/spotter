package at.ac.tuwien.sepr.groupphase.backend.datagenerator;

import at.ac.tuwien.sepr.groupphase.backend.entity.User;
import at.ac.tuwien.sepr.groupphase.backend.enums.RoleEnum;
import at.ac.tuwien.sepr.groupphase.backend.repository.IUserRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;

@Profile("generateData")
@Component
public class UserDataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final int NUMBER_OF_USERS_TO_GENERATE = 5;
    private static final String[] FIRST_NAMES = {"Alice", "Bob", "Charlie", "Diana", "Eve"};
    private static final String[] LAST_NAMES = {"Smith", "Johnson", "Williams", "Brown", "Jones"};
    private static final String EMAIL_DOMAIN = "@example.com";
    private static final long[] MOBILE_NUMBERS = {123456789L, 987654321L, 112233445L, 556677889L, 998877665L};
    private static final String DEFAULT_PASSWORD = "password123";

    private static final RoleEnum[] ROLES = {RoleEnum.ADMIN, RoleEnum.EMPLOYEE, RoleEnum.CUSTOMER, RoleEnum.GUEST};

    private final IUserRepository IuserRepository;

    public UserDataGenerator(IUserRepository userRepository) {
        this.IuserRepository = userRepository;
    }

    @PostConstruct
    private void generateUsers() {
        if (!IuserRepository.findAll().isEmpty()) {
            LOGGER.debug("Users already generated");
        } else {
            LOGGER.debug("Generating {} user entries", NUMBER_OF_USERS_TO_GENERATE);
            for (int i = 0; i < NUMBER_OF_USERS_TO_GENERATE; i++) {
                User user = User.UserBuilder.anUser()
                    .withFirstName(FIRST_NAMES[i % FIRST_NAMES.length])
                    .withLastName(LAST_NAMES[i % LAST_NAMES.length])
                    .witheMail(FIRST_NAMES[i % FIRST_NAMES.length].toLowerCase() + EMAIL_DOMAIN)
                    .withMobileNumber(MOBILE_NUMBERS[i % MOBILE_NUMBERS.length])
                    .withPassword(DEFAULT_PASSWORD)
                    .withRole(ROLES[i % ROLES.length])
                    .build();
                LOGGER.debug("Saving user {}", user);
                IuserRepository.save(user);
            }
        }
    }

}
