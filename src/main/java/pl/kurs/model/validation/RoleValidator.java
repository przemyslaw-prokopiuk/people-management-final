package pl.kurs.model.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import pl.kurs.model.Role;
import pl.kurs.model.validation.annotation.ValidRole;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class RoleValidator implements ConstraintValidator<ValidRole, Role> {

    private Set<Role> validRoles;

    @Override
    public void initialize(ValidRole constraintAnnotation) {
        validRoles = new HashSet<>(Arrays.asList(constraintAnnotation.allowed()));
    }

    @Override
    public boolean isValid(Role value, ConstraintValidatorContext context) {
        return validRoles.contains(value);
    }
}
