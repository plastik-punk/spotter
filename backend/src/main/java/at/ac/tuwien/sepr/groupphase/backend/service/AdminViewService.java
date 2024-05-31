package at.ac.tuwien.sepr.groupphase.backend.service;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.PredictionDto;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Service for the admin view.
 */
public interface AdminViewService {
    /**
     * Get the prediction for the next week.
     *
     * @param area      the area
     * @param startTime the start time
     * @param date      the date
     * @return the prediction
     */
    PredictionDto getPrediction(String area, LocalTime startTime, LocalDate date);
}
