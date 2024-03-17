package pl.kurs.model.command.creation;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import pl.kurs.model.Role;
import pl.kurs.model.validation.annotation.ValidRole;

@Data
public class CreateUserCommand {

    @NotNull(message = "Email cannot be empty.")
    @Pattern(regexp = "^(?!.*\\.{2})[a-zA-Z0-9._%+-]+@[^.][a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}[^.]$", message = "Pattern not matched {regexp}")
    private String email;

    @NotNull(message = "Password cannot be empty.")
    private String password;

    @ValidRole
    private Role role;
}
