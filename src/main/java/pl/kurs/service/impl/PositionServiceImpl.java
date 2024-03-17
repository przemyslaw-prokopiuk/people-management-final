package pl.kurs.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.exception.EmployeeNotFoundException;
import pl.kurs.exception.PositionOverlapException;
import pl.kurs.mapper.PositionMapper;
import pl.kurs.model.Employee;
import pl.kurs.model.Person;
import pl.kurs.model.Position;
import pl.kurs.model.command.creation.CreatePositionCommand;
import pl.kurs.model.dto.PositionDto;
import pl.kurs.repository.PersonRepository;
import pl.kurs.repository.PositionRepository;
import pl.kurs.service.PositionService;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PositionServiceImpl implements PositionService {

    private final PositionRepository positionRepository;
    private final PersonRepository personRepository;
    private final PositionMapper positionMapper;

    @Override
    @Transactional
    public PositionDto save(Long employeeId, CreatePositionCommand command) {
        Employee employee = findEmployee(employeeId);
        if (command.getCurrentPositionEndDate() == null) {
            return handleAddingHistoricalPosition(command, employee);
        }
        return handleAddingCurrentPosition(command, employee);
    }

    private PositionDto handleAddingHistoricalPosition(CreatePositionCommand command, Employee employee) {
        if (positionRepository.existsByEmployeeIdAndStartDateAndEndDate(employee.getId(), command.getStartDate(), command.getEndDate())) {
            throw new PositionOverlapException(MessageFormat.format(
                    "Employee with id {0} already has a position with start date {1} and end date {2}.",
                    employee.getId(), command.getStartDate(), command.getEndDate()));
        }

        Position positionToAdd = positionMapper.fromCommandToEntity(command);
        positionToAdd.setEmployee(employee);
        return addPositionToEmployeePastPositions(positionToAdd);
    }

    private PositionDto addPositionToEmployeePastPositions(Position positionToAdd) {
        List<Position> historicalPositions = positionRepository.findAllByEmployeeId(positionToAdd.getEmployee().getId());
        historicalPositions.forEach(historicalPosition -> checkForDateOverlap(historicalPosition, positionToAdd));
        return positionMapper.fromEntityToDto(positionRepository.saveAndFlush(positionToAdd));
    }

    private PositionDto handleAddingCurrentPosition(CreatePositionCommand command, Employee employee) {
        addEmployeesCurrentPositionToPastPositions(command, employee);
        return updateEmployeesCurrentPosition(command, employee);
    }

    private void addEmployeesCurrentPositionToPastPositions(CreatePositionCommand command, Employee employee) {
        Position currentPosition = positionMapper.fromEmployeesCurrentPositionDetailsToEntity(employee);
        if (!currentPosition.getStartDate().isAfter(command.getCurrentPositionEndDate())) {
            currentPosition.setEmployee(employee);
            currentPosition.setEndDate(command.getCurrentPositionEndDate());
            addPositionToEmployeePastPositions(currentPosition);
        } else {
            throw new PositionOverlapException(MessageFormat.format(
                    "Employee''s start date {0} is after indicated position''s end date {1}.",
                    currentPosition.getStartDate(), command.getCurrentPositionEndDate()));
        }
    }

    private PositionDto updateEmployeesCurrentPosition(CreatePositionCommand command, Employee employee) {
        employee.setPositionName(command.getPositionName());
        employee.setStartDate(command.getStartDate());
        employee.setSalary(command.getSalary());
        personRepository.save(employee);
        return PositionDto.builder()
                .employeeId(employee.getId())
                .positionName(command.getPositionName())
                .startDate(command.getStartDate())
                .salary(command.getSalary()).build();
    }

    private Employee findEmployee(Long employeeId) {
        Person person = personRepository.findWithLockingById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format(
                        "Person with id {0} not found", employeeId)));
        if (person.getClass() != Employee.class) {
            throw new EmployeeNotFoundException("Retrieved person is not an employee in the database.");
        }
        return (Employee) person;
    }

    private void checkForDateOverlap(Position historicalPosition, Position newPosition) {
        LocalDate NPSD = newPosition.getStartDate();
        LocalDate NPED = newPosition.getEndDate();
        LocalDate HPSD = historicalPosition.getStartDate();
        LocalDate HPED = historicalPosition.getEndDate();

        if ((NPSD.isBefore(HPSD) && NPED.isAfter(HPSD)) ||
                (NPSD.isBefore(HPED) && NPED.isAfter(HPED)) ||
                (NPSD.isAfter(HPSD) && NPED.isBefore(HPED))) {
            throw new PositionOverlapException(MessageFormat.format(
                    "New position {0} - {1} overlaps with historical position {2} - {3}.", NPSD, NPED, HPSD, HPED));
        }
    }
}
