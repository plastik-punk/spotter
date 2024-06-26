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

@Mapper
public interface SpecialOfferMapper {

    @Named("specialOfferToSpecialOfferDetailDto")
    SpecialOfferDetailDto specialOfferToSpecialOfferDetailDto(SpecialOffer specialOffer);

    @IterableMapping(qualifiedByName = "specialOfferToSpecialOfferDetailDto")
    List<SpecialOfferDetailDto> specialOffersToSpecialOfferDetailDtos(List<SpecialOffer> specialOffers);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "image", source = "image", qualifiedByName = "multipartFileToByteArray")
    SpecialOffer specialOfferCreateDtoToSpecialOffer(SpecialOfferCreateDto specialOfferCreateDto);

    @Mapping(target = "image", ignore = true)
    SpecialOfferCreateDto specialOfferToSpecialOfferCreateDto(SpecialOffer specialOffer);

    @Named("specialOfferToSpecialOfferListDto")
    SpecialOfferListDto specialOfferToSpecialOfferListDto(SpecialOffer specialOffer);

    @IterableMapping(qualifiedByName = "specialOfferToSpecialOfferListDto")
    List<SpecialOfferListDto> specialOffersToSpecialOfferListDtos(List<SpecialOffer> specialOffers);

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

