package at.ac.tuwien.sepr.groupphase.backend.entity;

import at.ac.tuwien.sepr.groupphase.backend.enums.RoleEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Objects;

@Entity
@Table(name = "app_user")
public class ApplicationUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String firstName;

    @Column(nullable = false, length = 255)
    private String lastName;

    @Column(nullable = false, length = 255)
    private String email;

    @Column(nullable = true, length = 15)
    private String mobileNumber;

    @Column(nullable = false, length = 255)
    private String password;

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