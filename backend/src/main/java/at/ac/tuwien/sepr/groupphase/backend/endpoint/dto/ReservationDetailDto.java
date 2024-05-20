package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

public class ReservationDetailDto {

    private Long id;

    private LocalTime startTime;

    private LocalTime endTime;

    private LocalDate date;

    private Long pax;

    private String notes;

    private List<Long> placeId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public List<Long> getPlaceId() {
        return placeId;
    }

    public void setPlaceId(List<Long> placeId) {
        this.placeId = placeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReservationDetailDto that)) {
            return false;
        }
        return Objects.equals(id, that.id)
            && Objects.equals(startTime, that.startTime)
            && Objects.equals(endTime, that.endTime)
            && Objects.equals(date, that.date)
            && Objects.equals(pax, that.pax)
            && Objects.equals(notes, that.notes)
            && Objects.equals(placeId, that.placeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, startTime, endTime, date, pax, notes, placeId);
    }

    @Override
    public String toString() {
        return "ReservationDetailDto{"
            + "id=" + id
            + ", startTime=" + startTime
            + ", endTime=" + endTime
            + ", date=" + date
            + ", pax=" + pax
            + ", notes='" + notes + '\''
            + ", placeId=" + placeId
            + '}';
    }

    public ReservationDetailDto copy() {
        return ReservationDetailDtoBuilder.aReservationDetailDto()
            .withId(id)
            .withStartTime(startTime)
            .withEndTime(endTime)
            .withDate(date)
            .withPax(pax)
            .withNotes(notes)
            .withPlaceId(placeId)
            .build();
    }

    public static final class ReservationDetailDtoBuilder {
        private Long id;
        private LocalTime startTime;
        private LocalTime endTime;
        private LocalDate date;
        private Long pax;
        private String notes;
        private List<Long> placeId;

        private ReservationDetailDtoBuilder() {
        }

        public static ReservationDetailDtoBuilder aReservationDetailDto() {
            return new ReservationDetailDtoBuilder();
        }

        public ReservationDetailDtoBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public ReservationDetailDtoBuilder withStartTime(LocalTime startTime) {
            this.startTime = startTime;
            return this;
        }

        public ReservationDetailDtoBuilder withEndTime(LocalTime endTime) {
            this.endTime = endTime;
            return this;
        }

        public ReservationDetailDtoBuilder withDate(LocalDate date) {
            this.date = date;
            return this;
        }

        public ReservationDetailDtoBuilder withPax(Long pax) {
            this.pax = pax;
            return this;
        }

        public ReservationDetailDtoBuilder withNotes(String notes) {
            this.notes = notes;
            return this;
        }

        public ReservationDetailDtoBuilder withPlaceId(List<Long> placeId) {
            this.placeId = placeId;
            return this;
        }

        public ReservationDetailDto build() {
            ReservationDetailDto reservationDetailDto = new ReservationDetailDto();
            reservationDetailDto.setId(id);
            reservationDetailDto.setStartTime(startTime);
            reservationDetailDto.setEndTime(endTime);
            reservationDetailDto.setDate(date);
            reservationDetailDto.setPax(pax);
            reservationDetailDto.setNotes(notes);
            reservationDetailDto.setPlaceId(placeId);
            return reservationDetailDto;
        }
    }
}