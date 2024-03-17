package pl.kurs.model.validation.annotation;

import jakarta.validation.Constraint;
import org.springframework.messaging.handler.annotation.Payload;
import pl.kurs.model.Role;
import pl.kurs.model.validation.RoleValidator;

import java.lang.annotation.*;

import static pl.kurs.model.Role.*;

@Documented
@Constraint(validatedBy = RoleValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidRole {

    Role[] allowed() default {ROLE_ADMIN, ROLE_EMPLOYEE, ROLE_IMPORTER};

    String message() default "Invalid type. Allowed types are: ROLE_ADMIN, ROLE_EMPLOYEE, ROLE_IMPORTER.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
