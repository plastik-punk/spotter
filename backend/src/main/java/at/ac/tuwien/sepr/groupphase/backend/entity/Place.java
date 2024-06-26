package at.ac.tuwien.sepr.groupphase.backend.entity;

import at.ac.tuwien.sepr.groupphase.backend.enums.StatusEnum;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;


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

    @Column(nullable = false)
    private Integer number;

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

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Place place)) {
            return false;
        }
        return Objects.equals(id, place.id)
            && Objects.equals(pax, place.pax)
            && status == place.status
            && Objects.equals(number, place.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, pax, status);
    }

    @Override
    public String toString() {
        return "Place{"
            + "id=" + id
            + ", pax=" + pax
            + ", status=" + status
            + ", number=" + number
            + '}';
    }

    public static final class PlaceBuilder {
        private Long id;
        private Long pax;
        private StatusEnum status;
        private Integer number;

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

        public PlaceBuilder withNumber(Integer number) {
            this.number = number;
            return this;
        }

        public Place build() {
            Place place = new Place();
            place.setId(id);
            place.setPax(pax);
            place.setStatus(status);
            place.setNumber(number);
            return place;
        }
    }
}
