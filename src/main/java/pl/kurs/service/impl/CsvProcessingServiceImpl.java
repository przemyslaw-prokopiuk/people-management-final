package pl.kurs.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.kurs.model.upload.dto.UploadDto;
import pl.kurs.model.validation.CsvValidator;
import pl.kurs.service.CsvProcessingService;
import pl.kurs.service.FileManagementService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

@Service
@RequiredArgsConstructor
@Slf4j
public class CsvProcessingServiceImpl implements CsvProcessingService {

    private final FileManagementService fileManagementService;
    private final CsvValidator csvValidator;
    private final JobLauncher jobLauncher;
    private final Job job;

    @Override
    @Async(value = "taskExecutor")
    public void processFileAsync(MultipartFile file, UploadDto uploadDto) {
        File tempFile = null;
        try {
            tempFile = fileManagementService.createTempFile(file);
            csvValidator.validateFile(tempFile, uploadDto.getId());
            startJob(uploadDto, tempFile);
        } catch (IOException | JobExecutionException e) {
            log.info("Unable to finish processing job. Aborting.");
        } finally {
            if (tempFile != null) {
                fileManagementService.cleanupTempFile(Path.of(tempFile.getPath()));
            }
            log.info("Processing file {} upload with ID {} has finished.", file.getName(), uploadDto.getId());
        }
    }

    private void startJob(UploadDto uploadDto, File tempFile) throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("filePath", tempFile.getPath())
                .addLong("uploadStatusId", uploadDto.getId())
                .toJobParameters();

        jobLauncher.run(job, jobParameters);
    }
}
