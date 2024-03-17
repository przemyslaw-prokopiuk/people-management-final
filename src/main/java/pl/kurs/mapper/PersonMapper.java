package pl.kurs.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.SubclassMapping;
import pl.kurs.exception.CsvParseException;
import pl.kurs.model.Employee;
import pl.kurs.model.Pensioner;
import pl.kurs.model.Person;
import pl.kurs.model.Student;
import pl.kurs.model.command.creation.CreateEmployeeCommand;
import pl.kurs.model.command.creation.CreatePensionerCommand;
import pl.kurs.model.command.creation.CreatePersonCommand;
import pl.kurs.model.command.creation.CreateStudentCommand;
import pl.kurs.model.command.update.UpdateEmployeeCommand;
import pl.kurs.model.command.update.UpdatePensionerCommand;
import pl.kurs.model.command.update.UpdatePersonCommand;
import pl.kurs.model.command.update.UpdateStudentCommand;
import pl.kurs.model.dto.EmployeeDto;
import pl.kurs.model.dto.PensionerDto;
import pl.kurs.model.dto.PersonDto;
import pl.kurs.model.dto.StudentDto;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import static org.mapstruct.SubclassExhaustiveStrategy.RUNTIME_EXCEPTION;

@Mapper(componentModel = "spring", subclassExhaustiveStrategy = RUNTIME_EXCEPTION)
public interface PersonMapper {

    @SubclassMapping(source = CreateStudentCommand.class, target = Student.class)
    @SubclassMapping(source = CreateEmployeeCommand.class, target = Employee.class)
    @SubclassMapping(source = CreatePensionerCommand.class, target = Pensioner.class)
    Person fromCommandToEntity(CreatePersonCommand command);

    @SubclassMapping(source = UpdateStudentCommand.class, target = Student.class)
    @SubclassMapping(source = UpdateEmployeeCommand.class, target = Employee.class)
    @SubclassMapping(source = UpdatePensionerCommand.class, target = Pensioner.class)
    Person fromCommandToEntity(UpdatePersonCommand command);

    @SubclassMapping(source = Student.class, target = StudentDto.class)
    @SubclassMapping(source = Employee.class, target = EmployeeDto.class)
    @SubclassMapping(source = Pensioner.class, target = PensionerDto.class)
    PersonDto fromEntityToDto(Person person);

    default CreateStudentCommand fromCsvToStudentCommand(String[] record) {
        CreateStudentCommand command = new CreateStudentCommand();
        parseCommonFieldsFromCsvToCommand(record, command);
        command.setUniversityName(parseString(record[7]));
        command.setGraduationYear(parseLocalDate(record[8]));
        command.setScholarshipAmount(parseDouble(record[9]));
        return command;
    }

    default CreateEmployeeCommand fromCsvToEmployeeCommand(String[] record) {
        CreateEmployeeCommand command = new CreateEmployeeCommand();
        parseCommonFieldsFromCsvToCommand(record, command);
        command.setPositionName(parseString(record[7]));
        command.setStartDate(parseLocalDate(record[8]));
        command.setSalary(new BigDecimal(parseBigInteger(record[9])));
        return command;
    }

    default CreatePensionerCommand fromCsvToPensionerCommand(String[] record) {
        CreatePensionerCommand command = new CreatePensionerCommand();
        parseCommonFieldsFromCsvToCommand(record, command);
        command.setMonthlyPension(parseBigInteger(record[7]));
        command.setTotalYearsOfWork(Integer.parseInt(parseString(record[8])));
        return command;
    }

    private void parseCommonFieldsFromCsvToCommand(String[] record, CreatePersonCommand command) {
        command.setFirstName(parseString(record[1]));
        command.setLastName(parseString(record[2]));
        command.setSocialNumber(parseString(record[3]));
        command.setHeight(parseDouble(record[4]));
        command.setWeight(parseDouble(record[5]));
        command.setEmail(parseString(record[6]));
    }

    private Double parseDouble(String input) {
        try {
            return Double.valueOf(parseString(input));
        } catch (NumberFormatException e) {
            throw new CsvParseException("Invalid double value: " + input, e);
        }
    }

    private LocalDate parseLocalDate(String input) {
        try {
            return LocalDate.parse(parseString(input));
        } catch (DateTimeParseException e) {
            throw new CsvParseException("Invalid date format: " + input, e);
        }
    }

    private BigInteger parseBigInteger(String input) {
        try {
            return new BigInteger(parseString(input));
        } catch (NumberFormatException e) {
            throw new CsvParseException("Invalid BigInteger value: " + input, e);
        }
    }

    private String parseString(String input) {
        return input.trim().replaceAll("^\"|\"$", "");
    }

}
