package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepr.groupphase.backend.enums.RepetitionEnum;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class PermanentReservationListDto {
    private Long id;

    private String userFirstName;

    private String userLastName;

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
    @Min(1)
    @Max(10)
    private Integer period;

    @NotNull(message = "Confirmation status is required")
    private Boolean confirmed;

    @NotNull(message = "Pax is required")
    private Long pax;

    private String hashedId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
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


    public Long getPax() {
        return pax;
    }

    public void setPax(Long pax) {
        this.pax = pax;
    }

    public String getHashedId() {
        return hashedId;
    }

    public void setHashedId(String hashedId) {
        this.hashedId = hashedId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PermanentReservationListDto that)) {
            return false;
        }
        return Objects.equals(id, that.id)
            && Objects.equals(userFirstName, that.userFirstName)
            && Objects.equals(userLastName, that.userLastName)
            && Objects.equals(startTime, that.startTime)
            && Objects.equals(endTime, that.endTime)
            && Objects.equals(startDate, that.startDate)
            && Objects.equals(endDate, that.endDate)
            && repetition == that.repetition
            && Objects.equals(period, that.period)
            && Objects.equals(confirmed, that.confirmed)
            && Objects.equals(pax, that.pax);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userFirstName, userLastName, startTime, endTime, startDate, endDate, repetition, period, confirmed);
    }

    @Override
    public String toString() {
        return "PermanentReservationCreateDto{"
            + "id=" + id
            + "userFirstName=" + userFirstName
            + "userLastName=" + userLastName
            + ", startTime=" + startTime
            + ", endTime=" + endTime
            + ", startDate=" + startDate
            + ", endDate=" + endDate
            + ", repetition=" + repetition
            + ", period=" + period
            + ", confirmed=" + confirmed
            + ", pax=" + pax + '}';
    }

    public static final class PermanentReservationListDtoBuilder {
        private Long id;

        private String userFirstName;

        private String userLastName;
        private LocalTime startTime;
        private LocalTime endTime;
        private LocalDate startDate;
        private LocalDate endDate;
        private RepetitionEnum repetition;
        private Integer period;
        private Boolean confirmed;
        private Long pax;
        private String hashedId;

        private PermanentReservationListDtoBuilder() {
        }

        public static PermanentReservationListDtoBuilder aPermanentReservationListDto() {
            return new PermanentReservationListDtoBuilder();
        }

        public PermanentReservationListDtoBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public PermanentReservationListDtoBuilder withUserFirstName(String userFirstName) {
            this.userFirstName = userFirstName;
            return this;
        }

        public PermanentReservationListDtoBuilder withUserLastName(String userLastName) {
            this.userLastName = userLastName;
            return this;
        }


        public PermanentReservationListDtoBuilder withStartTime(LocalTime startTime) {
            this.startTime = startTime;
            return this;
        }

        public PermanentReservationListDtoBuilder withEndTime(LocalTime endTime) {
            this.endTime = endTime;
            return this;
        }

        public PermanentReservationListDtoBuilder withStartDate(LocalDate startDate) {
            this.startDate = startDate;
            return this;
        }

        public PermanentReservationListDtoBuilder withEndDate(LocalDate endDate) {
            this.endDate = endDate;
            return this;
        }

        public PermanentReservationListDtoBuilder withRepetition(RepetitionEnum repetition) {
            this.repetition = repetition;
            return this;
        }

        public PermanentReservationListDtoBuilder withPeriod(Integer period) {
            this.period = period;
            return this;
        }

        public PermanentReservationListDtoBuilder withConfirmed(Boolean confirmed) {
            this.confirmed = confirmed;
            return this;
        }

        public PermanentReservationListDtoBuilder withPax(Long pax) {
            this.pax = pax;
            return this;
        }

        public PermanentReservationListDtoBuilder withHashedId(String hashedId) {
            this.hashedId = hashedId;
            return this;
        }

        public PermanentReservationListDto build() {
            PermanentReservationListDto dto = new PermanentReservationListDto();
            dto.setId(id);
            dto.setUserFirstName(userFirstName);
            dto.setUserLastName(userLastName);
            dto.setStartTime(startTime);
            dto.setEndTime(endTime);
            dto.setStartDate(startDate);
            dto.setEndDate(endDate);
            dto.setRepetition(repetition);
            dto.setPeriod(period);
            dto.setConfirmed(confirmed);
            dto.setPax(pax);
            dto.setHashedId(hashedId);
            return dto;
        }
    }
}
