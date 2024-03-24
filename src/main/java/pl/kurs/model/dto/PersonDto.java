package pl.kurs.model.dto;

import lombok.Data;

@Data
public abstract class PersonDto {

    private Long id;
    private String type;
    private String firstName;
    private String lastName;
    private String socialNumber;
    private Double height;
    private Double weight;
    private String email;
}