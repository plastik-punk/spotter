package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

import java.util.Objects;

public class SpecialOfferListDto {

    private Long id;
    private String name;
    private Float pricePerPax;

    public Long getId() {
        return id;
    }

    public SpecialOfferListDto setId(Long id) {
        this.id = id;
        return this;
    }

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

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        SpecialOfferListDto that = (SpecialOfferListDto) object;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(pricePerPax, that.pricePerPax);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, pricePerPax);
    }

    @Override
    public String toString() {
        return "SpecialOfferListDto{"
            + "id=" + id
            + ", name='" + name + '\''
            + ", pricePerPax=" + pricePerPax
            + '}';
    }

    public SpecialOfferListDto copy() {
        return SpecialOfferListBuilder.aSpecialOfferListDto()
            .withId(id)
            .withName(name)
            .withPricePerPax(pricePerPax)
            .build();
    }

    public static final class SpecialOfferListBuilder {

        private Long id;
        private String name;
        private Float pricePerPax;

        private SpecialOfferListBuilder() {
        }

        public static SpecialOfferListBuilder aSpecialOfferListDto() {
            return new SpecialOfferListBuilder();
        }

        public SpecialOfferListBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public SpecialOfferListBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public SpecialOfferListBuilder withPricePerPax(Float pricePerPax) {
            this.pricePerPax = pricePerPax;
            return this;
        }

        public SpecialOfferListDto build() {
            SpecialOfferListDto specialOfferListDto = new SpecialOfferListDto();
            specialOfferListDto.setId(id);
            specialOfferListDto.setName(name);
            specialOfferListDto.setPricePerPax(pricePerPax);
            return specialOfferListDto;
        }
    }


}
