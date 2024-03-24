package pl.kurs.service.upload.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.kurs.model.upload.dto.UploadDto;
import pl.kurs.service.upload.CsvProcessor;
import pl.kurs.service.upload.UploadProgressService;
import pl.kurs.service.upload.UploadRequestProcessor;

@Service
@RequiredArgsConstructor
@Getter
@Slf4j
public class UploadRequestProcessorImpl implements UploadRequestProcessor {

    private final UploadProgressService uploadProgressService;
    private final CsvProcessor csvProcessor;

    public UploadDto processUploadRequest(MultipartFile file) {
        UploadDto uploadDto = uploadProgressService.initializeUploadTracking();
        csvProcessor.processFileAsync(file, uploadDto);
        return uploadDto;
    }
}