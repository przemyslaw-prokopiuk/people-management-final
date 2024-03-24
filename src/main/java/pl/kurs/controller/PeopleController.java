package pl.kurs.controller;


import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kurs.model.dto.PersonDto;
import pl.kurs.model.search.SearchCriteria;
import pl.kurs.service.PeopleService;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/people")
public class PeopleController {

    private final PeopleService peopleService;

    @PostMapping
    public ResponseEntity<PersonDto> save(@RequestBody JsonNode creationNode) {
        return ResponseEntity.status(CREATED).body(peopleService.save(creationNode));
    }

    @GetMapping
    public ResponseEntity<Page<PersonDto>> get(@RequestBody @Valid List<SearchCriteria> searchCriteria,
                                               @PageableDefault(size = 5) Pageable pageable) {
        return ResponseEntity.ok(peopleService.findBy(searchCriteria, pageable));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PersonDto> update(@PathVariable("id") Long id, @RequestBody JsonNode updateNode) {
        return ResponseEntity.ok(peopleService.update(id, updateNode));
    }
}
