package pl.kurs.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

import static jakarta.persistence.GenerationType.SEQUENCE;
import static jakarta.persistence.InheritanceType.JOINED;

@Entity
@Data
@Inheritance(strategy = JOINED)
public abstract class Person {

    @Id
    @GeneratedValue(strategy = SEQUENCE)
    private Long id;
    private String firstName;
    private String lastName;

    @Column(unique = true)
    private String socialNumber;
    private Double height;
    private Double weight;
    private String email;
    private LocalDate dateOfBirth;

    private Long uploadId;

    @Version
    private Long version;
}


