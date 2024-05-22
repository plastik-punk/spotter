package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class ReservationEditDto {
    private ApplicationUser user;
    private Long reservationId;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDate date;
    private Long pax;
    private String notes;
    private String hashedId;
    private Long placeId;

    public ApplicationUser getUser() {
        return user;
    }

    public void setUser(ApplicationUser user) {
        this.user = user;
    }

    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
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

    public LocalDate getDate() {
        return date;
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

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getHashedId() {
        return hashedId;
    }

    public void setHashedId(String hashedId) {
        this.hashedId = hashedId;
    }

    public Long getPlaceId() {
        return placeId;
    }

    public void setPlaceId(Long placeId) {
        this.placeId = placeId;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        ReservationEditDto that = (ReservationEditDto) object;
        return Objects.equals(user, that.user)
            && Objects.equals(reservationId, that.reservationId)
            && Objects.equals(startTime, that.startTime)
            && Objects.equals(endTime, that.endTime)
            && Objects.equals(date, that.date)
            && Objects.equals(pax, that.pax)
            && Objects.equals(notes, that.notes)
            && Objects.equals(hashedId, that.hashedId)
            && Objects.equals(placeId, that.placeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, reservationId, startTime, endTime, date, pax, notes, hashedId, placeId);
    }

    @Override
    public String toString() {
        return "ReservationEditDto{"
            + "user=" + user
            + ", reservationId=" + reservationId
            + ", startTime=" + startTime
            + ", endTime=" + endTime
            + ", date=" + date
            + ", pax=" + pax
            + ", notes='" + notes + '\''
            + ", hashedId='" + hashedId + '\''
            + ", placeId=" + placeId
            + '}';
    }

    public ReservationEditDto copy() {
        return ReservationEditDtoBuilder.aReservationEditDto()
            .withUser(user)
            .withReservationId(reservationId)
            .withStartTime(startTime)
            .withEndTime(endTime)
            .withDate(date)
            .withPax(pax)
            .withNotes(notes)
            .withHashedId(hashedId)
            .withPlaceId(placeId)
            .build();
    }

    public static final class ReservationEditDtoBuilder {
        private ApplicationUser user;
        private Long reservationId;
        private LocalTime startTime;
        private LocalTime endTime;
        private LocalDate date;
        private Long pax;
        private String notes;
        private String hashedId;
        private Long placeId;

        public ReservationEditDtoBuilder() {
        }

        public static ReservationEditDtoBuilder aReservationEditDto() {
            return new ReservationEditDtoBuilder();
        }

        public ReservationEditDtoBuilder withUser(ApplicationUser user) {
            this.user = user;
            return this;
        }

        public ReservationEditDtoBuilder withReservationId(Long reservationId) {
            this.reservationId = reservationId;
            return this;
        }

        public ReservationEditDtoBuilder withStartTime(LocalTime startTime) {
            this.startTime = startTime;
            return this;
        }

        public ReservationEditDtoBuilder withEndTime(LocalTime endTime) {
            this.endTime = endTime;
            return this;
        }

        public ReservationEditDtoBuilder withDate(LocalDate date) {
            this.date = date;
            return this;
        }

        public ReservationEditDtoBuilder withPax(Long pax) {
            this.pax = pax;
            return this;
        }

        public ReservationEditDtoBuilder withNotes(String notes) {
            this.notes = notes;
            return this;
        }

        public ReservationEditDtoBuilder withHashedId(String hashedId) {
            this.hashedId = hashedId;
            return this;
        }

        public ReservationEditDtoBuilder withPlaceId(Long placeId) {
            this.placeId = placeId;
            return this;
        }

        public ReservationEditDto build() {
            ReservationEditDto reservationEditDto = new ReservationEditDto();
            reservationEditDto.setUser(user);
            reservationEditDto.setReservationId(reservationId);
            reservationEditDto.setStartTime(startTime);
            reservationEditDto.setEndTime(endTime);
            reservationEditDto.setDate(date);
            reservationEditDto.setPax(pax);
            reservationEditDto.setNotes(notes);
            reservationEditDto.setHashedId(hashedId);
            reservationEditDto.setPlaceId(placeId);
            return reservationEditDto;
        }
    }
}

