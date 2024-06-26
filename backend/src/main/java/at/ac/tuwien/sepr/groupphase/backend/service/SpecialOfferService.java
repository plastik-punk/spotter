package at.ac.tuwien.sepr.groupphase.backend.service;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.SpecialOfferCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.SpecialOfferDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.SpecialOfferListDto;

import java.util.List;

/**
 * Service for operations on special offers (e.g. creating a new special offer).
 */
public interface SpecialOfferService {

    /**
     * Creates a new special offer.
     *
     * @param specialOfferCreateDto the special offer to create
     * @return the created special offer
     */
    SpecialOfferCreateDto createSpecialOffer(SpecialOfferCreateDto specialOfferCreateDto);

    /**
     * Gets all special offers.
     *
     * @return all special offers
     */
    List<SpecialOfferListDto> getAllSpecialOffers();

    /**
     * Deletes a special offer.
     *
     * @param id the id of the special offer to delete
     */
    void deleteSpecialOffer(Long id);

    /**
     * Gets a special offer.
     *
     * @param id the id of the special offer to get
     * @return the special offer
     */
    SpecialOfferDetailDto getSpecialOffer(Long id);

    /**
     * Gets all special offers with image.
     *
     * @return all special offers as detailDTOs
     */
    List<SpecialOfferDetailDto> getAllSpecialOfferDetails();
}
