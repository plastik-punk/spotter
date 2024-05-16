package at.ac.tuwien.sepr.groupphase.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

@Entity
public class OpeningHours {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false, referencedColumnName = "id")
    private Restaurant restaurant;

    @Column(name = "day_of_week", nullable = false)
    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek; // Enum representing the day of the week

    @Column(name = "opening_time", nullable = false)
    private LocalTime openingTime;

    @Column(name = "closing_time", nullable = false)
    private LocalTime closingTime;

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void getRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public LocalTime getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(LocalTime openingTime) {
        this.openingTime = openingTime;
    }

    public LocalTime getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(LocalTime closingTime) {
        this.closingTime = closingTime;
    }

    // Equals, hashCode, and toString methods
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OpeningHours that)) {
            return false;
        }
        return Objects.equals(id, that.id)
            && Objects.equals(restaurant, that.restaurant)
            && dayOfWeek == that.dayOfWeek
            && Objects.equals(openingTime, that.openingTime)
            && Objects.equals(closingTime, that.closingTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, restaurant, dayOfWeek, openingTime, closingTime);
    }

    @Override
    public String toString() {
        return "OpeningHour{"
            + "id=" + id
            + ", restaurant=" + restaurant
            + ", dayOfWeek=" + dayOfWeek
            + ", openingTime=" + openingTime
            + ", closingTime=" + closingTime
            + '}';
    }

    // Builder pattern
    public static final class OpeningHourBuilder {
        private Long id;
        private Restaurant restaurant;
        private DayOfWeek dayOfWeek;
        private LocalTime openingTime;
        private LocalTime closingTime;

        private OpeningHourBuilder() {
        }

        public static OpeningHourBuilder anOpeningHour() {
            return new OpeningHourBuilder();
        }

        public OpeningHourBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public OpeningHourBuilder withRestaurant(Restaurant restaurant) {
            this.restaurant = restaurant;
            return this;
        }

        public OpeningHourBuilder withDayOfWeek(DayOfWeek dayOfWeek) {
            this.dayOfWeek = dayOfWeek;
            return this;
        }

        public OpeningHourBuilder withOpeningTime(LocalTime openingTime) {
            this.openingTime = openingTime;
            return this;
        }

        public OpeningHourBuilder withClosingTime(LocalTime closingTime) {
            this.closingTime = closingTime;
            return this;
        }

        public OpeningHours build() {
            OpeningHours openingHour = new OpeningHours();
            openingHour.setId(id);
            openingHour.setRestaurant(restaurant);
            openingHour.setDayOfWeek(dayOfWeek);
            openingHour.setOpeningTime(openingTime);
            openingHour.setClosingTime(closingTime);
            return openingHour;
        }
    }
}
