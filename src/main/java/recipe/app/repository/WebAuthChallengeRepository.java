package recipe.app.repository;

import recipe.app.entity.WebAuthChallenge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WebAuthChallengeRepository extends JpaRepository<WebAuthChallenge, Long> {
    Optional<WebAuthChallenge> findByChallenge(String challenge);
}
