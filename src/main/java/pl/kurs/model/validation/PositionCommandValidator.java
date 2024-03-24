package pl.kurs.model.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import pl.kurs.model.command.creation.PositionCommand;
import pl.kurs.model.validation.annotation.ValidDate;

public class PositionCommandValidator implements ConstraintValidator<ValidDate, PositionCommand> {

    @Override
    public boolean isValid(PositionCommand command, ConstraintValidatorContext context) {
        if (command.getStartDate() == null) {
            addConstraintViolation(context, "Start date cannot be empty.");
            return false;
        } else if (command.getEndDate() == null && command.getCurrentPositionEndDate() == null) {
            addConstraintViolation(context, "Specify either historical or current position end date.");
            return false;
        } else if (command.getEndDate() != null && command.getCurrentPositionEndDate() != null) {
            addConstraintViolation(context, "Specify either historical or current position end date.");
            return false;
        } else if (command.getEndDate() != null && command.getStartDate().isAfter(command.getEndDate())) {
            addConstraintViolation(context, "Start date must be before end date for historical positions.");
            return false;
        } else if (command.getCurrentPositionEndDate() != null && command.getCurrentPositionEndDate().isAfter(command.getStartDate())) {
            addConstraintViolation(context, "Current position end date must be before new position start date.");
            return false;
        }
        return true;
    }

    private void addConstraintViolation(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
    }
}
