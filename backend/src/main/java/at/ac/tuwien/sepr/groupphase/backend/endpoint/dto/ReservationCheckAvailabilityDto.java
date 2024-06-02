package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class ReservationCheckAvailabilityDto {

    @NotNull(message = "startTime is required")
    private LocalTime startTime;

    private LocalTime endTime;

    @NotNull(message = "Date is required")
    private LocalDate date;

    @NotNull(message = "Pax is required")
    @Positive(message = "Pax should be at least 1")
    private Long pax;

    /**
     * If a reservation is edited, its ID should be excluded from the availability check.
     */
    private Long idToExclude;

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

    public Long getIdToExclude() {
        return idToExclude;
    }

    public void setIdToExclude(Long idToExclude) {
        this.idToExclude = idToExclude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReservationCheckAvailabilityDto that)) {
            return false;
        }
        return Objects.equals(startTime, that.startTime)
            && Objects.equals(endTime, that.endTime)
            && Objects.equals(date, that.date)
            && Objects.equals(pax, that.pax);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startTime, endTime, date, pax);
    }

    @Override
    public String toString() {
        return "ReservationCheckAvailabilityDto{"
            + "startTime=" + startTime
            + ", endTime=" + endTime
            + ", date=" + date
            + ", pax=" + pax
            + ", idToExclude=" + idToExclude
            + '}';
    }

    public ReservationCheckAvailabilityDto copy() {
        return ReservationCheckAvailabilityDtoBuilder.aReservationCheckAvailabilityDto()
            .withStartTime(this.startTime)
            .withEndTime(this.endTime)
            .withDate(this.date)
            .withPax(this.pax)
            .withIdToExclude(this.idToExclude)
            .build();
    }

    public static final class ReservationCheckAvailabilityDtoBuilder {
        private LocalTime startTime;
        private LocalTime endTime;
        private LocalDate date;
        private Long pax;
        private Long idToExclude;

        private ReservationCheckAvailabilityDtoBuilder() {
        }

        public static ReservationCheckAvailabilityDtoBuilder aReservationCheckAvailabilityDto() {
            return new ReservationCheckAvailabilityDtoBuilder();
        }

        public ReservationCheckAvailabilityDtoBuilder withStartTime(LocalTime startTime) {
            this.startTime = startTime;
            return this;
        }

        public ReservationCheckAvailabilityDtoBuilder withEndTime(LocalTime endTime) {
            this.endTime = endTime;
            return this;
        }

        public ReservationCheckAvailabilityDtoBuilder withDate(LocalDate date) {
            this.date = date;
            return this;
        }

        public ReservationCheckAvailabilityDtoBuilder withPax(Long pax) {
            this.pax = pax;
            return this;
        }

        public ReservationCheckAvailabilityDtoBuilder withIdToExclude(Long idToExclude) {
            this.idToExclude = idToExclude;
            return this;
        }

        public ReservationCheckAvailabilityDto build() {
            ReservationCheckAvailabilityDto reservationCheckAvailabilityDto = new ReservationCheckAvailabilityDto();
            reservationCheckAvailabilityDto.setStartTime(startTime);
            reservationCheckAvailabilityDto.setEndTime(endTime);
            reservationCheckAvailabilityDto.setDate(date);
            reservationCheckAvailabilityDto.setPax(pax);
            reservationCheckAvailabilityDto.setIdToExclude(idToExclude);
            return reservationCheckAvailabilityDto;
        }
    }
}