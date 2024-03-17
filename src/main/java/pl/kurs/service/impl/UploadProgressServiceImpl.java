package pl.kurs.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.exception.UploadInProgressException;
import pl.kurs.mapper.UploadStatusMapper;
import pl.kurs.model.upload.UploadProgress;
import pl.kurs.model.upload.dto.UploadDto;
import pl.kurs.model.upload.dto.UploadProgressDto;
import pl.kurs.repository.UploadProgressRepository;
import pl.kurs.service.UploadProgressService;

import java.text.MessageFormat;
import java.time.LocalDateTime;

import static pl.kurs.model.upload.UploadStatus.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class UploadProgressServiceImpl implements UploadProgressService {

    private final UploadProgressRepository uploadProgressRepository;
    private final UploadStatusMapper uploadStatusMapper;

    @Getter
    private Long currentUploadStatusId;

    @Override
    @Transactional
    public UploadDto initializeUploadTracking() {
        if (uploadProgressRepository.existsByStatusIs(IN_PROGRESS)) {
            throw new UploadInProgressException("There's currently an ongoing upload of CSV file.");
        }
        UploadProgress newUpload = uploadProgressRepository.save(new UploadProgress());
        currentUploadStatusId = newUpload.getId();
        return new UploadDto(currentUploadStatusId);
    }

    @Override
    @Transactional
    public UploadProgressDto findById(Long uploadStatusId) {
        return uploadStatusMapper.fromEntityToDto(uploadProgressRepository.findWithLockingById(uploadStatusId)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format(
                        "Upload progress with {0} not found.", uploadStatusId))));
    }

    @Override
    @Transactional
    public UploadProgress retrieveUploadStatus(Long uploadStatusId) {
        return uploadProgressRepository.findWithLockingById(uploadStatusId)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format(
                        "Upload progress with {0} not found.", uploadStatusId)));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateLinesToBeProcessed(Long uploadStatusId, int totalLines) {
        UploadProgress uploadProgress = retrieveUploadStatus(uploadStatusId);
        uploadProgress.setLinesToBeProcessed(totalLines);
        uploadProgressRepository.save(uploadProgress);
    }

    @Override
    @Async
    @Transactional
    public void updateUploadProgress(Long uploadStatusId, int recordsProcessed) {
        log.info("Commencing update upload progress for upload ID: {}. Lines processed {}.", uploadStatusId, recordsProcessed);
        UploadProgress uploadProgress = retrieveUploadStatus(uploadStatusId);
        uploadProgress.setLinesProcessed(recordsProcessed);
        uploadProgress.setLinesProcessed(recordsProcessed);
        uploadProgressRepository.save(uploadProgress);
    }

    @Override
    @Async
    @Transactional
    public void uploadFailed(Long uploadStatusId) {
        UploadProgress uploadProgress = retrieveUploadStatus(uploadStatusId);
        uploadProgress.setFinishTime(LocalDateTime.now());
        uploadProgress.setStatus(FAILED);
        uploadProgressRepository.save(uploadProgress);
        log.info("Marking upload ID {} as failed.", uploadStatusId);
    }

    @Override
    @Async
    @Transactional
    public void uploadSuccessful(Long uploadStatusId) {
        UploadProgress uploadProgress = retrieveUploadStatus(uploadStatusId);
        if (uploadProgress.getStatus() == IN_PROGRESS) {
            uploadProgress.setFinishTime(LocalDateTime.now());
            uploadProgress.setStatus(FINISHED);
            uploadProgressRepository.save(uploadProgress);
            log.info("Marking upload ID {} as finished.", uploadStatusId);
        } else {
            log.info("Upload process with id {} currently has status {}, unable to finish.", uploadStatusId, uploadProgress.getStatus());
        }
    }
}
