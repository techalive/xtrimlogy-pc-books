package us.awardspace.tekkno.xtrimlogy.users.web;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import us.awardspace.tekkno.xtrimlogy.users.application.port.UserRegistrationUseCase;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UsersController {

    private final UserRegistrationUseCase register;

    @PostMapping
    public ResponseEntity<?> register(@Valid @RequestBody RegisterCommand command) {
        return register
                .register(command.username, command.password)
                .handle(
                        entity -> ResponseEntity.accepted().build(),
                        error -> ResponseEntity.badRequest().body(error)
                );
    }

    @Data
    static class RegisterCommand {
        @Email
        String username;
        @Size(min = 3, max = 100)
        String password;
    }
}
