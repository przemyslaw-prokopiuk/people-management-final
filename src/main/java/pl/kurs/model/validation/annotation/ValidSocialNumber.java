package pl.kurs.model.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import pl.kurs.model.validation.SocialNumberValidator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = SocialNumberValidator.class)
@Target(value = ElementType.FIELD)
@Retention(value = RUNTIME)
public @interface ValidSocialNumber {

    String message() default "Provided social number is invalid.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}

