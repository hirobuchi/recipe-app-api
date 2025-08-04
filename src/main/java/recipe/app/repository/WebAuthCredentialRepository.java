package recipe.app.repository;

import org.springframework.stereotype.Repository;
import recipe.app.entity.WebAuthCredential;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WebAuthCredentialRepository extends JpaRepository<WebAuthCredential, Long> {
    List<WebAuthCredential> findByUserId(Long userId);
    Optional<WebAuthCredential> findByCredentialId(String credentialId);
}
