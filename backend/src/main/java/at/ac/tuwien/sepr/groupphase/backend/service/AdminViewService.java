package at.ac.tuwien.sepr.groupphase.backend.service;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.AdminViewDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.PredictionDto;

/**
 * Service for the admin view.
 */
public interface AdminViewService {
    /**
     * Get the prediction for the next game.
     *
     * @param adminViewDto the admin view dto
     * @return the prediction
     */
    PredictionDto getPrediction(AdminViewDto adminViewDto);
}
