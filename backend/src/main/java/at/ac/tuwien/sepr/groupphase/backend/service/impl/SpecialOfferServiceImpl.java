package at.ac.tuwien.sepr.groupphase.backend.service.impl;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.SpecialOfferCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.SpecialOfferDetailDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.SpecialOfferListDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.SpecialOffer;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.repository.SpecialOfferRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.SpecialOfferService;
import at.ac.tuwien.sepr.groupphase.backend.service.mapper.SpecialOfferMapper;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class SpecialOfferServiceImpl implements SpecialOfferService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final SpecialOfferRepository specialOfferRepository;
    private final SpecialOfferMapper specialOfferMapper;

    @Autowired
    public SpecialOfferServiceImpl(SpecialOfferRepository specialOfferRepository, SpecialOfferMapper specialOfferMapper) {
        this.specialOfferRepository = specialOfferRepository;
        this.specialOfferMapper = specialOfferMapper;
    }

    @Override
    public SpecialOfferCreateDto createSpecialOffer(@Valid SpecialOfferCreateDto specialOfferCreateDto) {
        LOGGER.trace("createSpecialOffer({})", specialOfferCreateDto);
        SpecialOffer specialOfferToCreate = specialOfferMapper.specialOfferCreateDtoToSpecialOffer(specialOfferCreateDto);
        if (specialOfferToCreate.getImage() == null) {
            try {
                byte[] image = readImageAsBytes("../backend/src/main/resources/special-offer-pictures/default.jpeg");
                specialOfferToCreate.setImage(image);
            } catch (IOException e) {
                LOGGER.error("Failed to read default image", e);
            }
        }
        SpecialOffer createdSpecialOffer = specialOfferRepository.save(specialOfferToCreate);
        return specialOfferMapper.specialOfferToSpecialOfferCreateDto(createdSpecialOffer);
    }

    @Override
    public List<SpecialOfferListDto> getAllSpecialOffers() {
        LOGGER.trace("getAllSpecialOffers()");
        List<SpecialOffer> foundSpecialOffers = specialOfferRepository.findAll();
        return specialOfferMapper.specialOffersToSpecialOfferListDtos(foundSpecialOffers);
    }

    @Override
    public void deleteSpecialOffer(Long id) {
        LOGGER.trace("deleteSpecialOffer({})", id);
        specialOfferRepository.deleteById(id);
    }

    @Override
    public SpecialOfferDetailDto getSpecialOffer(Long id) {
        LOGGER.trace("getSpecialOffer({})", id);
        SpecialOffer foundSpecialOffer = specialOfferRepository.findById(id).orElseThrow(() -> new NotFoundException("Special offer not found"));
        return specialOfferMapper.specialOfferToSpecialOfferDetailDto(foundSpecialOffer);
    }

    @Override
    public List<SpecialOfferDetailDto> getAllSpecialOfferDetails() {
        LOGGER.trace("getAllSpecialOfferDetails()");
        List<SpecialOffer> foundSpecialOffers = specialOfferRepository.findAll();
        return specialOfferMapper.specialOffersToSpecialOfferDetailDtos(foundSpecialOffers);
    }

    private byte[] readImageAsBytes(String relativePath) throws IOException {
        Path imagePath = Paths.get(relativePath).toAbsolutePath().normalize();
        return Files.readAllBytes(imagePath);
    }
}
