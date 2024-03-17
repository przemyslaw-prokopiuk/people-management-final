package pl.kurs.properties;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "csv.validator")
@Getter
@Setter
public class CsvValidatorProperties {

    private Integer studentFieldCount;
    private Integer employeeFieldCount;
    private Integer pensionerFieldCount;
    private Integer maxAllowedErrors;
}
