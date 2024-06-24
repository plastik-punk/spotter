package at.ac.tuwien.sepr.groupphase.backend.service.impl;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RestaurantOpeningHoursDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.RestaurantDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.OpeningHours;
import at.ac.tuwien.sepr.groupphase.backend.repository.OpeningHoursRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.RestaurantRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.RestaurantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class RestaurantServiceImpl implements RestaurantService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final RestaurantRepository restaurantRepository;
    private final OpeningHoursRepository openingHoursRepository;

    @Autowired
    public RestaurantServiceImpl(RestaurantRepository restaurantRepository,
                                 OpeningHoursRepository openingHoursRepository) {
        this.restaurantRepository = restaurantRepository;
        this.openingHoursRepository = openingHoursRepository;
    }

    @Override
    public RestaurantDto getRestaurantInfo() {
        LOGGER.trace("getRestaurantInfo ()");
        RestaurantDto dto = RestaurantDto.RestaurantDtoBuilder.aRestaurantDto()
            .withName(restaurantRepository.findNameById(1L))
            .withAddress(restaurantRepository.findAddressById(1L))
            .build();

        return dto;
    }

    @Override
    public RestaurantOpeningHoursDto getOpeningHours() {
        LOGGER.trace("getOpeningHours ()");
        return RestaurantOpeningHoursDto.RestaurantOpeningHoursDtoBuilder.aRestaurantOpeningHoursDto()
            .withMonday(getOpeningClosingTimes(DayOfWeek.MONDAY))
            .withTuesday(getOpeningClosingTimes(DayOfWeek.TUESDAY))
            .withWednesday(getOpeningClosingTimes(DayOfWeek.WEDNESDAY))
            .withThursday(getOpeningClosingTimes(DayOfWeek.THURSDAY))
            .withFriday(getOpeningClosingTimes(DayOfWeek.FRIDAY))
            .withSaturday(getOpeningClosingTimes(DayOfWeek.SATURDAY))
            .withSunday(getOpeningClosingTimes(DayOfWeek.SUNDAY))
            .build();
    }

    private List<LocalTime> getOpeningClosingTimes(DayOfWeek dayOfWeek) {
        List<OpeningHours> openingHours = openingHoursRepository.findByDayOfWeek(dayOfWeek);
        List<LocalTime> times = new ArrayList<>();
        for (OpeningHours oh : openingHours) {
            times.add(oh.getOpeningTime());
            times.add(oh.getClosingTime());
        }
        return times;
    }
}