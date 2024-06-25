package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

public class ReservationModalDetailDto {

    private String firstName;
    private String lastName;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDate date;
    private String notes;
    private List<Long> placeIds;

    public String getFirstName() {
        return firstName;
    }

    public ReservationModalDetailDto setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public ReservationModalDetailDto setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public ReservationModalDetailDto setStartTime(LocalTime startTime) {
        this.startTime = startTime;
        return this;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public ReservationModalDetailDto setEndTime(LocalTime endTime) {
        this.endTime = endTime;
        return this;
    }

    public LocalDate getDate() {
        return date;
    }

    public ReservationModalDetailDto setDate(LocalDate date) {
        this.date = date;
        return this;
    }

    public String getNotes() {
        return notes;
    }

    public ReservationModalDetailDto setNotes(String notes) {
        this.notes = notes;
        return this;
    }

    public List<Long> getPlaceIds() {
        return placeIds;
    }

    public ReservationModalDetailDto setPlaceIds(List<Long> placeIds) {
        this.placeIds = placeIds;
        return this;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        ReservationModalDetailDto that = (ReservationModalDetailDto) object;
        return Objects.equals(firstName, that.firstName)
            && Objects.equals(lastName, that.lastName)
            && Objects.equals(startTime, that.startTime)
            && Objects.equals(endTime, that.endTime)
            && Objects.equals(date, that.date)
            && Objects.equals(notes, that.notes)
            && Objects.equals(placeIds, that.placeIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, startTime, endTime, date, notes, placeIds);
    }

    @Override
    public String toString() {
        return "ReservationModalDetailDto{"
            + "firstName='" + firstName + '\''
            + ", lastName='" + lastName + '\''
            + ", startTime=" + startTime
            + ", endTime=" + endTime
            + ", date=" + date
            + ", notes='" + notes + '\''
            + ", placeIds=" + placeIds
            + '}';
    }

    public ReservationModalDetailDto copy() {
        return new ReservationModalDetailDto()
            .setFirstName(firstName)
            .setLastName(lastName)
            .setStartTime(startTime)
            .setEndTime(endTime)
            .setDate(date)
            .setNotes(notes)
            .setPlaceIds(placeIds);
    }

    public static final class ReservationModalDetailDtoBuilder {
        private String firstName;
        private String lastName;
        private LocalTime startTime;
        private LocalTime endTime;
        private LocalDate date;
        private String notes;
        private List<Long> placeIds;

        private ReservationModalDetailDtoBuilder() {
        }

        public static ReservationModalDetailDtoBuilder aReservationModalDetailDto() {
            return new ReservationModalDetailDtoBuilder();
        }

        public ReservationModalDetailDtoBuilder withFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public ReservationModalDetailDtoBuilder withLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public ReservationModalDetailDtoBuilder withStartTime(LocalTime startTime) {
            this.startTime = startTime;
            return this;
        }

        public ReservationModalDetailDtoBuilder withEndTime(LocalTime endTime) {
            this.endTime = endTime;
            return this;
        }

        public ReservationModalDetailDtoBuilder withDate(LocalDate date) {
            this.date = date;
            return this;
        }

        public ReservationModalDetailDtoBuilder withNotes(String notes) {
            this.notes = notes;
            return this;
        }

        public ReservationModalDetailDtoBuilder withPlaceIds(List<Long> placeIds) {
            this.placeIds = placeIds;
            return this;
        }

        public ReservationModalDetailDto build() {
            ReservationModalDetailDto reservationModalDetailDto = new ReservationModalDetailDto();
            reservationModalDetailDto.setFirstName(firstName);
            reservationModalDetailDto.setLastName(lastName);
            reservationModalDetailDto.setStartTime(startTime);
            reservationModalDetailDto.setEndTime(endTime);
            reservationModalDetailDto.setDate(date);
            reservationModalDetailDto.setNotes(notes);
            reservationModalDetailDto.setPlaceIds(placeIds);
            return reservationModalDetailDto;
        }
    }
}
