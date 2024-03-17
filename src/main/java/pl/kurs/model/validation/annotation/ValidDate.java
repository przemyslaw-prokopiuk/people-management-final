package pl.kurs.model.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import pl.kurs.model.validation.PositionCommandValidator;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = PositionCommandValidator.class)
@Target(value = TYPE)
@Retention(value = RUNTIME)
public @interface ValidDate {

    String message() default "Dates provided in the command are invalid.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default{};
}
