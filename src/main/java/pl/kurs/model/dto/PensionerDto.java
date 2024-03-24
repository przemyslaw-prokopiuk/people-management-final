package pl.kurs.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigInteger;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PensionerDto extends PersonDto {

    private BigInteger monthlyPension;
    private Integer totalYearsOfWork;
}
