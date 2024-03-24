package pl.kurs.model.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import pl.kurs.model.validation.annotation.ValidSocialNumber;

import java.time.LocalDate;

public class SocialNumberValidator implements ConstraintValidator<ValidSocialNumber, String> {

    @Override
    public boolean isValid(String socialNumber, ConstraintValidatorContext context) {
        if (socialNumber == null) {
            return true;
        }

        int year;
        int month = Integer.parseInt(socialNumber.substring(2, 4));
        int day = Integer.parseInt(socialNumber.substring(4, 6));

        if (month > 12) {
            year = 2000 + Integer.parseInt(socialNumber.substring(0, 2));
            month -= 20;
        } else {
            year = 1900 + Integer.parseInt(socialNumber.substring(0, 2));
        }

        if (year > LocalDate.now().getYear() || month < 1 || month > 12 || day < 1 || day > 31) {
            return false;
        }
        return true;
    }
}
