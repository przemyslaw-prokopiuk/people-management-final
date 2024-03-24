package pl.kurs.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kurs.model.command.creation.PositionCommand;
import pl.kurs.model.dto.PositionDto;
import pl.kurs.service.PositionService;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/employees")
public class EmployeeController {

    private final PositionService positionService;

    @PostMapping("/{employeeId}/positions")
    public ResponseEntity<PositionDto> addPosition(@PathVariable("employeeId") Long employeeId,
                                                   @Valid @RequestBody PositionCommand command) {
        return ResponseEntity.status(CREATED).body(positionService.save(employeeId, command));
    }
}
