package at.ac.tuwien.sepr.groupphase.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.time.LocalDate;
import java.util.Objects;

@Entity
public class ClosedDay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false, referencedColumnName = "id")
    private Restaurant restaurant;

    @Column(name = "date", nullable = false)
    private LocalDate date;

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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClosedDay that)) {
            return false;
        }
        return Objects.equals(id, that.id)
            && Objects.equals(restaurant, that.restaurant)
            && Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, restaurant, date);
    }

    @Override
    public String toString() {
        return "ClosedDay{"
            + "id=" + id
            + ", restaurant=" + restaurant
            + ", date=" + date
            + '}';
    }

    public static final class ClosedDayBuilder {
        private Long id;
        private Restaurant restaurant;
        private LocalDate date;

        private ClosedDayBuilder() {
        }

        public static ClosedDayBuilder aClosedDay() {
            return new ClosedDayBuilder();
        }

        public ClosedDayBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public ClosedDayBuilder withRestaurant(Restaurant restaurant) {
            this.restaurant = restaurant;
            return this;
        }

        public ClosedDayBuilder withDate(LocalDate date) {
            this.date = date;
            return this;
        }

        public ClosedDay build() {
            ClosedDay closedDay = new ClosedDay();
            closedDay.setId(id);
            closedDay.setRestaurant(restaurant);
            closedDay.setDate(date);
            return closedDay;
        }
    }
}