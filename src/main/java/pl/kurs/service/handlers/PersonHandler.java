package pl.kurs.service.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import pl.kurs.model.Person;
import pl.kurs.model.command.creation.CreatePersonCommand;
import pl.kurs.model.dto.PersonDto;

import java.util.List;

public interface PersonHandler {

    Person fromNodeToEntity(JsonNode jsonNode) throws JsonProcessingException;

    Person fromCommandToEntity(CreatePersonCommand command);

    PersonDto fromEntityToDto(Person person);

    String supportsType();

    Person update(Person personToUpdate, JsonNode updateNode) throws JsonProcessingException;

    int insertBatch(List<String[]> lines);
}
