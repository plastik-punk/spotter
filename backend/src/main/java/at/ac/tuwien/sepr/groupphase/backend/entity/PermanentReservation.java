package at.ac.tuwien.sepr.groupphase.backend.entity;

import at.ac.tuwien.sepr.groupphase.backend.enums.RepetitionEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

@Entity
@Table(name = "permanent_reservation")
public class PermanentReservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "id")
    private ApplicationUser applicationUser;
    @NotNull(message = "Start date is required")
    @Column(nullable = false)
    private LocalDate startDate;

    @NotNull(message = "Start time is required")
    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    @Column(nullable = true)
    private LocalDate endDate;

    @NotNull(message = "Repetition is required")
    @Enumerated(EnumType.STRING)
    private RepetitionEnum repetition;

    @NotNull(message = "Period is required")
    @Column(nullable = false)
    private Integer period;

    @NotNull(message = "Confirmation status is required")
    @Column(nullable = false)
    private Boolean confirmed;

    @NotNull(message = "Pax required")
    @Column(nullable = false)
    private Long pax;

    @NotNull(message = "hashedId required")
    @Column(nullable = false)
    private String hashedId;

    // Getters
    public Long getId() {
        return id;
    }

    public ApplicationUser getApplicationUser() {
        return applicationUser;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public RepetitionEnum getRepetition() {
        return repetition;
    }

    public Integer getPeriod() {
        return period;
    }

    public Boolean getConfirmed() {
        return confirmed;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setApplicationUser(ApplicationUser applicationUser) {
        this.applicationUser = applicationUser;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void setRepetition(RepetitionEnum repetition) {
        this.repetition = repetition;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public void setConfirmed(Boolean confirmed) {
        this.confirmed = confirmed;
    }

    public String getHashedId() {
        return hashedId;
    }

    public void setHashedId(String hashedId) {
        this.hashedId = hashedId;
    }

    public Long getPax() {
        return pax;
    }

    public void setPax(Long pax) {
        this.pax = pax;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PermanentReservation that)) {
            return false;
        }
        return Objects.equals(id, that.id)
            && Objects.equals(applicationUser, that.applicationUser)
            && Objects.equals(startDate, that.startDate)
            && Objects.equals(startTime, that.startTime)
            && Objects.equals(endTime, that.endTime)
            && Objects.equals(endDate, that.endDate)
            && repetition == that.repetition
            && Objects.equals(period, that.period)
            && Objects.equals(confirmed, that.confirmed)
            && Objects.equals(pax, that.pax);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, applicationUser, startDate, startTime, endTime, endDate, repetition, period, confirmed);
    }

    @Override
    public String toString() {
        return "PermanentReservation{"
            + "id=" + id
            + ", user=" + applicationUser
            + ", startDate=" + startDate
            + ", startTime=" + startTime
            + ", endTime=" + endTime
            + ", endDate=" + endDate
            + ", repetition=" + repetition
            + ", period=" + period
            + ", confirmed=" + confirmed
            + ", pax=" + pax + '}';
    }

    // Builder
    public static final class PermanentReservationBuilder {
        private Long id;
        private ApplicationUser applicationUser;
        private LocalDate startDate;
        private LocalTime startTime;
        private LocalTime endTime;
        private LocalDate endDate;
        private RepetitionEnum repetition;
        private Integer period;
        private Boolean confirmed;
        private Long pax;
        private String hashedId;

        private PermanentReservationBuilder() {
        }

        public PermanentReservationBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public PermanentReservationBuilder withApplicationUser(ApplicationUser applicationUser) {
            this.applicationUser = applicationUser;
            return this;
        }

        public PermanentReservationBuilder withStartDate(LocalDate startDate) {
            this.startDate = startDate;
            return this;
        }

        public PermanentReservationBuilder withStartTime(LocalTime startTime) {
            this.startTime = startTime;
            return this;
        }

        public PermanentReservationBuilder withEndTime(LocalTime endTime) {
            this.endTime = endTime;
            return this;
        }

        public PermanentReservationBuilder withEndDate(LocalDate endDate) {
            this.endDate = endDate;
            return this;
        }

        public PermanentReservationBuilder withRepetition(RepetitionEnum repetition) {
            this.repetition = repetition;
            return this;
        }

        public PermanentReservationBuilder withPeriod(Integer period) {
            this.period = period;
            return this;
        }

        public PermanentReservationBuilder withConfirmed(Boolean confirmed) {
            this.confirmed = confirmed;
            return this;
        }

        public PermanentReservationBuilder withPax(Long pax) {
            this.pax = pax;
            return this;
        }

        public PermanentReservationBuilder withHashedId(String hashedId) {
            this.hashedId = hashedId;
            return this;
        }

        public PermanentReservation build() {
            PermanentReservation permanentReservation = new PermanentReservation();
            permanentReservation.setId(id);
            permanentReservation.setApplicationUser(applicationUser);
            permanentReservation.setStartDate(startDate);
            permanentReservation.setStartTime(startTime);
            permanentReservation.setEndTime(endTime);
            permanentReservation.setEndDate(endDate);
            permanentReservation.setRepetition(repetition);
            permanentReservation.setPeriod(period);
            permanentReservation.setConfirmed(confirmed);
            permanentReservation.setPax(pax);
            permanentReservation.setHashedId(hashedId);
            return permanentReservation;
        }
    }
}