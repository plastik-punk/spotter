package at.ac.tuwien.sepr.groupphase.backend.entity;

import jakarta.persistence.*;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Objects;

@Entity
@Table(name = "opening_hours")
public class OpeningHours {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "day_of_week", nullable = false)
    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek; // Enum representing the day of the week

    @Column(name = "opening_time", nullable = false)
    private LocalTime openingTime;

    @Column(name = "closing_time", nullable = false)
    private LocalTime closingTime;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
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
            && dayOfWeek == that.dayOfWeek
            && Objects.equals(openingTime, that.openingTime)
            && Objects.equals(closingTime, that.closingTime)
            && Objects.equals(restaurant, that.restaurant);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dayOfWeek, openingTime, closingTime, restaurant);
    }

    @Override
    public String toString() {
        return "OpeningHour{"
            + "id=" + id
            + ", dayOfWeek=" + dayOfWeek
            + ", openingTime=" + openingTime
            + ", closingTime=" + closingTime
            + ", restaurant=" + restaurant
            + '}';
    }

    // Builder pattern
    public static final class OpeningHourBuilder {
        private Long id;
        private DayOfWeek dayOfWeek;
        private LocalTime openingTime;
        private LocalTime closingTime;
        private Restaurant restaurant;

        private OpeningHourBuilder() {
        }

        public static OpeningHourBuilder anOpeningHour() {
            return new OpeningHourBuilder();
        }

        public OpeningHourBuilder withId(Long id) {
            this.id = id;
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

        public OpeningHourBuilder withRestaurant(Restaurant restaurant) {
            this.restaurant = restaurant;
            return this;
        }

        public OpeningHours build() {
            OpeningHours openingHour = new OpeningHours();
            openingHour.setId(id);
            openingHour.setDayOfWeek(dayOfWeek);
            openingHour.setOpeningTime(openingTime);
            openingHour.setClosingTime(closingTime);
            openingHour.setRestaurant(restaurant);
            return openingHour;
        }
    }
}
