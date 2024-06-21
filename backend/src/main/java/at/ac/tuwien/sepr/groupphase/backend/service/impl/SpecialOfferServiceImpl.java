package at.ac.tuwien.sepr.groupphase.backend.service.impl;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.SpecialOfferCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.SpecialOfferListDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.SpecialOffer;
import at.ac.tuwien.sepr.groupphase.backend.repository.SpecialOfferRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.SpecialOfferService;
import at.ac.tuwien.sepr.groupphase.backend.service.mapper.SpecialOfferMapper;
import jakarta.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.List;

@Service
public class SpecialOfferServiceImpl implements SpecialOfferService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final SpecialOfferRepository specialOfferRepository;
    private final SpecialOfferMapper specialOfferMapper;

    @Autowired
    private Validator validator;

    @Autowired
    public SpecialOfferServiceImpl(SpecialOfferRepository specialOfferRepository, SpecialOfferMapper specialOfferMapper) {
        this.specialOfferRepository = specialOfferRepository;
        this.specialOfferMapper = specialOfferMapper;
    }

    @Override
    public SpecialOfferCreateDto createSpecialOffer(SpecialOfferCreateDto specialOfferCreateDto) {
        LOGGER.trace("createSpecialOffer({})", specialOfferCreateDto);
        SpecialOffer specialOfferToCreate = specialOfferMapper.specialOfferCreateDtoToSpecialOffer(specialOfferCreateDto);
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
}
