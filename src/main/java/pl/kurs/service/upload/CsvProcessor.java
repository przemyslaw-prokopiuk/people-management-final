package pl.kurs.service.upload;

import org.springframework.web.multipart.MultipartFile;
import pl.kurs.model.upload.dto.UploadDto;

import java.io.File;

public interface CsvProcessor {

    void processFileAsync(MultipartFile file, UploadDto uploadDto);

    void iterateThroughFile(File file, Long uploadId);
}
