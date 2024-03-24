package pl.kurs.model.validation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.kurs.exception.CsvValidationException;
import pl.kurs.exception.InvalidCsvFileException;
import pl.kurs.service.upload.UploadProgressService;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


@Service
@Slf4j
@RequiredArgsConstructor
public class CsvValidator {

    private final UploadProgressService uploadProgressService;

    public void validateFile(File file, Long uploadStatusId) {
        try {
            checkFileExtension(file);
            countLines(file, uploadStatusId);
        } catch (InvalidCsvFileException e) {
            throw new CsvValidationException("Validation failed: " + e.getMessage());
        }
    }

    private void checkFileExtension(File file) {
        if (!file.getName().endsWith(".csv")) {
            throw new InvalidCsvFileException("Invalid file format.");
        }
    }

    private void countLines(File file, Long uploadStatusId) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            int lineNumber = 0;
            while (reader.readLine() != null) {
                lineNumber++;
            }
            uploadProgressService.updateLinesToBeProcessed(uploadStatusId, lineNumber);
        } catch (IOException e) {
            throw new InvalidCsvFileException("Error reading file: " + file.getName());
        }
    }
}
