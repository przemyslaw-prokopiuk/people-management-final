package pl.kurs.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "executor")
@Getter
@Setter
public class ExecutorProperties {

    private Integer corePoolSize;
    private Integer maxPoolSize;

}
