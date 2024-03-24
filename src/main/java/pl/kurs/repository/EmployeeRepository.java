package pl.kurs.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import pl.kurs.model.Employee;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Lock(LockModeType.OPTIMISTIC)
    Optional<Employee> findWithLockingById(Long id);
}
