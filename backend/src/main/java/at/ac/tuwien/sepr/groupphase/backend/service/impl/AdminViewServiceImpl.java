package at.ac.tuwien.sepr.groupphase.backend.service.impl;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ForeCastDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.PredictionDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepr.groupphase.backend.entity.Area;
import at.ac.tuwien.sepr.groupphase.backend.entity.Event;
import at.ac.tuwien.sepr.groupphase.backend.entity.Place;
import at.ac.tuwien.sepr.groupphase.backend.entity.Reservation;
import at.ac.tuwien.sepr.groupphase.backend.enums.RoleEnum;
import at.ac.tuwien.sepr.groupphase.backend.repository.ApplicationUserRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.AreaRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.PlaceRepository;
import at.ac.tuwien.sepr.groupphase.backend.repository.ReservationRepository;
import at.ac.tuwien.sepr.groupphase.backend.service.AdminViewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
public class AdminViewServiceImpl implements AdminViewService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final ReservationRepository reservationRepository;
    private final PlaceRepository placeRepository;
    private final ApplicationUserRepository applicationUserRepository;
    private final AreaRepository areaRepository;
    private final EventRepository eventRepository;

    @Autowired
    public AdminViewServiceImpl(ReservationRepository reservationRepository, PlaceRepository placeRepository, ApplicationUserRepository applicationUserRepository, AreaRepository areaRepository, EventRepository eventRepository) {
        this.reservationRepository = reservationRepository;
        this.placeRepository = placeRepository;
        this.applicationUserRepository = applicationUserRepository;
        this.areaRepository = areaRepository;
        this.eventRepository = eventRepository;
    }


    @Override
    public PredictionDto getPrediction(LocalTime startTimeForPrediction, LocalDate dateForPrediction) {
        LOGGER.info("Calculating Prediction for given Area and Time: {}, {}", startTimeForPrediction, dateForPrediction);

        List<Area> areas = areaRepository.findAll();

        if (areas.isEmpty()) {
            throw new NoSuchElementException("No Areas found");
        }

        List<Long> predictedList = new ArrayList<>();
        String[] areasNames = new String[areas.size()];
        for (int i = 0; i < areas.size(); i++) {
            areasNames[i] = areas.get(i).getName();
            calculatePrediction(predictedList, areas.get(i).getId(), dateForPrediction); //TODO: Also Calculate Pax without Reservation
        }

        long amountOfEmployees = predictedList.stream().mapToLong(Long::longValue).sum();
        return PredictionDto.PredictionBuilder.aPredictionDto()
            .withPredictionText("The Amount of Employees needed for " + dateForPrediction + " is: " + amountOfEmployees)
            .withAreaNames(areasNames)
            .withPredictions(predictedList.toArray(new Long[0]))
            .build();
    }

    private float calculatePrediction(List<Long> predictedList, Long areaId, LocalDate dateToCalculate) {
        List<Reservation> sameDayReservationListInThePastWithReservation = new ArrayList<>();
        List<Reservation> sameDayReservationListInThePastWithOutReservation = new ArrayList<>();

        Map<LocalDate, Long> amountOfCustomersPerDayMapInThePast = new HashMap<>();
        Map<LocalDate, Long> amountOfWalkInCustomersPerDayMapInThePast = new HashMap<>();

        //1. Get all Reservations from the last 5 Years
        //2. Calculate the maximum Pax per Day over the last year
        for (int i = 0; i < 260; i++) {
            List<Reservation> reservations = reservationRepository.findAllReservationsByAreaIdAndDateWithoutWalkInUsers(areaId, dateToCalculate.minusWeeks(i));
            sameDayReservationListInThePastWithReservation.addAll(reservations);

            List<Reservation> walkInUserReservations = reservationRepository.findAllWalkInReservationsByAreaIdAndDate(areaId, dateToCalculate.minusWeeks(i));
            sameDayReservationListInThePastWithOutReservation.addAll(walkInUserReservations);

            Map<LocalTime, Long> maxPaxPerHour = new HashMap<>();
            getMaxPaxPerHour(amountOfCustomersPerDayMapInThePast, reservations, maxPaxPerHour, null);

            Map<LocalTime, Long> maxPaxPerHourWalkIn = new HashMap<>();
            getMaxPaxPerHour(amountOfWalkInCustomersPerDayMapInThePast, walkInUserReservations, maxPaxPerHourWalkIn, null);
        }

        //3. Get all Reservations from the same day
        List<Reservation> reservationsListSameDay = reservationRepository.findAllReservationsByAreaIdAndDateWithoutWalkInUsers(areaId, dateToCalculate);
        if (reservationsListSameDay.isEmpty()) {
            predictedList.add(0L);
            return 0.0f;
        }
        //4. Get the Reservation List Per day
        Map<LocalDate, List<Reservation>> reservationsByDateMap = new HashMap<>();
        Map<LocalDate, List<Reservation>> walkInsByDateMap = new HashMap<>();

        getReservationsByDayMap(sameDayReservationListInThePastWithReservation, reservationsByDateMap);
        getReservationsByDayMap(sameDayReservationListInThePastWithOutReservation, walkInsByDateMap);

        //5. Calculate the maximum Pax per Hour
        Map<Integer, Long> amountOfCustomersPerHourMap = getIntegerLongMap(reservationsListSameDay);
        //Map<Integer, Long> amountOfWalkInCustomersPerHourMap = getIntegerLongMap(walkInsByDateMap.get(dateToCalculate)); TODO fix with Test data

        //6. Calculate the Pax of the Restaurant
        List<Place> totalPlaces = placeRepository.findAll();
        long totalPax = 0;
        for (Place place : totalPlaces) {
            totalPax += place.getPax();
        }

        long maxPaxAtSameTimeExpected = Collections.max(amountOfCustomersPerHourMap.values()); // + Collections.max(amountOfWalkInCustomersPerHourMap.values());


        //7. Calculate the amount of the Employees
        List<ApplicationUser> employeeList = applicationUserRepository.findAllByRole(RoleEnum.EMPLOYEE);
        long totalEmployeeCount = 5; //employeeList.size() / 2 * (maxPaxAtSameTimeExpected / totalPax); //TODO get the real count of employees with test Data

        //8. Take Events in consideration
        List<Event> events = eventRepository.findAllByStartTimeBetween(dateToCalculate.atStartOfDay(),
            dateToCalculate.atStartOfDay().toLocalDate().atTime(23, 59));
        float eventInfluence = 1.0f;
        if (!events.isEmpty()) {
            eventInfluence = calculateEventInfluence(events, dateToCalculate);
        }


        //9. calculate the percentage of the Employees
        long maxPaxAtSameTimeCurrDay = 0;
        if (!amountOfCustomersPerDayMapInThePast.isEmpty()) {
            maxPaxAtSameTimeCurrDay = Collections.max(amountOfCustomersPerHourMap.values());
        }
        long maxPaxAtSameTimeInThePast = 0;
        if (!amountOfCustomersPerDayMapInThePast.isEmpty()) {
            maxPaxAtSameTimeInThePast = Collections.max(amountOfCustomersPerDayMapInThePast.values());
        }
        long maxPaxAtSameTimeWalkIn = 0;
        if (!amountOfWalkInCustomersPerDayMapInThePast.isEmpty()) {
            maxPaxAtSameTimeWalkIn = Collections.max(amountOfWalkInCustomersPerDayMapInThePast.values());
        }
        long averagePaxInThePast = amountOfCustomersPerDayMapInThePast.values().stream().mapToLong(Long::longValue).sum() / amountOfCustomersPerDayMapInThePast.size();

        float offsetFromAverage = (float) maxPaxAtSameTimeCurrDay / (float) averagePaxInThePast;
        float offsetFromWalkIn = (float) maxPaxAtSameTimeCurrDay / (float) maxPaxAtSameTimeWalkIn;

        float percentageOfPax = (float) maxPaxAtSameTimeCurrDay / (float) maxPaxAtSameTimeInThePast;
        long employeePrediction = (long) (totalEmployeeCount * percentageOfPax * offsetFromAverage * offsetFromWalkIn * eventInfluence);

        predictedList.add(employeePrediction);
        return employeePrediction;
    }

    private void getReservationsByDayMap(List<Reservation> sameDayReservationListInThePastWithReservation, Map<LocalDate, List<Reservation>> reservationsByDateMap) {
        for (Reservation reservation : sameDayReservationListInThePastWithReservation) {
            LocalDate date = reservation.getDate();
            List<Reservation> reservations = new ArrayList<>();
            if (reservationsByDateMap != null && reservationsByDateMap.containsKey(date)) {
                reservationsByDateMap.get(date);
            }
            reservations.add(reservation);
            reservationsByDateMap.put(date, reservations);
        }
    }

    private static Map<Integer, Long> getIntegerLongMap(List<Reservation> reservationsListSameDay) {
        Map<Integer, Long> amountOfCustomersPerHourMap = new HashMap<>();
        for (Reservation reservation : reservationsListSameDay) {
            LocalTime startTime = reservation.getStartTime();
            LocalTime endTime = reservation.getEndTime();
            for (LocalTime time = startTime; time.isBefore(endTime); time = time.plusHours(1)) {
                amountOfCustomersPerHourMap.put(time.getHour(), amountOfCustomersPerHourMap.getOrDefault(time.getHour(), 0L) + reservation.getPax());
            }
        }
        return amountOfCustomersPerHourMap;
    }

    private void getMaxPaxPerHour(Map<LocalDate, Long> amountOfCustomersPerDayMapInThePast, List<Reservation> reservations, Map<LocalTime, Long> maxPaxPerHour, LocalDate date) {
        for (Reservation res : reservations) {
            date = res.getDate();
            LocalTime startTime = res.getStartTime();
            LocalTime endTime = res.getEndTime();
            long pax = res.getPax();

            for (LocalTime time = startTime; time.isBefore(endTime); time = time.plusHours(1)) {
                maxPaxPerHour.put(time, maxPaxPerHour.getOrDefault(time, 0L) + pax);
            }
        }
        if (date != null) {
            amountOfCustomersPerDayMapInThePast.put(date, Collections.max(maxPaxPerHour.values()));
        }
    }

    private float calculateEventInfluence(List<Event> events, LocalDate dateToCalculate) {
        float result = 0.0f;

        List<Area> areas = areaRepository.findAll();

        for (Event event : events) {
            if (!event.getStartTime().toLocalDate().equals(dateToCalculate)) {
                long actualPax = reservationRepository.findPaxByDate(dateToCalculate).stream().mapToLong(Long::longValue).sum();
                float predictedPax = 0.0f;
                for (Area area : areas) {
                    predictedPax += calculatePrediction(new ArrayList<>(), area.getId(), dateToCalculate);
                }
                if (predictedPax != 0.0f) {
                    result = (actualPax / predictedPax);
                }
            }
        }
        return result;
    }

    @Override
    public ForeCastDto getForecast(Long areaId, LocalDate date) {
        LOGGER.info("Calculating Forecast for given Area and Date: {}, {}", areaId, date);
        ForeCastDto foreCastDto = new ForeCastDto();

        foreCastDto.setMaxPlace(placeRepository.findAll().size());
        String[] days = new String[7];
        days[0] = "TODAY";
        days[1] = "TOMORROW";
        for (int i = 2; i < 7; i++) {
            days[i] = date.plusDays(i).getDayOfWeek().toString();
        }
        foreCastDto.setDays(days);
        int[] forecast = new int[7];
        for (int i = 0; i < 7; i++) {
            forecast[i] = reservationRepository.findAllByDate(date.plusDays(i)).size();
        }
        foreCastDto.setForecast(forecast);
        return foreCastDto;
    }
}
