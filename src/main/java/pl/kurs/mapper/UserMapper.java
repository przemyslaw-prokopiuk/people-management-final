package pl.kurs.mapper;

import org.mapstruct.Mapper;
import pl.kurs.model.User;
import pl.kurs.model.command.creation.CreateUserCommand;
import pl.kurs.model.dto.UserDto;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User fromCommandToEntity(CreateUserCommand command);

    UserDto fromEntityToDto(User user);
}
