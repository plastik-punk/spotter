package at.ac.tuwien.sepr.groupphase.backend.datagenerator;

import at.ac.tuwien.sepr.groupphase.backend.entity.Place;
import at.ac.tuwien.sepr.groupphase.backend.entity.Segment;
import at.ac.tuwien.sepr.groupphase.backend.repository.PlaceRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.SegmentRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.List;

@Profile({"generateData", "test"})
@Component
@Order(9)
public class SegmentDataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final SegmentRepository segmentRepository;
    private final PlaceRepository placeRepository;

    public SegmentDataGenerator(SegmentRepository segmentRepository, PlaceRepository placeRepository, AreaDataGenerator areaDataGenerator) {
        this.segmentRepository = segmentRepository;
        this.placeRepository = placeRepository;
    }

    @PostConstruct
    private void generateSegments() {
        LOGGER.trace("generateSegments");

        List<Place> places = placeRepository.findAll();

        if (segmentRepository.count() > 0) {
            LOGGER.debug("Segments have already been generated");
        } else if (places.isEmpty()) {
            LOGGER.warn("Cannot generate segments: No places found");
        } else {
            LOGGER.debug("Generating predefined segment entries");

            List<Segment> segments = Arrays.asList(
                createSegment(1, 3),
                createSegment(2, 3),
                createSegment(1, 5),
                createSegment(2, 5),
                createSegment(1, 7),
                createSegment(2, 7),
                createSegment(5, 3),
                createSegment(6, 3),
                createSegment(5, 5),
                createSegment(6, 5),
                createSegment(5, 7),
                createSegment(6, 7),
                createSegment(11, 3),
                createSegment(12, 3),
                createSegment(11, 5),
                createSegment(12, 5),
                createSegment(11, 7),
                createSegment(12, 7),
                createSegment(12, 0),
                createSegment(14, 0),
                createSegment(15, 0),
                createSegment(15, 1),
                createSegment(15, 2),
                createSegment(15, 4),
                createSegment(15, 6),
                createSegment(2, 0),
                createSegment(3, 0),
                createSegment(4, 0),
                createSegment(5, 0),
                createSegment(6, 0),
                createSegment(7, 0),
                createSegment(8, 0),
                createSegment(9, 0),
                createSegment(10, 0),

                createSegment(1, 2),
                createSegment(2, 2),
                createSegment(1, 4),
                createSegment(2, 4),
                createSegment(1, 6),
                createSegment(2, 6),
                createSegment(5, 2),
                createSegment(6, 2),
                createSegment(5, 4),
                createSegment(6, 4),
                createSegment(5, 6),
                createSegment(6, 6),
                createSegment(11, 2),
                createSegment(12, 2)

            );

            for (Segment segment : segments) {
                LOGGER.debug("Saving segment {}", segment);
                segmentRepository.save(segment);
            }
        }
    }

    private Segment createSegment(int x, int y) {
        Segment segment = Segment.SegmentBuilder.aSegment()
            .withX(x)
            .withY(y)
            .build();
        return segment;
    }
}
