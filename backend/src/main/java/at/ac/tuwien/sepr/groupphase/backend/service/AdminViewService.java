package at.ac.tuwien.sepr.groupphase.backend.service;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.ForeCastDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.PredictionDto;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Service for the admin view.
 */
public interface AdminViewService {
    /**
     * Get the prediction for the next week split into the areas.
     *
     * @param areaId    the areaId
     * @param startTime the start time
     * @param date      the date
     * @return the predictions for each area
     */
    PredictionDto getPrediction(Long areaId, LocalTime startTime, LocalDate date);

    /**
     * Get the forecast for the next week.
     *
     * @param areaId the area
     * @param date   the date
     * @return the forecast
     */
    ForeCastDto getForecast(Long areaId, LocalDate date);
}
