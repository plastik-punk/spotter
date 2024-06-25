package at.ac.tuwien.sepr.groupphase.backend.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;

import java.io.Serializable;
import java.util.Objects;

@Entity
public class AreaPlaceSegment {
    @EmbeddedId
    private AreaPlaceSegmentId id;

    @MapsId("areaId")
    @ManyToOne
    @JoinColumn(name = "area_id", insertable = false, updatable = false)
    private Area area;

    @MapsId("placeId")
    @ManyToOne
    @JoinColumn(name = "place_id", insertable = false, updatable = false)
    private Place place;

    @MapsId("segmentId")
    @ManyToOne
    @JoinColumn(name = "segment_id", insertable = false, updatable = false)
    private Segment segment;

    public AreaPlaceSegmentId getId() {
        return id;
    }

    public void setId(AreaPlaceSegmentId id) {
        this.id = id;
    }

    public Area getArea() {
        return area;
    }

    public Place getPlace() {
        return place;
    }

    public Segment getSegment() {
        return segment;
    }

    @Embeddable
    public static class AreaPlaceSegmentId implements Serializable {

        private Long areaId;
        private Long placeId;
        private Long segmentId;

        public Long getAreaId() {
            return areaId;
        }

        public void setAreaId(Long areaId) {
            this.areaId = areaId;
        }

        public Long getPlaceId() {
            return placeId;
        }

        public void setPlaceId(Long placeId) {
            this.placeId = placeId;
        }

        public Long getSegmentId() {
            return segmentId;
        }

        public void setSegmentId(Long segmentId) {
            this.segmentId = segmentId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            AreaPlaceSegmentId that = (AreaPlaceSegmentId) o;
            return Objects.equals(areaId, that.areaId)
                && Objects.equals(placeId, that.placeId)
                && Objects.equals(segmentId, that.segmentId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(areaId, placeId, segmentId);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AreaPlaceSegment)) {
            return false;
        }
        AreaPlaceSegment that = (AreaPlaceSegment) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, area, place, segment);
    }

    @Override
    public String toString() {
        return "AreaPlaceSegment{"
            + "id=" + id
            + ", area=" + area
            + ", place=" + place
            + ", segment=" + segment
            + '}';
    }

    public static final class AreaPlaceSegmentBuilder {
        private Area area;
        private Place place;
        private Segment segment;

        private AreaPlaceSegmentBuilder() {
        }

        public static AreaPlaceSegmentBuilder anAreaPlaceSegment() {
            return new AreaPlaceSegmentBuilder();
        }

        public AreaPlaceSegmentBuilder withArea(Area area) {
            this.area = area;
            return this;
        }

        public AreaPlaceSegmentBuilder withPlace(Place place) {
            this.place = place;
            return this;
        }

        public AreaPlaceSegmentBuilder withSegment(Segment segment) {
            this.segment = segment;
            return this;
        }

        public AreaPlaceSegment build() {
            final AreaPlaceSegmentId id = new AreaPlaceSegmentId();
            id.setAreaId(area.getId());
            id.setPlaceId(place.getId());
            id.setSegmentId(segment.getId());

            final AreaPlaceSegment areaPlaceSegment = new AreaPlaceSegment();
            areaPlaceSegment.setId(id);
            areaPlaceSegment.area = area;
            areaPlaceSegment.place = place;
            areaPlaceSegment.segment = segment;

            return areaPlaceSegment;
        }
    }
}
