package at.ac.tuwien.sepr.groupphase.backend.entity;

import at.ac.tuwien.sepr.groupphase.backend.validation.FutureOrPresentValidation;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

@Entity
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "id")
    private ApplicationUser applicationUser;

    @NotNull(message = "startTime is required")
    @Column(nullable = false)
    private LocalTime startTime;

    @NotNull(message = "Date must not be null")
    @FutureOrPresent(message = "Date cannot be in the past")
    @Column(nullable = false)
    private LocalDate date;

    @Column
    private LocalTime endTime;

    @NotNull(message = "Pax must not be null")
    @Positive(message = "Pax should be greater than 0")
    @Column(nullable = false)
    private Long pax;

    @Size(max = 100000, message = "Notes shouldn't be longer than 100000 characters")
    @Column(length = 100000)
    private String notes;

    @Column(nullable = false)
    private String hashValue;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ApplicationUser getApplicationUser() {
        return applicationUser;
    }

    public void setUser(ApplicationUser applicationUser) {
        this.applicationUser = applicationUser;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
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

    public String getHashValue() {
        return hashValue;
    }

    public void setHashValue(String hashValue) {
        this.hashValue = hashValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Reservation reservation)) {
            return false;
        }
        return Objects.equals(id, reservation.id)
            && Objects.equals(applicationUser, reservation.applicationUser)
            && Objects.equals(startTime, reservation.startTime)
            && Objects.equals(date, reservation.date)
            && Objects.equals(endTime, reservation.endTime)
            && Objects.equals(pax, reservation.pax)
            && Objects.equals(notes, reservation.notes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, applicationUser, startTime, date, endTime, pax, notes);
    }

    @Override
    public String toString() {
        return "Reservation{"
            + "id=" + id
            + ", applicationUser=" + applicationUser
            + ", startTime=" + startTime
            + ", date=" + date
            + ", endTime=" + endTime
            + ", pax=" + pax
            + ", notes='" + notes + '\''
            + '}';
    }

    public Reservation copy() {
        return ReservationBuilder.aReservation()
            .withId(this.id)
            .withUser(this.applicationUser)
            .withStartTime(this.startTime)
            .withDate(this.date)
            .withEndTime(this.endTime)
            .withPax(this.pax)
            .withNotes(this.notes)
            .withHashValue(this.hashValue)
            .build();
    }

    public static final class ReservationBuilder {
        private Long id;
        private ApplicationUser applicationUser;
        private LocalTime startTime;
        private LocalDate date;
        private LocalTime endTime;
        private Long pax;
        private String notes;
        private String hashValue;

        private ReservationBuilder() {
        }

        public static ReservationBuilder aReservation() {
            return new ReservationBuilder();
        }

        public ReservationBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public ReservationBuilder withUser(ApplicationUser applicationUser) {
            this.applicationUser = applicationUser;
            return this;
        }

        public ReservationBuilder withStartTime(LocalTime startTime) {
            this.startTime = startTime;
            return this;
        }

        public ReservationBuilder withDate(LocalDate date) {
            this.date = date;
            return this;
        }

        public ReservationBuilder withEndTime(LocalTime endTime) {
            this.endTime = endTime;
            return this;
        }

        public ReservationBuilder withPax(Long pax) {
            this.pax = pax;
            return this;
        }

        public ReservationBuilder withNotes(String notes) {
            this.notes = notes;
            return this;
        }

        public ReservationBuilder withHashValue(String hashValue) {
            this.hashValue = hashValue;
            return this;
        }

        public Reservation build() {
            Reservation reservation = new Reservation();
            reservation.setId(id);
            reservation.setUser(applicationUser);
            reservation.setStartTime(startTime);
            reservation.setDate(date);
            reservation.setEndTime(endTime);
            reservation.setPax(pax);
            reservation.setNotes(notes);
            reservation.setHashValue(hashValue);
            return reservation;
        }
    }
}
