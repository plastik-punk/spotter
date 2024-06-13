package at.ac.tuwien.sepr.groupphase.backend.service.impl;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.AreaLayoutDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.AreaListDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ReservationLayoutCheckAvailabilityDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.Area;
import at.ac.tuwien.sepr.groupphase.backend.entity.AreaPlaceSegment;
import at.ac.tuwien.sepr.groupphase.backend.entity.OpeningHours;
import at.ac.tuwien.sepr.groupphase.backend.entity.Place;
import at.ac.tuwien.sepr.groupphase.backend.entity.Segment;
import at.ac.tuwien.sepr.groupphase.backend.enums.StatusEnum;
import at.ac.tuwien.sepr.groupphase.backend.repository.ApplicationUserRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.AreaPlaceSegmentRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.AreaRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.ClosedDayRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.OpeningHoursRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.PlaceRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.ReservationPlaceRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.ReservationRepository;
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
    private final AreaPlaceSegmentRepository areaPlaceSegmentRepository;

    @Autowired
    private Validator validator;

    @Autowired
    public LayoutServiceImpl(ReservationRepository reservationRepository, ApplicationUserRepository applicationUserRepository, PlaceRepository placeRepository, ReservationMapper reservationMapper, LayoutMapper layoutMapper,
                             OpeningHoursRepository openingHoursRepository, ClosedDayRepository closedDayRepository, ReservationPlaceRepository reservationPlaceRepository, EmailService emailService, HashService hashService,
                             ApplicationUserServiceImpl applicationUserService, AreaRepository areaRepository, AreaPlaceSegmentRepository areaPlaceSegmentRepository) {
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
}
