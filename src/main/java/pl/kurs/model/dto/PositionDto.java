package pl.kurs.model.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class PositionDto {

    private Long employeeId;
    private String positionName;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal salary;
}
