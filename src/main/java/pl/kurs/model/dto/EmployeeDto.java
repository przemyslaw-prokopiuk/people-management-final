package pl.kurs.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class EmployeeDto extends PersonDto {

    private String positionName;
    private LocalDate startDate;
    private BigDecimal salary;
}
