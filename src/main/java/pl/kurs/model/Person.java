package pl.kurs.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

import static jakarta.persistence.GenerationType.IDENTITY;
import static jakarta.persistence.GenerationType.SEQUENCE;
import static jakarta.persistence.InheritanceType.JOINED;
import static jakarta.persistence.InheritanceType.SINGLE_TABLE;

@Entity
@Data
@Inheritance(strategy = SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
public abstract class Person {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(insertable = false, updatable = false)
    private String type;

    private String firstName;
    private String lastName;

    @Column(unique = true)
    private String socialNumber;
    private Double height;
    private Double weight;
    private String email;

    @Version
    private Long version;
}


