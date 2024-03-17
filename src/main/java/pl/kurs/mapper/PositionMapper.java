package pl.kurs.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.kurs.model.Employee;
import pl.kurs.model.Position;
import pl.kurs.model.command.creation.CreatePositionCommand;
import pl.kurs.model.dto.PositionDto;

@Mapper(componentModel = "spring")
public interface PositionMapper {

    Position fromCommandToEntity(CreatePositionCommand command);

    @Mapping(target = "id", ignore = true)
    Position fromEmployeesCurrentPositionDetailsToEntity(Employee employee);

    @Mapping(target = "employeeId", expression = "java(position.getEmployee().getId())")
    PositionDto fromEntityToDto(Position position);
}
