package recipe.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import recipe.app.entity.AuthCredentials;

public interface AuthCredentialsRepository extends JpaRepository<AuthCredentials, Long> {
    Optional<AuthCredentials> findByUserId(Long userId);
}
