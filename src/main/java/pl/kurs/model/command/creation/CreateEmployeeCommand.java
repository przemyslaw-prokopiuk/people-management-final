package pl.kurs.model.command.creation;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
@SuperBuilder
public class CreateEmployeeCommand extends CreatePersonCommand {

    @NotBlank(message = "Position name cannot be empty.")
    @Pattern(regexp = "[a-zA-Z0-9 ]+", message = "Position name must match {regexp}.")
    private String positionName;

    @NotNull(message = "Start date cannot be empty.")
    @PastOrPresent(message = "Start date cannot be in the future.")
    private LocalDate startDate;

    @NotNull(message = "Salary cannot be empty.")
    @Min(value = 100, message = "Salary amount cannot be lower than 100.")
    private BigDecimal salary;
}
