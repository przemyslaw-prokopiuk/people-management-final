package pl.kurs.service;

import pl.kurs.model.command.creation.CreatePositionCommand;
import pl.kurs.model.dto.PositionDto;

public interface PositionService {

    PositionDto save(Long employeeId, CreatePositionCommand command);
}
