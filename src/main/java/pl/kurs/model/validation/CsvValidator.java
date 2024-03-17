package pl.kurs.model.validation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.kurs.exception.CsvValidationException;
import pl.kurs.exception.InvalidCsvFileException;
import pl.kurs.properties.CsvValidatorProperties;
import pl.kurs.service.UploadProgressService;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.MessageFormat;


@Service
@Slf4j
@RequiredArgsConstructor
public class CsvValidator {

    private final CsvValidatorProperties properties;
    private final UploadProgressService uploadProgressService;

    public void validateFile(File file, Long uploadStatusId) {
        try {
            checkFileExtension(file);
            validateRows(file, uploadStatusId);
        } catch (InvalidCsvFileException e) {
            throw new CsvValidationException("Validation failed: " + e.getMessage());
        }
    }

    private void checkFileExtension(File file) {
        if (!file.getName().endsWith(".csv")) {
            throw new InvalidCsvFileException("Invalid file format.");
        }
    }

    private void validateRows(File file, Long uploadStatusId) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (!checkFieldCount(line)) {
                    log.error("Validation error at line number: {}", lineNumber);
                    throw new InvalidCsvFileException(MessageFormat.format(
                            "Unable to process file {0} due to validation errors.", file.getName()));
                }
            }
            uploadProgressService.updateLinesToBeProcessed(uploadStatusId, lineNumber);
        } catch (IOException e) {
            throw new InvalidCsvFileException("Error reading file: " + file.getName());
        }
    }

    private boolean checkFieldCount(String line) {
        String[] fields = line.split(",");
        String recordType = fields[0].toLowerCase().trim();

        return switch (sanitizeRecord(recordType)) {
            case "student" -> fields.length == properties.getStudentFieldCount();
            case "employee" -> fields.length == properties.getEmployeeFieldCount();
            case "pensioner" -> fields.length == properties.getPensionerFieldCount();
            default -> {
                log.error("Unknown record type: {}", recordType);
                yield false;
            }
        };
    }

    private String sanitizeRecord(String record) {
        return record.trim()
                .replaceAll("[^\\p{Print}]", "")
                .replaceAll("^\"|\"$", "");
    }
}
