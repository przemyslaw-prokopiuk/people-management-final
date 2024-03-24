package pl.kurs.model.command.creation;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import pl.kurs.model.validation.annotation.ValidDate;

import java.math.BigDecimal;
import java.time.LocalDate;

/*
 * Endpoint to add historical position if startDate and endDate are specified, but currentPositionEndDate is empty.
 * Endpoint to add new current position for an employee if startDate and currentPositionEndDate are provided, but endDate is empty.
 */

@Data
@ValidDate
public class PositionCommand {

    @NotNull(message = "Position name cannot be empty.")
    @Pattern(regexp = "[a-zA-Z0-9 ]+", message = "Position name must match {regexp}.")
    private String positionName;

    @NotNull(message = "Start date cannot be empty.")
    @PastOrPresent(message = "Start date cannot be in the future.")
    private LocalDate startDate;

    @PastOrPresent(message = "End date cannot be in the future.")
    private LocalDate endDate;

    @PastOrPresent(message = "Current position end date cannot be in the future.")
    private LocalDate currentPositionEndDate;

    @NotNull(message = "Salary amount cannot be empty.")
    @Min(value = 100, message = "Salary amount cannot be lower than 100.")
    private BigDecimal salary;
}
