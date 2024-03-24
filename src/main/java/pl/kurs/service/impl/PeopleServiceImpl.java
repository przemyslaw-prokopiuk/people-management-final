package pl.kurs.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.TypeMismatchException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.model.Person;
import pl.kurs.model.dto.PersonDto;
import pl.kurs.model.search.PersonSpecifications;
import pl.kurs.model.search.SearchCriteria;
import pl.kurs.service.handlers.PersonHandlerFactory;
import pl.kurs.service.handlers.PersonHandler;
import pl.kurs.repository.PersonRepository;
import pl.kurs.service.PeopleService;

import java.text.MessageFormat;
import java.util.ConcurrentModificationException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PeopleServiceImpl implements PeopleService {

    private final PersonRepository personRepository;
    private final PersonHandlerFactory personHandlerFactory;

    @Override
    @Transactional
    public PersonDto save(JsonNode creationNode) {
        try {
            PersonHandler handler = personHandlerFactory.getHandler(creationNode.get("type").asText());
            Person person = handler.fromNodeToEntity(creationNode);
            return handler.fromEntityToDto(personRepository.save(person));
        } catch (JsonProcessingException e) {
            log.info("Encountered error during saving person. Aborting.");
            throw new RuntimeException(e);
        }
    }

    @Override
    public Page<PersonDto> findBy(List<SearchCriteria> searchCriteria, Pageable pageable) {
        Specification<Person> specifications = PersonSpecifications.buildSpecification(searchCriteria);
        return personRepository.findAll(specifications, pageable)
                .map(person -> {
                    PersonHandler handler = personHandlerFactory.getHandler(person.getType());
                    return handler.fromEntityToDto(person);
                });
    }

    @Override
    @Transactional
    public PersonDto update(Long id, JsonNode updateNode) {
        try {
            Person person = personRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format(
                            "Person with id {0} not found.", id)));
            PersonHandler handler = personHandlerFactory.getHandler(updateNode.get("type").asText());

            if (!person.getType().equals(handler.supportsType())) {
                throw new TypeMismatchException("Retrieved person type is not the same as in update request.");
            }

            if (person.getVersion() != updateNode.get("version").asLong()) {
                throw new ConcurrentModificationException("Unable to update. The person has been modified by another transaction.");
            }

            Person updatedPerson = personRepository.save(handler.update(person, updateNode));
            return handler.fromEntityToDto(updatedPerson);
        } catch (JsonProcessingException e) {
            log.info("Unable to update person with id {}.", id);
            throw new RuntimeException(e);
        }
    }
}
