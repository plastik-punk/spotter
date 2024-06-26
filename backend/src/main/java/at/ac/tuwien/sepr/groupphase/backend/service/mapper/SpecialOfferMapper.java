package at.ac.tuwien.sepr.groupphase.backend.service.mapper;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.SpecialOfferCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.SpecialOfferDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.SpecialOfferListDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.SpecialOffer;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


/**
 * Mapper interface for converting between special offer-related DTOs and entities.
 * This interface uses MapStruct for generating the implementation.
 */
@Mapper
public interface SpecialOfferMapper {


    /**
     * Converts a {@link SpecialOffer} to a {@link SpecialOfferDetailDto}.
     *
     * @param specialOffer the entity to convert
     * @return the converted {@link SpecialOfferDetailDto}
     */
    @Named("specialOfferToSpecialOfferDetailDto")
    SpecialOfferDetailDto specialOfferToSpecialOfferDetailDto(SpecialOffer specialOffer);


    /**
     * Converts a list of {@link SpecialOffer} entities to a list of {@link SpecialOfferDetailDto}.
     *
     * @param specialOffers the list of entities to convert
     * @return the list of converted {@link SpecialOfferDetailDto}
     */
    @IterableMapping(qualifiedByName = "specialOfferToSpecialOfferDetailDto")
    List<SpecialOfferDetailDto> specialOffersToSpecialOfferDetailDtos(List<SpecialOffer> specialOffers);


    /**
     * Converts a {@link SpecialOfferCreateDto} to a {@link SpecialOffer}.
     * Ignores the {@code id} field and maps the {@code image} field using the {@code multipartFileToByteArray} method.
     *
     * @param specialOfferCreateDto the DTO to convert
     * @return the converted {@link SpecialOffer}
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "image", source = "image", qualifiedByName = "multipartFileToByteArray")
    SpecialOffer specialOfferCreateDtoToSpecialOffer(SpecialOfferCreateDto specialOfferCreateDto);


    /**
     * Converts a {@link SpecialOffer} to a {@link SpecialOfferCreateDto}.
     * Ignores the {@code image} field during mapping.
     *
     * @param specialOffer the entity to convert
     * @return the converted {@link SpecialOfferCreateDto}
     */
    @Mapping(target = "image", ignore = true)
    SpecialOfferCreateDto specialOfferToSpecialOfferCreateDto(SpecialOffer specialOffer);


    /**
     * Converts a {@link SpecialOffer} to a {@link SpecialOfferListDto}.
     *
     * @param specialOffer the entity to convert
     * @return the converted {@link SpecialOfferListDto}
     */
    @Named("specialOfferToSpecialOfferListDto")
    SpecialOfferListDto specialOfferToSpecialOfferListDto(SpecialOffer specialOffer);


    /**
     * Converts a list of {@link SpecialOffer} entities to a list of {@link SpecialOfferListDto}.
     *
     * @param specialOffers the list of entities to convert
     * @return the list of converted {@link SpecialOfferListDto}
     */
    @IterableMapping(qualifiedByName = "specialOfferToSpecialOfferListDto")
    List<SpecialOfferListDto> specialOffersToSpecialOfferListDtos(List<SpecialOffer> specialOffers);


    /**
     * Converts a {@link MultipartFile} to a byte array.
     *
     * @param file the file to convert
     * @return the converted byte array
     */
    @Named("multipartFileToByteArray")
    default byte[] multipartFileToByteArray(MultipartFile file) {
        try {
            return file != null ? file.getBytes() : null;
        } catch (IOException e) {
            // Handle the exception as needed, e.g., throw a custom runtime exception
            throw new RuntimeException("Error converting MultipartFile to byte[]", e);
        }
    }


}

