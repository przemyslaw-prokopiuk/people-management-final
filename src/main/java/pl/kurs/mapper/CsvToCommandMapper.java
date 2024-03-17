package pl.kurs.mapper;
@FunctionalInterface
public interface CsvToCommandMapper<T> {

    T map (String[] record);
}
