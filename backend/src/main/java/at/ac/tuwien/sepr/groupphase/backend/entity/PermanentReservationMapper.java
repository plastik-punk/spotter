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
public class PermanentReservationMapper {
    @EmbeddedId
    private PermanentReservationMapperId id;

    @MapsId("permanentReservationId")
    @ManyToOne
    @JoinColumn(name = "permanent_reservation_id", insertable = false, updatable = false)
    private PermanentReservation permanentReservation;

    @MapsId("reservationId")
    @ManyToOne
    @JoinColumn(name = "reservation_id", insertable = false, updatable = false)
    private Reservation reservation;

    public PermanentReservationMapperId getId() {
        return id;
    }

    public void setId(PermanentReservationMapperId id) {
        this.id = id;
    }

    public PermanentReservation getPermanentReservation() {
        return permanentReservation;
    }

    public void setPermanentReservation(PermanentReservation permanentReservation) {
        this.permanentReservation = permanentReservation;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PermanentReservationMapper)) {
            return false;
        }
        PermanentReservationMapper that = (PermanentReservationMapper) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "id=" + "PermanentReservationMapper{" + id + ", permanentReservation=" + permanentReservation + ", reservation=" + reservation + '}';
    }

    @Embeddable
    public static class PermanentReservationMapperId implements Serializable {
        private Long permanentReservationId;
        private Long reservationId;

        public Long getPermanentReservationId() {
            return permanentReservationId;
        }

        public void setPermanentReservationId(Long permanentReservationId) {
            this.permanentReservationId = permanentReservationId;
        }

        public Long getReservationId() {
            return reservationId;
        }

        public void setReservationId(Long reservationId) {
            this.reservationId = reservationId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            PermanentReservationMapperId that = (PermanentReservationMapperId) o;
            return Objects.equals(permanentReservationId, that.permanentReservationId)
                && Objects.equals(reservationId, that.reservationId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(permanentReservationId, reservationId);
        }
    }

    public static final class PermanentReservationMapperBuilder {
        private PermanentReservationMapperId id;
        private Reservation reservation;
        private PermanentReservation permanentReservation;

        private PermanentReservationMapperBuilder() {
        }

        public static PermanentReservationMapperBuilder aPermanentReservationMapper() {
            return new PermanentReservationMapperBuilder();
        }

        public PermanentReservationMapperBuilder withReservation(Reservation reservation) {
            this.reservation = reservation;
            return this;
        }

        public PermanentReservationMapperBuilder withPermanentReservation(PermanentReservation permanentReservation) {
            this.permanentReservation = permanentReservation;
            return this;
        }

        public PermanentReservationMapper build() {
            PermanentReservationMapper permanentReservationMapper = new PermanentReservationMapper();
            permanentReservationMapper.setId(id);
            permanentReservationMapper.setPermanentReservation(permanentReservation);
            permanentReservationMapper.setReservation(reservation);
            return permanentReservationMapper;
        }
    }
}
