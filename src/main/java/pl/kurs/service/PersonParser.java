package pl.kurs.service;

import pl.kurs.model.Person;

public interface PersonParser {

    Person parseStringToPerson(String line);
}
