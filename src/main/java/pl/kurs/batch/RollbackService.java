package pl.kurs.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.repository.PersonRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class RollbackService {

    private final PersonRepository personRepository;

    @Transactional
    public void rollbackUpload(Long uploadId) {
        int maxRetries = 6;

        for (int retryCount = 0; retryCount < maxRetries; retryCount++) {
            if (rollCommittedTransactionsBackForBatchId(uploadId)) {
                break;
            } else {
                log.info("Failed to rollback transactions for upload ID: " + uploadId);
            }
        }
    }

    private boolean rollCommittedTransactionsBackForBatchId(Long uploadId) {
        log.info("Executing rollback for upload ID: {}", uploadId);
        try {
            personRepository.deleteAllByUploadId(uploadId);
            Thread.sleep(10000);
            if (personRepository.existsByUploadId(uploadId)) {
                return false;
            }
        } catch (Exception e) {
            log.error("Error during rollback. Retrying.", e);
            return false;
        }
        log.info("Successfully deleted all objects related to upload ID: {}", uploadId);
        return true;
    }
}
