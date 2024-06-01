package at.ac.tuwien.sepr.groupphase.backend.validation.impl;

import at.ac.tuwien.sepr.groupphase.backend.validation.FutureOrPresentValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class FutureOrPresentValidationImpl implements ConstraintValidator<FutureOrPresentValidation, LocalDate> {

    @Autowired
    private Environment environment;

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        // If the "generateData" or "test" profiles are active, skip the validation
        if (environment.acceptsProfiles("generateData", "test")) {
            return true;
        }

        // Otherwise, perform the validation
        return value == null || !value.isBefore(LocalDate.now());
    }
}