package pl.kurs.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import pl.kurs.properties.ExecutorProperties;

import java.util.concurrent.ExecutorService;

@Configuration
@RequiredArgsConstructor
public class AsyncConfig {

    private final ExecutorProperties properties;

    @Bean(name = "taskExecutor")
    public ExecutorService executorService() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(properties.getCorePoolSize());
        executor.setMaxPoolSize(properties.getMaxPoolSize());
        executor.initialize();
        return executor.getThreadPoolExecutor();
    }
}
