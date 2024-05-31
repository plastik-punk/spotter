package at.ac.tuwien.sepr.groupphase.backend.datagenerator;

import at.ac.tuwien.sepr.groupphase.backend.entity.Area;
import at.ac.tuwien.sepr.groupphase.backend.entity.AreaPlaceSegment;
import at.ac.tuwien.sepr.groupphase.backend.entity.Place;
import at.ac.tuwien.sepr.groupphase.backend.entity.Segment;
import at.ac.tuwien.sepr.groupphase.backend.repository.AreaPlaceSegmentRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.AreaRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.PlaceRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.SegmentRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Arrays;

@Profile({"generateData", "test"})
@Component
@Order(10)
public class AreaPlaceSegmentDataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final AreaPlaceSegmentRepository areaPlaceSegmentRepository;
    private final AreaRepository areaRepository;
    private final PlaceRepository placeRepository;
    private final SegmentRepository segmentRepository;

    public AreaPlaceSegmentDataGenerator(AreaPlaceSegmentRepository areaPlaceSegmentRepository,
                                         AreaRepository areaRepository,
                                         PlaceRepository placeRepository,
                                         SegmentRepository segmentRepository) {
        this.areaPlaceSegmentRepository = areaPlaceSegmentRepository;
        this.areaRepository = areaRepository;
        this.placeRepository = placeRepository;
        this.segmentRepository = segmentRepository;
    }

    @PostConstruct
    private void generateAreaPlaceSegments() {
        LOGGER.trace("generateAreaPlaceSegments");

        if (areaPlaceSegmentRepository.count() > 0) {
            LOGGER.debug("AreaPlaceSegments already generated");
            return;
        }

        List<Area> areas = areaRepository.findAll();
        List<Place> places = placeRepository.findAll();
        List<Segment> segments = segmentRepository.findAll();

        if (areas.isEmpty() || places.isEmpty() || segments.isEmpty()) {
            LOGGER.warn("Cannot generate AreaPlaceSegments: No areas, places, or segments found");
            return;
        }

        Area area = areas.get(0); // Assuming we use the first area

        List<Mapping> mappings = Arrays.asList(
            new Mapping(1L, 1L),
            new Mapping(1L, 2L),
            new Mapping(2L, 3L),
            new Mapping(2L, 4L),
            new Mapping(3L, 5L),
            new Mapping(3L, 6L),
            new Mapping(4L, 7L),
            new Mapping(4L, 8L),
            new Mapping(5L, 9L),
            new Mapping(5L, 10L),
            new Mapping(6L, 11L),
            new Mapping(6L, 12L),
            new Mapping(7L, 13L),
            new Mapping(7L, 14L),
            new Mapping(8L, 15L),
            new Mapping(8L, 16L),
            new Mapping(9L, 17L),
            new Mapping(9L, 18L),
            new Mapping(10L, 19L),
            new Mapping(11L, 20L),
            new Mapping(11L, 21L),
            new Mapping(11L, 22L),
            new Mapping(11L, 23L),
            new Mapping(13L, 24L),
            new Mapping(14L, 25L),
            new Mapping(16L, 26L),
            new Mapping(16L, 27L),
            new Mapping(16L, 28L),
            new Mapping(16L, 29L),
            new Mapping(16L, 30L),
            new Mapping(16L, 31L),
            new Mapping(16L, 32L),
            new Mapping(16L, 33L),
            new Mapping(16L, 34L)
        );

        for (Mapping mapping : mappings) {
            Place place = places.stream().filter(p -> p.getId().equals(mapping.placeId)).findFirst().orElse(null);
            Segment segment = segments.stream().filter(s -> s.getId().equals(mapping.segmentId)).findFirst().orElse(null);

            if (place != null && segment != null) {
                AreaPlaceSegment areaPlaceSegment = AreaPlaceSegment.AreaPlaceSegmentBuilder.anAreaPlaceSegment()
                    .withArea(area)
                    .withPlace(place)
                    .withSegment(segment)
                    .build();
                areaPlaceSegmentRepository.save(areaPlaceSegment);
                LOGGER.debug("Saving AreaPlaceSegment {}", areaPlaceSegment);
            } else {
                LOGGER.warn("Skipping mapping for placeId {} and segmentId {}: Place or Segment not found", mapping.placeId, mapping.segmentId);
            }
        }
    }

    private static class Mapping {
        Long placeId;
        Long segmentId;

        Mapping(Long placeId, Long segmentId) {
            this.placeId = placeId;
            this.segmentId = segmentId;
        }
    }
}
