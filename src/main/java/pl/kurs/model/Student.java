package pl.kurs.model;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Student extends Person {

    private String universityName;
    private LocalDate graduationYear;
    private Double scholarshipAmount;
}

