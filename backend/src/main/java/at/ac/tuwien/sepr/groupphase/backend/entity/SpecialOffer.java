package at.ac.tuwien.sepr.groupphase.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;

import java.util.Objects;

@Entity
public class SpecialOffer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Float pricePerPax;
    
    @Lob
    @Column(name = "image")
    private byte[] image;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public SpecialOffer setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public SpecialOffer setName(String name) {
        this.name = name;
        return this;
    }

    public Float getPricePerPax() {
        return pricePerPax;
    }

    public SpecialOffer setPricePerPax(Float pricePerPax) {
        this.pricePerPax = pricePerPax;
        return this;
    }

    public byte[] getImage() {
        return image;
    }

    public SpecialOffer setImage(byte[] image) {
        this.image = image;
        return this;
    }

    // Equals, hashCode, toString methods

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SpecialOffer specialOffer)) {
            return false;
        }
        return id.equals(specialOffer.id)
            && name.equals(specialOffer.name)
            && pricePerPax.equals(specialOffer.pricePerPax);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, pricePerPax);
    }

    @Override
    public String toString() {
        return "SpecialOffer{"
            + "id=" + id
            + ", name='" + name + '\''
            + ", pricePerPax=" + pricePerPax
            + '}';
    }

    // SpecialOfferBuilder

    public static final class SpecialOfferBuilder {
        private Long id;
        private String name;
        private Float pricePerPax;
        private byte[] image;

        private SpecialOfferBuilder() {
        }

        public static SpecialOfferBuilder aSpecialOffer() {
            return new SpecialOfferBuilder();
        }

        public SpecialOfferBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public SpecialOfferBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public SpecialOfferBuilder withPricePerPax(Float pricePerPax) {
            this.pricePerPax = pricePerPax;
            return this;
        }

        public SpecialOfferBuilder withImage(byte[] image) {
            this.image = image;
            return this;
        }

        public SpecialOffer build() {
            SpecialOffer specialOffer = new SpecialOffer();
            specialOffer.setId(id);
            specialOffer.setName(name);
            specialOffer.setPricePerPax(pricePerPax);
            specialOffer.setImage(image);
            return specialOffer;
        }
    }
}
