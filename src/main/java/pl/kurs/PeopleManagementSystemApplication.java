package pl.kurs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;
import pl.kurs.properties.ExecutorProperties;
import pl.kurs.properties.UploadProperties;

@SpringBootApplication
@EnableConfigurationProperties({ExecutorProperties.class, UploadProperties.class})
@EnableAsync
public class PeopleManagementSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(PeopleManagementSystemApplication.class, args);
    }

}