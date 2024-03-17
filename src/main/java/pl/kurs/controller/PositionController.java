package pl.kurs.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kurs.model.command.creation.CreatePositionCommand;
import pl.kurs.model.dto.PositionDto;
import pl.kurs.service.PositionService;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/employees")
public class PositionController {

    private final PositionService positionService;

    @PostMapping("/{employeeId}/positions")
    public ResponseEntity<PositionDto> addPosition(@PathVariable("employeeId") Long employeeId,
                                                   @Valid @RequestBody CreatePositionCommand command) {
        return ResponseEntity.status(CREATED).body(positionService.save(employeeId, command));
    }
}
