package at.ac.tuwien.sepr.groupphase.backend.endpoint;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.SpecialOfferCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.SpecialOfferDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.SpecialOfferListDto;
import at.ac.tuwien.sepr.groupphase.backend.service.SpecialOfferService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.security.PermitAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.lang.invoke.MethodHandles;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/special-offers")
public class SpecialOfferEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final SpecialOfferService specialOfferService;

    @Autowired
    public SpecialOfferEndpoint(SpecialOfferService specialOfferService) {
        this.specialOfferService = specialOfferService;
    }

    // TODO: dto as param and validation
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @Secured("ROLE_ADMIN")
    public SpecialOfferCreateDto createSpecialOffer(@RequestParam("name") String name,
                                                    @RequestParam("pricePerPax") Float pricePerPax,
                                                    @RequestParam(value = "image", required = false) MultipartFile image) {
        SpecialOfferCreateDto specialOfferCreateDto = new SpecialOfferCreateDto();
        specialOfferCreateDto.setName(name);
        specialOfferCreateDto.setPricePerPax(pricePerPax);
        specialOfferCreateDto.setImage(image);

        LOGGER.info("POST /api/v1/special-offers " + "/{}", specialOfferCreateDto);
        return specialOfferService.createSpecialOffer(specialOfferCreateDto);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    @PermitAll
    public List<SpecialOfferListDto> getAllSpecialOffers() {
        LOGGER.info("GET /api/v1/special-offers");
        return specialOfferService.getAllSpecialOffers();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping({"/detail"})
    @Secured("ROLE_ADMIN")
    public SpecialOfferDetailDto getSpecialOffer(@RequestParam("id") Long id) {
        LOGGER.info("GET /api/v1/special-offers id: {}", id);
        return specialOfferService.getSpecialOffer(id);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Secured("ROLE_ADMIN")
    @DeleteMapping
    @Operation(summary = "Delete a special offer")
    public ResponseEntity<Void> deleteSpecialOffer(@RequestBody Long id) {
        LOGGER.info("DELETE /api/v1/special-offers?id={}", id);
        specialOfferService.deleteSpecialOffer(id);
        return ResponseEntity.noContent().build();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping({"/details"})
    @PermitAll
    public List<SpecialOfferDetailDto> getAllSpecialOfferDetails() {
        LOGGER.info("GET /api/v1/special-offers/details");
        return specialOfferService.getAllSpecialOfferDetails();
    }
}