package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

public class SpecialOfferAmountDto {
    private SpecialOfferListDto specialOffer;
    private Integer amount;

    public SpecialOfferListDto getSpecialOffer() {
        return specialOffer;
    }

    public SpecialOfferAmountDto setSpecialOffer(SpecialOfferListDto specialOffer) {
        this.specialOffer = specialOffer;
        return this;
    }

    public Integer getAmount() {
        return amount;
    }

    public SpecialOfferAmountDto setAmount(Integer amount) {
        this.amount = amount;
        return this;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        SpecialOfferAmountDto that = (SpecialOfferAmountDto) object;
        return java.util.Objects.equals(specialOffer, that.specialOffer) && java.util.Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(specialOffer, amount);
    }

    @Override
    public String toString() {
        return "SpecialOfferAmountDto{"
            + "specialOffer=" + specialOffer
            + ", amount=" + amount
            + '}';
    }

    public SpecialOfferAmountDto copy() {
        return SpecialOfferAmountDtoBuilder.aSpecialOfferAmountDto()
            .withSpecialOffer(specialOffer)
            .withAmount(amount)
            .build();
    }

    public static final class SpecialOfferAmountDtoBuilder {
        private SpecialOfferListDto specialOffer;
        private Integer amount;

        private SpecialOfferAmountDtoBuilder() {
        }

        public static SpecialOfferAmountDtoBuilder aSpecialOfferAmountDto() {
            return new SpecialOfferAmountDtoBuilder();
        }

        public SpecialOfferAmountDtoBuilder withSpecialOffer(SpecialOfferListDto specialOffer) {
            this.specialOffer = specialOffer;
            return this;
        }

        public SpecialOfferAmountDtoBuilder withAmount(Integer amount) {
            this.amount = amount;
            return this;
        }

        public SpecialOfferAmountDto build() {
            SpecialOfferAmountDto specialOfferAmountDto = new SpecialOfferAmountDto();
            specialOfferAmountDto.setSpecialOffer(specialOffer);
            specialOfferAmountDto.setAmount(amount);
            return specialOfferAmountDto;
        }
    }
}
