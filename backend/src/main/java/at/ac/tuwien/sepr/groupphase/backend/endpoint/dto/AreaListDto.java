package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

import java.util.List;
import java.util.Objects;

public class AreaListDto {

    private List<AreaDto> areas;

    public List<AreaDto> getAreas() {
        return areas;
    }

    public void setAreas(List<AreaDto> areas) {
        this.areas = areas;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AreaListDto that = (AreaListDto) o;
        return Objects.equals(areas, that.areas);
    }

    @Override
    public int hashCode() {
        return Objects.hash(areas);
    }

    @Override
    public String toString() {
        return "AreaListDto{"
            + "areas="
            + areas
            + '}';
    }

    public static class AreaDto {

        private Long id;
        private String name;

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

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            AreaDto that = (AreaDto) o;
            return Objects.equals(id, that.id)
                && Objects.equals(name, that.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, name);
        }

        @Override
        public String toString() {
            return "AreaDto{"
                + "id="
                + id
                + ", name='"
                + name + '\''
                + '}';
        }
    }
}
