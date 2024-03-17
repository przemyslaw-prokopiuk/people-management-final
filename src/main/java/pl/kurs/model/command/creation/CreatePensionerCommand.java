package pl.kurs.model.command.creation;

import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigInteger;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonTypeName("pensioner")
public class CreatePensionerCommand extends CreatePersonCommand {

    @NotNull(message = "Monthly pension cannot be empty.")
    @Min(value = 1000, message = "Monthly pension cannot be lower than 1000.")
    private BigInteger monthlyPension;

    @NotNull(message = "Total years of work cannot be empty.")
    @Min(value = 1, message = "Years worked cannot be lower than 1.")
    @Max(value = 80, message = "Years worked cannot be great than 80.")
    private Integer totalYearsOfWork;
}
