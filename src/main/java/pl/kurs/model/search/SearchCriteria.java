package pl.kurs.model.search;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class SearchCriteria {

    @NotBlank
    private String key;

    @NotBlank
    private String value;

}
