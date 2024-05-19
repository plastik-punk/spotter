package at.ac.tuwien.sepr.groupphase.backend.service.impl;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.UserRegistrationDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
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

        validateFirstName(validationErrors, registrationDto.getFirstName());
        validateLastName(validationErrors, registrationDto.getLastName());
        validateEmail(validationErrors, registrationDto.getEmail());
        validatePhoneNumber(validationErrors, registrationDto.getMobileNumber());
        validateRole(validationErrors, registrationDto.getRole());
        validatePassword(validationErrors, registrationDto.getPassword());

        if (!validationErrors.isEmpty()) {
            throw new ValidationException("Validation of registration data failed", validationErrors);
        }
    }

    public void validateApplicationUser(List<String> validationErrors, ApplicationUser applicationUser) {
        LOG.trace("validateRegistration({})", applicationUser);

        validateFirstName(validationErrors, applicationUser.getFirstName());
        validateLastName(validationErrors, applicationUser.getLastName());
        validateEmail(validationErrors, applicationUser.getEmail());
        validatePhoneNumber(validationErrors, applicationUser.getMobileNumber());
        validateRoleForReservation(validationErrors, applicationUser.getRole());
    }

    public void validateFirstName(List<String> validationErrors, String firstName) {
        LOG.trace("validateFirstName({})", firstName);
        if (firstName == null) {
            validationErrors.add("No first name given");
        } else if (firstName.trim().isEmpty()) {
            validationErrors.add("First name should not be empty");
        } else if (firstName.length() > 255) {
            validationErrors.add("First name shouldn't be longer than 255 characters");
        } else if (!firstName.matches("^[A-Za-z ]+$")) {
            validationErrors.add("First name must consist of letters and spaces only");
        }
    }

    public void validateLastName(List<String> validationErrors, String lastName) {
        LOG.trace("validateLastName({})", lastName);
        if (lastName == null) {
            validationErrors.add("No last name given");
        } else if (lastName.trim().isEmpty()) {
            validationErrors.add("Last name should not be empty");
        } else if (lastName.length() > 255) {
            validationErrors.add("Last name shouldn't be longer than 255 characters");
        } else if (!lastName.matches("^[A-Za-z ]+$")) {
            validationErrors.add("Last name must consist of letters and spaces only");
        }
    }

    private void validateRole(List<String> validationErrors, RoleEnum role) {
        LOG.trace("validateRole({})", role);
        if (role == null) {
            validationErrors.add("User role is required");
        } else if (!role.equals(RoleEnum.CUSTOMER) && !role.equals(RoleEnum.UNCONFIRMED_ADMIN)
            && !role.equals(RoleEnum.UNCONFIRMED_EMPLOYEE)) {
            validationErrors.add("Unexpected user role");
        }
    }

    private void validateRoleForReservation(List<String> validationErrors, RoleEnum role) {
        LOG.trace("validateRole({})", role);
        if (role == null) {
            validationErrors.add("User role is required");
        }
    }

    public void validateEmail(List<String> validationErrors, String email) {
        LOG.trace("validateEmail({})", email);
        final String emailPattern =
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

        final Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(email);

        if (email.trim().isEmpty()) {
            validationErrors.add("Email should not be empty");
        } else if (!matcher.matches()) {
            validationErrors.add("Invalid eMail");
        }
    }

    public void validatePhoneNumber(List<String> validationErrors, String phoneNumber) {
        LOG.trace("validatePhoneNumber({})", phoneNumber);
        final String phonePattern = "^[0-9]{1,15}$";

        final Pattern pattern = Pattern.compile(phonePattern);

        if (phoneNumber != null) {
            Matcher matcher = pattern.matcher(phoneNumber);
            if (!matcher.matches()) {
                validationErrors.add("Invalid mobile number. It must start with '+' followed by up to 14 digits");
            }

        }
    }

    private void validatePassword(List<String> validationErrors, String pw) {
        LOG.trace("validatePassowrd({})", pw);
        if (pw == null) {
            validationErrors.add("No password given");
        } else if (pw.trim().isEmpty()) {
            validationErrors.add("Password should not be empty");
        } else if (pw.length() < 8) {
            validationErrors.add("Password has to be longer than 8 characters");
        }
    }
}
