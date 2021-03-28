package us.awardspace.tekkno.xtrimlogy.users.application;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import us.awardspace.tekkno.xtrimlogy.user.db.UserEntityRepository;
import us.awardspace.tekkno.xtrimlogy.user.domain.UserEntity;
import us.awardspace.tekkno.xtrimlogy.users.application.port.UserRegistrationUseCase;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService implements UserRegistrationUseCase {

    private final UserEntityRepository repository;
    private final PasswordEncoder encoder;


    @Override
    @Transactional
    public RegisterResponse register(String username, String password) {
        Optional<UserEntity> user = repository.findByUsernameIgnoreCase(username);
        if(user.isPresent()) {
            return RegisterResponse.failure("Account already exists");
        }
        UserEntity entity = new UserEntity(username, encoder.encode(password));
        return RegisterResponse.success(repository.save(entity));
    }
}
