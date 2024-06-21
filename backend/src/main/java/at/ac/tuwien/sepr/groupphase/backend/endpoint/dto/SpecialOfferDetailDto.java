package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

import java.util.Arrays;
import java.util.Objects;

public class SpecialOfferDetailDto {
    private Long id;
    private String name;
    private Float pricePerPax;
    private byte[] image;

    public Long getId() {
        return id;
    }

    public SpecialOfferDetailDto setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public SpecialOfferDetailDto setName(String name) {
        this.name = name;
        return this;
    }

    public Float getPricePerPax() {
        return pricePerPax;
    }

    public SpecialOfferDetailDto setPricePerPax(Float pricePerPax) {
        this.pricePerPax = pricePerPax;
        return this;
    }

    public byte[] getImage() {
        return image;
    }

    public SpecialOfferDetailDto setImage(byte[] image) {
        this.image = image;
        return this;
    }

    @Override
    public String toString() {
        return "SpecialOfferDetailDto{"
            + "id=" + id
            + ", name='" + name + '\''
            + ", pricePerPax=" + pricePerPax
            + ", image=" + image
            + '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        SpecialOfferDetailDto that = (SpecialOfferDetailDto) object;
        return id.equals(that.id) && name.equals(that.name) && pricePerPax.equals(that.pricePerPax) && Arrays.equals(image, that.image);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, pricePerPax, Arrays.hashCode(image));
    }

    public SpecialOfferDetailDto copy() {
        return SpecialOfferDetailDtoBuilder.aSpecialOfferDetailDto()
            .withId(id)
            .withName(name)
            .withPricePerPax(pricePerPax)
            .withImage(image)
            .build();
    }

    public static final class SpecialOfferDetailDtoBuilder {
        private Long id;
        private String name;
        private Float pricePerPax;
        private byte[] image;

        private SpecialOfferDetailDtoBuilder() {
        }

        public static SpecialOfferDetailDtoBuilder aSpecialOfferDetailDto() {
            return new SpecialOfferDetailDtoBuilder();
        }

        public SpecialOfferDetailDtoBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public SpecialOfferDetailDtoBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public SpecialOfferDetailDtoBuilder withPricePerPax(Float pricePerPax) {
            this.pricePerPax = pricePerPax;
            return this;
        }

        public SpecialOfferDetailDtoBuilder withImage(byte[] image) {
            this.image = image;
            return this;
        }

        public SpecialOfferDetailDto build() {
            SpecialOfferDetailDto specialOfferDetailDto = new SpecialOfferDetailDto();
            specialOfferDetailDto.setId(id);
            specialOfferDetailDto.setName(name);
            specialOfferDetailDto.setPricePerPax(pricePerPax);
            specialOfferDetailDto.setImage(image);
            return specialOfferDetailDto;
        }
    }
}
