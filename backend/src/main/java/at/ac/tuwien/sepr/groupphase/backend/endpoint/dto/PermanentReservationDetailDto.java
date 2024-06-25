package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepr.groupphase.backend.enums.RepetitionEnum;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

public class PermanentReservationDetailDto {
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
    @Max(14)
    private Integer period;

    @NotNull(message = "Confirmation status is required")
    private Boolean confirmed;

    @NotNull(message = "Pax is required")
    private Long pax;

    private String hashedId;

    private List<ReservationListDto> singleReservationList;

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
        if (!(o instanceof PermanentReservationDetailDto that)) {
            return false;
        }
        return Objects.equals(id, that.id) && Objects.equals(userFirstName, that.userFirstName)
            && Objects.equals(userLastName, that.userLastName) && Objects.equals(startTime, that.startTime)
            && Objects.equals(endTime, that.endTime) && Objects.equals(startDate, that.startDate)
            && Objects.equals(endDate, that.endDate) && repetition == that.repetition
            && Objects.equals(period, that.period) && Objects.equals(confirmed, that.confirmed)
            && Objects.equals(pax, that.pax);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userFirstName, userLastName, startTime, endTime, startDate,
            endDate, repetition, period, confirmed, singleReservationList);
    }

    @Override
    public String toString() {
        return "PermanentReservationCreateDto{" + "id=" + id + "userFirstName=" + userFirstName
            + "userLastName=" + userLastName + ", startTime=" + startTime
            + ", endTime=" + endTime + ", startDate=" + startDate
            + ", endDate=" + endDate + ", repetition=" + repetition
            + ", period=" + period + ", confirmed=" + confirmed
            + ", pax=" + pax + ", singleReservationList=" + singleReservationList + '}';
    }

    public List<ReservationListDto> getSingleReservationList() {
        return singleReservationList;
    }

    public void setSingleReservationList(List<ReservationListDto> singleReservationList) {
        this.singleReservationList = singleReservationList;
    }

    public static final class PermanentReservationDetailDtoBuilder {
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
        private List<ReservationListDto> singleReservationList;

        private PermanentReservationDetailDtoBuilder() {
        }

        public static PermanentReservationDetailDtoBuilder aPermanentReservationDetailDto() {
            return new PermanentReservationDetailDtoBuilder();
        }

        public PermanentReservationDetailDtoBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public PermanentReservationDetailDtoBuilder withUserFirstName(String userFirstName) {
            this.userFirstName = userFirstName;
            return this;
        }

        public PermanentReservationDetailDtoBuilder withUserLastName(String userLastName) {
            this.userLastName = userLastName;
            return this;
        }


        public PermanentReservationDetailDtoBuilder withStartTime(LocalTime startTime) {
            this.startTime = startTime;
            return this;
        }

        public PermanentReservationDetailDtoBuilder withEndTime(LocalTime endTime) {
            this.endTime = endTime;
            return this;
        }

        public PermanentReservationDetailDtoBuilder withStartDate(LocalDate startDate) {
            this.startDate = startDate;
            return this;
        }

        public PermanentReservationDetailDtoBuilder withEndDate(LocalDate endDate) {
            this.endDate = endDate;
            return this;
        }

        public PermanentReservationDetailDtoBuilder withRepetition(RepetitionEnum repetition) {
            this.repetition = repetition;
            return this;
        }

        public PermanentReservationDetailDtoBuilder withPeriod(Integer period) {
            this.period = period;
            return this;
        }

        public PermanentReservationDetailDtoBuilder withConfirmed(Boolean confirmed) {
            this.confirmed = confirmed;
            return this;
        }

        public PermanentReservationDetailDtoBuilder withPax(Long pax) {
            this.pax = pax;
            return this;
        }

        public PermanentReservationDetailDtoBuilder withHashedId(String hashedId) {
            this.hashedId = hashedId;
            return this;
        }

        public PermanentReservationDetailDtoBuilder withSingleReservationList(List<ReservationListDto> singleReservationList) {
            this.singleReservationList = singleReservationList;
            return this;
        }

        public PermanentReservationDetailDto build() {
            PermanentReservationDetailDto dto = new PermanentReservationDetailDto();
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
            dto.setSingleReservationList(singleReservationList);
            return dto;
        }
    }

}
