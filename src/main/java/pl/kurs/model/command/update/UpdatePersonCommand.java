package pl.kurs.model.command.update;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = UpdateStudentCommand.class, name = "student"),
        @JsonSubTypes.Type(value = UpdateEmployeeCommand.class, name = "employee"),
        @JsonSubTypes.Type(value = UpdatePensionerCommand.class, name = "pensioner")})
public abstract class UpdatePersonCommand {

    @Pattern(regexp = "[A-Z][a-z]{2,}( [A-Z][a-z]{2,})?", message = "First name must match {regexp}.")
    private String firstName;

    @Pattern(regexp = "[A-Z][a-z]{2,}( [A-Z][a-z]{2,})?", message = "Last name must match {regexp}.")
    private String lastName;

    @DecimalMin(value = "50.0", message = "Height must be greater than {value} cm.")
    @DecimalMax(value = "250.0", message = "Height must be lower than {value} cm.")
    private Double height;

    @DecimalMin(value = "30.0", message = "Weight must be greater than {value} kg.")
    @DecimalMax(value = "500.0", message = "Weight must be lower than {value} kg.")
    private Double weight;

    @Email(message = "Please provide valid email address.")
    private String email;

}