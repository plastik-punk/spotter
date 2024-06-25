package at.ac.tuwien.sepr.groupphase.backend.unittests.service.impl;

import at.ac.tuwien.sepr.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ApplicationUserRegistrationDto;
import at.ac.tuwien.sepr.groupphase.backend.exception.ConflictException;
import at.ac.tuwien.sepr.groupphase.backend.repository.ApplicationUserRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.ApplicationUserService;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ApplicationUserServiceImplTest implements TestData {

    @Autowired
    private ApplicationUserService service;

    @Autowired
    private ApplicationUserRepository repository;

    @Test
    @Transactional
    public void givenAccountAlreadyExists_whenRegister_thenThrowConflictException() throws ConflictException {
        ApplicationUserRegistrationDto dto = TEST_REGISTRATION_CUSTOMER;
        ApplicationUserRegistrationDto dto2 = dto.copy();
        service.register(dto);

        assertThrows(ConflictException.class, () -> service.register(dto2));
    }
}