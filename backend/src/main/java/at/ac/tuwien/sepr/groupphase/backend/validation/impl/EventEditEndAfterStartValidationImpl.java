package at.ac.tuwien.sepr.groupphase.backend.validation.impl;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.EventEditDto;
import at.ac.tuwien.sepr.groupphase.backend.validation.EventEditEndAfterStartValidation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EventEditEndAfterStartValidationImpl implements ConstraintValidator<EventEditEndAfterStartValidation, EventEditDto> {

    @Override
    public void initialize(EventEditEndAfterStartValidation constraintAnnotation) {
    }

    @Override
    public boolean isValid(EventEditDto event, ConstraintValidatorContext context) {
        if (event.getEndTime() == null || event.getStartTime() == null) {
            return true;
        }
        if (event.getEndTime().isBefore(event.getStartTime())) {
            return false;
        }
        return true;
    }
}
