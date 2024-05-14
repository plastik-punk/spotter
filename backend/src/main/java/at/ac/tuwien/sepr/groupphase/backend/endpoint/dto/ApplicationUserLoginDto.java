package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

public class ApplicationUserLoginDto {

    @NotNull(message = "Email must not be null")
    @Email
    private String email;

    @NotNull(message = "Password must not be null")
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ApplicationUserLoginDto applicationUserLoginDto)) {
            return false;
        }
        return Objects.equals(email, applicationUserLoginDto.email)
            && Objects.equals(password, applicationUserLoginDto.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, password);
    }

    @Override
    public String toString() {
        return "ApplicationUserLoginDto{"
            + "email='" + email + '\''
            + ", password='" + password + '\''
            + '}';
    }


    public static final class ApplicationUserLoginDtoBuilder {
        private String email;
        private String password;

        private ApplicationUserLoginDtoBuilder() {
        }

        public static ApplicationUserLoginDtoBuilder anUserLoginDto() {
            return new ApplicationUserLoginDtoBuilder();
        }

        public ApplicationUserLoginDtoBuilder withEmail(String email) {
            this.email = email;
            return this;
        }

        public ApplicationUserLoginDtoBuilder withPassword(String password) {
            this.password = password;
            return this;
        }

        public ApplicationUserLoginDto build() {
            ApplicationUserLoginDto applicationUserLoginDto = new ApplicationUserLoginDto();
            applicationUserLoginDto.setEmail(email);
            applicationUserLoginDto.setPassword(password);
            return applicationUserLoginDto;
        }
    }
}
