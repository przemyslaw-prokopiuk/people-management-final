package pl.kurs.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class StudentDto extends PersonDto {

    private String universityName;
    private LocalDate graduationYear;
    private Double scholarshipAmount;
}
