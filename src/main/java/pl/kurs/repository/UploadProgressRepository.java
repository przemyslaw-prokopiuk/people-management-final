package pl.kurs.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;
import pl.kurs.model.upload.UploadProgress;
import pl.kurs.model.upload.UploadStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface UploadProgressRepository extends JpaRepository<UploadProgress, Long> {

    boolean existsByStatusIs(UploadStatus uploadStatus);

    List<UploadProgress> findByStatusIs(UploadStatus uploadStatus);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<UploadProgress> findWithLockingById(Long id);
}
