package pl.kurs.service.upload.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pl.kurs.exception.CsvProcessingException;
import pl.kurs.exception.InvalidCsvFileException;
import pl.kurs.exception.UnsupportedTypeException;
import pl.kurs.model.upload.dto.UploadDto;
import pl.kurs.model.validation.CsvValidator;
import pl.kurs.service.handlers.PersonHandlerFactory;
import pl.kurs.service.handlers.PersonHandler;
import pl.kurs.properties.UploadProperties;
import pl.kurs.service.upload.CsvProcessor;
import pl.kurs.service.upload.FileManagementService;
import pl.kurs.service.upload.UploadProgressService;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CsvProcessorImpl implements CsvProcessor {

    private final FileManagementService fileManagementService;
    private final PersonHandlerFactory personHandlerFactory;
    private final UploadProgressService uploadProgressService;
    private final CsvValidator csvValidator;
    private final UploadProperties uploadProperties;

    @Override
    @Transactional
    @Async(value = "taskExecutor")
    public void processFileAsync(MultipartFile file, UploadDto uploadDto) {
        File tempFile = null;
        try {
            tempFile = fileManagementService.createTempFile(file);
            csvValidator.validateFile(tempFile, uploadDto.getId());
            iterateThroughFile(tempFile, uploadDto.getId());
        } catch (IOException | RuntimeException e) {
            uploadProgressService.uploadFailed(uploadDto.getId());
            log.info("Unable to finish processing CSV upload. Aborting.");
            throw new CsvProcessingException("Rolling back transaction.");
        } finally {
            if (tempFile != null) {
                fileManagementService.cleanupTempFile(Path.of(tempFile.getPath()));
            }
            log.info("Processing file {} upload with ID {} has finished.", file.getOriginalFilename(), uploadDto.getId());
        }
    }

    @Override
    public void iterateThroughFile(File file, Long uploadId) {
        Map<String, List<String[]>> recordsByType = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                String type = data[0];

                if (personHandlerFactory.isSupportedType(type)) {
                    recordsByType.computeIfAbsent(type, k -> new ArrayList<>()).add(data);
                    if (recordsByType.get(type).size() >= uploadProperties.getBatchSize()) {
                        processBatch(type, recordsByType.get(type), uploadId);
                        recordsByType.get(type).clear();
                    }
                } else {
                    throw new UnsupportedTypeException("Unsupported type encountered. Aborting.");
                }
            }
            recordsByType.forEach((personType, records) -> {
                if (!records.isEmpty()) {
                    processBatch(personType, records, uploadId);
                    records.clear();
                }
            });
            uploadProgressService.uploadSuccessful(uploadId);

        } catch (IOException e) {
            throw new InvalidCsvFileException("Error reading file: " + file.getName());
        }
    }

    private void processBatch(String type, List<String[]> recordsByType, Long uploadId) {
        PersonHandler handler = personHandlerFactory.getHandler(type);
        int recordsInserted = handler.insertBatch(recordsByType);
        uploadProgressService.updateUploadProgress(uploadId, recordsInserted);
    }
}
