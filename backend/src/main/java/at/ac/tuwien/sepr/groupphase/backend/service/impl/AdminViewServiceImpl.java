package at.ac.tuwien.sepr.groupphase.backend.service.impl;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.PredictionDto;
import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepr.groupphase.backend.entity.Place;
import at.ac.tuwien.sepr.groupphase.backend.entity.Reservation;
import at.ac.tuwien.sepr.groupphase.backend.enums.RoleEnum;
import at.ac.tuwien.sepr.groupphase.backend.repository.ApplicationUserRepository;
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

@Service
public class AdminViewServiceImpl implements AdminViewService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final ReservationRepository reservationRepository;
    private final PlaceRepository placeRepository;
    private final ApplicationUserRepository applicationUserRepository;

    @Autowired
    public AdminViewServiceImpl(ReservationRepository reservationRepository, PlaceRepository placeRepository, ApplicationUserRepository applicationUserRepository) {
        this.reservationRepository = reservationRepository;
        this.placeRepository = placeRepository;
        this.applicationUserRepository = applicationUserRepository;
    }

    @Override
    public PredictionDto getPrediction(String area, LocalTime startTimeForPrediction, LocalDate dateForPrediction) {
        LOGGER.info("Calculating Prediction for given Area and Time: {}, {}, {}", area, startTimeForPrediction, dateForPrediction);
        LocalDate dateToCalculate = dateForPrediction;

        //TODO: distinguish between different Areas
        //TODO: Also Calculate Pax without Reservation
        List<Reservation> sameDayReservationListLastYear = new ArrayList<>();
        Map<LocalDate, Long> amountOfCustomersPerDayMapLastYear = new HashMap<>();
        //1. Get all Reservations from the last 52 Weeks
        //2. Calculate the maximum Pax per Day over the last year
        for (int i = 0; i < 52; i++) {
            List<Reservation> reservations = reservationRepository.findAllByDate(dateToCalculate.minusWeeks(i));
            sameDayReservationListLastYear.addAll(reservations);
            Map<LocalTime, Long> maxPaxPerHour = new HashMap<>();
            LocalDate date = null;
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
                amountOfCustomersPerDayMapLastYear.put(date, Collections.max(maxPaxPerHour.values()));
            }
        }

        //3. Get all Reservations from the same day
        List<Reservation> reservationsListSameDay = reservationRepository.findAllByDate(dateToCalculate);

        //4. Get the Reservation List Per day
        Map<LocalDate, List<Reservation>> reservationsByDateMap = null;
        for (Reservation reservation : sameDayReservationListLastYear) {
            LocalDate date = reservation.getDate();
            List<Reservation> reservations = new ArrayList<>();
            if (reservationsByDateMap != null && reservationsByDateMap.containsKey(date)) {
                reservationsByDateMap.get(date);
            } else {
                reservationsByDateMap = new HashMap<>();
            }
            reservations.add(reservation);
            reservationsByDateMap.put(date, reservations);
        }

        //5. Calculate the maximum Pax per Hour same day
        Map<Integer, Long> amountOfCustomersPerHourMap = new HashMap<>();
        for (Reservation reservation : reservationsListSameDay) {
            LocalTime startTime = reservation.getStartTime();
            LocalTime endTime = reservation.getEndTime();
            for (LocalTime time = startTime; time.isBefore(endTime); time = time.plusHours(1)) {
                amountOfCustomersPerHourMap.put(time.getHour(), reservation.getPax());
            }
        }

        //6. Calculate the Pax of the Restaurant
        List<Place> totalPlaces = placeRepository.findAll();
        long totalPax = 0;
        for (Place place : totalPlaces) {
            totalPax += place.getPax();
        }
        int totalPlacesCount = totalPlaces.size();

        //7. Calculate the amount of the Employees
        List<ApplicationUser> employeeList = applicationUserRepository.findAllByRole(RoleEnum.EMPLOYEE);
        int totalEmployeeCount = employeeList.size();
        //8, calculate the percentage of the Employees
        long maxPaxAtSameTimeCurrDay = Collections.max(amountOfCustomersPerHourMap.values());
        long maxPaxAtSameTimeLastYear = Collections.max(amountOfCustomersPerDayMapLastYear.values());
        long averagePaxLastYear = amountOfCustomersPerDayMapLastYear.values().stream().mapToLong(Long::longValue).sum() / amountOfCustomersPerDayMapLastYear.size();

        float percentageOfPax = (float) maxPaxAtSameTimeCurrDay / (float) maxPaxAtSameTimeLastYear;
        float percentageOfEmployees = (float) totalEmployeeCount * percentageOfPax;


        return null;
    }
}
