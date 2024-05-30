package at.ac.tuwien.sepr.groupphase.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.Objects;

@Entity
public class Segment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int x1;

    @Column(nullable = false)
    private int y1;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getX() {
        return x1;
    }

    public void setX(int x) {
        this.x1 = x;
    }

    public int getY() {
        return y1;
    }

    public void setY(int y) {
        this.y1 = y;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Segment segment)) {
            return false;
        }
        return x1 == segment.x1 && y1 == segment.y1;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x1, y1);
    }

    @Override
    public String toString() {
        return "Segment{" + "x=" + x1 + ", y=" + y1 + '}';
    }

    public static final class SegmentBuilder {
        private int x1;
        private int y1;


        private SegmentBuilder() {
        }

        public static SegmentBuilder aSegment() {
            return new SegmentBuilder();
        }

        public SegmentBuilder withX(int x) {
            this.x1 = x;
            return this;
        }

        public SegmentBuilder withY(int y) {
            this.y1 = y;
            return this;
        }


        public Segment build() {
            Segment segment = new Segment();
            segment.setX(x1);
            segment.setY(y1);

            return segment;
        }
    }
}