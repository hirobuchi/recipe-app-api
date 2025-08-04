package recipe.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import recipe.app.entity.Users;
import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByUsername(String username);
}
