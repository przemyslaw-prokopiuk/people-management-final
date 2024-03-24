package pl.kurs.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "upload")
@Getter
@Setter
public class UploadProperties {

    private int batchSize;
}
