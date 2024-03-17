package pl.kurs.model.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import pl.kurs.model.validation.annotation.ValidType;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class TypeValidator implements ConstraintValidator<ValidType, String> {

    private Set<String> validTypes;

    @Override
    public void initialize(ValidType constraint) {
        validTypes = new HashSet<>(Arrays.asList(constraint.allowed()));
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && validTypes.contains(value.toLowerCase());
    }
}
