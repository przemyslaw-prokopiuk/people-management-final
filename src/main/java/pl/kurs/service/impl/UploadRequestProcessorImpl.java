package pl.kurs.service.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.kurs.model.upload.dto.UploadDto;
import pl.kurs.service.CsvProcessingService;
import pl.kurs.service.UploadProgressService;
import pl.kurs.service.UploadRequestProcessor;

@Service
@RequiredArgsConstructor
@Getter
@Slf4j
public class UploadRequestProcessorImpl implements UploadRequestProcessor {

    private final UploadProgressService uploadProgressService;
    private final CsvProcessingService csvProcessingService;

    public UploadDto processUploadRequest(MultipartFile file) {
        UploadDto uploadDto = uploadProgressService.initializeUploadTracking();
        csvProcessingService.processFileAsync(file, uploadDto);
        return uploadDto;
    }
}