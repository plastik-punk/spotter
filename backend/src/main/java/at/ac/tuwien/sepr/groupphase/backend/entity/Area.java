package at.ac.tuwien.sepr.groupphase.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

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

    @Column(nullable = false)
    private boolean isMain;

    @Column(nullable = true)
    private LocalTime openingTime;

    @Column(nullable = true)
    private LocalTime closingTime;

    @Column(nullable = false)
    @Min(0)
    @Max(15)
    private int width;

    @Column(nullable = false)
    @Min(0)
    @Max(8)
    private int height;

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

    public void setIsOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }

    public boolean isMain() {
        return isMain;
    }

    public void setIsMain(boolean isMain) {
        this.isMain = isMain;
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

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Area)) {
            return false;
        }
        Area area = (Area) o;
        return width == area.width
            && height == area.height
            && Objects.equals(id, area.id)
            && Objects.equals(name, area.name)
            && Objects.equals(isOpen, area.isOpen)
            && Objects.equals(isMain, area.isMain)
            && Objects.equals(openingTime, area.openingTime)
            && Objects.equals(closingTime, area.closingTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, isOpen, isMain, openingTime, closingTime, width, height);
    }

    @Override
    public String toString() {
        return "Area{"
            + "id=" + id
            + ", name='" + name + '\''
            + ", isOpen=" + isOpen
            + ", isMain=" + isMain
            + ", openingTime=" + openingTime
            + ", closingTime=" + closingTime
            + '}';
    }

    public static final class AreaBuilder {
        private Long id;
        private String name;
        private boolean isOpen;
        private boolean isMain;
        private LocalTime openingTime;
        private LocalTime closingTime;
        private int width;
        private int height;

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

        public AreaBuilder withMain(boolean main) {
            this.isMain = main;
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

        public AreaBuilder withWidth(int width) {
            this.width = width;
            return this;
        }

        public AreaBuilder withHeight(int height) {
            this.height = height;
            return this;
        }

        public Area build() {
            Area area = new Area();
            area.setId(id);
            area.setName(name);
            area.setIsOpen(isOpen);
            area.setIsMain(isMain);
            area.setOpeningTime(openingTime);
            area.setClosingTime(closingTime);
            area.setWidth(width);
            area.setHeight(height);
            return area;
        }
    }
}
