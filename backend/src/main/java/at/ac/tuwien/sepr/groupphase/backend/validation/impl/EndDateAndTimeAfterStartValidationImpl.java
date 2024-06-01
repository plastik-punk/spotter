package at.ac.tuwien.sepr.groupphase.backend.validation.impl;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.EventCreateDto;
import at.ac.tuwien.sepr.groupphase.backend.validation.EndDateAndTimeAfterStartValidation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EndDateAndTimeAfterStartValidationImpl implements ConstraintValidator<EndDateAndTimeAfterStartValidation, EventCreateDto> {

    @Override
    public void initialize(EndDateAndTimeAfterStartValidation constraintAnnotation) {
    }

    @Override
    public boolean isValid(EventCreateDto event, ConstraintValidatorContext context) {
        if (event.getEndDate() == null || event.getStartDate() == null) {
            return true;
        }
        if (event.getEndDate().isBefore(event.getStartDate())) {
            return false;
        }
        if (event.getEndDate().isEqual(event.getStartDate())) {
            if (event.getEndTime() == null || event.getStartTime() == null) {
                return true;
            }
            return !event.getEndTime().isBefore(event.getStartTime());
        }
        return true;
    }
}