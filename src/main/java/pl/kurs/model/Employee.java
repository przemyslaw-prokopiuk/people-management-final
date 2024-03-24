package pl.kurs.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import static jakarta.persistence.CascadeType.ALL;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@DiscriminatorValue("employee")
public class Employee extends Person {

    private String positionName;
    private LocalDate startDate;
    private BigDecimal salary;

    @OneToMany(mappedBy = "employee", cascade = ALL, orphanRemoval = true)
    private Set<Position> positions;
}
