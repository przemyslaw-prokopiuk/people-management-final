package pl.kurs.model.search;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import pl.kurs.exception.IncorrectSearchCriteriaException;
import pl.kurs.model.Person;

import java.time.LocalDate;
import java.util.List;

public class PersonSpecifications {

    public static Specification<Person> buildSpecification(List<SearchCriteria> criteriaList) {
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = criteriaList.stream()
                    .map(criteria -> processCriteria(criteria, root, criteriaBuilder))
                    .toList();
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }

    private static Predicate processCriteria(SearchCriteria criteria, Root<Person> root, CriteriaBuilder criteriaBuilder) {
        String key = criteria.getKey();
        String value = criteria.getValue();

        if (key == null || value == null) {
            throw new IncorrectSearchCriteriaException("Key or value is null");
        }

        if (isRange(value)) {
            return isDateRange(value) ? buildDatePredicate(key, parseDateRange(value), root, criteriaBuilder) :
                    buildNumericPredicate(key, parseNumericRange(value), root, criteriaBuilder);
        } else {
            return buildStringPredicate(key, value, root, criteriaBuilder);
        }
    }

    private static Predicate buildDatePredicate(String key, DateRange dateRange, Root<Person> root, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.between(root.get(key), dateRange.start, dateRange.end);
    }

    private static Predicate buildNumericPredicate(String key, NumericRange numericRange, Root<Person> root, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.between(root.get(key), numericRange.start, numericRange.end);
    }

    private static Predicate buildStringPredicate(String key, String value, Root<Person> root, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.like(criteriaBuilder.lower(root.get(key)), "%" + value.toLowerCase() + "%");
    }

    private static boolean isRange(String value) {
        return value.startsWith("<") && value.endsWith(">");
    }

    private static NumericRange parseNumericRange(String value) {
        String[] parts = value.substring(1, value.length() - 1).split(", ");
        Double start = Double.parseDouble(parts[0]);
        Double end = Double.parseDouble(parts[1]);
        return new NumericRange(start, end);
    }

    private static boolean isDateRange(String value) {
        return value.matches("<\\d{4}-\\d{2}-\\d{2}, \\d{4}-\\d{2}-\\d{2}>");
    }

    private static DateRange parseDateRange(String value) {
        String[] parts = value.substring(1, value.length() - 1).split(", ");
        LocalDate start = LocalDate.parse(parts[0]);
        LocalDate end = LocalDate.parse(parts[1]);
        return new DateRange(start, end);
    }

    private static class NumericRange {
        Double start;
        Double end;

        NumericRange(Double start, Double end) {
            this.start = start;
            this.end = end;
        }
    }

    private static class DateRange {
        LocalDate start;
        LocalDate end;

        DateRange(LocalDate start, LocalDate end) {
            this.start = start;
            this.end = end;
        }
    }
}
