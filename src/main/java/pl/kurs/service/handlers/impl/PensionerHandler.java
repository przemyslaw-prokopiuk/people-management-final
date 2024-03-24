package pl.kurs.service.handlers.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import pl.kurs.model.Pensioner;
import pl.kurs.model.Person;
import pl.kurs.model.command.creation.CreatePensionerCommand;
import pl.kurs.model.command.creation.CreatePersonCommand;
import pl.kurs.model.command.update.UpdatePensionerCommand;
import pl.kurs.model.dto.PensionerDto;
import pl.kurs.model.dto.PersonDto;
import pl.kurs.model.validation.CommandValidator;
import pl.kurs.service.handlers.PersonHandler;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PensionerHandler implements PersonHandler {

    private final ObjectMapper objectMapper;
    private final JdbcTemplate jdbcTemplate;
    private final CommandValidator validator;

    @Override
    public Person fromNodeToEntity(JsonNode jsonNode) throws JsonProcessingException {
        CreatePensionerCommand command = objectMapper.treeToValue(jsonNode, CreatePensionerCommand.class);
        validator.validateCommand(command);
        return fromCommandToEntity(command);
    }

    @Override
    public Person fromCommandToEntity(CreatePersonCommand command) {
        return objectMapper.convertValue(command, Pensioner.class);
    }

    @Override
    public PersonDto fromEntityToDto(Person person) {
        return objectMapper.convertValue(person, PensionerDto.class);
    }

    @Override
    public String supportsType() {
        return "pensioner";
    }

    @Override
    public Person update(Person personToUpdate, JsonNode updateNode) throws JsonProcessingException {
        UpdatePensionerCommand command = objectMapper.treeToValue(updateNode, UpdatePensionerCommand.class);
        validator.validateCommand(command);
        return objectMapper.updateValue(personToUpdate, updateNode);
    }

    @Override
    public int insertBatch(List<String[]> lines) {
        String sql = "INSERT INTO person (type, first_name, last_name, social_number, height, weight, email, monthly_pension, total_years_of_work, version)" +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        List<Object[]> objectsToInsert = new ArrayList<>();

        for (String[] line : lines) {
            CreatePensionerCommand command = CreatePensionerCommand.builder()
                    .type(line[0])
                    .firstName(line[1])
                    .lastName(line[2])
                    .socialNumber(line[3])
                    .height(Double.valueOf(line[4]))
                    .weight(Double.valueOf(line[5]))
                    .email(line[6])
                    .monthlyPension(BigInteger.valueOf(Long.parseLong(line[7])))
                    .totalYearsOfWork(Integer.valueOf(line[8]))
                    .build();
            validator.validateCommand(command);
            objectsToInsert.add(convertToInsertableArray(command));
        }
        jdbcTemplate.batchUpdate(sql, objectsToInsert);
        return objectsToInsert.size();
    }

    private Object[] convertToInsertableArray(CreatePensionerCommand command) {
        return new Object[]{
                command.getType(),
                command.getFirstName(),
                command.getLastName(),
                command.getSocialNumber(),
                command.getHeight(),
                command.getWeight(),
                command.getEmail(),
                command.getMonthlyPension(),
                command.getTotalYearsOfWork(),
                0
        };
    }
}
