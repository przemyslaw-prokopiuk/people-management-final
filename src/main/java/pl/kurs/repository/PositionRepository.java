package pl.kurs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.kurs.model.Position;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PositionRepository extends JpaRepository<Position, Long> {

    List<Position> findAllByEmployeeId(Long employeeId);

    boolean existsByEmployeeIdAndStartDateAndEndDate(Long employeeId, LocalDate startDate, LocalDate endDate);

}
