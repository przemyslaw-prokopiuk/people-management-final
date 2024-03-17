package pl.kurs.mapper;

import pl.kurs.model.Person;

@FunctionalInterface
public interface CommandToEntityMapper<T> {

    Person map(T command);
}
