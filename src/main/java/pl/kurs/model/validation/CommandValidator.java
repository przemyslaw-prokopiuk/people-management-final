package pl.kurs.model.validation;


import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class CommandValidator {

    private final LocalValidatorFactoryBean validator;

    public <T> void validateCommand(T command) {
        Set<ConstraintViolation<T>> violations = validator.validate(command);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}
