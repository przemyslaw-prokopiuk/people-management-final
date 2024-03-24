package pl.kurs.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.kurs.model.dto.PersonDto;
import pl.kurs.model.search.SearchCriteria;

import java.util.List;

public interface PeopleService {

    PersonDto save(JsonNode creationNode);

    Page<PersonDto> findBy(List<SearchCriteria> searchCriteria, Pageable pageable);

    PersonDto update(Long id, JsonNode updateNode);
}