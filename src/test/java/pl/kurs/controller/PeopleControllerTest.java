package pl.kurs.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.kurs.model.Employee;
import pl.kurs.model.command.creation.CreateEmployeeCommand;
import pl.kurs.model.command.creation.CreatePensionerCommand;
import pl.kurs.model.command.creation.CreatePositionCommand;
import pl.kurs.model.command.creation.CreateStudentCommand;
import pl.kurs.model.command.update.UpdateEmployeeCommand;
import pl.kurs.model.command.update.UpdatePensionerCommand;
import pl.kurs.model.command.update.UpdateStudentCommand;
import pl.kurs.model.search.SearchCriteria;
import pl.kurs.repository.PersonRepository;
import pl.kurs.repository.PositionRepository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class PeopleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PositionRepository positionRepository;

    @Container
    @ServiceConnection
    public static MySQLContainer container = new MySQLContainer("mysql:5.7")
            .withDatabaseName("example_db")
            .withUsername("Test")
            .withPassword("Test");

    @BeforeAll
    public static void setUp() {
        container.withReuse(true);
        container.start();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testSave_StudentSavedByAdmin_Success() throws Exception {
        assertFalse(personRepository.findById(1L).isPresent());

        CreateStudentCommand command = new CreateStudentCommand();
        command.setFirstName("Frank");
        command.setLastName("Sinatra");
        command.setSocialNumber("95052100000");
        command.setHeight(186.0);
        command.setWeight(105.0);
        command.setEmail("email@example.com");
        command.setUniversityName("Uniwersytet Warszawski");
        command.setGraduationYear(LocalDate.of(2017, 7, 1));
        command.setScholarshipAmount(1000.0);

        mockMvc.perform(post("/api/v1/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(Matchers.notNullValue()))
                .andExpect(jsonPath("$.firstName").value(command.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(command.getLastName()))
                .andExpect(jsonPath("$.socialNumber").value(command.getSocialNumber()))
                .andExpect(jsonPath("$.height").value(command.getHeight()))
                .andExpect(jsonPath("$.weight").value(command.getWeight()))
                .andExpect(jsonPath("$.email").value(command.getEmail()))
                .andExpect(jsonPath("$.universityName").value(command.getUniversityName()))
                .andExpect(jsonPath("$.graduationYear").value(command.getGraduationYear().toString()))
                .andExpect(jsonPath("$.scholarshipAmount").value(command.getScholarshipAmount()));

        assertTrue(personRepository.findById(1L).isPresent());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testSave_EmployeeSavedByAdmin_Success() throws Exception {
        assertFalse(personRepository.findById(1L).isPresent());

        CreateEmployeeCommand command = new CreateEmployeeCommand();
        command.setFirstName("Frank");
        command.setLastName("Sinatra");
        command.setSocialNumber("95052100000");
        command.setHeight(186.0);
        command.setWeight(105.0);
        command.setEmail("email@example.com");
        command.setPositionName("Drukarz Frytkarz");
        command.setStartDate(LocalDate.of(2023, 3, 1));
        command.setSalary(BigDecimal.valueOf(5000.0));

        mockMvc.perform(post("/api/v1/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(Matchers.notNullValue()))
                .andExpect(jsonPath("$.firstName").value(command.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(command.getLastName()))
                .andExpect(jsonPath("$.socialNumber").value(command.getSocialNumber()))
                .andExpect(jsonPath("$.height").value(command.getHeight()))
                .andExpect(jsonPath("$.weight").value(command.getWeight()))
                .andExpect(jsonPath("$.email").value(command.getEmail()))
                .andExpect(jsonPath("$.positionName").value(command.getPositionName()))
                .andExpect(jsonPath("$.startDate").value(command.getStartDate().toString()))
                .andExpect(jsonPath("$.salary").value(command.getSalary()));

        assertTrue(personRepository.findById(1L).isPresent());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testSave_PensionerSavedByAdmin_Success() throws Exception {
        assertFalse(personRepository.findById(1L).isPresent());

        CreatePensionerCommand command = new CreatePensionerCommand();
        command.setFirstName("Frank");
        command.setLastName("Sinatra");
        command.setSocialNumber("95052100000");
        command.setHeight(186.0);
        command.setWeight(105.0);
        command.setEmail("email@example.com");
        command.setMonthlyPension(BigInteger.valueOf(3000L));
        command.setTotalYearsOfWork(50);

        mockMvc.perform(post("/api/v1/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(Matchers.notNullValue()))
                .andExpect(jsonPath("$.firstName").value(command.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(command.getLastName()))
                .andExpect(jsonPath("$.socialNumber").value(command.getSocialNumber()))
                .andExpect(jsonPath("$.height").value(command.getHeight()))
                .andExpect(jsonPath("$.weight").value(command.getWeight()))
                .andExpect(jsonPath("$.email").value(command.getEmail()))
                .andExpect(jsonPath("$.monthlyPension").value(command.getMonthlyPension()))
                .andExpect(jsonPath("$.totalYearsOfWork").value(command.getTotalYearsOfWork()));

        assertTrue(personRepository.findById(1L).isPresent());
    }

    @Test
    public void testSave_PersonNotSavedByUnauthenticatedUser_UnauthenticatedAccess() throws Exception {
        assertFalse(personRepository.findById(1L).isPresent());

        mockMvc.perform(post("/api/v1/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CreatePensionerCommand())))
                .andExpect(status().isUnauthorized());

        assertFalse(personRepository.findById(1L).isPresent());
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    public void testSave_PersonNotSavedEmployeeUnauthorized_UnauthorizedAccess() throws Exception {
        assertFalse(personRepository.findById(1L).isPresent());

        mockMvc.perform(post("/api/v1/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CreatePensionerCommand())))
                .andExpect(status().isForbidden());

        assertFalse(personRepository.findById(1L).isPresent());
    }

    @Test
    @WithMockUser(roles = "IMPORTER")
    public void testSave_PersonNotSavedByImporterUnauthorized_UnauthorizedAccess() throws Exception {
        assertFalse(personRepository.findById(1L).isPresent());

        mockMvc.perform(post("/api/v1/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CreatePensionerCommand())))
                .andExpect(status().isForbidden());

        assertFalse(personRepository.findById(1L).isPresent());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testSave_StudentWithNullFields_MethodArgumentNotValidExceptionThrown() throws Exception {
        assertFalse(personRepository.findById(1L).isPresent());

        CreateStudentCommand command = new CreateStudentCommand();
        command.setFirstName(null);
        command.setLastName(null);
        command.setSocialNumber(null);
        command.setHeight(null);
        command.setWeight(null);
        command.setEmail(null);
        command.setUniversityName(null);
        command.setGraduationYear(null);
        command.setScholarshipAmount(null);

        mockMvc.perform(post("/api/v1/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp").value(Matchers.notNullValue()))
                .andExpect(jsonPath("$.message").value("Field validations errors"))
                .andExpect(jsonPath("$.violations[?(@.field=='firstName')].message").value("First name cannot be empty."))
                .andExpect(jsonPath("$.violations[?(@.field=='lastName')].message").value("Last name cannot be empty."))
                .andExpect(jsonPath("$.violations[?(@.field=='socialNumber')].message").value("Social number cannot be empty."))
                .andExpect(jsonPath("$.violations[?(@.field=='height')].message").value("Height cannot be empty."))
                .andExpect(jsonPath("$.violations[?(@.field=='weight')].message").value("Weight cannot be empty."))
                .andExpect(jsonPath("$.violations[?(@.field=='email')].message").value("Email cannot be empty."))
                .andExpect(jsonPath("$.violations[?(@.field=='universityName')].message").value("University name cannot be empty."))
                .andExpect(jsonPath("$.violations[?(@.field=='graduationYear')].message").value("Graduation year cannot be empty."))
                .andExpect(jsonPath("$.violations[?(@.field=='scholarshipAmount')].message").value("Scholarship amount cannot be empty."));

        assertFalse(personRepository.findById(1L).isPresent());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testSave_StudentNotSavedValidationFailedMinimumHeightAndWeightNotReached_MethodArgumentNotValidExceptionThrown() throws Exception {
        assertFalse(personRepository.findById(1L).isPresent());

        CreateStudentCommand command = new CreateStudentCommand();
        command.setFirstName("Frank");
        command.setLastName("Sinatra");
        command.setSocialNumber("95052100000");
        command.setHeight(36.0);
        command.setWeight(25.0);
        command.setEmail("email@example.com");
        command.setUniversityName("Uniwersytet Warszawski");
        command.setGraduationYear(LocalDate.of(2017, 7, 1));
        command.setScholarshipAmount(1000.0);

        mockMvc.perform(post("/api/v1/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp").value(Matchers.notNullValue()))
                .andExpect(jsonPath("$.message").value("Field validations errors"))
                .andExpect(jsonPath("$.violations[?(@.field=='height')].message").value("Height must be greater than 50.0 cm."))
                .andExpect(jsonPath("$.violations[?(@.field=='weight')].message").value("Weight must be greater than 30.0 kg."));

        assertFalse(personRepository.findById(1L).isPresent());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testSave_StudentNotSavedValidationFailedWithPatternMismatchAndExceedingLimits_MethodArgumentNotValidExceptionThrown() throws Exception {
        assertFalse(personRepository.findById(1L).isPresent());

        CreateStudentCommand command = new CreateStudentCommand();
        command.setFirstName("BOBO");
        command.setLastName("BOMBASTIC");
        command.setSocialNumber("950501000000");
        command.setHeight(300.0);
        command.setWeight(550.0);
        command.setEmail("email@example.com");
        command.setUniversityName("Ulica mnie wychowała. #pozdro");
        command.setGraduationYear(LocalDate.of(2100, 1, 1));
        command.setScholarshipAmount(50.0);

        mockMvc.perform(post("/api/v1/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp").value(Matchers.notNullValue()))
                .andExpect(jsonPath("$.message").value("Field validations errors"))
                .andExpect(jsonPath("$.violations[?(@.field=='firstName')].message").value("First name must match [A-Z][a-z]{2,}( [A-Z][a-z]{2,})?."))
                .andExpect(jsonPath("$.violations[?(@.field=='lastName')].message").value("Last name must match [A-Z][a-z]{2,}( [A-Z][a-z]{2,})?."))
                .andExpect(jsonPath("$.violations[?(@.field=='socialNumber')].message").value("Social number must contain 11 digits."))
                .andExpect(jsonPath("$.violations[?(@.field=='height')].message").value("Height must be lower than 250.0 cm."))
                .andExpect(jsonPath("$.violations[?(@.field=='weight')].message").value("Weight must be lower than 500.0 kg."))
                .andExpect(jsonPath("$.violations[?(@.field=='universityName')].message").value("University name must match [a-zA-Z0-9 ]+."))
                .andExpect(jsonPath("$.violations[?(@.field=='graduationYear')].message").value("Graduation year cannot be in the future."))
                .andExpect(jsonPath("$.violations[?(@.field=='scholarshipAmount')].message").value("Scholarship amount cannot be lower than 100."));

        assertFalse(personRepository.findById(1L).isPresent());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testSave_EmployeeNotSavedValidationFailedNullValuesOnEmployeeSpecificFields_MethodArgumentNotValidExceptionThrown() throws Exception {
        assertFalse(personRepository.findById(1L).isPresent());

        CreateEmployeeCommand command = new CreateEmployeeCommand();
        command.setFirstName("Frank");
        command.setLastName("Sinatra");
        command.setSocialNumber("95052100000");
        command.setHeight(186.0);
        command.setWeight(105.0);
        command.setEmail("email@example.com");
        command.setPositionName(null);
        command.setStartDate(null);
        command.setSalary(null);

        mockMvc.perform(post("/api/v1/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp").value(Matchers.notNullValue()))
                .andExpect(jsonPath("$.message").value("Field validations errors"))
                .andExpect(jsonPath("$.violations[?(@.field=='positionName')].message").value("Position name cannot be empty."))
                .andExpect(jsonPath("$.violations[?(@.field=='startDate')].message").value("Start date cannot be empty."))
                .andExpect(jsonPath("$.violations[?(@.field=='salary')].message").value("Salary cannot be empty."));

        assertFalse(personRepository.findById(1L).isPresent());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testSave_EmployeeNotSavedValidationFailedOnEmployeeSpecificFields_MethodArgumentNotValidExceptionThrown() throws Exception {
        assertFalse(personRepository.findById(1L).isPresent());

        CreateEmployeeCommand command = new CreateEmployeeCommand();
        command.setFirstName("Frank");
        command.setLastName("Sinatra");
        command.setSocialNumber("95052100000");
        command.setHeight(186.0);
        command.setWeight(105.0);
        command.setEmail("email@example.com");
        command.setPositionName("#influencer");
        command.setStartDate(LocalDate.of(2100, 1, 1));
        command.setSalary(BigDecimal.valueOf(50.0));

        mockMvc.perform(post("/api/v1/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp").value(Matchers.notNullValue()))
                .andExpect(jsonPath("$.message").value("Field validations errors"))
                .andExpect(jsonPath("$.violations[?(@.field=='positionName')].message").value("Position name must match [a-zA-Z0-9 ]+."))
                .andExpect(jsonPath("$.violations[?(@.field=='startDate')].message").value("Start date cannot be in the future."))
                .andExpect(jsonPath("$.violations[?(@.field=='salary')].message").value("Salary amount cannot be lower than 100."));

        assertFalse(personRepository.findById(1L).isPresent());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testSave_PensionerNotSavedValidationFailedOnPensionerSpecificFieldsNullValues_MethodArgumentNotValidExceptionThrown() throws Exception {
        assertFalse(personRepository.findById(1L).isPresent());

        CreatePensionerCommand command = new CreatePensionerCommand();
        command.setFirstName("Frank");
        command.setLastName("Sinatra");
        command.setSocialNumber("95052100000");
        command.setHeight(186.0);
        command.setWeight(105.0);
        command.setEmail("email@example.com");
        command.setMonthlyPension(null);
        command.setTotalYearsOfWork(null);

        mockMvc.perform(post("/api/v1/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp").value(Matchers.notNullValue()))
                .andExpect(jsonPath("$.message").value("Field validations errors"))
                .andExpect(jsonPath("$.violations[?(@.field=='monthlyPension')].message").value("Monthly pension cannot be empty."))
                .andExpect(jsonPath("$.violations[?(@.field=='totalYearsOfWork')].message").value("Total years of work cannot be empty."));

        assertFalse(personRepository.findById(1L).isPresent());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testSave_PensionerNotSavedValidationFailedOnPensionerSpecificFieldsMinimumsNotReached_MethodArgumentNotValidExceptionThrown() throws Exception {
        assertFalse(personRepository.findById(1L).isPresent());

        CreatePensionerCommand command = new CreatePensionerCommand();
        command.setFirstName("Frank");
        command.setLastName("Sinatra");
        command.setSocialNumber("95052100000");
        command.setHeight(186.0);
        command.setWeight(105.0);
        command.setEmail("email@example.com");
        command.setMonthlyPension(BigInteger.valueOf(500L));
        command.setTotalYearsOfWork(0);

        mockMvc.perform(post("/api/v1/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp").value(Matchers.notNullValue()))
                .andExpect(jsonPath("$.message").value("Field validations errors"))
                .andExpect(jsonPath("$.violations[?(@.field=='monthlyPension')].message").value("Monthly pension cannot be lower than 1000."))
                .andExpect(jsonPath("$.violations[?(@.field=='totalYearsOfWork')].message").value("Years worked cannot be lower than 1."));

        assertFalse(personRepository.findById(1L).isPresent());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testSave_PensionerNotSavedValidationFailedOnPensionerSpecificFieldMaximumBreached_MethodArgumentNotValidExceptionThrown() throws Exception {
        assertFalse(personRepository.findById(1L).isPresent());

        CreatePensionerCommand command = new CreatePensionerCommand();
        command.setFirstName("Frank");
        command.setLastName("Sinatra");
        command.setSocialNumber("95052100000");
        command.setHeight(186.0);
        command.setWeight(105.0);
        command.setEmail("email@example.com");
        command.setMonthlyPension(BigInteger.valueOf(3000L));
        command.setTotalYearsOfWork(90);

        mockMvc.perform(post("/api/v1/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp").value(Matchers.notNullValue()))
                .andExpect(jsonPath("$.message").value("Field validations errors"))
                .andExpect(jsonPath("$.violations[?(@.field=='totalYearsOfWork')].message").value("Years worked cannot be great than 80."));

        assertFalse(personRepository.findById(1L).isPresent());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testSave_PersonNotSavedInvalidSocialNumber_InvalidSocialNumberExceptionThrown() throws Exception {
        assertFalse(personRepository.findById(1L).isPresent());

        CreatePensionerCommand command = new CreatePensionerCommand();
        command.setFirstName("Frank");
        command.setLastName("Sinatra");
        command.setSocialNumber("00000000000");
        command.setHeight(186.0);
        command.setWeight(105.0);
        command.setEmail("email@example.com");
        command.setMonthlyPension(BigInteger.valueOf(3000L));
        command.setTotalYearsOfWork(50);

        mockMvc.perform(post("/api/v1/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid PESEL number: " + command.getSocialNumber()));

        assertFalse(personRepository.findById(1L).isPresent());
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    @Sql("/data.sql")
    @Transactional
    public void testSearch_AllTypesOfPersonFoundByNameByAdmin_Success() throws Exception {
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setKey("firstName");
        searchCriteria.setValue("Karol");
        mockMvc.perform(get("/api/v1/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of(searchCriteria))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", Matchers.hasSize(3)))
                .andExpect(jsonPath("$.content[*].socialNumber", containsInAnyOrder(
                        "95052103000", "95052103003", "95052103006")))
                .andExpect(jsonPath("$.content[*].firstName", containsInAnyOrder(
                        searchCriteria.getValue(), searchCriteria.getValue(), searchCriteria.getValue())))
                .andExpect(jsonPath("$.pageable.pageSize").value(5));

    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    @Sql("/data.sql")
    @Transactional
    public void testSearch_AllTypesOfPersonFoundByNameByEmployee_Success() throws Exception {
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setKey("firstName");
        searchCriteria.setValue("Karol");
        mockMvc.perform(get("/api/v1/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of(searchCriteria))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", Matchers.hasSize(3)))
                .andExpect(jsonPath("$.content[*].socialNumber", containsInAnyOrder(
                        "95052103000", "95052103003", "95052103006")))
                .andExpect(jsonPath("$.content[*].firstName", containsInAnyOrder(
                        searchCriteria.getValue(), searchCriteria.getValue(), searchCriteria.getValue())))
                .andExpect(jsonPath("$.pageable.pageSize").value(5));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @Sql("/data.sql")
    @Transactional
    public void testSearch_AllTypesOfPersonFoundByLastNameByAdmin_Success() throws Exception {
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setKey("lastName");
        searchCriteria.setValue("Kowalski");
        mockMvc.perform(get("/api/v1/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of(searchCriteria))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", Matchers.hasSize(3)))
                .andExpect(jsonPath("$.content[*].socialNumber", containsInAnyOrder(
                        "95052103000", "95052103003", "95052103006")))
                .andExpect(jsonPath("$.content[*].lastName", containsInAnyOrder(
                        searchCriteria.getValue(), searchCriteria.getValue(), searchCriteria.getValue())))
                .andExpect(jsonPath("$.pageable.pageSize").value(5));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @Sql("/data.sql")
    @Transactional
    public void testSearch_PersonFoundBySocialNumberByAdmin_Success() throws Exception {
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setKey("socialNumber");
        searchCriteria.setValue("95052103000");
        mockMvc.perform(get("/api/v1/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of(searchCriteria))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].lastName").value("Kowalski"))
                .andExpect(jsonPath("$.content[0].socialNumber").value(searchCriteria.getValue()));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @Sql("/data.sql")
    @Transactional
    public void testSearch_AllTypesOfPersonByHeightByAdmin_Success() throws Exception {
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setKey("height");
        searchCriteria.setValue("<170.0, 172.0>");

        mockMvc.perform(get("/api/v1/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of(searchCriteria))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", Matchers.hasSize(3)))
                .andExpect(jsonPath("$.content[*].socialNumber", containsInAnyOrder(
                        "95052103000", "95052103003", "95052103006")))
                .andExpect(jsonPath("$.pageable.pageSize").value(5));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @Sql("/data.sql")
    @Transactional
    public void testSearch_AllTypesOfPersonByWeightByAdmin_Success() throws Exception {
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setKey("weight");
        searchCriteria.setValue("<70.0, 72.0>");

        mockMvc.perform(get("/api/v1/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of(searchCriteria))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", Matchers.hasSize(3)))
                .andExpect(jsonPath("$.content[*].socialNumber", containsInAnyOrder(
                        "95052103000", "95052103003", "95052103006")))
                .andExpect(jsonPath("$.pageable.pageSize").value(5));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @Sql("/data.sql")
    @Transactional
    public void testSearch_PersonFoundByEmailByAdmin_Success() throws Exception {
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setKey("email");
        searchCriteria.setValue("email@example.com");

        mockMvc.perform(get("/api/v1/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of(searchCriteria))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].lastName").value("Kowalski"))
                .andExpect(jsonPath("$.content[0].socialNumber").value("95052103000"))
                .andExpect(jsonPath("$.content[0].email").value(searchCriteria.getValue()));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @Sql("/data.sql")
    @Transactional
    public void testSearch_StudentsFoundByUniversityNameByAdmin_Success() throws Exception {
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setKey("universityName");
        searchCriteria.setValue("Uniwersytet Warszawski");

        mockMvc.perform(get("/api/v1/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of(searchCriteria))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", Matchers.hasSize(2)))
                .andExpect(jsonPath("$.content[*].socialNumber", containsInAnyOrder("95052103000", "95052103001")))
                .andExpect(jsonPath("$.content[*].universityName", containsInAnyOrder(
                        searchCriteria.getValue(), searchCriteria.getValue())))
                .andExpect(jsonPath("$.pageable.pageSize").value(5));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @Sql("/data.sql")
    @Transactional
    public void testSearch_StudentsFoundByGraduationYearByAdmin_Success() throws Exception {
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setKey("graduationYear");
        searchCriteria.setValue("<2000-01-01, 2015-01-01>");

        mockMvc.perform(get("/api/v1/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of(searchCriteria))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", Matchers.hasSize(2)))
                .andExpect(jsonPath("$.content[*].socialNumber", containsInAnyOrder("95052103000", "95052103001")))
                .andExpect(jsonPath("$.pageable.pageSize").value(5));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @Sql("/data.sql")
    @Transactional
    public void testSearch_StudentsFoundByScholarshipAmountByAdmin_Success() throws Exception {
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setKey("scholarshipAmount");
        searchCriteria.setValue("<750, 919>");

        mockMvc.perform(get("/api/v1/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of(searchCriteria))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", Matchers.hasSize(2)))
                .andExpect(jsonPath("$.content[*].socialNumber", containsInAnyOrder("95052103000", "95052103001")))
                .andExpect(jsonPath("$.pageable.pageSize").value(5));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @Sql("/data.sql")
    @Transactional
    public void testSearch_EmployeesFoundByPositionNameByAdmin_Success() throws Exception {
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setKey("positionName");
        searchCriteria.setValue("Drukarz Frytkarz");

        mockMvc.perform(get("/api/v1/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of(searchCriteria))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", Matchers.hasSize(2)))
                .andExpect(jsonPath("$.content[*].socialNumber", containsInAnyOrder("95052103003", "95052103004")))
                .andExpect(jsonPath("$.pageable.pageSize").value(5));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @Sql("/data.sql")
    @Transactional
    public void testSearch_EmployeesFoundByStartDateByAdmin_Success() throws Exception {
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setKey("startDate");
        searchCriteria.setValue("<1972-01-01, 1981-01-01>");

        mockMvc.perform(get("/api/v1/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of(searchCriteria))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", Matchers.hasSize(2)))
                .andExpect(jsonPath("$.content[*].socialNumber", containsInAnyOrder("95052103003", "95052103004")))
                .andExpect(jsonPath("$.pageable.pageSize").value(5));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @Sql("/data.sql")
    @Transactional
    public void testSearch_EmployeesFoundBySalaryByAdmin_Success() throws Exception {
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setKey("salary");
        searchCriteria.setValue("<263, 274>");

        mockMvc.perform(get("/api/v1/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of(searchCriteria))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", Matchers.hasSize(2)))
                .andExpect(jsonPath("$.content[*].socialNumber", containsInAnyOrder("95052103004", "95052103005")))
                .andExpect(jsonPath("$.pageable.pageSize").value(5));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @Sql("/data.sql")
    @Transactional
    public void testSearch_PensionersFoundByMonthlyPensionByAdmin_Success() throws Exception {
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setKey("monthlyPension");
        searchCriteria.setValue("<3414, 3719>");

        mockMvc.perform(get("/api/v1/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of(searchCriteria))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", Matchers.hasSize(2)))
                .andExpect(jsonPath("$.content[*].socialNumber", containsInAnyOrder("95052103007", "95052103008")))
                .andExpect(jsonPath("$.pageable.pageSize").value(5));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @Sql("/data.sql")
    @Transactional
    public void testSearch_PensionersFoundByTotalYearsOfWorkByAdmin_Success() throws Exception {
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setKey("totalYearsOfWork");
        searchCriteria.setValue("<4, 10>");

        mockMvc.perform(get("/api/v1/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of(searchCriteria))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", Matchers.hasSize(2)))
                .andExpect(jsonPath("$.content[*].socialNumber", containsInAnyOrder("95052103007", "95052103008")))
                .andExpect(jsonPath("$.pageable.pageSize").value(5));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @Sql("/data.sql")
    @Transactional
    public void testSearch_MultipleParametersSearchByAdmin_Success() throws Exception {
        SearchCriteria searchCriteria1 = new SearchCriteria();
        searchCriteria1.setKey("totalYearsOfWork");
        searchCriteria1.setValue("<4, 10>");

        SearchCriteria searchCriteria2 = new SearchCriteria();
        searchCriteria2.setKey("monthlyPension");
        searchCriteria2.setValue("<3414, 3500>");

        mockMvc.perform(get("/api/v1/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of(searchCriteria1, searchCriteria2))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.content[0].socialNumber").value("95052103008"))
                .andExpect(jsonPath("$.pageable.pageSize").value(5));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @Sql("/data.sql")
    @Transactional
    public void testSearch_SearchUnsuccessfulDueToMissingParameters_MethodArgumentNotValidException() throws Exception {
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setKey(null);
        searchCriteria.setValue(null);

        mockMvc.perform(get("/api/v1/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of(searchCriteria))))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "IMPORTER")
    @Sql("/data.sql")
    @Transactional
    public void testSearch_SearchNotWorkingForImporter_UnauthorizedAccess() throws Exception {
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setKey(null);
        searchCriteria.setValue(null);

        mockMvc.perform(get("/api/v1/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of(searchCriteria))))
                .andExpect(status().isForbidden());
    }

    @Test
    @Sql("/data.sql")
    @Transactional
    public void testSearch_SearchNotWorkingForUnauthenticatedUser_UnauthenticatedAccess() throws Exception {
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setKey(null);
        searchCriteria.setValue(null);

        mockMvc.perform(get("/api/v1/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of(searchCriteria))))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @Sql("/data.sql")
    @Transactional
    public void testUpdate_StudentUpdatedByAdmin_Success() throws Exception {
        UpdateStudentCommand command = new UpdateStudentCommand();
        command.setFirstName("Frank");
        command.setLastName("Sinatra");
        command.setHeight(186.0);
        command.setWeight(105.0);
        command.setEmail("email@example.com");
        command.setUniversityName("Uniwersytet Warszawski");
        command.setGraduationYear(LocalDate.of(2017, 7, 1));
        command.setScholarshipAmount(1000.0);

        mockMvc.perform(patch("/api/v1/people/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(Matchers.notNullValue()))
                .andExpect(jsonPath("$.firstName").value(command.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(command.getLastName()))
                .andExpect(jsonPath("$.socialNumber").value(personRepository.findById(1L).get().getSocialNumber()))
                .andExpect(jsonPath("$.height").value(command.getHeight()))
                .andExpect(jsonPath("$.weight").value(command.getWeight()))
                .andExpect(jsonPath("$.email").value(command.getEmail()))
                .andExpect(jsonPath("$.universityName").value(command.getUniversityName()))
                .andExpect(jsonPath("$.graduationYear").value(command.getGraduationYear().toString()))
                .andExpect(jsonPath("$.scholarshipAmount").value(command.getScholarshipAmount()));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @Sql("/data.sql")
    @Transactional
    public void testUpdate_EmployeeUpdatedByAdmin_Success() throws Exception {
        UpdateEmployeeCommand command = new UpdateEmployeeCommand();
        command.setFirstName("Frank");
        command.setLastName("Sinatra");
        command.setHeight(186.0);
        command.setWeight(105.0);
        command.setEmail("email@example.com");
        command.setPositionName("Frytkarz");
        command.setStartDate(LocalDate.of(2023, 3, 1));
        command.setSalary(BigDecimal.valueOf(5000.0));

        mockMvc.perform(patch("/api/v1/people/4")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(Matchers.notNullValue()))
                .andExpect(jsonPath("$.firstName").value(command.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(command.getLastName()))
                .andExpect(jsonPath("$.socialNumber").value(personRepository.findById(4L).get().getSocialNumber()))
                .andExpect(jsonPath("$.height").value(command.getHeight()))
                .andExpect(jsonPath("$.weight").value(command.getWeight()))
                .andExpect(jsonPath("$.email").value(command.getEmail()))
                .andExpect(jsonPath("$.positionName").value(command.getPositionName()))
                .andExpect(jsonPath("$.startDate").value(command.getStartDate().toString()))
                .andExpect(jsonPath("$.salary").value(command.getSalary()));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @Sql("/data.sql")
    @Transactional
    public void testUpdate_PensionerUpdatedByAdmin_Success() throws Exception {
        UpdatePensionerCommand command = new UpdatePensionerCommand();
        command.setFirstName("Frank");
        command.setLastName("Sinatra");
        command.setHeight(186.0);
        command.setWeight(105.0);
        command.setEmail("email@example.com");
        command.setMonthlyPension(BigInteger.valueOf(3000L));
        command.setTotalYearsOfWork(50);

        mockMvc.perform(patch("/api/v1/people/7")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(Matchers.notNullValue()))
                .andExpect(jsonPath("$.firstName").value(command.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(command.getLastName()))
                .andExpect(jsonPath("$.socialNumber").value(personRepository.findById(7L).get().getSocialNumber()))
                .andExpect(jsonPath("$.height").value(command.getHeight()))
                .andExpect(jsonPath("$.weight").value(command.getWeight()))
                .andExpect(jsonPath("$.email").value(command.getEmail()))
                .andExpect(jsonPath("$.monthlyPension").value(command.getMonthlyPension()))
                .andExpect(jsonPath("$.totalYearsOfWork").value(command.getTotalYearsOfWork()));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @Sql("/data.sql")
    @Transactional
    public void testUpdate_PersonNotFoundUpdateFailed_EntityNotFoundExceptionThrown() throws Exception {
        UpdatePensionerCommand command = new UpdatePensionerCommand();
        command.setFirstName("Frank");

        mockMvc.perform(patch("/api/v1/people/0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.timestamp").value(Matchers.notNullValue()))
                .andExpect(jsonPath("$.message").value("Person with id 0 not found."));
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    public void testUpdate_PersonNotUpdatedEmployeeUnauthorized_UnauthorizedAccess() throws Exception {
        UpdateStudentCommand command = new UpdateStudentCommand();
        command.setFirstName("Frank");

        mockMvc.perform(patch("/api/v1/people/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "IMPORTER")
    public void testUpdate_PersonNotUpdatedImporterUnauthorized_UnauthorizedAccess() throws Exception {
        UpdateStudentCommand command = new UpdateStudentCommand();
        command.setFirstName("Frank");

        mockMvc.perform(patch("/api/v1/people/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testUpdate_PersonNotUpdatedUnauthenticatedUser_UnauthenticatedAccess() throws Exception {
        UpdateStudentCommand command = new UpdateStudentCommand();
        command.setFirstName("Frank");

        mockMvc.perform(patch("/api/v1/people/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testUpdate_StudentNotUpdatedValidationFailed_MethodArgumentNotValidExceptionThrown() throws Exception {
        UpdateStudentCommand command = new UpdateStudentCommand();
        command.setFirstName("BONZO");
        command.setLastName("BOMBASTIC");
        command.setHeight(30.0);
        command.setWeight(20.0);
        command.setEmail("@email@ex@ampl@e.com");
        command.setUniversityName("Uniwersytet #Uliczny");
        command.setGraduationYear(LocalDate.of(2117, 7, 1));
        command.setScholarshipAmount(50.0);

        mockMvc.perform(patch("/api/v1/people/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp").value(Matchers.notNullValue()))
                .andExpect(jsonPath("$.message").value("Field validations errors"))
                .andExpect(jsonPath("$.violations[?(@.field=='firstName')].message").value("First name must match [A-Z][a-z]{2,}( [A-Z][a-z]{2,})?."))
                .andExpect(jsonPath("$.violations[?(@.field=='lastName')].message").value("Last name must match [A-Z][a-z]{2,}( [A-Z][a-z]{2,})?."))
                .andExpect(jsonPath("$.violations[?(@.field=='height')].message").value("Height must be greater than 50.0 cm."))
                .andExpect(jsonPath("$.violations[?(@.field=='weight')].message").value("Weight must be greater than 30.0 kg."))
                .andExpect(jsonPath("$.violations[?(@.field=='email')].message").value("Please provide valid email address."))
                .andExpect(jsonPath("$.violations[?(@.field=='universityName')].message").value("University name must match [a-zA-Z0-9 ]+."))
                .andExpect(jsonPath("$.violations[?(@.field=='graduationYear')].message").value("Graduation year cannot be in the future."))
                .andExpect(jsonPath("$.violations[?(@.field=='scholarshipAmount')].message").value("Scholarship amount cannot be lower than 100."));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testUpdate_EmployeeNotUpdatedValidationFailed_MethodArgumentNotValidExceptionThrown() throws Exception {
        UpdateEmployeeCommand command = new UpdateEmployeeCommand();
        command.setPositionName("XXXXXXX");
        command.setStartDate(LocalDate.of(2111, 1, 1));
        command.setSalary(BigDecimal.valueOf(50.0));

        mockMvc.perform(patch("/api/v1/people/4")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp").value(Matchers.notNullValue()))
                .andExpect(jsonPath("$.message").value("Field validations errors"))
                .andExpect(jsonPath("$.violations[?(@.field=='positionName')].message").value("Position name must match [A-Z][a-z]{2,}."))
                .andExpect(jsonPath("$.violations[?(@.field=='startDate')].message").value("Start date cannot be in the future."))
                .andExpect(jsonPath("$.violations[?(@.field=='salary')].message").value("Salary amount cannot be lower than 100."));

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testUpdate_PensionerNotUpdatedValidationFailed_MethodArgumentNotValidExceptionThrown() throws Exception {
        CreatePensionerCommand command = new CreatePensionerCommand();
        command.setMonthlyPension(BigInteger.valueOf(500L));
        command.setTotalYearsOfWork(0);

        mockMvc.perform(patch("/api/v1/people/7")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp").value(Matchers.notNullValue()))
                .andExpect(jsonPath("$.message").value("Field validations errors"))
                .andExpect(jsonPath("$.violations[?(@.field=='monthlyPension')].message").value("Monthly pension cannot be lower than 1000."))
                .andExpect(jsonPath("$.violations[?(@.field=='totalYearsOfWork')].message").value("Years worked cannot be lower than 1."));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @Sql("/data.sql")
    @Transactional
    public void testAddPosition_HistoricalPositionAddedToEmployeeByAdmin_Success() throws Exception {
        assertTrue(positionRepository.findAllByEmployeeId(4L).isEmpty());

        CreatePositionCommand command = new CreatePositionCommand();
        command.setPositionName("Drukarz Frytkarz");
        command.setStartDate(LocalDate.of(1960, 1, 1));
        command.setEndDate(LocalDate.of(1961, 1, 1));
        command.setSalary(BigDecimal.valueOf(3000.0));

        mockMvc.perform(post("/api/v1/employees/4/positions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.employeeId").value(4L))
                .andExpect(jsonPath("$.positionName").value(command.getPositionName()))
                .andExpect(jsonPath("$.startDate").value(command.getStartDate().toString()))
                .andExpect(jsonPath("$.endDate").value(command.getEndDate().toString()))
                .andExpect(jsonPath("$.salary").value(command.getSalary()));

        assertFalse(positionRepository.findAllByEmployeeId(4L).isEmpty());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @Sql("/data.sql")
    @Transactional
    public void testAddPosition_CurrentPositionAddedToEmployeeByAdmin_Success() throws Exception {
        assertTrue(positionRepository.findAllByEmployeeId(4L).isEmpty());

        CreatePositionCommand command = new CreatePositionCommand();
        command.setPositionName("Drukarz Frytkarz");
        command.setStartDate(LocalDate.of(1974, 1, 1));
        command.setSalary(BigDecimal.valueOf(3000.0));
        command.setCurrentPositionEndDate(LocalDate.of(1973, 1, 1));

        mockMvc.perform(post("/api/v1/employees/4/positions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.employeeId").value(4L))
                .andExpect(jsonPath("$.positionName").value(command.getPositionName()))
                .andExpect(jsonPath("$.startDate").value(command.getStartDate().toString()))
                .andExpect(jsonPath("$.salary").value(command.getSalary()));


        assertTrue(positionRepository.findById(1L).isPresent());
        assertEquals(positionRepository.findById(1L).get().getEndDate(), command.getCurrentPositionEndDate());
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    @Sql("/data.sql")
    @Transactional
    public void testAddPosition_HistoricalPositionAddedToEmployeeByEmployee_Success() throws Exception {
        assertTrue(positionRepository.findAllByEmployeeId(4L).isEmpty());

        CreatePositionCommand command = new CreatePositionCommand();
        command.setPositionName("Drukarz Frytkarz");
        command.setStartDate(LocalDate.of(1960, 1, 1));
        command.setEndDate(LocalDate.of(1961, 1, 1));
        command.setSalary(BigDecimal.valueOf(3000.0));

        mockMvc.perform(post("/api/v1/employees/4/positions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.employeeId").value(4L))
                .andExpect(jsonPath("$.positionName").value(command.getPositionName()))
                .andExpect(jsonPath("$.startDate").value(command.getStartDate().toString()))
                .andExpect(jsonPath("$.endDate").value(command.getEndDate().toString()))
                .andExpect(jsonPath("$.salary").value(command.getSalary()));

        assertFalse(positionRepository.findAllByEmployeeId(4L).isEmpty());
    }

    @Test
    @WithMockUser(roles = "IMPORTER")
    @Sql("/data.sql")
    @Transactional
    public void testAddPosition_HistoricalPositionNotAddedByImporterForbidden_UnauthorizedAccess() throws Exception {
        assertTrue(positionRepository.findAllByEmployeeId(4L).isEmpty());

        CreatePositionCommand command = new CreatePositionCommand();
        command.setPositionName("Drukarz Frytkarz");
        command.setStartDate(LocalDate.of(1960, 1, 1));
        command.setEndDate(LocalDate.of(1961, 1, 1));
        command.setSalary(BigDecimal.valueOf(3000.0));

        mockMvc.perform(post("/api/v1/employees/4/positions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isForbidden());

        assertTrue(positionRepository.findAllByEmployeeId(4L).isEmpty());
    }

    @Test
    @WithMockUser(roles = "IMPORTER")
    @Sql("/data.sql")
    @Transactional
    public void testAddPosition_HistoricalPositionNotAddedByUnauthenticatedUser_UnauthenticatedAccess() throws Exception {
        assertTrue(positionRepository.findAllByEmployeeId(4L).isEmpty());

        CreatePositionCommand command = new CreatePositionCommand();
        command.setPositionName("Drukarz Frytkarz");
        command.setStartDate(LocalDate.of(1960, 1, 1));
        command.setEndDate(LocalDate.of(1961, 1, 1));
        command.setSalary(BigDecimal.valueOf(3000.0));

        mockMvc.perform(post("/api/v1/employees/4/positions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isForbidden());

        assertTrue(positionRepository.findAllByEmployeeId(4L).isEmpty());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @Sql("/data.sql")
    @Transactional
    public void testAddPosition_EmployeeNotFound_EntityNotFoundExceptionThrown() throws Exception {
        CreatePositionCommand command = new CreatePositionCommand();
        command.setPositionName("Drukarz Frytkarz");
        command.setStartDate(LocalDate.of(1960, 1, 1));
        command.setEndDate(LocalDate.of(1961, 1, 1));
        command.setSalary(BigDecimal.valueOf(3000.0));

        mockMvc.perform(post("/api/v1/employees/0/positions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @Sql("/data.sql")
    @Transactional
    public void testAddPosition_PositionNotAddedValidationFailedOnPositionNameAndSalary_MethodArgumentNotValidExceptionThrown() throws Exception {
        assertTrue(positionRepository.findAllByEmployeeId(4L).isEmpty());

        CreatePositionCommand command = new CreatePositionCommand();
        command.setPositionName("Pałkarz#$#$#$#");
        command.setStartDate(LocalDate.of(2023, 1, 1));
        command.setEndDate(LocalDate.of(1961, 1, 1));
        command.setSalary(BigDecimal.valueOf(50.0));

        mockMvc.perform(post("/api/v1/employees/4/positions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp").value(Matchers.notNullValue()))
                .andExpect(jsonPath("$.message").value("Field validations errors"))
                .andExpect(jsonPath("$.violations[?(@.field=='positionName')].message").value("Position name must match [a-zA-Z0-9 ]+."))
                .andExpect(jsonPath("$.violations[?(@.field=='salary')].message").value("Salary amount cannot be lower than 100."));

        assertTrue(positionRepository.findAllByEmployeeId(4L).isEmpty());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @Sql("/data.sql")
    @Transactional
    public void testAddPosition_PositionNotAddedStartDateNull_MethodArgumentNotValidExceptionThrown() throws Exception {
        assertTrue(positionRepository.findAllByEmployeeId(4L).isEmpty());

        CreatePositionCommand command = new CreatePositionCommand();
        command.setPositionName("Kucharz");
        command.setStartDate(null);
        command.setEndDate(LocalDate.of(1961, 1, 1));
        command.setSalary(BigDecimal.valueOf(3000.0));

        mockMvc.perform(post("/api/v1/employees/4/positions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp").value(Matchers.notNullValue()))
                .andExpect(jsonPath("$.message").value("Field validations errors"))
                .andExpect(jsonPath("$.violations[?(@.field=='startDate')].message").value("Start date cannot be empty."))
                .andExpect(jsonPath("$.violations[?(@.field=='createPositionCommand')].message").value("Start date must be present."));

        assertTrue(positionRepository.findAllByEmployeeId(4L).isEmpty());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @Sql("/data.sql")
    @Transactional
    public void testAddPosition_PositionNotAddedStartDateAndCurrentPositionEndDateNull_MethodArgumentNotValidExceptionThrown() throws Exception {
        assertTrue(positionRepository.findAllByEmployeeId(4L).isEmpty());

        CreatePositionCommand command = new CreatePositionCommand();
        command.setPositionName("Kucharz");
        command.setStartDate(LocalDate.of(2022, 1, 1));
        command.setEndDate(null);
        command.setSalary(BigDecimal.valueOf(3000.0));
        command.setCurrentPositionEndDate(null);

        mockMvc.perform(post("/api/v1/employees/4/positions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp").value(Matchers.notNullValue()))
                .andExpect(jsonPath("$.message").value("Field validations errors"))
                .andExpect(jsonPath("$.violations[?(@.field=='createPositionCommand')].message").value("Specify either historical or current position dates, not both."));

        assertTrue(positionRepository.findAllByEmployeeId(4L).isEmpty());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @Sql("/data.sql")
    @Transactional
    public void testAddPosition_PositionNotAddedEndDateAndCurrentPositionEndDatePresent_MethodArgumentNotValidExceptionThrown() throws Exception {
        assertTrue(positionRepository.findAllByEmployeeId(4L).isEmpty());

        CreatePositionCommand command = new CreatePositionCommand();
        command.setPositionName("Kucharz");
        command.setStartDate(LocalDate.of(2022, 1, 1));
        command.setEndDate(LocalDate.of(2023, 1, 1));
        command.setSalary(BigDecimal.valueOf(3000.0));
        command.setCurrentPositionEndDate(LocalDate.of(1999, 1, 1));

        mockMvc.perform(post("/api/v1/employees/4/positions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp").value(Matchers.notNullValue()))
                .andExpect(jsonPath("$.message").value("Field validations errors"))
                .andExpect(jsonPath("$.violations[?(@.field=='createPositionCommand')].message").value("End date and current position's end date can't both be present."));

        assertTrue(positionRepository.findAllByEmployeeId(4L).isEmpty());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @Sql("/data.sql")
    @Transactional
    public void testAddPosition_PositionNotAddedStartDateIsAfterEndDate_MethodArgumentNotValidExceptionThrown() throws Exception {
        assertTrue(positionRepository.findAllByEmployeeId(4L).isEmpty());

        CreatePositionCommand command = new CreatePositionCommand();
        command.setPositionName("Kucharz");
        command.setStartDate(LocalDate.of(2023, 1, 1));
        command.setEndDate(LocalDate.of(2022, 1, 1));
        command.setSalary(BigDecimal.valueOf(3000.0));

        mockMvc.perform(post("/api/v1/employees/4/positions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp").value(Matchers.notNullValue()))
                .andExpect(jsonPath("$.message").value("Field validations errors"))
                .andExpect(jsonPath("$.violations[?(@.field=='createPositionCommand')].message").value("Start date must be before end date for historical positions."));

        assertTrue(positionRepository.findAllByEmployeeId(4L).isEmpty());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @Sql("/data.sql")
    @Transactional
    public void testAddPosition_PositionNotAddedCurrentPositionEndDateIsAfterNewPositionStartDate_MethodArgumentNotValidExceptionThrown() throws Exception {
        assertTrue(positionRepository.findAllByEmployeeId(4L).isEmpty());

        CreatePositionCommand command = new CreatePositionCommand();
        command.setPositionName("Kucharz");
        command.setStartDate(LocalDate.of(2023, 1, 1));
        command.setSalary(BigDecimal.valueOf(3000.0));
        command.setCurrentPositionEndDate(LocalDate.of(2024, 1, 1));

        mockMvc.perform(post("/api/v1/employees/4/positions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp").value(Matchers.notNullValue()))
                .andExpect(jsonPath("$.message").value("Field validations errors"))
                .andExpect(jsonPath("$.violations[?(@.field=='createPositionCommand')].message").value("Current position end date must be before new position start date."));

        assertTrue(positionRepository.findAllByEmployeeId(4L).isEmpty());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @Sql("/data.sql")
    @Transactional
    public void testAddPosition_PositionNotAddedCurrentPositionEndDateIsBeforeCurrentPositionStartDate_PositionOverlapException() throws Exception {
        assertTrue(positionRepository.findAllByEmployeeId(4L).isEmpty());

        Employee employee = (Employee) personRepository.findById(4L).get();

        CreatePositionCommand command = new CreatePositionCommand();
        command.setPositionName("Kucharz");
        command.setStartDate(LocalDate.of(2023, 1, 1));
        command.setCurrentPositionEndDate(LocalDate.of(1970, 1, 1));
        command.setSalary(BigDecimal.valueOf(3000.0));

        mockMvc.perform(post("/api/v1/employees/4/positions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp").value(Matchers.notNullValue()))
                .andExpect(jsonPath("$.message").value(MessageFormat.format(
                        "Employee''s start date {0} is after indicated position''s end date {1}.",
                        employee.getStartDate(), command.getCurrentPositionEndDate())));
        assertTrue(positionRepository.findAllByEmployeeId(4L).isEmpty());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testUploadCsv_UploadSuccessfulByAdmin_Success() throws Exception {
        Path csvPath = Paths.get("src/test/resources/dummy csv.csv");
        String content = new String(Files.readAllBytes(csvPath));
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "dummy csv.csv",
                "text/plain",
                content.getBytes()
        );

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/upload")
                        .file(file))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @WithMockUser(roles = "IMPORTER")
    public void testUploadCsv_UploadSuccessfulByImporter_Success() throws Exception {
        Path csvPath = Paths.get("src/test/resources/dummy csv.csv");
        String content = new String(Files.readAllBytes(csvPath));
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "dummy csv.csv",
                "text/plain",
                content.getBytes()
        );


        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/upload")
                        .file(file))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    public void testUploadCsv_UploadUnsuccessfulUnauthorizedEmployee_UnauthorizedAccess() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.csv",
                "text/plain",
                "type, socialNumber, name\nstudent, 123456789, John Doe".getBytes()
        );

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/upload")
                        .file(file))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testUploadCsv_UploadUnsuccessfulUnauthenticatedUser_UnauthenticatedAccess() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.csv",
                "text/plain",
                "type, socialNumber, name\nstudent, 123456789, John Doe".getBytes()
        );

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/upload")
                        .file(file))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    public void testGetUploadStatus_UploadStatusNotReceivedUnsuccessfulUnauthorizedEmployee_UnauthorizedAccess() throws Exception {
        mockMvc.perform(get("/api/v1/upload/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testGetUploadStatus_UploadStatusNotReceivedUnauthenticatedUser_UnauthenticatedAccess() throws Exception {
        mockMvc.perform(get("/api/v1/upload/1"))
                .andExpect(status().isUnauthorized());
    }
}