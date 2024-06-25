package at.ac.tuwien.sepr.groupphase.backend.endpoint.dto;

import java.util.List;
import java.util.Objects;

public class AreaDetailListDto {

    private List<AreaDetailDto> areas;

    public List<AreaDetailDto> getAreas() {
        return areas;
    }

    public void setAreas(List<AreaDetailDto> areas) {
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
        AreaDetailListDto that = (AreaDetailListDto) o;
        return Objects.equals(areas, that.areas);
    }

    @Override
    public int hashCode() {
        return Objects.hash(areas);
    }

    @Override
    public String toString() {
        return "AreaDetailListDto{"
            + "areas="
            + areas
            + '}';
    }
}
