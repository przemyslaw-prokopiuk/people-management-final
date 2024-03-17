package pl.kurs.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.kurs.model.Person;
import pl.kurs.model.command.creation.CreatePersonCommand;
import pl.kurs.model.command.update.UpdatePersonCommand;
import pl.kurs.model.dto.PersonDto;
import pl.kurs.model.search.SearchCriteria;

import java.time.LocalDate;
import java.util.List;

public interface PeopleService {

    PersonDto save(CreatePersonCommand command);

    Person findById(Long id);

    Page<PersonDto> findBy(List<SearchCriteria> searchCriteria, Pageable pageable);

    PersonDto update(Long id, UpdatePersonCommand command);

    LocalDate decodeDateOfBirth(String socialNumber);
}