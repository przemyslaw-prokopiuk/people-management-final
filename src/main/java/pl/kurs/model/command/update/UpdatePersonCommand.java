package pl.kurs.model.command.update;

import jakarta.validation.constraints.*;
import lombok.Data;
import pl.kurs.model.validation.annotation.ValidSocialNumber;

@Data
public class UpdatePersonCommand {

    @NotNull(message = "Type cannot be empty.")
    private String type;

    @Pattern(regexp = "[A-Z][a-z]{2,}( [A-Z][a-z]{2,})?", message = "First name must match {regexp}.")
    private String firstName;

    @Pattern(regexp = "[A-Z][a-z]{2,}( [A-Z][a-z]{2,})?", message = "Last name must match {regexp}.")
    private String lastName;

    @Pattern(regexp = "[0-9]{11}", message = "Social number must contain 11 digits.")
    @ValidSocialNumber
    private String socialNumber;

    @DecimalMin(value = "50.0", message = "Height must be greater than {value} cm.")
    @DecimalMax(value = "250.0", message = "Height must be lower than {value} cm.")
    private Double height;

    @DecimalMin(value = "30.0", message = "Weight must be greater than {value} kg.")
    @DecimalMax(value = "500.0", message = "Weight must be lower than {value} kg.")
    private Double weight;

    @Email(message = "Please provide valid email address.")
    private String email;

    @NotNull(message = "Version cannot be null.")
    private Long version;
}