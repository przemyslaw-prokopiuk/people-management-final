package pl.kurs.model;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigInteger;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Pensioner extends Person {

    private BigInteger monthlyPension;
    private Integer totalYearsOfWork;

}


