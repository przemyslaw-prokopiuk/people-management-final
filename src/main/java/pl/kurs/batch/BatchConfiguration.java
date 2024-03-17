package pl.kurs.batch;

import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.PassThroughLineMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;
import pl.kurs.model.Person;
import pl.kurs.service.PeopleService;
import pl.kurs.service.PersonParser;
import pl.kurs.service.UploadProgressService;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
@Slf4j
public class BatchConfiguration {

    private final EntityManagerFactory entityManagerFactory;
    private final PlatformTransactionManager transactionManager;
    private final UploadProgressService uploadProgressService;
    private final RollbackService rollbackService;
    private final PeopleService peopleService;
    private final JobRepository jobRepository;
    private final PersonParser personParser;

    @Bean
    public Job runJob() {
        return new JobBuilder("csvUpload", jobRepository)
                .start(step1())
                .listener(jobExecutionListener())
                .build();
    }

    @Bean
    public Step step1() {
        return new StepBuilder("insertToPersonRepository", jobRepository)
                .<String, Person>chunk(400, transactionManager)
                .reader(reader(null))
                .processor(processor(personParser, peopleService, null))
                .writer(writer(entityManagerFactory))
                .listener(uploadProgressListener())
                .listener(stepExecutionListener())
                .taskExecutor(taskExecutor2())
                .build();
    }

    @Bean
    public JpaItemWriter<Person> writer(EntityManagerFactory entityManagerFactory) {
        JpaItemWriter<Person> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(entityManagerFactory);
        return writer;
    }

    @Bean
    @StepScope
    public FlatFileItemReader<String> reader(@Value("#{jobParameters['filePath']}") String filePath) {
        return new FlatFileItemReaderBuilder<String>()
                .name("personItemReader")
                .resource(new FileSystemResource(filePath))
                .lineMapper(new PassThroughLineMapper())
                .build();
    }

    @Bean
    @StepScope
    public ItemProcessor<String, Person> processor(PersonParser personParser, PeopleService peopleService,
                                                   @Value("#{jobParameters['uploadStatusId']}") Long uploadStatusId) {
        return line -> {
            Person person = personParser.parseStringToPerson(line);
            person.setDateOfBirth(peopleService.decodeDateOfBirth(person.getSocialNumber()));
            person.setUploadId(uploadStatusId);
            return person;
        };
    }



    @Bean
    public ChunkListener uploadProgressListener() {
        return new ChunkListener() {
            @Override
            public void afterChunk(ChunkContext context) {
                Long uploadStatusId = context.getStepContext().getStepExecution().getJobExecution().getJobParameters().getLong("uploadStatusId");
                long chunkSize = context.getStepContext().getStepExecution().getWriteCount();
                uploadProgressService.updateUploadProgress(uploadStatusId, (int) chunkSize);
            }
        };
    }

    @Bean
    public JobExecutionListener jobExecutionListener() {
        return new JobExecutionListener() {
            @Override
            public void afterJob(JobExecution jobExecution) {
                Long uploadStatusId = jobExecution.getJobParameters().getLong("uploadStatusId");
                if (jobExecution.getStatus() == BatchStatus.FAILED) {
                    uploadProgressService.uploadFailed(uploadStatusId);
                } else {
                    uploadProgressService.uploadSuccessful(uploadStatusId);
                }
            }
        };
    }

    @Bean
    public StepExecutionListener stepExecutionListener() {
        return new StepExecutionListener() {
            @Override
            public ExitStatus afterStep(StepExecution stepExecution) {
                if (stepExecution.getStatus() == BatchStatus.FAILED) {
                    stepExecution.getJobExecution().getExecutionContext().put("jobFailed", true);
                    rollbackService.rollbackUpload(stepExecution.getJobExecution().getJobParameters().getLong("uploadStatusId"));
                    return ExitStatus.FAILED;
                }
                return stepExecution.getExitStatus();
            }
        };
    }

    @Bean
    public TaskExecutor taskExecutor2() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);
        executor.setMaxPoolSize(8);
        executor.setQueueCapacity(10000);
        executor.afterPropertiesSet();
        return executor;
    }
}
