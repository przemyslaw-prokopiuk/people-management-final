package pl.kurs.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.kurs.exception.InvalidCsvFileException;
import pl.kurs.mapper.CommandToEntityMapper;
import pl.kurs.mapper.CsvToCommandMapper;
import pl.kurs.mapper.PersonMapper;
import pl.kurs.model.Person;
import pl.kurs.model.command.creation.CreatePersonCommand;
import pl.kurs.model.validation.RecordValidator;
import pl.kurs.service.PersonParser;

@Service
@RequiredArgsConstructor
@Slf4j
public class PersonParserImpl implements PersonParser {

    private final PersonMapper personMapper;
    private final RecordValidator recordValidator;

    @Override
    public Person parseStringToPerson(String line) {
        String[] record = line.split(",");
        String type = sanitizeRecord(record[0]);
        switch (type) {
            case "student" -> {
                return mapToPerson(record, personMapper::fromCsvToStudentCommand, personMapper::fromCommandToEntity);
            }
            case "employee" -> {
                return mapToPerson(record, personMapper::fromCsvToEmployeeCommand, personMapper::fromCommandToEntity);
            }
            case "pensioner" -> {
                return mapToPerson(record, personMapper::fromCsvToPensionerCommand, personMapper::fromCommandToEntity);
            }
            default -> throw new InvalidCsvFileException("Unknown type: " + type);
        }
    }

    private <T extends CreatePersonCommand> Person mapToPerson(String[] record, CsvToCommandMapper<T> csvToCommandMapper,
                                                               CommandToEntityMapper<T> commandToEntityMapper) {
        T command = csvToCommandMapper.map(record);
        if (!recordValidator.validate(command)) {
            throw new InvalidCsvFileException("Error processing record.");
        }
        return commandToEntityMapper.map(command);
    }

    private String sanitizeRecord(String record) {
        return record.trim()
                .replaceAll("[^\\p{Print}]", "")
                .replaceAll("^\"|\"$", "");
    }
}
