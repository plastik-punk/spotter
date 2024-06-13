package at.ac.tuwien.sepr.groupphase.backend.unittests.repository;

import at.ac.tuwien.sepr.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepr.groupphase.backend.enums.RoleEnum;
import at.ac.tuwien.sepr.groupphase.backend.repository.ApplicationUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
public class ApplicationUserRepositoryTest implements TestData {

    @Autowired
    private ApplicationUserRepository applicationUserRepository;

    @BeforeEach
    public void clearDatabase() {
        applicationUserRepository.deleteAll();
    }

    @Test
    @Transactional
    public void givenValidData_whenFindByEmail_thenReturnUser() {
        ApplicationUser user = applicationUserRepository.save(TEST_APPLICATION_USER_CUSTOMER_1);
        ApplicationUser found = applicationUserRepository.findByEmail(TEST_APPLICATION_USER_CUSTOMER_1.getEmail());

        assertEquals(found, user);
    }

    @Test
    @Transactional
    public void givenValidData_whenFindRoleInOrderByFirstNameAsc_thenReturnUsersInOrder() {
        applicationUserRepository.save(TEST_APPLICATION_USER_CUSTOMER_1);
        applicationUserRepository.save(TEST_APPLICATION_USER_CUSTOMER_3);

        List<RoleEnum> roles = new ArrayList<>();
        roles.add(RoleEnum.CUSTOMER);

        List<ApplicationUser> users = applicationUserRepository.findByRoleInOrderByFirstNameAsc(roles);

        assertAll(
            () -> assertEquals(((List<?>) users).get(0), TEST_APPLICATION_USER_CUSTOMER_3),
            () -> assertEquals(users.get(1), TEST_APPLICATION_USER_CUSTOMER_1)
        );
    }

    @Test
    @Transactional
    public void givenValidData_whenFindAllByRole_thenReturnUsers() {
        ApplicationUser user1 = applicationUserRepository.save(TEST_APPLICATION_USER_CUSTOMER_1);
        ApplicationUser user2 = applicationUserRepository.save(TEST_APPLICATION_USER_CUSTOMER_3);
        ApplicationUser user3 = applicationUserRepository.save(TEST_APPLICATION_USER_GUEST_2);

        List<ApplicationUser> users = applicationUserRepository.findAllByRole(RoleEnum.CUSTOMER);

        assertAll(
            () -> assertEquals(2, users.size(), "Users list should contain exactly 2 items"),
            () -> assertEquals(((List<?>) users).get(0), user1),
            () -> assertEquals(users.get(1), user2)
        );
    }
}