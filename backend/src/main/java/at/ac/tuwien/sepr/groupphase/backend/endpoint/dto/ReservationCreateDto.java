package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class ReservationCreateDto {

    private Long id;

    private ApplicationUser applicationUser;

    private String firstName;

    private String lastName;

    private LocalTime startTime;

    private LocalTime endTime;

    private LocalDate date;

    private int pax;

    private String notes;

    private String email;

    private String mobileNumber;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ApplicationUser getApplicationUser() {
        return applicationUser;
    }

    public void setApplicationUser(ApplicationUser applicationUser) {
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

    public int getPax() {
        return pax;
    }

    public void setPax(int pax) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReservationCreateDto that)) {
            return false;
        }
        return Objects.equals(id, that.id)
            && Objects.equals(applicationUser, that.applicationUser)
            && Objects.equals(firstName, that.firstName)
            && Objects.equals(lastName, that.lastName)
            && Objects.equals(startTime, that.startTime)
            && Objects.equals(endTime, that.endTime)
            && Objects.equals(date, that.date)
            && Objects.equals(pax, that.pax)
            && Objects.equals(notes, that.notes)
            && Objects.equals(email, that.email)
            && Objects.equals(mobileNumber, that.mobileNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, applicationUser, firstName, lastName, startTime, endTime, date, pax, notes, email, mobileNumber);
    }

    @Override
    public String toString() {
        return "ReservationCreateDto{"
            + "id=" + id
            + ", applicationUser='" + applicationUser + '\''
            + ", firstName='" + firstName + '\''
            + ", lastName='" + lastName + '\''
            + ", startTime=" + startTime
            + ", endTime=" + endTime
            + ", date=" + date
            + ", pax=" + pax
            + ", notes='" + notes + '\''
            + ", email='" + email + '\''
            + ", mobileNumber=" + mobileNumber
            + '}';
    }

    public static final class ReservationCreateDtoBuilder {
        private Long id;
        private ApplicationUser applicationUser;
        private String firstName;
        private String lastName;
        private LocalTime startTime;
        private LocalTime endTime;
        private LocalDate date;
        private int pax;
        private String notes;
        private String email;
        private String mobileNumber;

        private ReservationCreateDtoBuilder() {
        }

        public static ReservationCreateDtoBuilder aReservationCreateDto() {
            return new ReservationCreateDtoBuilder();
        }

        public ReservationCreateDtoBuilder withId(Long id) {
            this.id = id;
            return this;
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

        public ReservationCreateDtoBuilder withPax(int pax) {
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

        public ReservationCreateDto build() {
            ReservationCreateDto reservationCreateDto = new ReservationCreateDto();
            reservationCreateDto.setId(id);
            reservationCreateDto.setApplicationUser(applicationUser);
            reservationCreateDto.setFirstName(firstName);
            reservationCreateDto.setLastName(lastName);
            reservationCreateDto.setStartTime(startTime);
            reservationCreateDto.setEndTime(endTime);
            reservationCreateDto.setDate(date);
            reservationCreateDto.setPax(pax);
            reservationCreateDto.setNotes(notes);
            reservationCreateDto.setEmail(email);
            reservationCreateDto.setMobileNumber(mobileNumber);
            return reservationCreateDto;
        }
    }
}