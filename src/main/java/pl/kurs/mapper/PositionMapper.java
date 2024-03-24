package pl.kurs.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.kurs.model.Position;
import pl.kurs.model.dto.PositionDto;

@Mapper(componentModel = "spring")
public interface PositionMapper {

    @Mapping(target = "employeeId", expression = "java(position.getEmployee().getId())")
    PositionDto fromEntityToDto(Position position);
}
