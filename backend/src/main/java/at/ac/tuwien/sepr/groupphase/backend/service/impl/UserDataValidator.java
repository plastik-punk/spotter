package at.ac.tuwien.sepr.groupphase.backend.service.impl;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.UserRegistrationDto;
import at.ac.tuwien.sepr.groupphase.backend.enums.RoleEnum;
import at.ac.tuwien.sepr.groupphase.backend.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class UserDataValidator {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());


    public void validateRegistration(UserRegistrationDto registrationDto) throws ValidationException {
        LOG.trace("validateRegistration({})", registrationDto);
        List<String> validationErrors = new ArrayList<>();

        validateFirstName(validationErrors, registrationDto);
        validateLastName(validationErrors, registrationDto);
        validateEmail(validationErrors, registrationDto);
        validatePhoneNumber(validationErrors, registrationDto);
        validateRole(validationErrors, registrationDto);
        validatePassword(validationErrors, registrationDto);

        if (!validationErrors.isEmpty()) {
            throw new ValidationException("Validation of registration data failed", validationErrors);
        }
    }

    private void validateFirstName(List<String> validationErrors, UserRegistrationDto registrationDto) {
        LOG.trace("validateFirstName({})", registrationDto);
        if (registrationDto.getFirstName() == null) {
            validationErrors.add("No first name given.");
        } else if (registrationDto.getFirstName().trim().isEmpty()) {
            validationErrors.add("First name should not be empty.");
        } else if (registrationDto.getFirstName().length() > 255) {
            validationErrors.add("First name shouldn't be longer than 255 characters.");
        } else if (!registrationDto.getFirstName().matches("^[A-Za-z ]+$")) {
            validationErrors.add("First name must consist of letters and spaces only.");
        }
    }

    private void validateLastName(List<String> validationErrors, UserRegistrationDto registrationDto) {
        LOG.trace("validateLastName({})", registrationDto);
        if (registrationDto.getLastName() == null) {
            validationErrors.add("No last name given.");
        } else if (registrationDto.getLastName().trim().isEmpty()) {
            validationErrors.add("Last name should not be empty.");
        } else if (registrationDto.getLastName().length() > 255) {
            validationErrors.add("Last name shouldn't be longer than 255 characters.");
        } else if (!registrationDto.getLastName().matches("^[A-Za-z ]+$")) {
            validationErrors.add("Last name must consist of letters and spaces only.");
        }
    }


    private void validateRole(List<String> validationErrors, UserRegistrationDto registrationDto) {
        LOG.trace("validateRole({})", registrationDto);
        if (registrationDto.getRole() == null) {
            validationErrors.add("User role is required.");
        } else if (!registrationDto.getRole().equals(RoleEnum.CUSTOMER) && !registrationDto.getRole().equals(RoleEnum.UNCONFIRMED_ADMIN)
            && !registrationDto.getRole().equals(RoleEnum.UNCONFIRMED_EMPLOYEE)) {
            validationErrors.add("Unexpected user role");

        }
    }

    private void validateEmail(List<String> validationErrors, UserRegistrationDto registrationDto) {
        LOG.trace("validateEmail({})", registrationDto);
        final String emailPattern =
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

        final Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(registrationDto.getEmail());
        if (!matcher.matches()) {
            validationErrors.add("Invalid eMail");
        }
    }


    private void validatePhoneNumber(List<String> validationErrors, UserRegistrationDto registrationDto) {
        LOG.trace("validatePhoneNumber({})", registrationDto);
        final String phonePattern = "^[0-9]{1,15}$";

        final Pattern pattern = Pattern.compile(phonePattern);
        String phoneNumber = registrationDto.getMobileNumber();

        if (phoneNumber != null) {
            Matcher matcher = pattern.matcher(phoneNumber);
            if (!matcher.matches()) {
                validationErrors.add("Invalid mobile number. It must start with '+' followed by up to 14 digits.");
            }

        }
    }

    private void validatePassword(List<String> validationErrors, UserRegistrationDto registrationDto) {
        LOG.trace("validatePassowrd({})", registrationDto);
        if (registrationDto.getPassword() == null) {
            validationErrors.add("No password given.");
        } else if (registrationDto.getPassword().trim().isEmpty()) {
            validationErrors.add("Password should not be empty.");
        } else if (registrationDto.getPassword().length() < 8) {
            validationErrors.add("Password has to be longer than 8 characters.");
        }
    }
}
