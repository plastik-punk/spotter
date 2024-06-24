package at.ac.tuwien.sepr.groupphase.backend.datagenerator;

import at.ac.tuwien.sepr.groupphase.backend.entity.SpecialOffer;
import at.ac.tuwien.sepr.groupphase.backend.repository.SpecialOfferRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Profile({"generateData", "test"})
@Component
@Order(13)
public class SpecialOfferDataGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final int SPECIAL_OFFERS_TO_GENERATE = 5;

    private final SpecialOfferRepository specialOfferRepository;

    private static final String[] SPECIAL_OFFERS_NAME = {
        "Margarita",
        "Mojito",
        "Caipirinha",
        "Pina Colada",
        "Tequila Sunrise"
    };
    private static final Float[] SPECIAL_OFFERS_PRICE_PER_PAX = {
        10.0f,
        20.0f,
        30.0f,
        40.0f,
        50.0f
    };

    public SpecialOfferDataGenerator(SpecialOfferRepository specialOfferRepository) {
        this.specialOfferRepository = specialOfferRepository;
    }

    @PostConstruct
    private void generateSpecialOffers() {
        LOGGER.trace("generateSpecialOffers");
        if (specialOfferRepository.count() > 0) {
            LOGGER.debug("special offers already generated");
        } else {
            LOGGER.debug("generating {} special offers", SPECIAL_OFFERS_TO_GENERATE);
            for (int i = 0; i < SPECIAL_OFFERS_TO_GENERATE; i++) {
                SpecialOffer specialOffer = generateSpecialOffer(i);
                LOGGER.debug("Generated special offer: {}", specialOffer);
                specialOfferRepository.save(specialOffer);
            }
        }
    }

    private SpecialOffer generateSpecialOffer(int i) {
        byte[] image = null;
        try {
            image = readImageAsBytes("../backend/src/main/resources/special-offer-pictures/" + SPECIAL_OFFERS_NAME[i].toLowerCase().replace(" ", "-") + ".jpg");
        } catch (IOException e) {
            LOGGER.error("Failed to read image for {}", SPECIAL_OFFERS_NAME[i], e);
            return SpecialOffer.SpecialOfferBuilder.aSpecialOffer()
                .withName(SPECIAL_OFFERS_NAME[i])
                .withPricePerPax(SPECIAL_OFFERS_PRICE_PER_PAX[i])
                .build();
        }

        return SpecialOffer.SpecialOfferBuilder.aSpecialOffer()
            .withName(SPECIAL_OFFERS_NAME[i])
            .withPricePerPax(SPECIAL_OFFERS_PRICE_PER_PAX[i])
            .withImage(image)
            .build();
    }

    private byte[] readImageAsBytes(String relativePath) throws IOException {
        Path imagePath = Paths.get(relativePath).toAbsolutePath().normalize();
        return Files.readAllBytes(imagePath);
    }
}
