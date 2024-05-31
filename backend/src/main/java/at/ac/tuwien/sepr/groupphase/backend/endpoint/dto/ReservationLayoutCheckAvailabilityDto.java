package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class ReservationLayoutCheckAvailabilityDto {

    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDate date;
    private Long idToExclude;
    private Long areaId;

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

    public Long getIdToExclude() {
        return idToExclude;
    }

    public void setIdToExclude(Long idToExclude) {
        this.idToExclude = idToExclude;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReservationLayoutCheckAvailabilityDto that)) {
            return false;
        }
        return Objects.equals(startTime, that.startTime)
            && Objects.equals(endTime, that.endTime)
            && Objects.equals(date, that.date)
            && Objects.equals(idToExclude, that.idToExclude)
            && Objects.equals(areaId, that.areaId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startTime, endTime, date, idToExclude, areaId);
    }

    @Override
    public String toString() {
        return "ReservationLayoutCheckAvailabilityDto{"
            + "startTime=" + startTime
            + ", endTime=" + endTime
            + ", date=" + date
            + ", idToExclude=" + idToExclude
            + ", areaId=" + areaId
            + '}';
    }

    public ReservationLayoutCheckAvailabilityDto copy() {
        return ReservationLayoutCheckAvailabilityDtoBuilder.aReservationLayoutCheckAvailabilityDto()
            .withStartTime(this.startTime)
            .withEndTime(this.endTime)
            .withDate(this.date)
            .withIdToExclude(this.idToExclude)
            .withAreaId(this.areaId)
            .build();
    }

    public static final class ReservationLayoutCheckAvailabilityDtoBuilder {
        private LocalTime startTime;
        private LocalTime endTime;
        private LocalDate date;
        private Long idToExclude;
        private Long areaId;

        private ReservationLayoutCheckAvailabilityDtoBuilder() {
        }

        public static ReservationLayoutCheckAvailabilityDtoBuilder aReservationLayoutCheckAvailabilityDto() {
            return new ReservationLayoutCheckAvailabilityDtoBuilder();
        }

        public ReservationLayoutCheckAvailabilityDtoBuilder withStartTime(LocalTime startTime) {
            this.startTime = startTime;
            return this;
        }

        public ReservationLayoutCheckAvailabilityDtoBuilder withEndTime(LocalTime endTime) {
            this.endTime = endTime;
            return this;
        }

        public ReservationLayoutCheckAvailabilityDtoBuilder withDate(LocalDate date) {
            this.date = date;
            return this;
        }


        public ReservationLayoutCheckAvailabilityDtoBuilder withIdToExclude(Long idToExclude) {
            this.idToExclude = idToExclude;
            return this;
        }

        public ReservationLayoutCheckAvailabilityDtoBuilder withAreaId(Long areaId) {
            this.areaId = areaId;
            return this;
        }

        public ReservationLayoutCheckAvailabilityDto build() {
            ReservationLayoutCheckAvailabilityDto reservationLayoutCheckAvailabilityDto = new ReservationLayoutCheckAvailabilityDto();
            reservationLayoutCheckAvailabilityDto.setStartTime(startTime);
            reservationLayoutCheckAvailabilityDto.setEndTime(endTime);
            reservationLayoutCheckAvailabilityDto.setDate(date);
            reservationLayoutCheckAvailabilityDto.setIdToExclude(idToExclude);
            reservationLayoutCheckAvailabilityDto.setAreaId(areaId);
            return reservationLayoutCheckAvailabilityDto;
        }
    }
}
