package pl.kurs.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.model.upload.UploadProgress;
import pl.kurs.repository.PersonRepository;
import pl.kurs.repository.UploadProgressRepository;

import java.time.LocalDateTime;
import java.util.List;

import static pl.kurs.model.upload.UploadStatus.FAILED;
import static pl.kurs.model.upload.UploadStatus.IN_PROGRESS;


@Component
@RequiredArgsConstructor
@Slf4j
public class StartupRecoveryService {

    private final UploadProgressRepository uploadProgressRepository;
    private final PersonRepository personRepository;

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    @Retryable(maxAttempts = 5, backoff = @Backoff(delay = 5000))
    public void removeEntitiesAssociatedWithUploadsInProgress() {
        List<UploadProgress> inProgressUploads = uploadProgressRepository.findByStatusIs(IN_PROGRESS);

        if (!inProgressUploads.isEmpty()) {
            for (UploadProgress upload : inProgressUploads) {
                log.info("Startup Recovery Service: Commencing clean up for upload ID: {}.", upload.getId());
                personRepository.deleteAllByUploadId(upload.getId());

                if (personRepository.existsByUploadId(upload.getId())) {
                    log.info("Startup Recovery Service: There's still objects related to upload ID: {}.", upload.getId());
                    return;
                }

                upload.setStatus(FAILED);
                upload.setLinesProcessed(0);
                upload.setFinishTime(LocalDateTime.now());
                uploadProgressRepository.save(upload);
                log.info("Startup Recovery Service: Finished clean up of all interrupted uploads.");
            }
        } else {
            log.info("Startup Recovery Service: No uploads in progress.");
        }

    }
}
