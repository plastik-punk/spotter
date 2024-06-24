package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

public class ReservationCreateDto {

    private ApplicationUser applicationUser;

    @NotNull(message = "First name is required")
    @Size(min = 1, message = "First name should not be empty")
    @Size(max = 255, message = "First name shouldn't be longer than 255 characters")
    @Pattern(regexp = "^[A-Za-zäöüÄÖÜß ]+$", message = "First name must consist of letters and spaces only")
    private String firstName;

    @NotNull(message = "Last name is required")
    @Size(min = 1, message = "Last name should not be empty")
    @Size(max = 255, message = "Last name shouldn't be longer than 255 characters")
    @Pattern(regexp = "^[A-Za-zäöüÄÖÜß ]+$", message = "Last name must consist of letters and spaces only")
    private String lastName;

    @NotNull(message = "startTime is required")
    private LocalTime startTime;

    private LocalTime endTime;

    @NotNull(message = "Date must not be null")
    @FutureOrPresent(message = "Date cannot be in the past")
    private LocalDate date;

    @NotNull(message = "Pax must not be null")
    @Positive(message = "Pax should be greater than 0")
    private Long pax;

    @Size(max = 100000, message = "Notes shouldn't be longer than 100.000 characters")
    private String notes;

    @NotNull(message = "Email is required")
    @NotEmpty(message = "Email must not be empty")
    @Email(message = "Email must be valid (e.g.: otter@spotter.com)")
    private String email;

    @Pattern(regexp = "^[0-9]{1,15}$", message = "Invalid mobile number. It must consist of max. 15 digits.")
    private String mobileNumber;

    private List<Long> placeIds;

    private List<SpecialOfferAmountDto> specialOffers;

    public ApplicationUser getUser() {
        return applicationUser;
    }

    public void setUser(ApplicationUser applicationUser) {
        this.applicationUser = applicationUser;
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

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Long getPax() {
        return pax;
    }

    public void setPax(Long pax) {
        this.pax = pax;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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

    public List<Long> getPlaceIds() {
        return placeIds;
    }

    public void setPlaceIds(List<Long> placeIds) {
        this.placeIds = placeIds;
    }

    public List<SpecialOfferAmountDto> getSpecialOffers() {
        return specialOffers;
    }

    public void setSpecialOffers(List<SpecialOfferAmountDto> specialOffers) {
        this.specialOffers = specialOffers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReservationCreateDto that)) {
            return false;
        }
        return Objects.equals(applicationUser, that.applicationUser)
            && Objects.equals(firstName, that.firstName)
            && Objects.equals(lastName, that.lastName)
            && Objects.equals(startTime, that.startTime)
            && Objects.equals(endTime, that.endTime)
            && Objects.equals(date, that.date)
            && Objects.equals(pax, that.pax)
            && Objects.equals(notes, that.notes)
            && Objects.equals(email, that.email)
            && Objects.equals(mobileNumber, that.mobileNumber)
            && Objects.equals(placeIds, that.placeIds)
            && Objects.equals(specialOffers, that.specialOffers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(applicationUser, firstName, lastName, startTime, endTime, date, pax, notes, email, mobileNumber, placeIds, specialOffers);
    }

    @Override
    public String toString() {
        return "ReservationCreateDto{"
            + "applicationUser='" + applicationUser + '\''
            + ", firstName='" + firstName + '\''
            + ", lastName='" + lastName + '\''
            + ", startTime=" + startTime
            + ", endTime=" + endTime
            + ", date=" + date
            + ", pax=" + pax
            + ", notes='" + notes + '\''
            + ", email='" + email + '\''
            + ", mobileNumber=" + mobileNumber
            + ", placeIds=" + placeIds
            + ", specialOffers=" + specialOffers
            + '}';
    }

    public ReservationCreateDto copy() {
        return ReservationCreateDtoBuilder.aReservationCreateDto()
            .withApplicationUser(this.applicationUser) // assuming ApplicationUser is immutable or has its own copy method
            .withFirstName(this.firstName)
            .withLastName(this.lastName)
            .withStartTime(this.startTime)
            .withEndTime(this.endTime)
            .withDate(this.date)
            .withPax(this.pax)
            .withNotes(this.notes)
            .withEmail(this.email)
            .withMobileNumber(this.mobileNumber)
            .withPlaceIds(this.placeIds)
            .withSpecialOffers(this.specialOffers)
            .build();
    }

    public static final class ReservationCreateDtoBuilder {
        private ApplicationUser applicationUser;
        private String firstName;
        private String lastName;
        private LocalTime startTime;
        private LocalTime endTime;
        private LocalDate date;
        private Long pax;
        private String notes;
        private String email;
        private String mobileNumber;
        private List<Long> placeIds;
        private List<SpecialOfferAmountDto> specialOffers;

        private ReservationCreateDtoBuilder() {
        }

        public static ReservationCreateDtoBuilder aReservationCreateDto() {
            return new ReservationCreateDtoBuilder();
        }

        public ReservationCreateDtoBuilder withApplicationUser(ApplicationUser applicationUser) {
            this.applicationUser = applicationUser;
            return this;
        }

        public ReservationCreateDtoBuilder withFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public ReservationCreateDtoBuilder withLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public ReservationCreateDtoBuilder withStartTime(LocalTime startTime) {
            this.startTime = startTime;
            return this;
        }

        public ReservationCreateDtoBuilder withEndTime(LocalTime endTime) {
            this.endTime = endTime;
            return this;
        }

        public ReservationCreateDtoBuilder withDate(LocalDate date) {
            this.date = date;
            return this;
        }

        public ReservationCreateDtoBuilder withPax(Long pax) {
            this.pax = pax;
            return this;
        }

        public ReservationCreateDtoBuilder withNotes(String notes) {
            this.notes = notes;
            return this;
        }

        public ReservationCreateDtoBuilder withEmail(String email) {
            this.email = email;
            return this;
        }

        public ReservationCreateDtoBuilder withMobileNumber(String mobileNumber) {
            this.mobileNumber = mobileNumber;
            return this;
        }

        public ReservationCreateDtoBuilder withPlaceIds(List<Long> placeIds) {
            this.placeIds = placeIds;
            return this;
        }

        public ReservationCreateDtoBuilder withSpecialOffers(List<SpecialOfferAmountDto> specialOffers) {
            this.specialOffers = specialOffers;
            return this;
        }

        public ReservationCreateDto build() {
            ReservationCreateDto reservationCreateDto = new ReservationCreateDto();
            reservationCreateDto.setUser(applicationUser);
            reservationCreateDto.setFirstName(firstName);
            reservationCreateDto.setLastName(lastName);
            reservationCreateDto.setStartTime(startTime);
            reservationCreateDto.setEndTime(endTime);
            reservationCreateDto.setDate(date);
            reservationCreateDto.setPax(pax);
            reservationCreateDto.setNotes(notes);
            reservationCreateDto.setEmail(email);
            reservationCreateDto.setMobileNumber(mobileNumber);
            reservationCreateDto.setPlaceIds(placeIds);
            reservationCreateDto.setSpecialOffers(specialOffers);
            return reservationCreateDto;
        }
    }
}
