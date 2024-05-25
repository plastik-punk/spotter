package at.ac.tuwien.sepr.groupphase.backend.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;

import java.io.Serializable;
import java.util.Objects;

@Entity
public class ReservationPlace {
    @EmbeddedId
    private ReservationPlaceId id;

    @MapsId("reservationId")
    @ManyToOne
    @JoinColumn(name = "reservation_id", insertable = false, updatable = false)
    private Reservation reservation;

    @MapsId("placeId")
    @ManyToOne
    @JoinColumn(name = "place_id", insertable = false, updatable = false)
    private Place place;

    public ReservationPlaceId getId() {
        return id;
    }

    public void setId(ReservationPlaceId id) {
        this.id = id;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public Place getPlace() {
        return place;
    }

    @Embeddable
    public static class ReservationPlaceId implements Serializable {

        private Long reservationId;
        private Long placeId;

        public Long getReservationId() {
            return reservationId;
        }

        public void setReservationId(Long reservationId) {
            this.reservationId = reservationId;
        }

        public Long getPlaceId() {
            return placeId;
        }

        public void setPlaceId(Long placeId) {
            this.placeId = placeId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            ReservationPlaceId that = (ReservationPlaceId) o;
            return Objects.equals(reservationId, that.reservationId)
                && Objects.equals(placeId, that.placeId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(reservationId, placeId);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReservationPlace reservationPlace)) {
            return false;
        }
        return id.equals(reservationPlace.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, reservation, place);
    }

    @Override
    public String toString() {
        return "ReservationPlace{"
            + "id=" + id
            + ", reservation=" + reservation
            + ", place=" + place
            + '}';
    }

    public ReservationPlace copy() {
        return ReservationPlaceBuilder.aReservationPlace()
            .withReservation(reservation)
            .withPlace(place)
            .build();
    }

    public static final class ReservationPlaceBuilder {
        private ReservationPlaceId id;
        private Reservation reservation;
        private Place place;

        private ReservationPlaceBuilder() {
        }

        public static ReservationPlaceBuilder aReservationPlace() {
            return new ReservationPlaceBuilder();
        }

        public ReservationPlaceBuilder withReservation(Reservation reservation) {
            this.reservation = reservation;
            return this;
        }

        public ReservationPlaceBuilder withPlace(Place place) {
            this.place = place;
            return this;
        }

        public ReservationPlace build() {
            ReservationPlace reservationPlace = new ReservationPlace();
            ReservationPlaceId id = new ReservationPlaceId();
            id.setPlaceId(place.getId());
            id.setReservationId(reservation.getId());
            reservationPlace.setId(id);
            reservationPlace.reservation = reservation;
            reservationPlace.place = place;
            return reservationPlace;
        }
    }
}