package pl.kurs.service.upload;


import pl.kurs.model.upload.dto.UploadDto;
import pl.kurs.model.upload.dto.UploadProgressDto;

public interface UploadProgressService {

    UploadDto initializeUploadTracking();

    UploadProgressDto findById(Long uploadStatusId);

    void updateLinesToBeProcessed(Long uploadStatusId, int totalLines);

    void updateUploadProgress(Long uploadStatusId, int recordsProcessed);

    void uploadFailed(Long uploadStatusId);

    void uploadSuccessful(Long uploadStatusId);
}
