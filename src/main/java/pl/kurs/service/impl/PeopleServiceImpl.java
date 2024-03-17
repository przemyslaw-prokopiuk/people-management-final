package pl.kurs.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.common.CustomBeanUtils;
import pl.kurs.exception.InvalidSocialNumberException;
import pl.kurs.exception.UpdateFailedException;
import pl.kurs.mapper.PersonMapper;
import pl.kurs.model.Person;
import pl.kurs.model.command.creation.CreatePersonCommand;
import pl.kurs.model.command.update.UpdatePersonCommand;
import pl.kurs.model.dto.PersonDto;
import pl.kurs.model.search.PersonSpecifications;
import pl.kurs.model.search.SearchCriteria;
import pl.kurs.repository.PersonRepository;
import pl.kurs.service.PeopleService;

import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PeopleServiceImpl implements PeopleService {

    private final PersonRepository personRepository;
    private final PersonMapper personMapper;
    private final CustomBeanUtils beanUtils;

    @Override
    @Transactional
    public PersonDto save(CreatePersonCommand command) {
        Person person = personMapper.fromCommandToEntity(command);
        person.setDateOfBirth(decodeDateOfBirth(command.getSocialNumber()));
        return personMapper.fromEntityToDto(personRepository.save(person));
    }

    @Override
    public Person findById(Long id) {
        return personRepository.findWithLockingById(id)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format(
                        "Person with id {0} not found.", id)));
    }

    @Override
    public Page<PersonDto> findBy(List<SearchCriteria> searchCriteria, Pageable pageable) {
        Specification<Person> specifications = PersonSpecifications.buildSpecification(searchCriteria);
        Page<Person> people = personRepository.findAll(specifications, pageable);
        return people.map(personMapper::fromEntityToDto);
    }

    @Override
    @Transactional
    public PersonDto update(Long id, UpdatePersonCommand command) {
        Person person = findById(id);
        Person personToUpdate = personMapper.fromCommandToEntity(command);
        overwriteNonNullProperties(person, personToUpdate);
        return personMapper.fromEntityToDto(personRepository.save(person));
    }

    @Override
    public LocalDate decodeDateOfBirth(String socialNumber) {
        int year;
        int month = Integer.parseInt(socialNumber.substring(2, 4));
        int day = Integer.parseInt(socialNumber.substring(4, 6));

        if (month > 12) {
            year = 2000 + Integer.parseInt(socialNumber.substring(0, 2));
            month -= 20;
        } else {
            year = 1900 + Integer.parseInt(socialNumber.substring(0, 2));
        }

        if (year > LocalDate.now().getYear() || month < 1 || month > 12 || day < 1 || day > 31) {
            throw new InvalidSocialNumberException("Invalid PESEL number: " + socialNumber);
        }
        return LocalDate.of(year, month, day);
    }

    private void overwriteNonNullProperties(Person person, Person personToUpdate) {
        try {
            beanUtils.copyProperties(person, personToUpdate);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new UpdateFailedException(MessageFormat.format(
                    "Failed to update person with id {0}.", person.getId()));
        }
    }
}
