package pl.kurs.model.command.update;

import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonTypeName("employee")
public class UpdateEmployeeCommand extends UpdatePersonCommand {

    @Pattern(regexp = "[A-Z][a-z]{2,}", message = "Position name must match {regexp}.")
    private String positionName;

    @PastOrPresent(message = "Start date cannot be in the future.")
    private LocalDate startDate;

    @Min(value = 100, message = "Salary amount cannot be lower than 100.")
    private BigDecimal salary;
}
