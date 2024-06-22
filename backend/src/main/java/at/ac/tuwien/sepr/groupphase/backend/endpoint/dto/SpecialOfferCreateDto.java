package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

public class SpecialOfferCreateDto {
    private String name;
    private Float pricePerPax;
    private MultipartFile image;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getPricePerPax() {
        return pricePerPax;
    }

    public void setPricePerPax(Float pricePerPax) {
        this.pricePerPax = pricePerPax;
    }

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        SpecialOfferCreateDto that = (SpecialOfferCreateDto) object;
        return Objects.equals(name, that.name) && Objects.equals(pricePerPax, that.pricePerPax) && Objects.equals(image, that.image);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, pricePerPax, image);
    }

    @Override
    public String toString() {
        return "SpecialOfferCreateDto{"
            + "name='" + name + '\''
            + ", pricePerPax=" + pricePerPax
            + ", image=" + (image != null ? image.getOriginalFilename() : null)
            + '}';
    }

    public SpecialOfferCreateDto copy() {
        return SpecialOfferCreateBuilder.aSpecialOfferCreateDto()
            .withName(name)
            .withPricePerPax(pricePerPax)
            .withImage(image)
            .build();
    }

    public static final class SpecialOfferCreateBuilder {
        private String name;
        private Float pricePerPax;
        private MultipartFile image;

        public SpecialOfferCreateBuilder() {
        }

        public static SpecialOfferCreateBuilder aSpecialOfferCreateDto() {
            return new SpecialOfferCreateBuilder();
        }

        public SpecialOfferCreateBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public SpecialOfferCreateBuilder withPricePerPax(Float pricePerPax) {
            this.pricePerPax = pricePerPax;
            return this;
        }

        public SpecialOfferCreateBuilder withImage(MultipartFile image) {
            this.image = image;
            return this;
        }

        public SpecialOfferCreateDto build() {
            SpecialOfferCreateDto specialOfferCreateDto = new SpecialOfferCreateDto();
            specialOfferCreateDto.setName(name);
            specialOfferCreateDto.setPricePerPax(pricePerPax);
            specialOfferCreateDto.setImage(image);
            return specialOfferCreateDto;
        }
    }
}
