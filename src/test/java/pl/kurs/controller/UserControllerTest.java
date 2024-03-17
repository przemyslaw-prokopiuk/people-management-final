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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.kurs.model.command.creation.CreateUserCommand;
import pl.kurs.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.kurs.model.Role.*;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

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
    public void testSave_UserSavedAsAdmin_Success() throws Exception {
        assertFalse(userRepository.findById(1L).isPresent());
        CreateUserCommand command = new CreateUserCommand();
        command.setEmail("email@example.com");
        command.setPassword("password");
        command.setRole(ROLE_ADMIN);

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value(command.getEmail()));

        assertTrue(userRepository.findById(1L).isPresent());
    }

    @Test
    public void testSave_UserSavedAsEmployee_Success() throws Exception {
        CreateUserCommand command = new CreateUserCommand();
        command.setEmail("email@example.com");
        command.setPassword("password");
        command.setRole(ROLE_EMPLOYEE);

        assertFalse(userRepository.findById(1L).isPresent());

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value(command.getEmail()));

        assertTrue(userRepository.findById(1L).isPresent());
    }

    @Test
    public void testSave_UserSavedAsImporter_Success() throws Exception {
        CreateUserCommand command = new CreateUserCommand();
        command.setEmail("email@example.com");
        command.setPassword("password");
        command.setRole(ROLE_IMPORTER);

        assertFalse(userRepository.findById(1L).isPresent());

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value(command.getEmail()));

        assertTrue(userRepository.findById(1L).isPresent());
    }

    @Test
    public void testSave_UserNotSavedAllFieldsFailed_MethodArgumentNotValidExceptionThrown() throws Exception {
        String emailPattern = "Pattern not matched ^(?!.*\\.{2})[a-zA-Z0-9._%+-]+@[^.][a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}[^.]$";

        CreateUserCommand command = new CreateUserCommand();
        command.setEmail("e@@mail@example.com");
        command.setPassword(null);
        command.setRole(null);

        assertFalse(userRepository.findById(1L).isPresent());

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp").value(Matchers.notNullValue()))
                .andExpect(jsonPath("$.message").value("Field validations errors"))
                .andExpect(jsonPath("$.violations[?(@.field=='email')].message").value(emailPattern))
                .andExpect(jsonPath("$.violations[?(@.field=='password')].message").value("Password cannot be empty."))
                .andExpect(jsonPath("$.violations[?(@.field=='role')].message").value(
                        "Invalid type. Allowed types are: ROLE_ADMIN, ROLE_EMPLOYEE, ROLE_IMPORTER."));

        assertFalse(userRepository.findById(1L).isPresent());
    }
}