package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepr.groupphase.backend.enums.RoleEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

public class ApplicationUserOverviewDto {

    @NotBlank(message = "Registered user needs an ID")
    private Long id;

    @NotBlank(message = "First name must not be empty")
    private String firstName;

    @NotBlank(message = "Last name must not be empty")
    private String lastName;

    @NotBlank(message = "Email must not be empty")
    @Email(message = "Email must be valid (e.g.: otter@spotter.com)")
    private String email;

    private String mobileNumber;  // Optional, no @NotNull or @NotBlank

    @NotNull(message = "Role must not be null")
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        if (!(o instanceof ApplicationUserOverviewDto)) {
            return false;
        }
        ApplicationUserOverviewDto that = (ApplicationUserOverviewDto) o;
        return Objects.equals(firstName, that.firstName)
            && Objects.equals(lastName, that.lastName)
            && Objects.equals(email, that.email)
            && Objects.equals(mobileNumber, that.mobileNumber)
            && Objects.equals(id, that.id)
            && role == that.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, email, mobileNumber, id, role);
    }

    @Override
    public String toString() {
        return "ApplicationUserRegistrationDto{"
            + "Id='" + id + '\''
            + "firstName='" + firstName + '\''
            + ", lastName='" + lastName + '\''
            + ", email='" + email + '\''
            + ", mobileNumber='" + mobileNumber + '\''
            + ", role=" + role
            + '}';
    }

    public static final class ApplicationUserOverviewDtoBuilder {
        private Long id;
        private String firstName;
        private String lastName;
        private String email;
        private String mobileNumber;
        private RoleEnum role;

        private ApplicationUserOverviewDtoBuilder() {
        }

        public static ApplicationUserOverviewDtoBuilder anApplicationUserOverviewDto() {
            return new ApplicationUserOverviewDtoBuilder();
        }

        public ApplicationUserOverviewDtoBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public ApplicationUserOverviewDtoBuilder withFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public ApplicationUserOverviewDtoBuilder withLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public ApplicationUserOverviewDtoBuilder withEmail(String email) {
            this.email = email;
            return this;
        }

        public ApplicationUserOverviewDtoBuilder withMobileNumber(String mobileNumber) {
            this.mobileNumber = mobileNumber;
            return this;
        }

        public ApplicationUserOverviewDtoBuilder withRole(RoleEnum role) {
            this.role = role;
            return this;
        }

        public ApplicationUserOverviewDto build() {
            ApplicationUserOverviewDto applicationUserOverviewDto = new ApplicationUserOverviewDto();
            applicationUserOverviewDto.setId(id);
            applicationUserOverviewDto.setFirstName(firstName);
            applicationUserOverviewDto.setLastName(lastName);
            applicationUserOverviewDto.setEmail(email);
            applicationUserOverviewDto.setMobileNumber(mobileNumber);
            applicationUserOverviewDto.setRole(role);
            return applicationUserOverviewDto;
        }
    }
}