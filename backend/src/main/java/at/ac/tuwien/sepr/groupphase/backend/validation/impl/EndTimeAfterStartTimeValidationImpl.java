package at.ac.tuwien.sepr.groupphase.backend.validation.impl;

import at.ac.tuwien.sepr.groupphase.backend.entity.Event;
import at.ac.tuwien.sepr.groupphase.backend.validation.EndTimeAfterStartTimeValidation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EndTimeAfterStartTimeValidationImpl implements ConstraintValidator<EndTimeAfterStartTimeValidation, Event> {

    @Override
    public void initialize(EndTimeAfterStartTimeValidation constraintAnnotation) {
    }

    @Override
    public boolean isValid(Event event, ConstraintValidatorContext context) {
        if (event.getEndTime() == null || event.getStartTime() == null) {
            return true;
        }
        return !event.getEndTime().isBefore(event.getStartTime());
    }
}