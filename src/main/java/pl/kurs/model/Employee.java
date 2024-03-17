package pl.kurs.model;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import static jakarta.persistence.CascadeType.ALL;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Employee extends Person {

    private String positionName;
    private LocalDate startDate;
    private BigDecimal salary;

    @OneToMany(mappedBy = "employee", cascade = ALL, orphanRemoval = true)
    private Set<Position> positions;
}
