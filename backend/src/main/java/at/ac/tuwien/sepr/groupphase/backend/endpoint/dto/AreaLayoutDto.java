package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

import java.util.List;
import java.util.Objects;

public class AreaLayoutDto {

    private Integer width;
    private Integer height;
    private List<PlaceVisualDto> placeVisuals;

    // Getters and Setters
    public Integer getWidth() {
        return width;

    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public List<PlaceVisualDto> getPlaceVisuals() {
        return placeVisuals;
    }

    public void setPlaceVisuals(List<PlaceVisualDto> placeVisuals) {
        this.placeVisuals = placeVisuals;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AreaLayoutDto that = (AreaLayoutDto) o;
        return Objects.equals(width, that.width)
            && Objects.equals(height, that.height)
            && Objects.equals(placeVisuals, that.placeVisuals);
    }

    @Override
    public int hashCode() {
        return Objects.hash(width, height, placeVisuals);
    }

    @Override
    public String toString() {
        return "AreaVisualDto{"
            + "width="
            + width
            + ", height="
            + height
            + ", placeVisuals="
            + placeVisuals
            + '}';
    }
}
