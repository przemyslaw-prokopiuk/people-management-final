package pl.kurs.service;

import pl.kurs.model.command.creation.PositionCommand;
import pl.kurs.model.dto.PositionDto;

public interface PositionService {

    PositionDto save(Long employeeId, PositionCommand command);
}
