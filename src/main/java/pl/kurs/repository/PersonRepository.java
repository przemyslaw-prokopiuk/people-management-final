package pl.kurs.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;
import pl.kurs.model.Person;

import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long>, JpaSpecificationExecutor<Person> {

    @Lock(LockModeType.OPTIMISTIC)
    Optional<Person> findWithLockingById(Long id);

    void deleteAllByUploadId(Long uploadId);

    boolean existsByUploadId(Long uploadId);
}
