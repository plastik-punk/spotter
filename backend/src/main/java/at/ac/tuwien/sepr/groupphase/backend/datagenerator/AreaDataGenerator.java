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
import java.time.LocalTime;

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
            LOGGER.debug("Generating 1 area entry");

            createArea();
        }
    }

    private void createArea() {
        Area area = Area.AreaBuilder.anArea()
            .withName("Single Area")
            .withWidth(16)
            .withHeight(9)
            .withOpen(true)
            .build();

        LOGGER.debug("Saving area {}", area);
        areaRepository.save(area);
    }
}
