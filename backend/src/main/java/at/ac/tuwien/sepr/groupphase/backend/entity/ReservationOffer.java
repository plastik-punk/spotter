package at.ac.tuwien.sepr.groupphase.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.io.Serializable;
import java.util.Objects;

@Entity
public class ReservationOffer {
    @EmbeddedId
    private ReservationOfferId id;

    @MapsId("reservationId")
    @ManyToOne
    @JoinColumn(name = "reservation_id", insertable = false, updatable = false)
    private Reservation reservation;

    @MapsId("offerId")
    @ManyToOne
    @JoinColumn(name = "offer_id", insertable = false, updatable = false)
    private SpecialOffer offer;

    @NotNull(message = "Amount must not be null")
    @Positive(message = "Amount should be greater than 0")
    @Column(nullable = false)
    private int amount;

    public ReservationOfferId getId() {
        return id;
    }

    public void setId(ReservationOfferId id) {
        this.id = id;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public SpecialOffer getOffer() {
        return offer;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Embeddable
    public static class ReservationOfferId implements Serializable {
        private Long reservationId;
        private Long offerId;

        public Long getReservationId() {
            return reservationId;
        }

        public void setReservationId(Long reservationId) {
            this.reservationId = reservationId;
        }

        public Long getOfferId() {
            return offerId;
        }

        public void setOfferId(Long offerId) {
            this.offerId = offerId;
        }


        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ReservationOfferId that = (ReservationOfferId) o;
            return reservationId.equals(that.reservationId) && offerId.equals(that.offerId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(reservationId, offerId);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReservationOffer reservationOffer)) return false;
        return id.equals(reservationOffer.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, reservation, offer, amount);
    }

    @Override
    public String toString() {
        return "ReservationOffer{" +
            "id=" + id +
            ", reservation=" + reservation +
            ", offer=" + offer +
            ", amount=" + amount +
            '}';
    }

    public ReservationOffer copy() {
        return ReservationOfferBuilder.aReservationOffer()
            .withReservation(reservation)
            .withOffer(offer)
            .withAmount(amount)
            .build();
    }

    public static final class ReservationOfferBuilder {
        private ReservationOfferId id;
        private Reservation reservation;
        private SpecialOffer offer;
        private int amount;

        private ReservationOfferBuilder() {
        }

        public static ReservationOfferBuilder aReservationOffer() {
            return new ReservationOfferBuilder();
        }

        public ReservationOfferBuilder withReservation(Reservation reservation) {
            this.reservation = reservation;
            return this;
        }

        public ReservationOfferBuilder withOffer(SpecialOffer offer) {
            this.offer = offer;
            return this;
        }

        public ReservationOfferBuilder withAmount(int amount) {
            this.amount = amount;
            return this;
        }

        public ReservationOffer build() {
            ReservationOffer reservationOffer = new ReservationOffer();
            ReservationOfferId id = new ReservationOfferId();
            id.setOfferId(offer.getId());
            id.setReservationId(reservation.getId());
            reservationOffer.setId(id);
            reservationOffer.reservation = this.reservation;
            reservationOffer.offer = this.offer;
            reservationOffer.amount = this.amount;
            return reservationOffer;
        }
    }
}
