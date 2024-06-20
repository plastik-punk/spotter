package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepr.groupphase.backend.enums.RepetitionEnum;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class PermanentReservationCreateDto {

    private ApplicationUser applicationUser;

    @NotNull(message = "Start time is required")
    private LocalTime startTime;

    private LocalTime endTime;

    @NotNull(message = "Start date must not be null")
    @FutureOrPresent(message = "Start date cannot be in the past")
    private LocalDate startDate;

    private LocalDate endDate;

    @NotNull(message = "Repetition type is required")
    private RepetitionEnum repetition;

    @NotNull(message = "Period is required")
    @Positive(message = "Period should be greater than 0")
    private Integer period;

    @NotNull(message = "Confirmation status is required")
    private Boolean confirmed;

    public ApplicationUser getApplicationUser() {
        return applicationUser;
    }

    public void setApplicationUser(ApplicationUser applicationUser) {
        this.applicationUser = applicationUser;
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

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public RepetitionEnum getRepetition() {
        return repetition;
    }

    public void setRepetition(RepetitionEnum repetition) {
        this.repetition = repetition;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public Boolean getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(Boolean confirmed) {
        this.confirmed = confirmed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PermanentReservationCreateDto that)) {
            return false;
        }
        return Objects.equals(applicationUser, that.applicationUser)
            && Objects.equals(startTime, that.startTime)
            && Objects.equals(endTime, that.endTime)
            && Objects.equals(startDate, that.startDate)
            && Objects.equals(endDate, that.endDate)
            && repetition == that.repetition
            && Objects.equals(period, that.period)
            && Objects.equals(confirmed, that.confirmed);
    }

    @Override
    public int hashCode() {
        return Objects.hash(applicationUser, startTime, endTime, startDate, endDate, repetition, period, confirmed);
    }

    @Override
    public String toString() {
        return "PermanentReservationCreateDto{"
            + "applicationUser=" + applicationUser
            + ", startTime=" + startTime
            + ", endTime=" + endTime
            + ", startDate=" + startDate
            + ", endDate=" + endDate
            + ", repetition=" + repetition
            + ", period=" + period
            + ", confirmed=" + confirmed + '}';
    }

    public static final class PermanentReservationCreateDtoBuilder {
        private ApplicationUser applicationUser;
        private LocalTime startTime;
        private LocalTime endTime;
        private LocalDate startDate;
        private LocalDate endDate;
        private RepetitionEnum repetition;
        private Integer period;
        private Boolean confirmed;

        private PermanentReservationCreateDtoBuilder() {
        }

        public static PermanentReservationCreateDtoBuilder aPermanentReservationCreateDto() {
            return new PermanentReservationCreateDtoBuilder();
        }

        public PermanentReservationCreateDtoBuilder withApplicationUser(ApplicationUser applicationUser) {
            this.applicationUser = applicationUser;
            return this;
        }

        public PermanentReservationCreateDtoBuilder withStartTime(LocalTime startTime) {
            this.startTime = startTime;
            return this;
        }

        public PermanentReservationCreateDtoBuilder withEndTime(LocalTime endTime) {
            this.endTime = endTime;
            return this;
        }

        public PermanentReservationCreateDtoBuilder withStartDate(LocalDate startDate) {
            this.startDate = startDate;
            return this;
        }

        public PermanentReservationCreateDtoBuilder withEndDate(LocalDate endDate) {
            this.endDate = endDate;
            return this;
        }

        public PermanentReservationCreateDtoBuilder withRepetition(RepetitionEnum repetition) {
            this.repetition = repetition;
            return this;
        }

        public PermanentReservationCreateDtoBuilder withPeriod(Integer period) {
            this.period = period;
            return this;
        }

        public PermanentReservationCreateDtoBuilder withConfirmed(Boolean confirmed) {
            this.confirmed = confirmed;
            return this;
        }

        public PermanentReservationCreateDto build() {
            PermanentReservationCreateDto dto = new PermanentReservationCreateDto();
            dto.setApplicationUser(applicationUser);
            dto.setStartTime(startTime);
            dto.setEndTime(endTime);
            dto.setStartDate(startDate);
            dto.setEndDate(endDate);
            dto.setRepetition(repetition);
            dto.setPeriod(period);
            dto.setConfirmed(confirmed);
            return dto;
        }
    }
}