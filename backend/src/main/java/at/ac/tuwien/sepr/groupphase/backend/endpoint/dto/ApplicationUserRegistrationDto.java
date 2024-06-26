package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepr.groupphase.backend.enums.RoleEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.Objects;

public class ApplicationUserRegistrationDto {

    @Size(min = 1, max = 255, message = "First name should not be empty and shouldn't be longer than 255 characters")
    @Pattern(regexp = "^[A-Za-zäöüÄÖÜß ]+$", message = "First name must consist of letters, umlauts, sharp s and spaces only")
    @NotBlank(message = "First name must not be empty")
    private String firstName;

    @Size(min = 1, max = 255, message = "Last name should not be empty and shouldn't be longer than 255 characters")
    @Pattern(regexp = "^[A-Za-zäöüÄÖÜß ]+$", message = "Laste name must consist of letters, umlauts, sharp s and spaces only")
    @NotBlank(message = "Last name must not be empty")
    private String lastName;

    @NotBlank(message = "Email must not be empty")
    @Email(message = "Email should be valid")
    private String email;

    @Pattern(regexp = "^[0-9]{1,15}$", message = "Invalid mobile number. It must consist of max. 15 digits.")
    private String mobileNumber;  // Optional, no @NotNull or @NotBlank

    @NotBlank(message = "Password must not be empty")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    @NotNull(message = "User role is required")
    @Enumerated(EnumType.STRING)
    private RoleEnum role;

    // Getters and setters
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public RoleEnum getRole() {
        return role;
    }

    public void setRole(RoleEnum role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ApplicationUserRegistrationDto)) {
            return false;
        }
        ApplicationUserRegistrationDto that = (ApplicationUserRegistrationDto) o;
        return Objects.equals(firstName, that.firstName)
            && Objects.equals(lastName, that.lastName)
            && Objects.equals(email, that.email)
            && Objects.equals(mobileNumber, that.mobileNumber)
            && Objects.equals(password, that.password)
            && role == that.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, email, mobileNumber, password, role);
    }

    @Override
    public String toString() {
        return "ApplicationUserRegistrationDto{"
            + "firstName='" + firstName + '\''
            + ", lastName='" + lastName + '\''
            + ", email='" + email + '\''
            + ", mobileNumber='" + mobileNumber + '\''
            + ", password='[PROTECTED]'"
            + ", role=" + role
            + '}';
    }

    public ApplicationUserRegistrationDto copy() {
        return ApplicationUserRegistrationDtoBuilder.anApplicationUserRegistrationDto()
            .withFirstName(firstName)
            .withLastName(lastName)
            .withEmail(email)
            .withMobileNumber(mobileNumber)
            .withPassword(password)
            .withRole(role)
            .build();
    }

    public static final class ApplicationUserRegistrationDtoBuilder {
        private String firstName;
        private String lastName;
        private String email;
        private String mobileNumber;
        private String password;
        private RoleEnum role;

        private ApplicationUserRegistrationDtoBuilder() {
        }

        public static ApplicationUserRegistrationDtoBuilder anApplicationUserRegistrationDto() {
            return new ApplicationUserRegistrationDtoBuilder();
        }

        public ApplicationUserRegistrationDtoBuilder withFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public ApplicationUserRegistrationDtoBuilder withLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public ApplicationUserRegistrationDtoBuilder withEmail(String email) {
            this.email = email;
            return this;
        }

        public ApplicationUserRegistrationDtoBuilder withMobileNumber(String mobileNumber) {
            this.mobileNumber = mobileNumber;
            return this;
        }

        public ApplicationUserRegistrationDtoBuilder withPassword(String password) {
            this.password = password;
            return this;
        }

        public ApplicationUserRegistrationDtoBuilder withRole(RoleEnum role) {
            this.role = role;
            return this;
        }

        public ApplicationUserRegistrationDto build() {
            ApplicationUserRegistrationDto applicationUserRegistrationDto = new ApplicationUserRegistrationDto();
            applicationUserRegistrationDto.setFirstName(firstName);
            applicationUserRegistrationDto.setLastName(lastName);
            applicationUserRegistrationDto.setEmail(email);
            applicationUserRegistrationDto.setMobileNumber(mobileNumber);
            applicationUserRegistrationDto.setPassword(password);
            applicationUserRegistrationDto.setRole(role);
            return applicationUserRegistrationDto;
        }
    }
}