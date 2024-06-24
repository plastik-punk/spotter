package at.ac.tuwien.sepr.groupphase.backend.service.impl;

import at.ac.tuwien.sepr.groupphase.backend.entity.Place;
import at.ac.tuwien.sepr.groupphase.backend.enums.StatusEnum;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.repository.PlaceRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.PlaceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Optional;

@Service
public class PlaceServiceImpl implements PlaceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final PlaceRepository placeRepository;

    @Autowired
    public PlaceServiceImpl(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    @Override
    public void block(List<Long> ids) throws NotFoundException {
        LOGGER.trace("block {}", ids);
        for (Long id : ids) {
            Optional<Place> optionalPlace = placeRepository.findById(id);
            if (optionalPlace.isEmpty()) {
                LOGGER.error("Error while blocking / unblocking place with id {}, Table not found", id);
                throw new NotFoundException("Table not found");
            }

            Place place = optionalPlace.get();

            if (place.getStatus().equals(StatusEnum.BLOCKED)) {
                place.setStatus(StatusEnum.AVAILABLE);
            } else {
                place.setStatus(StatusEnum.BLOCKED);
            }

            placeRepository.save(place);
        }
    }
}
