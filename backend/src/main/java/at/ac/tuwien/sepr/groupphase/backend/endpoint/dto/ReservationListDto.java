package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

import java.time.LocalDateTime;
import java.util.Objects;

public class ReservationListDto {


    private Long id;

    private String user;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private Long pax;

    private Long placeId;

    public Long getId() {
        return id;
    }

    public ReservationListDto setId(Long id) {
        this.id = id;
        return this;
    }

    public String getUser() {
        return user;
    }

    public ReservationListDto setUser(String user) {
        this.user = user;
        return this;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public ReservationListDto setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
        return this;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public ReservationListDto setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReservationListDto that)) {
            return false;
        }
        return Objects.equals(id, that.id)
            && Objects.equals(user, that.user)
            && Objects.equals(startDate, that.startDate)
            && Objects.equals(endDate, that.endDate)
            && Objects.equals(pax, that.pax)
            && Objects.equals(placeId, that.placeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, startDate, endDate, pax, placeId);
    }

    @Override
    public String toString() {
        return "reservationListDto{"
            + "id=" + id
            + ", user='" + user + '\''
            + ", startDate=" + startDate + '\''
            + ", endDate=" + endDate + '\''
            + ", pax=" + pax + '\''
            + ", placeId=" + placeId
            + '}';
    }


    public static final class ReservationListDtoBuilder {
        private Long id;
        private String user;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private Long pax;
        private Long placeId;

        private ReservationListDtoBuilder() {
        }

        public static ReservationListDtoBuilder ReservationListDto() {
            return new ReservationListDtoBuilder();
        }

        public ReservationListDtoBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public ReservationListDtoBuilder withUser(String user) {
            this.user = user;
            return this;
        }

        public ReservationListDtoBuilder withStartDate(LocalDateTime startDate) {
            this.startDate = startDate;
            return this;
        }

        public ReservationListDtoBuilder withEndDate(LocalDateTime endDate) {
            this.endDate = endDate;
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

        public ReservationListDto build() {
            ReservationListDto reservationListDto = new ReservationListDto();
            reservationListDto.setId(id);
            reservationListDto.setUser(user);
            reservationListDto.setStartDate(startDate);
            reservationListDto.setEndDate(endDate);
            reservationListDto.setPax(pax);
            reservationListDto.setPlaceId(placeId);
            return reservationListDto;
        }
    }
}
