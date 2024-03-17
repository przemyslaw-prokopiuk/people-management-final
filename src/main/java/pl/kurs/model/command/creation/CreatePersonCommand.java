package pl.kurs.model.command.creation;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = CreateStudentCommand.class, name = "student"),
        @JsonSubTypes.Type(value = CreateEmployeeCommand.class, name = "employee"),
        @JsonSubTypes.Type(value = CreatePensionerCommand.class, name = "pensioner")})
public abstract class CreatePersonCommand {

    @NotBlank(message = "First name cannot be empty.")
    @Pattern(regexp = "[A-Z][a-z]{2,}( [A-Z][a-z]{2,})?", message = "First name must match {regexp}.")
    private String firstName;

    @NotBlank(message = "Last name cannot be empty.")
    @Pattern(regexp = "[A-Z][a-z]{2,}( [A-Z][a-z]{2,})?", message = "Last name must match {regexp}.")
    private String lastName;

    @NotBlank(message = "Social number cannot be empty.")
    @Pattern(regexp = "[0-9]{11}", message = "Social number must contain 11 digits.")
    private String socialNumber;

    @NotNull(message = "Height cannot be empty.")
    @DecimalMin(value = "50.0", message = "Height must be greater than {value} cm.")
    @DecimalMax(value = "250.0", message = "Height must be lower than {value} cm.")
    private Double height;

    @NotNull(message = "Weight cannot be empty.")
    @DecimalMin(value = "30.0", message = "Weight must be greater than {value} kg.")
    @DecimalMax(value = "500.0", message = "Weight must be lower than {value} kg.")
    private Double weight;

    @NotNull(message = "Email cannot be empty.")
    @Email(message = "Please provide valid email address.")
    private String email;
}