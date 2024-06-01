package at.ac.tuwien.sepr.groupphase.backend.validation;

import at.ac.tuwien.sepr.groupphase.backend.validation.impl.EndDateAndTimeAfterStartValidationImpl;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EndDateAndTimeAfterStartValidationImpl.class)
public @interface EndDateAndTimeAfterStartValidation {
    String message() default "End date and time must be after start date and time";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
