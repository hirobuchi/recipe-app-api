package recipe.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import recipe.app.entity.LoginSessions;

public interface LoginSessionsRepository extends JpaRepository<LoginSessions, Long> {
    Optional<LoginSessions> findBySessionTokenAndRevokedFalse(String token);
}
