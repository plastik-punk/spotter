package at.ac.tuwien.sepr.groupphase.backend.service.impl;

import at.ac.tuwien.sepr.groupphase.backend.enums.StatusEnum;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.repository.PlaceRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.PlaceService;
import jakarta.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;

@Service
public class PlaceServiceImpl implements PlaceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final PlaceRepository placeRepository;

    @Autowired
    private Validator validator;

    @Autowired
    public PlaceServiceImpl(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    @Override
    public void block(long id) {
        LOGGER.trace("block {}", id);
        try {
            placeRepository.findById(id).ifPresent(place -> {
                place.setStatus(StatusEnum.BLOCKED);
                placeRepository.save(place);
            });
        } catch (Exception e) {
            LOGGER.error("Error while blocking place with id {}", id, e);
            throw new NotFoundException("Table with Number " + id + " not found.");
        }
    }

    @Override
    public void unblock(long id) throws NotFoundException {
        LOGGER.trace("unblock {}", id);
        try {
            placeRepository.findById(id).ifPresent(place -> {
                place.setStatus(StatusEnum.AVAILABLE);
                placeRepository.save(place);
            });
        } catch (Exception e) {
            LOGGER.error("Error while unblocking place with id {}", id, e);
            throw new NotFoundException("Table with Number " + id + " not found.");
        }
    }

}
