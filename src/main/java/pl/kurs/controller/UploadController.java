package pl.kurs.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.kurs.model.upload.dto.UploadDto;
import pl.kurs.model.upload.dto.UploadProgressDto;
import pl.kurs.service.UploadRequestProcessor;
import pl.kurs.service.UploadProgressService;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/upload")
public class UploadController {

    private final UploadRequestProcessor uploadRequestProcessor;
    private final UploadProgressService uploadProgressService;

    @PostMapping
    public ResponseEntity<UploadDto> upload(@RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(uploadRequestProcessor.processUploadRequest(file));
    }

    @GetMapping("/{uploadId}")
    public ResponseEntity<UploadProgressDto> getUploadStatus(@PathVariable("uploadId") Long uploadId) {
        return ResponseEntity.ok(uploadProgressService.findById(uploadId));
    }
}
