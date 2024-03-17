package pl.kurs.model.validation.annotation;

import jakarta.validation.Constraint;
import org.springframework.messaging.handler.annotation.Payload;
import pl.kurs.model.validation.TypeValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = TypeValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidType {

    String[] allowed() default {"student", "employee", "pensioner"};

    String message() default "Invalid type. Allowed types are: student, employee, pensioner.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
