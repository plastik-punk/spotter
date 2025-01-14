package at.ac.tuwien.sepr.groupphase.backend.entity;

import at.ac.tuwien.sepr.groupphase.backend.enums.RoleEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.Objects;

@Entity
@Table(name = "app_user")
public class ApplicationUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "First name is required")
    @Size(min = 1, message = "First name should not be empty")
    @Size(max = 255, message = "First name shouldn't be longer than 255 characters")
    @Pattern(regexp = "^[A-Za-zäöüÄÖÜß ]+$", message = "First name must consist of letters, umlauts, sharp s and spaces only")
    @Column(nullable = false, length = 255)
    private String firstName;

    @NotNull(message = "Last name is required")
    @Size(min = 1, message = "Last name should not be empty")
    @Size(max = 255, message = "Last name shouldn't be longer than 255 characters")
    @Pattern(regexp = "^[A-Za-zäöüÄÖÜß ]+$", message = "Last name must consist of letters, umlauts, sharp s and spaces only")
    @Column(nullable = false, length = 255)
    private String lastName;

    @NotNull(message = "Email is required")
    @Email(message = "Email must be valid")
    @Column(nullable = false, length = 255)
    private String email;

    @Pattern(regexp = "^[0-9]{1,15}$", message = "Invalid mobile number. It must consist of max. 15 digits.")
    @Column(nullable = true, length = 15)
    private String mobileNumber;

    @NotNull(message = "Password is required")
    @Size(max = 255, message = "Password shouldn't be longer than 255 characters")
    @Column(nullable = false, length = 255)
    private String password;

    @NotNull(message = "User role is required")
    @Column(nullable = false)
    private RoleEnum role;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
        if (!(o instanceof ApplicationUser applicationUser)) {
            return false;
        }
        return Objects.equals(id, applicationUser.id)
            && Objects.equals(firstName, applicationUser.firstName)
            && Objects.equals(lastName, applicationUser.lastName)
            && Objects.equals(email, applicationUser.email)
            && Objects.equals(mobileNumber, applicationUser.mobileNumber)
            && Objects.equals(password, applicationUser.password)
            && Objects.equals(role, applicationUser.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, email, mobileNumber, password, role);
    }

    @Override
    public String toString() {
        return "User{"
            + "id=" + id
            + ", firstName='" + firstName + '\''
            + ", lastName='" + lastName + '\''
            + ", eMail='" + email + '\''
            + ", mobileNumber='" + mobileNumber + '\''
            + ", password='" + password + '\''
            + ", role='" + role + '\''
            + '}';
    }

    public ApplicationUser copy() {
        return ApplicationUserBuilder.anApplicationUser()
            .withId(id)
            .withFirstName(firstName)
            .withLastName(lastName)
            .withEmail(email)
            .withMobileNumber(mobileNumber)
            .withPassword(password)
            .withRole(role)
            .build();
    }

    public static final class ApplicationUserBuilder {
        private Long id;
        private String firstName;
        private String lastName;
        private String email;
        private String mobileNumber;
        private String password;
        private RoleEnum role;

        private ApplicationUserBuilder() {
        }

        public static ApplicationUserBuilder anApplicationUser() {
            return new ApplicationUserBuilder();
        }

        public ApplicationUserBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public ApplicationUserBuilder withFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public ApplicationUserBuilder withLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public ApplicationUserBuilder withEmail(String email) {
            this.email = email;
            return this;
        }

        public ApplicationUserBuilder withMobileNumber(String mobileNumber) {
            this.mobileNumber = mobileNumber;
            return this;
        }

        public ApplicationUserBuilder withPassword(String password) {
            this.password = password;
            return this;
        }

        public ApplicationUserBuilder withoutPassword() {
            this.password = "guest";
            return this;
        }

        public ApplicationUserBuilder withRole(RoleEnum role) {
            this.role = role;
            return this;
        }

        public ApplicationUser build() {
            ApplicationUser applicationUser = new ApplicationUser();
            applicationUser.setId(id);
            applicationUser.setFirstName(firstName);
            applicationUser.setLastName(lastName);
            applicationUser.setEmail(email);
            applicationUser.setMobileNumber(mobileNumber);
            applicationUser.setPassword(password);
            applicationUser.setRole(role);
            return applicationUser;
        }
    }
}