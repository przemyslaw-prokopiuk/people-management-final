package pl.kurs.service;

import pl.kurs.model.command.creation.CreateUserCommand;
import pl.kurs.model.dto.UserDto;

public interface UserService {

    UserDto register(CreateUserCommand command);
}
