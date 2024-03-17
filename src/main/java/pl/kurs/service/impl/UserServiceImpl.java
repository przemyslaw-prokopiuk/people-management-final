package pl.kurs.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.mapper.UserMapper;
import pl.kurs.model.User;
import pl.kurs.model.command.creation.CreateUserCommand;
import pl.kurs.model.dto.UserDto;
import pl.kurs.repository.UserRepository;
import pl.kurs.service.UserService;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserDto register(CreateUserCommand command) {
        User user = userMapper.fromCommandToEntity(command);
        user.setPassword(passwordEncoder.encode(command.getPassword()));
        user.setRegistrationTime(LocalDateTime.now());
        return userMapper.fromEntityToDto(userRepository.save(user));
    }
}
