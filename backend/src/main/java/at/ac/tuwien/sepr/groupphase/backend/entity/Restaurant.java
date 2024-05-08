package at.ac.tuwien.sepr.groupphase.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

import java.util.Objects;

@Entity
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToOne
    @JoinColumn(name = "openingHours_id", nullable = false, referencedColumnName = "id")
    private OpeningHours openingHours;

    @Column(nullable = false)
    private String address;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OpeningHours getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(OpeningHours openingHours) {
        this.openingHours = openingHours;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Restaurant)) {
            return false;
        }
        Restaurant restaurant = (Restaurant) o;
        return Objects.equals(id, restaurant.id)
            && Objects.equals(name, restaurant.name)
            && Objects.equals(openingHours, restaurant.openingHours)
            && Objects.equals(address, restaurant.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, openingHours, address);
    }

    @Override
    public String toString() {
        return "Restaurant{"
            + "id=" + id
            + ", name='" + name + '\''
            + ", openingHours=" + openingHours
            + ", address='" + address + '\''
            + '}';
    }

    public static final class RestaurantBuilder {
        private Long id;
        private String name;
        private OpeningHours openingHours;
        private String address;

        private RestaurantBuilder() {
        }

        public static RestaurantBuilder aRestaurant() {
            return new RestaurantBuilder();
        }

        public RestaurantBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public RestaurantBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public RestaurantBuilder withOpeningHours(OpeningHours openingHours) {
            this.openingHours = openingHours;
            return this;
        }

        public RestaurantBuilder withAddress(String address) {
            this.address = address;
            return this;
        }

        public Restaurant build() {
            Restaurant restaurant = new Restaurant();
            restaurant.setId(id);
            restaurant.setName(name);
            restaurant.setOpeningHours(openingHours);
            restaurant.setAddress(address);
            return restaurant;
        }
    }
}
