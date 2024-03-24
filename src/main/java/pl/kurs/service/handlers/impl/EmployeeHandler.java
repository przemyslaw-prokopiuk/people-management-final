package pl.kurs.service.handlers.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import pl.kurs.model.Employee;
import pl.kurs.model.Person;
import pl.kurs.model.command.creation.CreateEmployeeCommand;
import pl.kurs.model.command.creation.CreatePersonCommand;
import pl.kurs.model.command.update.UpdatePersonCommand;
import pl.kurs.model.dto.EmployeeDto;
import pl.kurs.model.dto.PersonDto;
import pl.kurs.model.validation.CommandValidator;
import pl.kurs.service.handlers.PersonHandler;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeHandler implements PersonHandler {

    private final ObjectMapper objectMapper;
    private final JdbcTemplate jdbcTemplate;
    private final CommandValidator validator;

    @Override
    public Person fromNodeToEntity(JsonNode jsonNode) throws JsonProcessingException {
        CreateEmployeeCommand command = objectMapper.treeToValue(jsonNode, CreateEmployeeCommand.class);
        validator.validateCommand(command);
        return fromCommandToEntity(command);
    }

    @Override
    public Person fromCommandToEntity(CreatePersonCommand command) {
        return objectMapper.convertValue(command, Employee.class);
    }

    @Override
    public PersonDto fromEntityToDto(Person person) {
        return objectMapper.convertValue(person, EmployeeDto.class);
    }

    @Override
    public String supportsType() {
        return "employee";
    }

    @Override
    public Person update(Person personToUpdate, JsonNode updateNode) throws JsonProcessingException {
        UpdatePersonCommand command = objectMapper.treeToValue(updateNode, UpdatePersonCommand.class);
        validator.validateCommand(command);
        return objectMapper.updateValue(personToUpdate, updateNode);
    }

    @Override
    public int insertBatch(List<String[]> lines) {
        String sql = "INSERT INTO person (type, first_name, last_name, social_number, height, weight, email, position_name, start_date, salary, version)" +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        List<Object[]> objectsToInsert = new ArrayList<>();

        for (String[] line : lines) {
            CreateEmployeeCommand command = CreateEmployeeCommand.builder()
                    .type(line[0])
                    .firstName(line[1])
                    .lastName(line[2])
                    .socialNumber(line[3])
                    .height(Double.valueOf(line[4]))
                    .weight(Double.valueOf(line[5]))
                    .email(line[6])
                    .positionName(line[7])
                    .startDate(LocalDate.parse(line[8]))
                    .salary(new BigDecimal(line[9]))
                    .build();
            validator.validateCommand(command);
            objectsToInsert.add(convertToInsertableArray(command));
        }
        jdbcTemplate.batchUpdate(sql, objectsToInsert);
        return objectsToInsert.size();
    }


    private Object[] convertToInsertableArray(CreateEmployeeCommand command) {
        return new Object[]{
                command.getType(),
                command.getFirstName(),
                command.getLastName(),
                command.getSocialNumber(),
                command.getHeight(),
                command.getWeight(),
                command.getEmail(),
                command.getPositionName(),
                command.getStartDate(),
                command.getSalary(),
                0
        };
    }
}
