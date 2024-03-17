package pl.kurs.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public interface FileManagementService {

    File createTempFile(MultipartFile file) throws IOException;

    void cleanupTempFile(Path tempFilePath);
}
