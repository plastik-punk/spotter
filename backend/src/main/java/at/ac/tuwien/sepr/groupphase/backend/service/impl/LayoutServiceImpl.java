package at.ac.tuwien.sepr.groupphase.backend.service.impl;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.*;
import at.ac.tuwien.sepr.groupphase.backend.entity.*;
import at.ac.tuwien.sepr.groupphase.backend.enums.StatusEnum;
import at.ac.tuwien.sepr.groupphase.backend.exception.ConflictException;
import at.ac.tuwien.sepr.groupphase.backend.repository.ApplicationUserRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.AreaPlaceSegmentRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.AreaRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.ClosedDayRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.OpeningHoursRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.PlaceRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.ReservationPlaceRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.ReservationRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.SegmentRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.EmailService;
import at.ac.tuwien.sepr.groupphase.backend.service.HashService;
import at.ac.tuwien.sepr.groupphase.backend.service.LayoutService;
import at.ac.tuwien.sepr.groupphase.backend.service.mapper.LayoutMapper;
import at.ac.tuwien.sepr.groupphase.backend.service.mapper.ReservationMapper;
import jakarta.transaction.Transactional;
import jakarta.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LayoutServiceImpl implements LayoutService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final ReservationRepository reservationRepository;
    private final ApplicationUserRepository applicationUserRepository;
    private final PlaceRepository placeRepository;
    private final ReservationMapper reservationMapper;
    private final LayoutMapper layoutMapper;
    private final OpeningHoursRepository openingHoursRepository;
    private final ClosedDayRepository closedDayRepository;
    private final ReservationPlaceRepository reservationPlaceRepository;
    private final EmailService emailService;
    private final HashService hashService;
    private final ApplicationUserServiceImpl applicationUserService;
    private final AreaRepository areaRepository;
    private final SegmentRepository segmentRepository;
    private final AreaPlaceSegmentRepository areaPlaceSegmentRepository;

    @Autowired
    private Validator validator;

    @Autowired
    public LayoutServiceImpl(ReservationRepository reservationRepository, ApplicationUserRepository applicationUserRepository, PlaceRepository placeRepository, ReservationMapper reservationMapper, LayoutMapper layoutMapper,
                             OpeningHoursRepository openingHoursRepository, ClosedDayRepository closedDayRepository, ReservationPlaceRepository reservationPlaceRepository, EmailService emailService, HashService hashService,
                             ApplicationUserServiceImpl applicationUserService, AreaRepository areaRepository, SegmentRepository segmentRepository, AreaPlaceSegmentRepository areaPlaceSegmentRepository) {
        this.reservationRepository = reservationRepository;
        this.applicationUserRepository = applicationUserRepository;
        this.placeRepository = placeRepository;
        this.reservationMapper = reservationMapper;
        this.layoutMapper = layoutMapper;
        this.openingHoursRepository = openingHoursRepository;
        this.closedDayRepository = closedDayRepository;
        this.reservationPlaceRepository = reservationPlaceRepository;
        this.emailService = emailService;
        this.hashService = hashService;
        this.applicationUserService = applicationUserService;
        this.areaRepository = areaRepository;
        this.segmentRepository = segmentRepository;
        this.areaPlaceSegmentRepository = areaPlaceSegmentRepository;
    }

    @Override
    public AreaListDto getAllAreas() {
        LOGGER.trace("getAllAreas()");

        List<Area> areas = areaRepository.findAll();
        LOGGER.debug("Found {} areas", areas.size());

        List<AreaListDto.AreaDto> areaDtos = areas.stream()
            .map(area -> {
                AreaListDto.AreaDto dto = new AreaListDto.AreaDto();
                dto.setId(area.getId());
                dto.setName(area.getName());
                return dto;
            })
            .collect(Collectors.toList());

        AreaListDto areaListDto = new AreaListDto();
        areaListDto.setAreas(areaDtos);
        return areaListDto;
    }

    @Override
    @Transactional
    public AreaLayoutDto getAreaLayout(ReservationLayoutCheckAvailabilityDto dto) {
        var area = areaRepository.getReferenceById(dto.getAreaId());

        // Set end time to two hours after start time if not provided
        if (dto.getEndTime() == null) {
            dto.setEndTime(dto.getStartTime().plusHours(2));
        }

        List<AreaPlaceSegment> areaPlaceSegments = areaPlaceSegmentRepository.findByAreaId(area.getId());
        List<Long> placeIds = areaPlaceSegments.stream().map(aps -> aps.getPlace().getId()).collect(Collectors.toList());
        List<Place> places = placeRepository.findAllById(placeIds);
        List<Long> reservationIds = reservationRepository.findReservationsAtSpecifiedTime(
            dto.getDate(), dto.getStartTime(), dto.getEndTime());
        List<Long> reservedPlaceIds = reservationPlaceRepository.findPlaceIdsByReservationIds(reservationIds);

        final boolean isOpen;
        DayOfWeek dayOfWeek = dto.getDate().getDayOfWeek();
        List<OpeningHours> openingHoursList = openingHoursRepository.findByDayOfWeek(dayOfWeek);

        if (openingHoursList.isEmpty()) {
            // If the area is closed on this day, set all place statuses to false
            isOpen = false;
        } else {
            boolean open = false;
            for (OpeningHours openingHours : openingHoursList) {
                if (!dto.getStartTime().isAfter(openingHours.getClosingTime())
                    && !dto.getStartTime().isBefore(openingHours.getOpeningTime())
                    && !dto.getEndTime().isBefore(openingHours.getOpeningTime())) {
                    if (!dto.getStartTime().plusHours(1).isAfter(openingHours.getClosingTime())) {
                        open = true;
                        if (!dto.getEndTime().isBefore(openingHours.getClosingTime())) {
                            dto.setEndTime(openingHours.getClosingTime());
                        }
                        break;
                    } else {
                        open = false;
                    }
                }
            }
            isOpen = open;
        }

        List<AreaLayoutDto.PlaceVisualDto> placeVisuals = places.stream().map(place -> {
            AreaLayoutDto.PlaceVisualDto placeVisual = new AreaLayoutDto.PlaceVisualDto();
            placeVisual.setPlaceNumber(place.getNumber());

            if (area.isOpen() && isOpen) {
                placeVisual.setStatus(place.getStatus() == StatusEnum.AVAILABLE);
            } else {
                placeVisual.setStatus(false);
            }

            placeVisual.setReservation(reservedPlaceIds.contains(place.getId()));
            placeVisual.setNumberOfSeats(place.getPax());

            List<Segment> segments = areaPlaceSegments.stream()
                .filter(aps -> aps.getPlace().getId().equals(place.getId()))
                .map(AreaPlaceSegment::getSegment)
                .toList();
            List<AreaLayoutDto.PlaceVisualDto.CoordinateDto> coordinates = segments.stream().map(segment -> {
                AreaLayoutDto.PlaceVisualDto.CoordinateDto coordinate = new AreaLayoutDto.PlaceVisualDto.CoordinateDto();
                coordinate.setX(segment.getX());
                coordinate.setY(segment.getY());
                return coordinate;
            }).collect(Collectors.toList());

            placeVisual.setCoordinates(coordinates);
            return placeVisual;
        }).collect(Collectors.toList());

        AreaLayoutDto areaLayoutDto = new AreaLayoutDto();
        areaLayoutDto.setWidth(area.getWidth());
        areaLayoutDto.setHeight(area.getHeight());
        areaLayoutDto.setPlaceVisuals(placeVisuals);
        LOGGER.debug("Built areaLayout: {}", areaLayoutDto);

        return areaLayoutDto;
    }

    @Override
    @Transactional
    public void createLayout(LayoutCreateDto layoutCreateDto) {
        // Separate the main area and other areas
        LayoutCreateDto.AreaCreateDto mainAreaDto = null;
        List<LayoutCreateDto.AreaCreateDto> otherAreas = new ArrayList<>();

        for (LayoutCreateDto.AreaCreateDto areaDto : layoutCreateDto.getAreas()) {
            if (areaDto.isMainArea()) {
                mainAreaDto = areaDto;
            } else {
                otherAreas.add(areaDto);
            }
        }

        // Ensure the main area is saved first if it exists
        if (mainAreaDto != null) {
            saveArea(mainAreaDto);
        }

        // Save other areas
        for (LayoutCreateDto.AreaCreateDto areaDto : otherAreas) {
            saveArea(areaDto);
        }
    }

    private void saveArea(LayoutCreateDto.AreaCreateDto areaDto) {
        // Create and save Area entity using mapper
        Area area = layoutMapper.areaCreateDtoToArea(areaDto);
        area.setIsOpen(areaDto.getIsOpen());
        areaRepository.save(area);

        // Iterate over each Place in the Area and save it
        for (LayoutCreateDto.AreaCreateDto.PlaceVisualDto placeDto : areaDto.getPlaces()) {
            savePlace(area, placeDto);
        }
    }

    private void savePlace(Area area, LayoutCreateDto.AreaCreateDto.PlaceVisualDto placeDto) {
        // Create and save Place entity using mapper
        Place place = layoutMapper.placeVisualDtoToPlace(placeDto);
        placeRepository.save(place);

        // Iterate over each Coordinate in the Place and save the Segment
        for (LayoutCreateDto.AreaCreateDto.PlaceVisualDto.CoordinateDto coordinateDto : placeDto.getCoordinates()) {
            saveSegment(area, place, coordinateDto);
        }
    }

    private void saveSegment(Area area, Place place, LayoutCreateDto.AreaCreateDto.PlaceVisualDto.CoordinateDto coordinateDto) {
        // Create and save Segment entity using mapper
        Segment segment = layoutMapper.coordinateDtoToSegment(coordinateDto);
        segment.setX(coordinateDto.getX());
        segment.setY(coordinateDto.getY());
        segmentRepository.save(segment);

        // Create and save AreaPlaceSegment entity
        AreaPlaceSegment areaPlaceSegment = new AreaPlaceSegment();
        areaPlaceSegment.setArea(area);
        areaPlaceSegment.setPlace(place);
        areaPlaceSegment.setSegment(segment);
        AreaPlaceSegment.AreaPlaceSegmentId id = new AreaPlaceSegment.AreaPlaceSegmentId();
        id.setAreaId(area.getId());
        id.setPlaceId(place.getId());
        id.setSegmentId(segment.getId());
        areaPlaceSegment.setId(id);
        areaPlaceSegmentRepository.save(areaPlaceSegment);
    }

    public void deleteArea(Long id) throws ConflictException {
        //check if there are any reservations in the future for any places in this area, if yes throw an error
        //if no, delete all area_place_segments, then all places, then all segments, then the area
        List<AreaPlaceSegment> areaPlaceSegments = areaPlaceSegmentRepository.findByAreaId(id);
        List<Long> placeIds = areaPlaceSegments.stream().map(aps -> aps.getPlace().getId()).collect(Collectors.toList());

        List<String> conflictExceptions = new ArrayList<>();

        for (Long placeId : placeIds) {
            List<ReservationPlace> reservationPlaces = reservationPlaceRepository.findByPlaceId(placeId);

            if (!reservationPlaces.isEmpty()) {
                conflictExceptions.add("There are reservations for the table with id " + placeId);
            }
        }

        if (!conflictExceptions.isEmpty()) {
            throw new ConflictException("Cannot delete area", conflictExceptions);
        }

        areaPlaceSegmentRepository.deleteAll(areaPlaceSegments);
        List<Place> places = placeRepository.findAllById(placeIds);
        placeRepository.deleteAll(places);

        areaRepository.deleteById(id);

    }

    public AreaDetailDto getAreaById(Long id) {
        Area area = areaRepository.findById(id).orElseThrow();
        List<AreaPlaceSegment> areaPlaceSegments = areaPlaceSegmentRepository.findByAreaId(id);
        List<Long> placeIds = areaPlaceSegments.stream().map(aps -> aps.getPlace().getId()).collect(Collectors.toList());
        List<Place> places = placeRepository.findAllById(placeIds);
        List<LayoutCreateDto.AreaCreateDto.PlaceVisualDto> placeVisuals = getPlaceVisualDtos(places, areaPlaceSegments);
        return getAreaDetailDto(placeVisuals, area);
    }

    private static List<LayoutCreateDto.AreaCreateDto.PlaceVisualDto> getPlaceVisualDtos(List<Place> places, List<AreaPlaceSegment> areaPlaceSegments) {
        return places.stream().map(place -> {
            LayoutCreateDto.AreaCreateDto.PlaceVisualDto placeVisual = new LayoutCreateDto.AreaCreateDto.PlaceVisualDto();
            placeVisual.setPlaceNumber(place.getNumber());
            placeVisual.setNumberOfSeats(place.getPax());
            List<Segment> segments = areaPlaceSegments.stream()
                .filter(aps -> aps.getPlace().getId().equals(place.getId()))
                .map(AreaPlaceSegment::getSegment)
                .toList();
            List<LayoutCreateDto.AreaCreateDto.PlaceVisualDto.CoordinateDto> coordinates = segments.stream().map(segment -> {
                LayoutCreateDto.AreaCreateDto.PlaceVisualDto.CoordinateDto coordinate = new LayoutCreateDto.AreaCreateDto.PlaceVisualDto.CoordinateDto();
                coordinate.setX(segment.getX());
                coordinate.setY(segment.getY());
                return coordinate;
            }).collect(Collectors.toList());
            placeVisual.setCoordinates(coordinates);
            return placeVisual;
        }).collect(Collectors.toList());
    }

    private static AreaDetailDto getAreaDetailDto(List<LayoutCreateDto.AreaCreateDto.PlaceVisualDto> placeVisuals, Area area) {
        AreaDetailDto areaDto = new AreaDetailDto();
        areaDto.setId(area.getId());
        areaDto.setPlaces(placeVisuals);
        areaDto.setOpen(area.isOpen());
        areaDto.setName(area.getName());
        //TODO Main Area
        areaDto.setMainArea(false);
        areaDto.setHeight(area.getHeight());
        areaDto.setWidth(area.getWidth());
        LocalTime opening = area.getOpeningTime();
        LocalTime closing = area.getClosingTime();
        if (opening != null) {
            areaDto.setOpeningTime(opening.toString());
        }
        if (closing != null) {
            areaDto.setClosingTime(closing.toString());
        }
        return areaDto;
    }

    @Override
    public AreaDetailListDto getAllAreasDetailed() {
        LOGGER.trace("getAllAreasDetailed()");

        List<Area> areas = areaRepository.findAll();
        LOGGER.debug("Found {} areas", areas.size());

        List<AreaDetailDto> areaDtos = new ArrayList<>();

        for (Area area : areas) {
            areaDtos.add(getAreaById(area.getId()));
        }

        AreaDetailListDto areaListDto = new AreaDetailListDto();
        areaListDto.setAreas(areaDtos);
        return areaListDto;
    }

    @Override
    public void toggleOpen(Long id, boolean isOpen) {
        Area area = areaRepository.findById(id).orElseThrow();
        area.setIsOpen(isOpen);
        areaRepository.save(area);
    }

    @Transactional
    @Override
    public void updateArea(AreaDetailDto areaDetailDto) throws ConflictException {
        Area area = areaRepository.findById(areaDetailDto.getId()).orElseThrow();
        area.setName(areaDetailDto.getName());
        area.setWidth(areaDetailDto.getWidth() - 1);
        area.setHeight(areaDetailDto.getHeight() - 1);
        String opening = areaDetailDto.getOpeningTime();
        String closing = areaDetailDto.getClosingTime();

        if (opening == null || opening.isBlank()) {
            area.setOpeningTime(null);
        } else {
            area.setOpeningTime(LocalTime.parse(opening));
        }

        if (closing == null || closing.isBlank()) {
            area.setClosingTime(null);
        } else {
            area.setClosingTime(LocalTime.parse(closing));
        }
        area.setIsOpen(areaDetailDto.isOpen());
        //TODO Main
        areaRepository.save(area);

        List<LayoutCreateDto.AreaCreateDto.PlaceVisualDto> newPlaces = areaDetailDto.getPlaces();

        // Check for each place in newPlaces if it already exists in the area.
        // If yes check if something has changed, either segments or other information, if so update the information.
        // If the place does not exist, save it and its segments to the area

        List<AreaPlaceSegment> areaPlaceSegments = areaPlaceSegmentRepository.findByAreaId(area.getId());
        List<Long> placeIds = areaPlaceSegments.stream().map(aps -> aps.getPlace().getId()).collect(Collectors.toList());
        List<Place> places = placeRepository.findAllById(placeIds);
        List<LayoutCreateDto.AreaCreateDto.PlaceVisualDto> oldPlaces = getPlaceVisualDtos(places, areaPlaceSegments);

        for (LayoutCreateDto.AreaCreateDto.PlaceVisualDto newPlace : newPlaces) {
            Place place = places.stream().filter(p -> p.getNumber().equals(newPlace.getPlaceNumber())).findFirst().orElse(null);
            if (place != null) {
                // Place already exists
                LayoutCreateDto.AreaCreateDto.PlaceVisualDto oldPlace = oldPlaces.stream().filter(p -> p.getPlaceNumber().equals(newPlace.getPlaceNumber())).findFirst().orElse(null);
                if (oldPlace != null) {
                    // Place has changed
                    if (oldPlace.getNumberOfSeats() != newPlace.getNumberOfSeats()) {
                        place.setPax(newPlace.getNumberOfSeats());
                        placeRepository.save(place);
                    }
                    List<Segment> oldSegments = areaPlaceSegments.stream()
                        .filter(aps -> aps.getPlace().getId().equals(place.getId()))
                        .map(AreaPlaceSegment::getSegment)
                        .toList();
                    List<LayoutCreateDto.AreaCreateDto.PlaceVisualDto.CoordinateDto> newCoordinates = newPlace.getCoordinates();
                    for (LayoutCreateDto.AreaCreateDto.PlaceVisualDto.CoordinateDto newCoordinate : newCoordinates) {
                        Segment segment = oldSegments.stream().filter(s -> s.getX() == newCoordinate.getX() && s.getY() == newCoordinate.getY()).findFirst().orElse(null);
                        if (segment != null) {
                            // Segment already exists
                        } else {
                            // Segment does not exist
                            saveSegment(area, place, newCoordinate);
                        }
                    }
                    for (Segment oldSegment : oldSegments) {
                        LayoutCreateDto.AreaCreateDto.PlaceVisualDto.CoordinateDto newCoordinate = newCoordinates.stream().filter(c -> c.getX().equals(oldSegment.getX()) && c.getY().equals(oldSegment.getY())).findFirst().orElse(null);
                        if (newCoordinate == null) {
                            // Segment does not exist anymore
                            areaPlaceSegmentRepository.deleteAreaPlaceSegmentByAreaIdAndPlaceIdAndSegmentId(area.getId(), place.getId(), oldSegment.getId());
                            segmentRepository.deleteById(oldSegment.getId());
                        }
                    }
                }
            } else {
                // Place does not exist
                savePlace(area, newPlace);
            }
        }

        //Check for places that are in the old list but not in the new list
        //If they are not in the new list, but there is a reservation for them, throw an error
        //If they are not in the new list and there is no reservation for them, delete the area_place_segment first, then the segments attached to it, then the place attached to it
        for (LayoutCreateDto.AreaCreateDto.PlaceVisualDto oldPlace : oldPlaces) {
            Place place = places.stream().filter(p -> p.getNumber().equals(oldPlace.getPlaceNumber())).findFirst().orElse(null);
            if (place != null) {
                LayoutCreateDto.AreaCreateDto.PlaceVisualDto newPlace = newPlaces.stream().filter(p -> p.getPlaceNumber().equals(oldPlace.getPlaceNumber())).findFirst().orElse(null);
                if (newPlace == null) {
                    List<ReservationPlace> reservationPlaces = reservationPlaceRepository.findByPlaceId(place.getId());
                    if (!reservationPlaces.isEmpty()) {
                        throw new ConflictException("Cannot delete place", List.of("There are reservations for the table with id " + place.getId()));
                    }
                    areaPlaceSegmentRepository.deleteAreaPlaceSegmentByAreaIdAndPlaceId(area.getId(), place.getId());
                    List<Segment> segments = areaPlaceSegments.stream()
                        .filter(aps -> aps.getPlace().getId().equals(place.getId()))
                        .map(AreaPlaceSegment::getSegment)
                        .toList();
                    for (Segment segment : segments) {
                        areaPlaceSegmentRepository.deleteAreaPlaceSegmentByAreaIdAndPlaceIdAndSegmentId(area.getId(), place.getId(), segment.getId());
                        segmentRepository.deleteById(segment.getId());
                    }
                    placeRepository.deleteById(place.getId());
                }
            }
        }


    }
}
