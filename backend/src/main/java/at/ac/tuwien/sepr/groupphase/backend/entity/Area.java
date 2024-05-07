package at.ac.tuwien.sepr.groupphase.backend.entity;

import jakarta.persistence.*;

import java.time.LocalTime;
import java.util.Objects;

@Entity
public class Area {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private boolean isOpen;

    @Column
    private LocalTime openingTime;

    @Column
    private LocalTime closingTime;

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

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Area)) return false;
        Area area = (Area) o;
        return isOpen == area.isOpen &&
            Objects.equals(id, area.id) &&
            Objects.equals(name, area.name) &&
            Objects.equals(openingTime, area.openingTime) &&
            Objects.equals(closingTime, area.closingTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, isOpen, openingTime, closingTime);
    }

    @Override
    public String toString() {
        return "Area{"
            + "id=" + id
            + ", name='" + name + '\''
            + ", isOpen=" + isOpen
            + ", openingTime=" + openingTime
            + ", closingTime=" + closingTime
            + '}';
    }

    public static final class AreaBuilder {
        private Long id;
        private String name;
        private boolean isOpen;
        private LocalTime openingTime;
        private LocalTime closingTime;

        private AreaBuilder() {
        }

        public static AreaBuilder anArea() {
            return new AreaBuilder();
        }

        public AreaBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public AreaBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public AreaBuilder withOpen(boolean open) {
            this.isOpen = open;
            return this;
        }

        public AreaBuilder withOpeningTime(LocalTime openingTime) {
            this.openingTime = openingTime;
            return this;
        }

        public AreaBuilder withClosingTime(LocalTime closingTime) {
            this.closingTime = closingTime;
            return this;
        }

        public Area build() {
            Area area = new Area();
            area.setId(id);
            area.setName(name);
            area.setOpen(isOpen);
            area.setOpeningTime(openingTime);
            area.setClosingTime(closingTime);
            return area;
        }
    }
}
