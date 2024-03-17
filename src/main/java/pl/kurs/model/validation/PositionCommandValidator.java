package pl.kurs.model.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import pl.kurs.model.command.creation.CreatePositionCommand;
import pl.kurs.model.validation.annotation.ValidDate;

public class PositionCommandValidator implements ConstraintValidator<ValidDate, CreatePositionCommand> {

    @Override
    public boolean isValid(CreatePositionCommand command, ConstraintValidatorContext context) {
        if (command.getStartDate() == null) {
            addConstraintViolation(context, "Start date must be present.");
            return false;
        } else if (command.getEndDate() == null && command.getCurrentPositionEndDate() == null) {
            addConstraintViolation(context, "Specify either historical or current position dates, not both.");
            return false;
        } else if (command.getEndDate() != null && command.getCurrentPositionEndDate() != null) {
            addConstraintViolation(context, "End date and current position's end date can't both be present.");
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
