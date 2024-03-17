package pl.kurs.model.command.creation;

import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonTypeName("student")
public class CreateStudentCommand extends CreatePersonCommand {

    @NotBlank(message = "University name cannot be empty.")
    @Pattern(regexp = "[a-zA-Z0-9 ]+", message = "University name must match {regexp}.")
    private String universityName;

    @NotNull(message = "Graduation year cannot be empty.")
    @PastOrPresent(message = "Graduation year cannot be in the future.")
    private LocalDate graduationYear;

    @NotNull(message = "Scholarship amount cannot be empty.")
    @Min(value = 100, message = "Scholarship amount cannot be lower than 100.")
    private Double scholarshipAmount;
}
