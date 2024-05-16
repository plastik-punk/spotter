package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class ReservationCheckAvailabilityDto {

    private LocalTime startTime;

    private LocalDate date;

    private Long pax;

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReservationCheckAvailabilityDto that)) {
            return false;
        }
        return Objects.equals(startTime, that.startTime)
            && Objects.equals(date, that.date)
            && Objects.equals(pax, that.pax);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startTime, date, pax);
    }

    @Override
    public String toString() {
        return "ReservationCreateDto{"
            + "startTime=" + startTime
            + ", date=" + date
            + ", pax=" + pax
            + '}';
    }

    public ReservationCreateDto copy() {
        return ReservationCreateDtoBuilder.aReservationCreateDto()
            .withStartTime(this.startTime)
            .withDate(this.date)
            .withPax(this.pax)
            .build();
    }

    public static final class ReservationCreateDtoBuilder {
        private LocalTime startTime;
        private LocalDate date;
        private Long pax;

        private ReservationCreateDtoBuilder() {
        }

        public static ReservationCreateDtoBuilder aReservationCreateDto() {
            return new ReservationCreateDtoBuilder();
        }

        public ReservationCreateDtoBuilder withStartTime(LocalTime startTime) {
            this.startTime = startTime;
            return this;
        }

        public ReservationCreateDtoBuilder withDate(LocalDate date) {
            this.date = date;
            return this;
        }

        public ReservationCreateDtoBuilder withPax(Long pax) {
            this.pax = pax;
            return this;
        }

        public ReservationCreateDto build() {
            ReservationCreateDto reservationCreateDto = new ReservationCreateDto();
            reservationCreateDto.setStartTime(startTime);
            reservationCreateDto.setDate(date);
            reservationCreateDto.setPax(pax);
            return reservationCreateDto;
        }
    }
}