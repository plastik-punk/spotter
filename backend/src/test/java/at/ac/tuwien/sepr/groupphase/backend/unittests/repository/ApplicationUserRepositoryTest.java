package at.ac.tuwien.sepr.groupphase.backend.unittests.repository;


import at.ac.tuwien.sepr.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepr.groupphase.backend.repository.ApplicationUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
public class ApplicationUserRepositoryTest implements TestData {

    @Autowired
    private ApplicationUserRepository applicationUserRepository;

    @Test
    @Transactional
    public void givenLegalData_whenSaveApplicationUser_thenFindListWithOneElementAndFindApplicationUserById() {
        applicationUserRepository.save(TEST_APPLICATION_USER_CUSTOMER_1);

        assertAll(
            () -> assertEquals(1, applicationUserRepository.findAll().size()),
            () -> assertNotNull(applicationUserRepository.findById(TEST_APPLICATION_USER_CUSTOMER_1.getId()))
        );
    }

    @Test
    @Transactional
    public void givenEmail_whenFindByEmail_thenFindUser() {
        applicationUserRepository.save(TEST_APPLICATION_USER_CUSTOMER_1);

        assertAll(
            () -> assertEquals(TEST_APPLICATION_USER_CUSTOMER_1, applicationUserRepository.findByEmail(TEST_APPLICATION_USER_CUSTOMER_1.getEmail()))
        );
    }
}