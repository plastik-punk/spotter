package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class PermanentReservationSearchDto {

    private Long userId;
    private LocalDate earliestDate;
    private LocalDate latestDate;
    private LocalTime earliestStartTime;
    private LocalTime latestEndTime;

    public Long getUserId() {
        return userId;
    }

    public PermanentReservationSearchDto setUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public LocalDate getEarliestDate() {
        return earliestDate;
    }

    public PermanentReservationSearchDto setEarliestDate(LocalDate earliestDate) {
        this.earliestDate = earliestDate;
        return this;
    }

    public LocalDate getLatestDate() {
        return latestDate;
    }

    public PermanentReservationSearchDto setLatestDate(LocalDate latestDate) {
        this.latestDate = latestDate;
        return this;
    }

    public LocalTime getEarliestStartTime() {
        return earliestStartTime;
    }

    public PermanentReservationSearchDto setEarliestStartTime(LocalTime earliestStartTime) {
        this.earliestStartTime = earliestStartTime;
        return this;
    }

    public LocalTime getLatestEndTime() {
        return latestEndTime;
    }

    public PermanentReservationSearchDto setLatestEndTime(LocalTime latestEndTime) {
        this.latestEndTime = latestEndTime;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PermanentReservationSearchDto that)) {
            return false;
        }
        return Objects.equals(userId, that.userId)
            && Objects.equals(earliestDate, that.earliestDate)
            && Objects.equals(latestDate, that.latestDate)
            && Objects.equals(earliestStartTime, that.earliestStartTime)
            && Objects.equals(latestEndTime, that.latestEndTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, earliestDate, latestDate, earliestStartTime, latestEndTime);
    }

    @Override
    public String toString() {
        return "PermanentReservationSearchDto{"
            + "userId=" + userId
            + ", earliestDate=" + earliestDate
            + ", latestDate=" + latestDate
            + ", earliestStartTime=" + earliestStartTime
            + ", latestEndTime=" + latestEndTime + '}';
    }

    public static final class PermanentReservationSearchBuilder {
        private LocalDate earliestDate;
        private LocalDate latestDate;
        private LocalTime earliestStartTime;
        private LocalTime latestEndTime;

        private PermanentReservationSearchBuilder() {
        }

        public static PermanentReservationSearchBuilder aReservationSearchDto() {
            return new PermanentReservationSearchBuilder();
        }

        public PermanentReservationSearchBuilder withEarliestDate(LocalDate earliestDate) {
            this.earliestDate = earliestDate;
            return this;
        }

        public PermanentReservationSearchBuilder withLatestDate(LocalDate latestDate) {
            this.latestDate = latestDate;
            return this;
        }

        public PermanentReservationSearchBuilder withEarliestStartTime(LocalTime earliestStartTime) {
            this.earliestStartTime = earliestStartTime;
            return this;
        }

        public PermanentReservationSearchBuilder withLatestEndTime(LocalTime latestEndTime) {
            this.latestEndTime = latestEndTime;
            return this;
        }

        public PermanentReservationSearchDto build() {
            PermanentReservationSearchDto permanentReservationSearchDto = new PermanentReservationSearchDto();
            permanentReservationSearchDto.setEarliestDate(earliestDate);
            permanentReservationSearchDto.setLatestDate(latestDate);
            permanentReservationSearchDto.setEarliestStartTime(earliestStartTime);
            permanentReservationSearchDto.setLatestEndTime(latestEndTime);
            return permanentReservationSearchDto;
        }
    }
}
