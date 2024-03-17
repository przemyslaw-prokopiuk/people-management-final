package pl.kurs.model.command.creation;

import jakarta.validation.constraints.*;
import lombok.Data;
import pl.kurs.model.validation.annotation.ValidDate;

import java.math.BigDecimal;
import java.time.LocalDate;

/*
    * Current design allows to either add historical position or add new current position.
    * Historical position is added by specifying start and end date, and omitting current position end date.
    * Current position is added by specifying start date and current position's end date without specifying end date.
 */

@Data
@ValidDate
public class CreatePositionCommand {

    @NotBlank(message = "Position name cannot be empty.")
    @Pattern(regexp = "[a-zA-Z0-9 ]+", message = "Position name must match {regexp}.")
    private String positionName;

    @NotNull(message = "Start date cannot be empty.")
    @PastOrPresent(message = "Start date cannot be in the future.")
    private LocalDate startDate;

    @PastOrPresent(message = "End date cannot be in the future.")
    private LocalDate endDate;

    @NotNull(message = "Salary amount cannot be empty.")
    @Min(value = 100, message = "Salary amount cannot be lower than 100.")
    private BigDecimal salary;

    @PastOrPresent(message = "Current position date cannot be in the future.")
    private LocalDate currentPositionEndDate;
}
