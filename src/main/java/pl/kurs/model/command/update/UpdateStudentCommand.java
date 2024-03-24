package pl.kurs.model.command.update;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
public class UpdateStudentCommand extends UpdatePersonCommand {

    @Pattern(regexp = "[a-zA-Z0-9 ]+", message = "University name must match {regexp}.")
    private String universityName;

    @PastOrPresent(message = "Graduation year cannot be in the future.")
    private LocalDate graduationYear;

    @Min(value = 100, message = "Scholarship amount cannot be lower than 100.")
    private Double scholarshipAmount;
}
