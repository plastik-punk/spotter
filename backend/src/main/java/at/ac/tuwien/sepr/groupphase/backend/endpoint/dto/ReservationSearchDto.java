package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class ReservationSearchDto {

    private LocalDate earliestDate;
    private LocalDate latestDate;
    private LocalTime earliestStartTime;
    private LocalTime latestEndTime;

    public LocalDate getEarliestDate() {
        return earliestDate;
    }

    public ReservationSearchDto setEarliestDate(LocalDate earliestDate) {
        this.earliestDate = earliestDate;
        return this;
    }

    public LocalDate getLatestDate() {
        return latestDate;
    }

    public ReservationSearchDto setLatestDate(LocalDate latestDate) {
        this.latestDate = latestDate;
        return this;
    }

    public LocalTime getEarliestStartTime() {
        return earliestStartTime;
    }

    public ReservationSearchDto setEarliestStartTime(LocalTime earliestStartTime) {
        this.earliestStartTime = earliestStartTime;
        return this;
    }

    public LocalTime getLatestEndTime() {
        return latestEndTime;
    }

    public ReservationSearchDto setLatestEndTime(LocalTime latestEndTime) {
        this.latestEndTime = latestEndTime;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReservationSearchDto that)) {
            return false;
        }
        return Objects.equals(earliestDate, that.earliestDate)
            && Objects.equals(latestDate, that.latestDate)
            && Objects.equals(earliestStartTime, that.earliestStartTime)
            && Objects.equals(latestEndTime, that.latestEndTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(earliestDate, latestDate, earliestStartTime, latestEndTime);
    }

    @Override
    public String toString() {
        return "ReservationSearchDto{"
            + "earliestDate=" + earliestDate
            + ", latestDate=" + latestDate
            + ", earliestStartTime=" + earliestStartTime
            + ", latestEndTime=" + latestEndTime
            + '}';
    }

    public static final class ReservationSearchDtoBuilder {
        private LocalDate earliestDate;
        private LocalDate latestDate;
        private LocalTime earliestStartTime;
        private LocalTime latestEndTime;

        private ReservationSearchDtoBuilder() {
        }

        public static ReservationSearchDtoBuilder aReservationSearchDto() {
            return new ReservationSearchDtoBuilder();
        }

        public ReservationSearchDtoBuilder withEarliestDate(LocalDate earliestDate) {
            this.earliestDate = earliestDate;
            return this;
        }

        public ReservationSearchDtoBuilder withLatestDate(LocalDate latestDate) {
            this.latestDate = latestDate;
            return this;
        }

        public ReservationSearchDtoBuilder withEarliestStartTime(LocalTime earliestStartTime) {
            this.earliestStartTime = earliestStartTime;
            return this;
        }

        public ReservationSearchDtoBuilder withLatestEndTime(LocalTime latestEndTime) {
            this.latestEndTime = latestEndTime;
            return this;
        }

        public ReservationSearchDto build() {
            ReservationSearchDto reservationSearchDto = new ReservationSearchDto();
            reservationSearchDto.setEarliestDate(earliestDate);
            reservationSearchDto.setLatestDate(latestDate);
            reservationSearchDto.setEarliestStartTime(earliestStartTime);
            reservationSearchDto.setLatestEndTime(latestEndTime);
            return reservationSearchDto;
        }
    }
}

