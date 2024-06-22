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
import java.util.Arrays;
import java.util.List;

@Profile({"generateData", "test"})
@Component
@Order(10)
public class AreaPlaceSegmentDataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final DataGeneratorBean dataGeneratorBean;
    private final AreaPlaceSegmentRepository areaPlaceSegmentRepository;
    private final AreaRepository areaRepository;
    private final PlaceRepository placeRepository;
    private final SegmentRepository segmentRepository;


    public AreaPlaceSegmentDataGenerator(DataGeneratorBean dataGeneratorBean,
                                         AreaPlaceSegmentRepository areaPlaceSegmentRepository,
                                         AreaRepository areaRepository,
                                         PlaceRepository placeRepository,
                                         SegmentRepository segmentRepository,
                                         AreaDataGenerator areaDataGenerator,
                                         PlaceDataGenerator placeDataGenerator,
                                         SegmentDataGenerator segmentDataGenerator) {
        this.dataGeneratorBean = dataGeneratorBean;
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

        if (areas.isEmpty()) {
            LOGGER.warn("Cannot generate AreaPlaceSegments: No areas found");
            return;
        }

        if (places.isEmpty()) {
            LOGGER.warn("Cannot generate AreaPlaceSegments: No places found");
            return;
        }

        if (segments.isEmpty()) {
            LOGGER.warn("Cannot generate AreaPlaceSegments: No segments found");
            return;
        }

        Area area1 = areas.get(0);
        Area area2 = areas.size() > 1 ? areas.get(1) : null;

        if (area2 == null) {
            LOGGER.warn("Area 2 not found, skipping generation for Area 2");
            return;
        }

        List<Mapping> mappings = Arrays.asList(
            // Area 1 mappings
            new Mapping(1L, 1L, area1.getId()),
            new Mapping(1L, 2L, area1.getId()),
            new Mapping(2L, 3L, area1.getId()),
            new Mapping(2L, 4L, area1.getId()),
            new Mapping(3L, 5L, area1.getId()),
            new Mapping(3L, 6L, area1.getId()),
            new Mapping(4L, 7L, area1.getId()),
            new Mapping(4L, 8L, area1.getId()),
            new Mapping(5L, 9L, area1.getId()),
            new Mapping(5L, 10L, area1.getId()),
            new Mapping(6L, 11L, area1.getId()),
            new Mapping(6L, 12L, area1.getId()),
            new Mapping(7L, 13L, area1.getId()),
            new Mapping(7L, 14L, area1.getId()),
            new Mapping(8L, 15L, area1.getId()),
            new Mapping(8L, 16L, area1.getId()),
            new Mapping(9L, 17L, area1.getId()),
            new Mapping(9L, 18L, area1.getId()),
            new Mapping(10L, 19L, area1.getId()),
            new Mapping(11L, 20L, area1.getId()),
            new Mapping(11L, 21L, area1.getId()),
            new Mapping(11L, 22L, area1.getId()),
            new Mapping(11L, 23L, area1.getId()),
            new Mapping(13L, 24L, area1.getId()),
            new Mapping(14L, 25L, area1.getId()),
            new Mapping(16L, 26L, area1.getId()),
            new Mapping(16L, 27L, area1.getId()),
            new Mapping(16L, 28L, area1.getId()),
            new Mapping(16L, 29L, area1.getId()),
            new Mapping(16L, 30L, area1.getId()),
            new Mapping(16L, 31L, area1.getId()),
            new Mapping(16L, 32L, area1.getId()),
            new Mapping(16L, 33L, area1.getId()),
            new Mapping(16L, 34L, area1.getId()),

            // Area 2 mappings
            new Mapping(17L, 35L, area2.getId()),
            new Mapping(17L, 36L, area2.getId()),
            new Mapping(18L, 37L, area2.getId()),
            new Mapping(18L, 38L, area2.getId()),
            new Mapping(19L, 39L, area2.getId()),
            new Mapping(19L, 40L, area2.getId()),
            new Mapping(20L, 41L, area2.getId()),
            new Mapping(20L, 42L, area2.getId()),
            new Mapping(21L, 43L, area2.getId()),
            new Mapping(21L, 44L, area2.getId()),
            new Mapping(22L, 45L, area2.getId()),
            new Mapping(22L, 46L, area2.getId()),
            new Mapping(23L, 47L, area2.getId()),
            new Mapping(23L, 48L, area2.getId())
        );

        for (Mapping mapping : mappings) {
            Place place = places.stream().filter(p -> p.getId().equals(mapping.placeId)).findFirst().orElse(null);
            Segment segment = segments.stream().filter(s -> s.getId().equals(mapping.segmentId)).findFirst().orElse(null);
            Area area = areas.stream().filter(a -> a.getId().equals(mapping.areaId)).findFirst().orElse(null);

            if (place != null && segment != null && area != null) {
                AreaPlaceSegment areaPlaceSegment = AreaPlaceSegment.AreaPlaceSegmentBuilder.anAreaPlaceSegment()
                    .withArea(area)
                    .withPlace(place)
                    .withSegment(segment)
                    .build();
                areaPlaceSegmentRepository.save(areaPlaceSegment);
                LOGGER.debug("Saving AreaPlaceSegment {}", areaPlaceSegment);
            } else {
                LOGGER.warn("Skipping mapping for placeId {}, segmentId {}, areaId {}: Place, Segment, or Area not found", mapping.placeId, mapping.segmentId, mapping.areaId);
            }
        }
    }

    private static class Mapping {
        Long placeId;
        Long segmentId;
        Long areaId;

        Mapping(Long placeId, Long segmentId, Long areaId) {
            this.placeId = placeId;
            this.segmentId = segmentId;
            this.areaId = areaId;
        }
    }
}
