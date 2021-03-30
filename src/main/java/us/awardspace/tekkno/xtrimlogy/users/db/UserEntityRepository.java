package us.awardspace.tekkno.xtrimlogy.users.db;

import org.springframework.data.jpa.repository.JpaRepository;
import us.awardspace.tekkno.xtrimlogy.users.domain.UserEntity;

import java.util.Optional;

public interface UserEntityRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUsernameIgnoreCase(String username);
}
