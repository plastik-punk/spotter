package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

import java.util.Objects;

public class RestaurantDto {
    private String name;
    private String address;

    public String getName() {
        return name;
    }

    public RestaurantDto setName(String name) {
        this.name = name;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public RestaurantDto setAddress(String address) {
        this.address = address;
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
        RestaurantDto that = (RestaurantDto) object;
        return name.equals(that.name) && address.equals(that.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, address);
    }

    @Override
    public String toString() {
        return "RestaurantDto{" + "name='" + name + '\'' + ", address='" + address + '\'' + '}';
    }

    public static final class RestaurantDtoBuilder {
        private String name;
        private String address;

        private RestaurantDtoBuilder() {
        }

        public static RestaurantDtoBuilder aRestaurantDto() {
            return new RestaurantDtoBuilder();
        }

        public RestaurantDtoBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public RestaurantDtoBuilder withAddress(String address) {
            this.address = address;
            return this;
        }

        public RestaurantDto build() {
            RestaurantDto restaurantDto = new RestaurantDto();
            restaurantDto.setName(name);
            restaurantDto.setAddress(address);
            return restaurantDto;
        }
    }
}
