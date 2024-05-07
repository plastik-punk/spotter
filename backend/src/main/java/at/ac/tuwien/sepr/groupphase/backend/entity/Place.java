package at.ac.tuwien.sepr.groupphase.backend.entity;

import at.ac.tuwien.sepr.groupphase.backend.enums.StatusEnum;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class Place {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long pax;

    @Column(nullable = false)
    private StatusEnum status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPax() {
        return pax;
    }

    public void setPax(Long pax) {
        this.pax = pax;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Place place)) return false;
        return Objects.equals(id, place.id) &&
            Objects.equals(pax, place.pax) &&
            status == place.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, pax, status);
    }

    @Override
    public String toString() {
        return "Place{" +
            "id=" + id +
            ", pax=" + pax +
            ", status=" + status +
            '}';
    }

    public static final class PlaceBuilder {
        private Long id;
        private Long pax;
        private StatusEnum status;

        private PlaceBuilder() {
        }

        public static PlaceBuilder aPlace() {
            return new PlaceBuilder();
        }

        public PlaceBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public PlaceBuilder withPax(Long pax) {
            this.pax = pax;
            return this;
        }

        public PlaceBuilder withStatus(StatusEnum status) {
            this.status = status;
            return this;
        }

        public Place build() {
            Place place = new Place();
            place.setId(id);
            place.setPax(pax);
            place.setStatus(status);
            return place;
        }
    }
}
