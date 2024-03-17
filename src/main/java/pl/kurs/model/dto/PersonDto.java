package pl.kurs.model.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public abstract class PersonDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String socialNumber;
    private Double height;
    private Double weight;
    private String email;
    private LocalDate dateOfBirth;
}