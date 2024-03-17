package pl.kurs.service;

import org.springframework.web.multipart.MultipartFile;
import pl.kurs.model.upload.dto.UploadDto;

import java.io.File;

public interface CsvProcessingService {

    void processFileAsync(MultipartFile file, UploadDto uploadDto);
}
