package pl.kurs.service.handlers.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import pl.kurs.model.Person;
import pl.kurs.model.Student;
import pl.kurs.model.command.creation.CreatePersonCommand;
import pl.kurs.model.command.creation.CreateStudentCommand;
import pl.kurs.model.command.update.UpdateStudentCommand;
import pl.kurs.model.dto.PersonDto;
import pl.kurs.model.dto.StudentDto;
import pl.kurs.model.validation.CommandValidator;
import pl.kurs.service.handlers.PersonHandler;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentHandler implements PersonHandler {

    private final ObjectMapper objectMapper;
    private final JdbcTemplate jdbcTemplate;
    private final CommandValidator validator;

    @Override
    public Person fromNodeToEntity(JsonNode jsonNode) throws JsonProcessingException {
        CreateStudentCommand command = objectMapper.treeToValue(jsonNode, CreateStudentCommand.class);
        validator.validateCommand(command);
        return fromCommandToEntity(command);
    }

    @Override
    public Person fromCommandToEntity(CreatePersonCommand command) {
        return objectMapper.convertValue(command, Student.class);
    }

    @Override
    public PersonDto fromEntityToDto(Person person) {
        return objectMapper.convertValue(person, StudentDto.class);
    }

    @Override
    public String supportsType() {
        return "student";
    }

    @Override
    public Person update(Person personToUpdate, JsonNode updateNode) throws JsonProcessingException {
        UpdateStudentCommand command = objectMapper.treeToValue(updateNode, UpdateStudentCommand.class);
        validator.validateCommand(command);
        return objectMapper.updateValue(personToUpdate, updateNode);
    }

    @Override
    public int insertBatch(List<String[]> lines) {
        String sql = "INSERT INTO person (type, first_name, last_name, social_number, height, weight, email, university_name, graduation_year, scholarship_amount, version)" +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        List<Object[]> objectsToInsert = new ArrayList<>();

        for (String[] line : lines) {
            CreateStudentCommand command = CreateStudentCommand.builder()
                    .type(line[0])
                    .firstName(line[1])
                    .lastName(line[2])
                    .socialNumber(line[3])
                    .height(Double.valueOf(line[4]))
                    .weight(Double.valueOf(line[5]))
                    .email(line[6])
                    .universityName(line[7])
                    .graduationYear(LocalDate.parse(line[8]))
                    .scholarshipAmount(Double.valueOf(line[9]))
                    .build();
            validator.validateCommand(command);
            objectsToInsert.add(convertToInsertableArray(command));
        }
        jdbcTemplate.batchUpdate(sql, objectsToInsert);
        return objectsToInsert.size();
    }


    private Object[] convertToInsertableArray(CreateStudentCommand command) {
        return new Object[]{
                command.getType(),
                command.getFirstName(),
                command.getLastName(),
                command.getSocialNumber(),
                command.getHeight(),
                command.getWeight(),
                command.getEmail(),
                command.getUniversityName(),
                command.getGraduationYear(),
                command.getScholarshipAmount(),
                0
        };
    }
}
