package pl.kurs.model.command.creation;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import pl.kurs.model.validation.annotation.ValidSocialNumber;

@Data
@NoArgsConstructor
@SuperBuilder
public abstract class CreatePersonCommand {

    @NotNull(message = "Type cannot be empty.")
    private String type;

    @NotBlank(message = "First name cannot be empty.")
    @Pattern(regexp = "[A-Z][a-z]{2,}( [A-Z][a-z]{2,})?", message = "First name must match {regexp}.")
    private String firstName;

    @NotBlank(message = "Last name cannot be empty.")
    @Pattern(regexp = "[A-Z][a-z]{2,}( [A-Z][a-z]{2,})?", message = "Last name must match {regexp}.")
    private String lastName;

    @NotNull(message = "Social number cannot be empty.")
    @Pattern(regexp = "[0-9]{11}", message = "Social number must contain 11 digits.")
    @ValidSocialNumber
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