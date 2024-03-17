package pl.kurs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;
import pl.kurs.properties.CsvValidatorProperties;
import pl.kurs.properties.ExecutorProperties;

@SpringBootApplication
@EnableConfigurationProperties({ExecutorProperties.class, CsvValidatorProperties.class})
@EnableAsync
@EnableRetry
public class PeopleManagementSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(PeopleManagementSystemApplication.class, args);
    }

}
