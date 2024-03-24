package pl.kurs.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.exception.PositionOverlapException;
import pl.kurs.mapper.PositionMapper;
import pl.kurs.model.Employee;
import pl.kurs.model.Position;
import pl.kurs.model.command.creation.PositionCommand;
import pl.kurs.model.dto.PositionDto;
import pl.kurs.repository.EmployeeRepository;
import pl.kurs.repository.PositionRepository;
import pl.kurs.service.PositionService;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PositionServiceImpl implements PositionService {

    private final PositionRepository positionRepository;
    private final EmployeeRepository employeeRepository;
    private final PositionMapper positionMapper;

    @Override
    @Transactional
    public PositionDto save(Long employeeId, PositionCommand command) {
        Employee employee = employeeRepository.findWithLockingById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format(
                        "Person with id {0} not found", employeeId)));

        if (command.getCurrentPositionEndDate() != null) {
            return assignNewCurrentPosition(employee, command);
        }
        return addHistoricalPosition(employee, command);
    }

    private PositionDto assignNewCurrentPosition(Employee employee, PositionCommand command) {
        moveCurrentPositionToHistory(employee, command);
        updateNewCurrentPositionDetails(employee, command);

        return PositionDto.builder()
                .employeeId(employee.getId())
                .positionName(employee.getPositionName())
                .startDate(employee.getStartDate())
                .salary(employee.getSalary())
                .build();
    }

    private void moveCurrentPositionToHistory(Employee employee, PositionCommand command) {
        if (command.getCurrentPositionEndDate().isBefore(employee.getStartDate())) {
            throw new PositionOverlapException("Current position end date is before its start date.");
        }
        Position currentPosition = Position.builder()
                .positionName(employee.getPositionName())
                .startDate(employee.getStartDate())
                .endDate(command.getCurrentPositionEndDate())
                .salary(employee.getSalary())
                .employee(employee)
                .build();
        positionRepository.save(currentPosition);
    }

    private void updateNewCurrentPositionDetails(Employee employee, PositionCommand command) {
        employee.setPositionName(command.getPositionName());
        employee.setStartDate(command.getStartDate());
        employee.setSalary(command.getSalary());
        employeeRepository.save(employee);
    }

    private PositionDto addHistoricalPosition(Employee employee, PositionCommand command) {
        Position newHistoricalPosition = Position.builder()
                .positionName(command.getPositionName())
                .startDate(command.getStartDate())
                .endDate(command.getEndDate())
                .salary(command.getSalary())
                .employee(employee)
                .build();
        List<Position> existingPositions = positionRepository.findAllByEmployeeId(employee.getId());

        if (isOverlappingWithExistingPositions(existingPositions, newHistoricalPosition)) {
            throw new PositionOverlapException("New historical positions overlaps with an existing position.");
        }
        return positionMapper.fromEntityToDto(newHistoricalPosition);
    }

    private boolean isOverlappingWithExistingPositions(List<Position> existingPositions, Position newHistoricalPosition) {
        LocalDate newStart = newHistoricalPosition.getStartDate();
        LocalDate newEnd = newHistoricalPosition.getEndDate();

        for (Position existingPosition : existingPositions) {
            LocalDate existingStart = existingPosition.getStartDate();
            LocalDate existingEnd = existingPosition.getEndDate();

            boolean startsDuringExisting = !newStart.isBefore(existingStart) && !newStart.isAfter(existingEnd);
            boolean endsDuringExisting = !newEnd.isBefore(existingStart) && !newEnd.isAfter(existingEnd);
            boolean encompassesExisting = newStart.isBefore(existingStart) && newEnd.isAfter(existingEnd);

            if (startsDuringExisting || endsDuringExisting || encompassesExisting) {
                return true;
            }
        }
        return false;
    }
}
