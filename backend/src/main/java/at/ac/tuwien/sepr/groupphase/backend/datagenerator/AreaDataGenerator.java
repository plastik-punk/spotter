package at.ac.tuwien.sepr.groupphase.backend.datagenerator;

import at.ac.tuwien.sepr.groupphase.backend.entity.Area;
import at.ac.tuwien.sepr.groupphase.backend.repository.AreaRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;

@Profile({"generateData", "test"})
@Component
@Order(8)
public class AreaDataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final AreaRepository areaRepository;

    public AreaDataGenerator(AreaRepository areaRepository) {
        this.areaRepository = areaRepository;
    }

    @PostConstruct
    private void generateAreas() {
        LOGGER.trace("generateAreas");

        if (areaRepository.count() > 0) {
            LOGGER.debug("Areas have already been generated");
        } else {
            LOGGER.debug("Generating 2 area entries");

            createAreas();
        }
    }

    private void createAreas() {
        Area area1 = Area.AreaBuilder.anArea()
            .withName("Main Area")
            .withWidth(15)
            .withHeight(8)
            .withOpen(true)
            .build();

        LOGGER.debug("Saving area {}", area1);
        areaRepository.save(area1);

        Area area2 = Area.AreaBuilder.anArea()
            .withName("Second Area")
            .withWidth(19)
            .withHeight(4)
            .withOpen(false)
            .build();

        LOGGER.debug("Saving area {}", area2);
        areaRepository.save(area2);
    }
}
