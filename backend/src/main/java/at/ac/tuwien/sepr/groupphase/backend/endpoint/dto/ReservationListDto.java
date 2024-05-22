package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class ReservationListDto {

    private Long id;

    private String userFirstName;

    private String userLastName;

    private LocalTime startTime;

    private LocalDate date;

    private LocalTime endTime;

    private Long pax;

    private Long placeId;

    private String hashId;

    public Long getId() {
        return id;
    }


    public ReservationListDto setId(Long id) {
        this.id = id;
        return this;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public ReservationListDto setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
        return this;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public ReservationListDto setUserLastName(String userLastName) {
        this.userLastName = userLastName;
        return this;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public ReservationListDto setStartTime(LocalTime startTime) {
        this.startTime = startTime;
        return this;
    }

    public LocalDate getDate() {
        return date;
    }

    public ReservationListDto setDate(LocalDate date) {
        this.date = date;
        return this;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public ReservationListDto setEndTime(LocalTime endTime) {
        this.endTime = endTime;
        return this;
    }

    public Long getPax() {
        return pax;
    }

    public ReservationListDto setPax(Long pax) {
        this.pax = pax;
        return this;
    }

    public Long getPlaceId() {
        return placeId;
    }

    public ReservationListDto setPlaceId(Long placeId) {
        this.placeId = placeId;
        return this;
    }

    public String getHashId() {
        return hashId;
    }

    public ReservationListDto setHashId(String hashId) {
        this.hashId = hashId;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReservationListDto that)) {
            return false;
        }
        return Objects.equals(id, that.id)
            && Objects.equals(userFirstName, that.userFirstName)
            && Objects.equals(userLastName, that.userLastName)
            && Objects.equals(startTime, that.startTime)
            && Objects.equals(date, that.date)
            && Objects.equals(endTime, that.endTime)
            && Objects.equals(pax, that.pax)
            && Objects.equals(placeId, that.placeId)
            && Objects.equals(hashId, that.hashId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userFirstName, userLastName, startTime, date, endTime, pax, placeId);
    }

    @Override
    public String toString() {
        return "reservationListDto{"
            + "id=" + id
            + ", userFirstName='" + userFirstName + '\''
            + ", userLastName='" + userLastName + '\''
            + ", startTime=" + startTime
            + ", date=" + date
            + ", endTime=" + endTime
            + ", pax=" + pax + '\''
            + ", placeId=" + placeId
            + '}';
    }


    public static final class ReservationListDtoBuilder {
        private Long id;
        private String userFirstName;
        private String userLastName;
        private LocalTime startTime;

        private LocalDate date;

        private LocalTime endTime;
        private Long pax;
        private Long placeId;
        private String hashId;

        private ReservationListDtoBuilder() {
        }

        public static ReservationListDtoBuilder aReservationListDto() {
            return new ReservationListDtoBuilder();
        }

        public ReservationListDtoBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public ReservationListDtoBuilder withUserFirstName(String userFirstName) {
            this.userFirstName = userFirstName;
            return this;
        }

        public ReservationListDtoBuilder withUserLastName(String userLastName) {
            this.userLastName = userLastName;
            return this;
        }

        public ReservationListDtoBuilder withStartTime(LocalTime startTime) {
            this.startTime = startTime;
            return this;
        }

        public ReservationListDtoBuilder withDate(LocalDate date) {
            this.date = date;
            return this;
        }

        public ReservationListDtoBuilder withEndTime(LocalTime endTime) {
            this.endTime = endTime;
            return this;
        }

        public ReservationListDtoBuilder withPax(Long pax) {
            this.pax = pax;
            return this;
        }

        public ReservationListDtoBuilder withPlaceId(Long placeId) {
            this.placeId = placeId;
            return this;
        }

        public ReservationListDtoBuilder withHashId(String hashId) {
            this.hashId = hashId;
            return this;
        }

        public ReservationListDto build() {
            ReservationListDto reservationListDto = new ReservationListDto();
            reservationListDto.setId(id);
            reservationListDto.setUserFirstName(userFirstName);
            reservationListDto.setUserLastName(userLastName);
            reservationListDto.setStartTime(startTime);
            reservationListDto.setDate(date);
            reservationListDto.setEndTime(endTime);
            reservationListDto.setPax(pax);
            reservationListDto.setPlaceId(placeId);
            reservationListDto.setHashId(hashId);
            return reservationListDto;
        }
    }
}
