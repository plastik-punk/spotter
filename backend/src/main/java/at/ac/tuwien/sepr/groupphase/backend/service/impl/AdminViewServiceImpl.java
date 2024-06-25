package at.ac.tuwien.sepr.groupphase.backend.service.impl;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ForeCastDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.PredictionDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.UnusualReservationsDto;
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
        return PredictionDto.PredictionBuilder.aPredictionDto().withPredictionText("The Amount of Employees needed for "
            + dateForPrediction + " is: " + amountOfEmployees).withAreaNames(areasNames).withPredictions(predictedList.toArray(new Long[0])).build();
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
        //Map<Integer, Long> amountOfWalkInCustomersPerHourMap = getIntegerLongMap(walkInsByDateMap.get(dateToCalculate)); //TODO

        //6. Calculate the Pax of the Restaurant
        List<Place> totalPlaces = placeRepository.findAll();
        long totalPax = 0;
        for (Place place : totalPlaces) {
            totalPax += place.getPax();
        }


        //7. Take Events in consideration
        List<Event> events = eventRepository.findAllByStartTimeBetween(dateToCalculate.atStartOfDay(), dateToCalculate.atStartOfDay().toLocalDate().atTime(23, 59));
        float eventInfluence = 1.0f;
        if (!events.isEmpty()) {
            List<Event> eventsInThePast = eventRepository.findAllByStartTimeBeforeAndStartTimeAfter(dateToCalculate.atStartOfDay(), dateToCalculate.minusYears(1).atStartOfDay());
            eventInfluence = calculateEventInfluence(eventsInThePast, dateToCalculate);
        }


        //8. calculate the percentage of the Employees
        long maxPaxAtSameTimeCurrDay = 1;
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

        //9. Calculate the amount of the Employees
        List<ApplicationUser> employeeList = applicationUserRepository.findAllByRole(RoleEnum.EMPLOYEE);
        long maxPaxAtSameTimeExpected = Collections.max(amountOfCustomersPerHourMap.values()); //+ Collections.max(amountOfWalkInCustomersPerHourMap.values());

        long averagePaxInThePast = amountOfCustomersPerDayMapInThePast.values().stream().mapToLong(Long::longValue).sum() / amountOfCustomersPerDayMapInThePast.size();


        float offsetFromWalkIn = (float) maxPaxAtSameTimeCurrDay / (float) maxPaxAtSameTimeWalkIn;
        if (maxPaxAtSameTimeWalkIn == 0) {
            offsetFromWalkIn = 1.0f;
        }

        float percentageOfPax = (float) maxPaxAtSameTimeCurrDay / (float) maxPaxAtSameTimeInThePast;
        if (maxPaxAtSameTimeInThePast == 0) {
            percentageOfPax = 1.0f;
        }
        float totalEmployeeCount = ((float) employeeList.size() * (clamp((float) (maxPaxAtSameTimeExpected / maxPaxAtSameTimeInThePast), 0.5f, 1.5f)));

        float offsetFromAverage = (float) maxPaxAtSameTimeCurrDay / (float) averagePaxInThePast;
        percentageOfPax = clamp(percentageOfPax, 0.8f, 1.2f);
        long employeePrediction = (long) (totalEmployeeCount * percentageOfPax * clamp(offsetFromAverage, 0.8f, 1.2f) * clamp(offsetFromWalkIn, 0.8f, 1.2f) * clamp(eventInfluence, 0.8f, 1.25f));

        predictedList.add(employeePrediction);
        return employeePrediction;
    }

    private static float clamp(float val, float min, float max) {
        return Math.max(min, Math.min(max, val));
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
        float result = 1.0f;

        List<Area> areas = areaRepository.findAll();

        for (Event event : events) {
            LocalDate start = event.getStartTime().toLocalDate();
            List<Reservation> paxList = reservationRepository.findAllByDate(start);
            long actualPax = 0;
            for (Reservation r : paxList) {
                actualPax += r.getPax();
            }
            float predictedPax = 0.0f;
            for (Area area : areas) {
                predictedPax += calculatePrediction(new ArrayList<>(), area.getId(), start);
            }
            if (predictedPax != 0.0f) {
                result = (actualPax / predictedPax);
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

    @Override
    public UnusualReservationsDto getUnusualReservations(LocalDate date) {
        LOGGER.info("Calculating Unusual Reservation Patters for next seven days following: {}", date);

        LocalDate dateToCalculate = date;
        long[] amountOfReservations = new long[7];
        long[] reservationsForNextWeek = new long[7];
        for (int i = 0; i < 7; i++) {
            long amountOfReservation = 0;
            long amountOfDaysWithReservations = 0;
            for (int j = 1; j < 52; j++) {
                List<Reservation> reservations = reservationRepository.findAllReservationsByDate(dateToCalculate.minusWeeks(j));
                amountOfReservation += reservations.size();
                if (!reservations.isEmpty()) {
                    amountOfDaysWithReservations++;
                }
            }
            if (amountOfDaysWithReservations == 0) {
                amountOfReservations[i] = 0;
            } else {
                amountOfReservations[i] = amountOfReservation / amountOfDaysWithReservations;
            }
            reservationsForNextWeek[i] = reservationRepository.findAllByDate(dateToCalculate).size();
            dateToCalculate = dateToCalculate.plusDays(1);
        }

        String[] days = new String[7];
        days[0] = "TODAY";
        days[1] = "TOMORROW";
        for (int i = 2; i < 7; i++) {
            days[i] = date.plusDays(i).getDayOfWeek().toString();
        }
        boolean isUnusual = false;
        String[] messages = new String[7];
        for (int i = 0; i < 7; i++) {
            if (reservationsForNextWeek[i] > amountOfReservations[i] * 1.3) {
                isUnusual = true;
                messages[i] = "Unusually high amount of Reservations detected: " + reservationsForNextWeek[i];
            } else if (reservationsForNextWeek[i] < amountOfReservations[i] * 0.7) {
                isUnusual = true;
                messages[i] = "Unusually low amount of Reservations detected: " + reservationsForNextWeek[i];
            } else {
                messages[i] = null;
            }
        }
        UnusualReservationsDto unusualReservationsDto = new UnusualReservationsDto();
        unusualReservationsDto.setDays(days);
        unusualReservationsDto.setMessages(messages);
        unusualReservationsDto.setIsUnusual(isUnusual);

        return unusualReservationsDto;
    }
}
