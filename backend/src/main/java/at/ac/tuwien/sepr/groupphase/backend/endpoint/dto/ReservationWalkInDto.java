package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

public class ReservationWalkInDto {
    @NotNull(message = "startTime is required")
    private LocalTime startTime;

    @NotNull(message = "Date must not be null")
    @FutureOrPresent(message = "Date cannot be in the past")
    private LocalDate date;

    @NotNull(message = "Pax must not be null")
    @Positive(message = "Pax should be greater than 0")
    private Long pax;

    @NotNull(message = "PlaceId must be given")
    private List<Long> placeIds;

    public LocalTime getStartTime() {
        return startTime;
    }

    public ReservationWalkInDto setStartTime(LocalTime startTime) {
        this.startTime = startTime;
        return this;
    }

    public LocalDate getDate() {
        return date;
    }

    public ReservationWalkInDto setDate(LocalDate date) {
        this.date = date;
        return this;
    }

    public Long getPax() {
        return pax;
    }

    public ReservationWalkInDto setPax(Long pax) {
        this.pax = pax;
        return this;
    }

    public List<Long> getPlaceIds() {
        return placeIds;
    }

    public ReservationWalkInDto setPlaceIds(List<Long> placeIds) {
        this.placeIds = placeIds;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReservationWalkInDto that)) {
            return false;
        }
        return Objects.equals(startTime, that.startTime)
            && Objects.equals(date, that.date)
            && Objects.equals(pax, that.pax)
            && Objects.equals(placeIds, that.placeIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startTime, date, pax, placeIds);
    }

    @Override
    public String toString() {
        return "WalkInReservationDto{" + "startTime=" + startTime + ", date=" + date + ", pax=" + pax + ", placeIds=" + placeIds + '}';
    }

    public ReservationWalkInDto copy() {
        return WalkInReservationDtoBuilder.aWalkInReservationDto()
            .withStartTime(startTime)
            .withDate(date)
            .withPax(pax)
            .withPlaceIds(placeIds)
            .build();
    }

    public static final class WalkInReservationDtoBuilder {
        private LocalTime startTime;
        private LocalDate date;
        private Long pax;
        private List<Long> placeIds;

        private WalkInReservationDtoBuilder() {
        }

        public static WalkInReservationDtoBuilder aWalkInReservationDto() {
            return new WalkInReservationDtoBuilder();
        }

        public WalkInReservationDtoBuilder withStartTime(LocalTime startTime) {
            this.startTime = startTime;
            return this;
        }

        public WalkInReservationDtoBuilder withDate(LocalDate date) {
            this.date = date;
            return this;
        }

        public WalkInReservationDtoBuilder withPax(Long pax) {
            this.pax = pax;
            return this;
        }

        public WalkInReservationDtoBuilder withPlaceIds(List<Long> placeIds) {
            this.placeIds = placeIds;
            return this;
        }

        public ReservationWalkInDto build() {
            ReservationWalkInDto reservationWalkInDto = new ReservationWalkInDto();
            reservationWalkInDto.setStartTime(startTime);
            reservationWalkInDto.setDate(date);
            reservationWalkInDto.setPax(pax);
            reservationWalkInDto.setPlaceIds(placeIds);
            return reservationWalkInDto;
        }
    }

}
