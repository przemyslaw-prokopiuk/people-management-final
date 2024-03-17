package pl.kurs.service;

import org.springframework.web.multipart.MultipartFile;
import pl.kurs.model.upload.dto.UploadDto;

import java.io.IOException;

public interface UploadRequestProcessor {

    UploadDto processUploadRequest(MultipartFile file) throws IOException;

}
